package events.formatter.family.wpcompact.v1;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import events.IMessage;
import events.formatter.Envelope;
import events.formatter.ISerializeMessage;
import events.formatter.encoder.IEncodeContent;
import events.formatter.family.wpcompact.meta.ConsequenceOf;
import events.formatter.family.wpcompact.meta.ConsequenceOfProvider;
import events.formatter.family.wpcompact.meta.Initiator;
import events.formatter.family.wpcompact.meta.InitiatorProvider;
import events.formatter.family.wpcompact.meta.MetaItem;
import events.formatter.family.wpcompact.meta.MetaItemProvider;
import events.formatter.family.wpcompact.meta.Origin;
import events.formatter.family.wpcompact.meta.WpMeta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WpSerializer implements ISerializeMessage {

  private List<IEncodeContent> encoders = new ArrayList<>();
  private List<MetaItemProvider> generatedByProviders;
  private InitiatorProvider initiatorProvider;
  private ConsequenceOfProvider consequenceOfProvider;
  private String contextName;

  public WpSerializer(List<IEncodeContent> encoders,
      List<MetaItemProvider> generatedByProviders,
      InitiatorProvider initiatorProvider,
      ConsequenceOfProvider consequenceOfProvider, String contextName) {
    this.encoders = encoders;
    this.generatedByProviders = generatedByProviders;
    this.initiatorProvider = initiatorProvider;
    this.consequenceOfProvider = consequenceOfProvider;
    this.contextName = contextName;
  }

  public Envelope serialize(IMessage message) throws Exception {

    ConsequenceOf consequenceOf;
    Initiator initiator;
    UUID messageId = UUID.randomUUID();

    try {
      initiator = this.initiatorProvider.get();
    } catch (Exception e) {
      Map<String, String> initiatorDetails = new HashMap<>();
      initiatorDetails.put("id", messageId.toString());
      initiator = Initiator.message(initiatorDetails);
    }

    try {
      consequenceOf = this.consequenceOfProvider.get();
    } catch (Exception e) {
      Map<String, String> consequenceOfDetails = new HashMap<>();
      consequenceOfDetails.put("id", messageId.toString());
      consequenceOf = ConsequenceOf.message(consequenceOfDetails);
    }

    List<MetaItem> generatedBy = new ArrayList<>();
    for (MetaItemProvider itemProvider: this.generatedByProviders) {
      generatedBy.add(itemProvider.get());
    }

    Origin origin = new Origin(this.contextName, consequenceOf, generatedBy);
    WpMeta wpMeta = new WpMeta(origin, initiator);
    WpMessage wpMessage = new WpMessage(message, messageId, wpMeta);

    WpEnvelope wpEnvelope = this.format(wpMessage);
    for (IEncodeContent encoder : this.encoders) {
      wpEnvelope.encodeContent(encoder);
    }
    return wpEnvelope.asEnvelope();
  }

  public WpEnvelope format(WpMessage message) throws Exception {
    WpMessageGsonClassDto dto = new WpMessageGsonClassDto();
    dto.id = message.getId().toString();
    dto.name = message.getName();
    dto.payload = message.getPayload();
    dto.version = message.getVersion();
    dto.occurredAt = message.getOccurredAt().toString();
    dto.category = message.getCategory();
    dto.meta = WpMessageMetaGsonClassDto.fromMeta(message.getMeta());

    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
    String jsonString = gson.toJson(dto);

    return WpEnvelope.fresh(jsonString.getBytes());
  }
}
