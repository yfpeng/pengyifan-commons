package com.pengyifan.commons.convert;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
import com.pengyifan.brat.BratDocument;
import com.pengyifan.brat.BratEntity;
import com.pengyifan.brat.BratRelation;
import com.pengyifan.brat.io.BratIOUtils;

@Deprecated
public class BioC2Brat {

  public static void bioc2brat(File biocFile, File bratDir)
      throws IOException, XMLStreamException {

    FileReader freader = new FileReader(biocFile);
    BioCCollectionReader breader = new BioCCollectionReader(freader);
    BioCCollection collection = breader.readCollection();
    breader.close();

    final BioC2BratConverter converter = new BioC2BratConverter();
    List<BratDocument> docs = converter.bioc2brat(collection);
    for(BratDocument bratdoc: docs) {
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