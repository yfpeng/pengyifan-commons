package com.pengyifan.brat;

public class AnnWriter {

  public static String writeEntity(Entity entity) {
    StringBuilder sb = new StringBuilder();
    sb.append(entity.id).append('\t').append(entity.type).append(' ');
    for (int i = 0; i < entity.numberOfSpans(); i++) {
      if (i != 0) {
        sb.append(';');
      }
      sb.append(entity.start(i)).append(' ').append(entity.end(i));
    }
    sb.append('\t').append(entity.text);
    return sb.toString();
  }

  public static String writeEvent(Event event) {
    StringBuilder sb = new StringBuilder();
    sb.append(event.id).append('\t').append(event.getTriggerType()).append(':')
        .append(event.getTriggerId());
    for (int i = 0; i < event.arguments.size(); i++) {
      sb.append(' ').append(event.getArgRole(i)).append(':')
          .append(event.getArgId(i));
    }
    return sb.toString();
  }

  public static String writeRelation(Relation relation) {
    StringBuilder sb = new StringBuilder();
    sb.append(relation.id).append('\t').append(relation.type);
    for (int i = 0; i < relation.arguments.size(); i++) {
      sb.append(' ').append(relation.getArgRole(i)).append(':')
          .append(relation.getArgId(i));
    }
    return sb.toString();
  }
}
