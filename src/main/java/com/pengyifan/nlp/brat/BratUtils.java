package com.pengyifan.nlp.brat;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class BratUtils {
  
  public static void write(Writer writer, BratDocument doc)
      throws IOException {
    StringBuilder sb = new StringBuilder();
    for (BratEntity entity : doc.getEntities()) {
      sb.append(write(entity)).append('\n');
    }
    for (BratRelation relation : doc.getRelations()) {
      sb.append(write(relation)).append('\n');
    }
    for (BratEvent event : doc.getEvents()) {
      sb.append(write(event)).append('\n');
    }
    for (BratAttribute att : doc.getAttributes()) {
      sb.append(write(att)).append('\n');
    }
    for (BratEquivRelation equivRel : doc.getEquivRelations()) {
      sb.append(write(equivRel)).append('\n');
    }
    for (BratNote note : doc.getNotes()) {
      sb.append(write(note)).append('\n');
    }
    writer.write(sb.toString());
    writer.close();
  }

  public static void write(File file, BratDocument doc)
      throws IOException {
    FileWriter writer = new FileWriter(file);
    write(writer, doc);
    writer.close();
  }

  public static BratDocument read(Reader reader, String docId)
      throws IOException {
    List<BratEvent> events = new ArrayList<BratEvent>();
    List<BratRelation> relations = new ArrayList<BratRelation>();
    List<BratEquivRelation> euqivRelations = new ArrayList<BratEquivRelation>();
    List<BratEntity> entities = new ArrayList<BratEntity>();
    List<BratAttribute> attributes = new ArrayList<BratAttribute>();
    List<BratNote> notes = new ArrayList<BratNote>();

    String line;
    LineNumberReader lnreader = new LineNumberReader(reader);
    while ((line = lnreader.readLine()) != null) {
      if (line.isEmpty()) {
        continue;
      }
      if (line.startsWith("T")) {
        entities.add(parseEntity(line));
      } else if (line.startsWith("E")) {
        events.add(parseEvent(line));
      } else if (line.startsWith("R")) {
        relations.add(parseRelation(line));
      } else if (line.startsWith("#")) {
        notes.add(parseNote(line));
      } else if (line.startsWith("A")) {
        attributes.add(parseAttribute(line));
      } else if (line.startsWith("M")) {
        attributes.add(parseAttribute(line));
      } else if (line.startsWith("N")) {
        ;
      } else if (line.startsWith("*")) {
        euqivRelations.add(parseEquivRelation(line));
      } else {
        reader.close();
        throw new IOException("cannot parse line: " + line);
      }
    }
    reader.close();

    BratDocument doc = new BratDocument();
    doc.setDocId(docId);

    for (BratEntity entity : entities) {
      doc.addAnnotation(entity);
    }
    for (BratRelation relation : relations) {
      doc.addAnnotation(relation);
    }
    for (BratEvent event : events) {
      doc.addAnnotation(event);
    }
    for (BratNote note : notes) {
      doc.addAnnotation(note);
    }
    return doc;
  }

  public static BratDocument read(File file)
      throws IOException {
    return read(new FileReader(file), file.getName());
  }

  public static BratNote parseNote(String line) {
    BratNote note = new BratNote();

    String toks[] = line.split("\\t+");
    note.setId(toks[0]);
    note.setText(toks[2]);

    int index = toks[1].indexOf(' ');
    note.setType(toks[1].substring(0, index));
    note.setRefId(toks[1].substring(index + 1));

    return note;
  }

  public static BratEvent parseEvent(String line) {
    BratEvent event = new BratEvent();

    String toks[] = line.split("\\t+");
    event.setId(toks[0]);

    toks = toks[1].split(" ");
    int index = toks[0].indexOf(':');
    event.setType(toks[0].substring(0, index));
    event.setTriggerId(toks[0].substring(index + 1));

    for (int i = 1; i < toks.length; i++) {
      index = toks[i].indexOf(':');
      event.addArgument(toks[i].substring(0, index), toks[i].substring(index + 1));
    }

    return event;
  }

  public static BratRelation parseRelation(String line) {
    BratRelation relation = new BratRelation();

    String toks[] = line.split("\\t+");
    relation.setId(toks[0]);

    toks = toks[1].split(" ");
    relation.setType(toks[0]);

    for (int i = 1; i < toks.length; i++) {
      int index = toks[i].indexOf(':');
      relation.addArgument(
          toks[i].substring(0, index),
          toks[i].substring(index + 1));
    }

    return relation;
  }

  public static BratEntity parseEntity(String line) {
    BratEntity entity = new BratEntity();
    String tabs[] = line.split("\t");
    entity.setId(tabs[0]);
    entity.setText(tabs[2]);
    int index = tabs[1].indexOf(' ');
    entity.setType(tabs[1].substring(0, index));
    for (String loc : tabs[1].substring(index + 1).split(";")) {
      int space = loc.indexOf(' ');
      entity.addSpan(
          Integer.parseInt(loc.substring(0, space)),
          Integer.parseInt(loc.substring(space + 1)));
    }
    return entity;
  }

  public static BratEquivRelation parseEquivRelation(String line) {
    BratEquivRelation relation = new BratEquivRelation();
    String tabs[] = line.split("\t");
    relation.setId(tabs[0]);
    int space = tabs[1].indexOf(' ');
    relation.setType(tabs[1].substring(0, space));
    for (String e : tabs[1].substring(space + 1).split(" ")) {
      relation.addArgument("Arg", e);
    }
    return relation;
  }

  public static BratAttribute parseAttribute(String line) {
    BratAttribute att = new BratAttribute();
    String tabs[] = line.split("\t");
    att.setId(tabs[0]);

    String[] toks = tabs[1].split(" ");
    att.setType(toks[0]);
    att.setRefId(toks[1]);
    for (int i = 2; i < toks.length; i++) {
      att.addAttribute(toks[i]);
    }
    return att;
  }

  public static String write(BratEntity entity) {
    StringBuilder sb = new StringBuilder();
    sb.append(entity.getId()).append('\t').append(entity.getType()).append(' ');
    for (int i = 0; i < entity.numberOfSpans(); i++) {
      if (i != 0) {
        sb.append(';');
      }
      sb.append(entity.start(i)).append(' ').append(entity.end(i));
    }
    sb.append('\t').append(entity.getText());
    return sb.toString();
  }

  public static String write(BratEvent event) {
    StringBuilder sb = new StringBuilder();
    sb.append(event.getId()).append('\t').append(event.getType()).append(':')
        .append(event.getTriggerId());
    for (int i = 0; i < event.numberOfArguments(); i++) {
      sb.append(' ').append(event.getArgRole(i)).append(':')
          .append(event.getArgId(i));
    }
    return sb.toString();
  }

  public static String write(BratRelation relation) {
    StringBuilder sb = new StringBuilder();
    sb.append(relation.getId()).append('\t').append(relation.getType());
    for (int i = 0; i < relation.numberOfArguments(); i++) {
      sb.append(' ').append(relation.getArgRole(i)).append(':')
          .append(relation.getArgId(i));
    }
    return sb.toString();
  }

  public static String write(BratEquivRelation relation) {
    StringBuilder sb = new StringBuilder();
    sb.append(relation.getId()).append('\t').append(relation.getType());
    for (int i = 0; i < relation.numberOfArguments(); i++) {
      sb.append(' ').append(relation.getArgId(i));
    }
    return sb.toString();
  }

  public static String write(BratAttribute attribute) {
    StringBuilder sb = new StringBuilder();
    sb.append(attribute.getId()).append('\t').append(attribute.getType())
        .append(' ').append(attribute.getRefId());
    for (int i = 0; i < attribute.numberOfAttributes(); i++) {
      sb.append(' ').append(attribute.getAttribute(i));
    }
    return sb.toString();
  }

  public static String write(BratNote note) {
    StringBuilder sb = new StringBuilder();
    sb.append(note.getId()).append('\t').append(note.getType()).append(' ')
        .append(note.getRefId()).append('\t').append(note.getText());
    return sb.toString();
  }
}
