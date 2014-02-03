package com.pengyifan.nlp.bioc;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;

import bioc.BioCAnnotation;
import bioc.BioCCollection;
import bioc.BioCDocument;
import bioc.BioCLocation;
import bioc.BioCNode;
import bioc.BioCPassage;
import bioc.BioCRelation;
import bioc.io.BioCCollectionReader;
import bioc.io.BioCFactory;

import com.pengyifan.nlp.brat.BratDocument;
import com.pengyifan.nlp.brat.BratEntity;
import com.pengyifan.nlp.brat.BratRelation;
import com.pengyifan.nlp.brat.BratUtils;

public class BioC2Brat {

  public static void bioc2brat(File biocFile, File bratDir)
      throws IOException, XMLStreamException {

    BioCFactory factory = BioCFactory.newFactory(BioCFactory.STANDARD);

    FileReader freader = new FileReader(biocFile);
    BioCCollectionReader breader = factory.createBioCCollectionReader(freader);
    BioCCollection collection = breader.readCollection();
    breader.close();

    for (BioCDocument doc : collection) {

      Validate.isTrue(doc.getSize() == 1);
      BioCPassage pass = doc.getPassage(0);

      BratDocument bratdoc = new BratDocument();
      bratdoc.setDocId(doc.getID());

      // text
      bratdoc.setText(pass.getText());

      // ann
      for (BioCAnnotation ann : pass.getAnnotations()) {
        BratEntity entity = new BratEntity();
        entity.setId(ann.getID());
        entity.setText(ann.getText());
        entity.setType(ann.getInfon("type"));
        for (BioCLocation loc : ann.getLocations()) {
          entity.addSpan(loc.getOffset(), loc.getOffset() + loc.getLength());
        }
        bratdoc.addAnnotation(entity);
      }
      // rel
      for (BioCRelation rel : pass.getRelations()) {
        BratRelation relation = new BratRelation();
        relation.setId(rel.getID());
        relation.setType(rel.getInfon("relation type"));
        for (BioCNode node : rel.getNodes()) {
          relation.addArgId(node.getRole(), node.getRefid());
        }
        bratdoc.addAnnotation(relation);
      }

      FileUtils.write(
          new File(bratDir + "/" + bratdoc.getDocId() + ".txt"),
          bratdoc.getText());
      BratUtils.write(
          new File(bratDir + "/" + bratdoc.getDocId() + ".ann"),
          bratdoc);
    }
  }

}