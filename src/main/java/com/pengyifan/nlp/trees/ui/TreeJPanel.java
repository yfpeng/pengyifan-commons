package com.pengyifan.nlp.trees.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import edu.stanford.nlp.trees.Tree;

/**
 * Class for displaying a Tree.
 *
 * @author Dan Klein
 */

@SuppressWarnings("serial") class TreeJPanel extends JPanel {

  private static final int INIT_WIDTH = 800;
  private static final int INIT_HEIGHT = 600;

  private int VERTICAL_ALIGN = SwingConstants.CENTER;
  private int HORIZONTAL_ALIGN = SwingConstants.CENTER;

  private int maxFontSize = 128;
  private int minFontSize = 2;

  private static final double sisterSkip = 2.5;
  private static final double parentSkip = 1.35;
  private static final double belowLineSkip = 0.075;
  private static final double aboveLineSkip = 0.075;

  private Tree tree;

  public Tree getTree() {
    return tree;
  }

  public void setTree(Tree tree) {
    this.tree = tree;
    repaint();
  }

  private static String nodeToString(Tree t) {
    return (t == null || t.value() == null) ? " " : t.value();
  }

  private static class WidthResult {

    private final double width; // = 0.0;
    private final double nodeTab; // = 0.0;
    private final double nodeCenter; // = 0.0;
    private final double childTab; // = 0.0;

    private WidthResult(
        double width,
        double nodeTab,
        double nodeCenter,
        double childTab) {
      this.width = width;
      this.nodeTab = nodeTab;
      this.nodeCenter = nodeCenter;
      this.childTab = childTab;
    }
  }

  private static double width(Tree tree, FontMetrics fM) {
    return widthResult(tree, fM).width;
  }

  private static WidthResult widthResult(Tree tree, FontMetrics fM) {
    if (tree == null) {
      return new WidthResult(0.0, 0.0, 0.0, 0.0);
    }
    double local = fM.stringWidth(nodeToString(tree));
    if (tree.isLeaf()) {
      return new WidthResult(local, 0.0, local / 2.0, 0.0);
    }
    double sub = 0.0;
    double nodeCenter = 0.0;
    // double childTab = 0.0;
    for (int i = 0, numKids = tree.numChildren(); i < numKids; i++) {
      WidthResult subWR = widthResult(tree.getChild(i), fM);
      if (i == 0) {
        nodeCenter += (sub + subWR.nodeCenter) / 2.0;
      }
      if (i == numKids - 1) {
        nodeCenter += (sub + subWR.nodeCenter) / 2.0;
      }
      sub += subWR.width;
      if (i < numKids - 1) {
        sub += sisterSkip * fM.stringWidth(" ");
      }
    }
    double localLeft = local / 2.0;
    double subLeft = nodeCenter;
    double totalLeft = Math.max(localLeft, subLeft);
    double localRight = local / 2.0;
    double subRight = sub - nodeCenter;
    double totalRight = Math.max(localRight, subRight);
    return new WidthResult(totalLeft + totalRight, totalLeft - localLeft,
        nodeCenter + totalLeft - subLeft, totalLeft - subLeft);
  }

  private static double height(Tree tree, FontMetrics fM) {
    if (tree == null) {
      return 0.0;
    }
    double depth = tree.depth();
    return fM.getHeight()
        * (1.0 + depth * (1.0 + parentSkip + aboveLineSkip + belowLineSkip));
  }

  private FontMetrics pickFont(Graphics2D g2, Tree tree, Dimension space) {
    Font font = g2.getFont();
    String fontName = font.getName();
    int style = font.getStyle();

    for (int size = maxFontSize; size > minFontSize; size--) {
      font = new Font(fontName, style, size);
      g2.setFont(font);
      FontMetrics fontMetrics = g2.getFontMetrics();
      if (height(tree, fontMetrics) > space.getHeight()) {
        continue;
      }
      if (width(tree, fontMetrics) > space.getWidth()) {
        continue;
      }
      // System.out.println("Chose: "+size+" for space: "+space.getWidth());
      return fontMetrics;
    }
    font = new Font(fontName, style, minFontSize);
    g2.setFont(font);
    return g2.getFontMetrics();
  }

