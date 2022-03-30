package events.formatter.family.wpcompact.v1;

import events.formatter.family.wpcompact.meta.ConsequenceOf;
import events.formatter.family.wpcompact.meta.Initiator;
import events.formatter.family.wpcompact.meta.MetaItem;
import events.formatter.family.wpcompact.meta.Origin;
import events.formatter.family.wpcompact.meta.WpMeta;
import java.util.ArrayList;
import java.util.List;

public class WpMessageMetaGsonClassDto {
  public WpMessageOriginGsonClassDto origin;
  public WpMessageMetaItemGsonClassDto initiator;

  public static WpMessageMetaGsonClassDto fromMeta(WpMeta meta) {
    WpMessageMetaGsonClassDto dto = new WpMessageMetaGsonClassDto();
    dto.initiator = new WpMessageMetaItemGsonClassDto();
    dto.initiator.name = meta.getInitiator().getName();
    dto.initiator.details = meta.getInitiator().getDetails();

    dto.origin = new WpMessageOriginGsonClassDto();
    dto.origin.contextName = meta.getOrigin().getContextName();
    dto.origin.consequenceOf = new WpMessageMetaItemGsonClassDto();
    dto.origin.consequenceOf.name = meta.getOrigin().getConsequenceOf().getName();
    dto.origin.consequenceOf.details = meta.getOrigin().getConsequenceOf().getDetails();
    dto.origin.generatedBy = new ArrayList<>();

    for (MetaItem item : meta.getOrigin().getGeneratedBy()) {
      WpMessageMetaItemGsonClassDto dtoItem = new WpMessageMetaItemGsonClassDto();
      dtoItem.name = item.getName();
      dtoItem.details = item.getDetails();
      dto.origin.generatedBy.add(dtoItem);
    }
    return dto;
  }

  public WpMeta toMeta() {
    List<MetaItem> generatedBy = new ArrayList<>();
    for (WpMessageMetaItemGsonClassDto item : this.origin.generatedBy) {
      generatedBy.add(new MetaItem(item.name, item.details));
    }
    Origin origin =
        new Origin(
            this.origin.contextName,
            new ConsequenceOf(this.origin.consequenceOf.name, this.origin.consequenceOf.details),
            generatedBy);
    Initiator initiator = new Initiator(this.initiator.name, this.initiator.details);
    return new WpMeta(origin, initiator);
  }
}
