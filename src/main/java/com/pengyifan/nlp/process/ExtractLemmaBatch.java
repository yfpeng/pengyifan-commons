package com.pengyifan.nlp.process;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Logger;

import com.pengyifan.brat.BratEntity;
import com.pengyifan.brat.BratIOUtils;

import edu.stanford.nlp.io.FileSequentialCollection;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetEndAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class ExtractLemmaBatch {

  private static Logger logger = Logger.getAnonymousLogger();
  private static final int NUM_THREADS = 8;

  private final StanfordCoreNLP pipeline;
  private final Path inputDir;

  public ExtractLemmaBatch(Path inputDir, Path outputDir) {
    this.inputDir = inputDir;
    Properties props = new Properties();
    props.put("annotators", "tokenize, ssplit, pos, lemma");
    props.put("ssplit.newlineIsSentenceBreak", "always");
    props.put("threads", Integer.toString(NUM_THREADS));
    props.put("inputDirectory", inputDir.toString());
    props.put("extension", ".txt");
    props.put("outputDirectory", outputDir.toString());
    props.put("outputExtension", ".a1");
    props.put("outputFormat", "text");
    props.put("replaceExtension", "true");

    logger.info(String.format("Parse props: %s", props));
    pipeline = new MyStanfordCoreNLP(props);
  }

  public void process()
      throws IOException {
    Properties props = pipeline.getProperties();
    String fileName = inputDir.toString();
    Collection<File> files = new FileSequentialCollection(new File(fileName),
        props.getProperty("extension"), false);
    pipeline.processFiles(null, files, NUM_THREADS);
  }

  static class MyStanfordCoreNLP extends StanfordCoreNLP {

    public MyStanfordCoreNLP(Properties props) {
      super(props);
    }

    /**
     * only print tree.
     */
    @Override
    public void prettyPrint(Annotation annotation, OutputStream os) {
      PrintWriter writer = new PrintWriter(os);
      prettyPrint(annotation, writer);
    }

    /**
     * print in a1 format.
     */
    @Override
    public void prettyPrint(Annotation annotation, PrintWriter writer) {
      int entityIndex = 0;
      for (CoreMap sentence : annotation.get(SentencesAnnotation.class)) {
        for (CoreMap token : sentence.get(TokensAnnotation.class)) {
          BratEntity entity = new BratEntity();
          entity.setId("T" + entityIndex);
          entity.addSpan(
              token.get(CharacterOffsetBeginAnnotation.class),
              token.get(CharacterOffsetEndAnnotation.class));
          entity.setText(token.get(LemmaAnnotation.class));
          entity.setType("Lemma");
          entityIndex++;
          writer.println(BratIOUtils.write(entity));
        }
      }
      writer.flush();
    }
  }
}
