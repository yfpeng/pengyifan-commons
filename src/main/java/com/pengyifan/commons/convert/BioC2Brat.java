package com.pengyifan.commons.convert;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.io.Files;
import com.pengyifan.bioc.BioCAnnotation;
import com.pengyifan.bioc.BioCCollection;
import com.pengyifan.bioc.BioCDocument;
import com.pengyifan.bioc.BioCLocation;
import com.pengyifan.bioc.BioCNode;
import com.pengyifan.bioc.BioCPassage;
import com.pengyifan.bioc.BioCRelation;
import com.pengyifan.bioc.io.BioCCollectionReader;
import com.pengyifan.nlp.brat.BratDocument;
import com.pengyifan.nlp.brat.BratEntity;
import com.pengyifan.nlp.brat.BratRelation;
import com.pengyifan.nlp.brat.BratIOUtils;

public class BioC2Brat {

  public static void bioc2brat(File biocFile, File bratDir)
      throws IOException, XMLStreamException {

    FileReader freader = new FileReader(biocFile);
    BioCCollectionReader breader = new BioCCollectionReader(freader);
    BioCCollection collection = breader.readCollection();
    breader.close();

    for (BioCDocument doc : collection.getDocuments()) {

      Validate.isTrue(doc.getPassageCount() == 1);
      BioCPassage pass = doc.getPassage(0);

      BratDocument bratdoc = new BratDocument();
      bratdoc.setDocId(doc.getID());

      // text
      Validate.isTrue(pass.getText().isPresent(), "BioC passage has no text");
      bratdoc.setText(pass.getText().get());

      // ann
      for (BioCAnnotation ann : pass.getAnnotations()) {
        BratEntity entity = new BratEntity();
        entity.setId(ann.getID());
        Validate.isTrue(
            ann.getText().isPresent(),
            "BioC annotation has no text");
        entity.setText(ann.getText().get());
        Validate.isTrue(
            ann.getInfon("type").isPresent(),
            "BioC annotation has no type information");
        entity.setType(ann.getInfon("type").get());
        for (BioCLocation loc : ann.getLocations()) {
          entity.addSpan(loc.getOffset(), loc.getOffset() + loc.getLength());
        }
        bratdoc.addAnnotation(entity);
      }
      // rel
      for (BioCRelation rel : pass.getRelations()) {
        BratRelation relation = new BratRelation();
        relation.setId(rel.getID());
        relation.setType(rel.getInfon("relation type").get());
        for (BioCNode node : rel.getNodes()) {
          relation.addArgument(node.getRole(), node.getRefid());
        }
        bratdoc.addAnnotation(relation);
      }

      FileUtils.write(
          new File(bratDir + "/" + bratdoc.getDocId() + ".txt"),
          bratdoc.getText());
      BratIOUtils.write(
          Files.newWriter(
              new File(bratDir + "/" + bratdoc.getDocId() + ".ann"),
              StandardCharsets.UTF_8),
          bratdoc);
    }
  }

}