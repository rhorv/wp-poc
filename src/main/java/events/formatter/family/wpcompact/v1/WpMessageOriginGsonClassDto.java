package events.formatter.family.wpcompact.v1;

import java.util.List;

public class WpMessageOriginGsonClassDto {
  public String contextName;
  public WpMessageMetaItemGsonClassDto consequenceOf;
  public List<WpMessageMetaItemGsonClassDto> generatedBy;
}