  private static double paintTree(Tree t, Point2D start, Graphics2D g2,
      FontMetrics fM) {
    if (t == null) {
      return 0.0;
    }
    String nodeStr = nodeToString(t);
    double nodeWidth = fM.stringWidth(nodeStr);
    double nodeHeight = fM.getHeight();
    double nodeAscent = fM.getAscent();
    WidthResult wr = widthResult(t, fM);
    double treeWidth = wr.width;
    double nodeTab = wr.nodeTab;
    double childTab = wr.childTab;
    double nodeCenter = wr.nodeCenter;
    // double treeHeight = height(t, fM);
    // draw root
    g2.drawString(
        nodeStr,
        (float) (nodeTab + start.getX()),
        (float) (start.getY() + nodeAscent));
    if (t.isLeaf()) {
      return nodeWidth;
    }
    double layerMultiplier = (1.0 + belowLineSkip + aboveLineSkip + parentSkip);
    double layerHeight = nodeHeight * layerMultiplier;
    double childStartX = start.getX() + childTab;
    double childStartY = start.getY() + layerHeight;
    double lineStartX = start.getX() + nodeCenter;
    double lineStartY = start.getY() + nodeHeight * (1.0 + belowLineSkip);
    double lineEndY = lineStartY + nodeHeight * parentSkip;
    // recursively draw children
    for (int i = 0; i < t.children().length; i++) {
      Tree child = t.children()[i];
      double cWidth = paintTree(child, new Point2D.Double(childStartX,
          childStartY), g2, fM);
      // draw connectors
      wr = widthResult(child, fM);
      double lineEndX = childStartX + wr.nodeCenter;
      g2.draw(new Line2D.Double(lineStartX, lineStartY, lineEndX, lineEndY));
      childStartX += cWidth;
      if (i < t.children().length - 1) {
        childStartX += sisterSkip * fM.stringWidth(" ");
      }
    }
    return treeWidth;
  }

  private void superPaint(Graphics g) {
    super.paintComponent(g);
  }

  @Override
  public void paintComponent(Graphics g) {
    superPaint(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    Dimension space = getSize();
    FontMetrics fM = pickFont(g2, tree, space);
    double width = width(tree, fM);
    double height = height(tree, fM);
    double startX = 0.0;
    double startY = 0.0;
    if (HORIZONTAL_ALIGN == SwingConstants.CENTER) {
      startX = (space.getWidth() - width) / 2.0;
    }
    if (HORIZONTAL_ALIGN == SwingConstants.RIGHT) {
      startX = space.getWidth() - width;
    }
    if (VERTICAL_ALIGN == SwingConstants.CENTER) {
      startY = (space.getHeight() - height) / 2.0;
    }
    if (VERTICAL_ALIGN == SwingConstants.BOTTOM) {
      startY = space.getHeight() - height;
    }
    paintTree(tree, new Point2D.Double(startX, startY), g2, fM);
  }

  public TreeJPanel() {
    this(SwingConstants.CENTER, SwingConstants.CENTER);
  }

  private TreeJPanel(int hAlign, int vAlign) {
    HORIZONTAL_ALIGN = hAlign;
    VERTICAL_ALIGN = vAlign;
    setPreferredSize(new Dimension(INIT_WIDTH, INIT_HEIGHT));
  }

  public void setMinFontSize(int size) {
    minFontSize = size;
  }

  public void setMaxFontSize(int size) {
    maxFontSize = size;
  }

//  public Font pickFont() {
//    Font font = getFont();
//    String fontName = font.getName();
//    int style = font.getStyle();
//    int size = (maxFontSize + minFontSize) / 2;
//    return new Font(fontName, style, size);
//  }
//
//  public Dimension getTreeDimension(Tree tree, Font font) {
//    FontMetrics fM = getFontMetrics(font);
//    return new Dimension((int) width(tree, fM), (int) height(tree, fM));
//  }

}
