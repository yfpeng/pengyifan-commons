package com.pengyifan.commons.convert;

import java.util.List;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Lists;
import com.pengyifan.bioc.BioCAnnotation;
import com.pengyifan.bioc.BioCCollection;
import com.pengyifan.bioc.BioCDocument;
import com.pengyifan.bioc.BioCLocation;
import com.pengyifan.bioc.BioCNode;
import com.pengyifan.bioc.BioCPassage;
import com.pengyifan.bioc.BioCRelation;
import com.pengyifan.brat.BratDocument;
import com.pengyifan.brat.BratEntity;
import com.pengyifan.brat.BratRelation;

public class BioC2BratConverter {
  
  /**
   * Annotations and relations are at the passage level
   */
  public static List<BratDocument> bioc2brat(BioCCollection collection) {
    List<BratDocument> bratDocs = Lists.newArrayList();
    
    for (BioCDocument biocDoc : collection.getDocuments()) {

      Validate.isTrue(biocDoc.getPassageCount() == 1);
      BioCPassage pass = biocDoc.getPassage(0);

      BratDocument bratDoc = new BratDocument();
      bratDoc.setDocId(biocDoc.getID());

      // text
      Validate.isTrue(pass.getText().isPresent(), "BioC passage has no text");
      bratDoc.setText(pass.getText().get());

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
        bratDoc.addAnnotation(entity);
      }
      // rel
      for (BioCRelation rel : pass.getRelations()) {
        BratRelation relation = new BratRelation();
        relation.setId(rel.getID());
        relation.setType(rel.getInfon("relation type").get());
        for (BioCNode node : rel.getNodes()) {
          relation.putArgument(node.getRole(), node.getRefid());
        }
        bratDoc.addAnnotation(relation);
      }

      bratDocs.add(bratDoc);
    }
    return bratDocs;
  }

}