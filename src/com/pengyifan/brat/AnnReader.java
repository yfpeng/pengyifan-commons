package com.pengyifan.brat;

public abstract class AnnReader {

  public static Event readEvent(String line) {
    Event event = new Event();

    String toks[] = line.split("\\t+");
    event.id = toks[0];

    toks = toks[1].split(" ");
    int index = toks[0].indexOf(':');
    event.setTrigger(toks[0].substring(0, index), toks[0].substring(index + 1));

    for (int i = 1; i < toks.length; i++) {
      index = toks[i].indexOf(':');
      event.addArgument(
          toks[i].substring(0, index),
          toks[i].substring(index + 1));
    }

    return event;
  }

  public static Relation readRelation(String line) {
    Relation relation = new Relation();

    String toks[] = line.split("\\t+");
    relation.id = toks[0];

    toks = toks[1].split(" ");
    relation.type = toks[0];

    for (int i = 1; i < toks.length; i++) {
      int index = toks[i].indexOf(':');
      relation.addArgument(
          toks[i].substring(0, index),
          toks[i].substring(index + 1));
    }

    return relation;
  }

  public static Entity readEntity(String line) {
    Entity entity = new Entity();
    String tabs[] = line.split("\t");
    entity.id = tabs[0];
    entity.text = tabs[2];
    int index = tabs[1].indexOf(' ');
    entity.type = tabs[1].substring(0, index);
    for (String loc : tabs[1].substring(index + 1).split(";")) {
      int space = loc.indexOf(' ');
      entity.addSpan(
          Integer.parseInt(loc.substring(0, space)),
          Integer.parseInt(loc.substring(space + 1)));
    }
    return entity;
  }

  public static Relation readEquivalence(String line) {
    Relation relation = new Relation();
    String tabs[] = line.split("\t");
    relation.id = tabs[0];
    int space = tabs[1].indexOf(' ');
    relation.type = tabs[1].substring(0, space);
    for (String e : tabs[1].substring(space + 1).split(" ")) {
      relation.addArgument("entity", e);
    }
    return relation;
  }

  public static Event readEventModification(String line) {
    Event event = new Event();
    String tabs[] = line.split("\t");
    event.id = tabs[0];
    int space = tabs[1].indexOf(' ');
    event.setTrigger(tabs[1].substring(0, space), tabs[1].substring(space + 1));
    return event;
  }
}
