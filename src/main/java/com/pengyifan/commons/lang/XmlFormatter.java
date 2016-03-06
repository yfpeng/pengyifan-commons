package com.pengyifan.commons.lang;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class XmlFormatter {

  public static XmlFormatter newFormatter() {
    return new XmlFormatter(65, true, 2);
  }

  private final int lineWidth;
  private final boolean isIndenting;

  private final int indent;

  private XmlFormatter(int lineWidth, boolean isIndenting, int indent) {
    this.lineWidth = lineWidth;
    this.isIndenting = isIndenting;
    this.indent = indent;
  }

  public String format(String unformattedXml) {
    try {
      final Document document = parseXmlFile(unformattedXml);
      OutputFormat format = new OutputFormat(document);
      format.setLineWidth(lineWidth);
      format.setIndenting(isIndenting);
      format.setIndent(indent);
      Writer out = new StringWriter();
      XMLSerializer serializer = new XMLSerializer(out, format);
      serializer.serialize(document);
      return out.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public int getIndent() {
    return indent;
  }

  public int getLineWidth() {
    return lineWidth;
  }

  public boolean isIndenting() {
    return isIndenting;
  }

  private Document parseXmlFile(String in) {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setValidating(false);
      DocumentBuilder db = dbf.newDocumentBuilder();
      db.setEntityResolver(new EntityResolver() {

        @Override
        public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException {
          return new InputSource(new StringReader(""));
        }
      });
      InputSource is = new InputSource(new StringReader(in));
      return db.parse(is);
    } catch (ParserConfigurationException|SAXException|IOException e) {
      throw new RuntimeException(e);
    }
  }

  public XmlFormatter withIndent(int indent) {
    checkArgument(indent >= 0, "Indent should not be negative: %s", indent);
    return new XmlFormatter(this.lineWidth, this.isIndenting, indent);
  }

  public XmlFormatter withIndenting(boolean isIndenting) {
    return new XmlFormatter(this.lineWidth, isIndenting, this.indent);
  }

  public XmlFormatter withLineWidth(int lineWidth) {
    checkArgument(
        lineWidth >= 0,
        "Line width should not be negative: %s",
        lineWidth);
    return new XmlFormatter(lineWidth, this.isIndenting, this.indent);
  }
}
