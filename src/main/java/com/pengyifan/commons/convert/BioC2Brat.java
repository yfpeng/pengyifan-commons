package com.pengyifan.commons.convert;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.biocreative.bioc.BioCAnnotation;
import org.biocreative.bioc.BioCCollection;
import org.biocreative.bioc.BioCDocument;
import org.biocreative.bioc.BioCLocation;
import org.biocreative.bioc.BioCNode;
import org.biocreative.bioc.BioCPassage;
import org.biocreative.bioc.BioCRelation;
import org.biocreative.bioc.io.BioCCollectionReader;
import org.biocreative.bioc.io.BioCFactory;
import org.biocreative.bioc.io.standard.JdkStrategy;

import com.pengyifan.nlp.brat.BratDocument;
import com.pengyifan.nlp.brat.BratEntity;
import com.pengyifan.nlp.brat.BratRelation;
import com.pengyifan.nlp.brat.BratUtils;

public class BioC2Brat {

  public static void bioc2brat(File biocFile, File bratDir)
      throws IOException, XMLStreamException {

    BioCFactory factory = BioCFactory.newFactory(new JdkStrategy());

    FileReader freader = new FileReader(biocFile);
    BioCCollectionReader breader = factory.createBioCCollectionReader(freader);
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
      BratUtils.write(
          new File(bratDir + "/" + bratdoc.getDocId() + ".ann"),
          bratdoc);
    }
  }

}