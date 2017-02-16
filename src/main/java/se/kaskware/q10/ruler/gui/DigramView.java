package se.kaskware.q10.ruler.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static se.kaskware.q10.ruler.gui.ViewNode.*;

/**
 * Created with pride by per on 2016-03-17.
 */
public class DigramView {
  private static final Font S_RULEFONT  = new Font("Arial", Font.BOLD, 14);
  private static final Font S_OPERFONT  = new Font("Arial", Font.PLAIN, 16);
  private static final Font S_LEVELFONT = new Font("Arial", Font.BOLD, 10);
  private static final int  S_EDGESTART = 75;

  public static final Color[] S_NODECOLORS = new Color[] {
      new Color(230, 230, 200), new Color(200, 230, 230),
      new Color(220, 200, 220), new Color(192, 192, 192),
      Color.yellow, Color.green
  };
  private static int S_NODECOLOR = 0;

  private JScrollPane  m_basePane;
  private DiagramPane  m_diagPane;
  private String       m_diagramName;
  private DiagramModel m_model;

  public DigramView(String name) {
    m_diagramName = name;
    m_diagPane = new DiagramPane();
    m_diagPane.setPreferredSize(new Dimension(1500, 800));
    m_basePane = new JScrollPane(m_diagPane);
  }

  public static void setNodeColor(int nodeColor) {
    S_NODECOLOR = nodeColor;
  }

  public JScrollPane getBasePane() {
    return m_basePane;
  }

  public String getName() {
    return m_diagramName;
  }

  public void setModel(DiagramModel model) {
    m_model = model;
  }

  public void setDiagramViewSize() {
    m_diagPane.setPreferredSize(ViewNode.getExtent());
    m_basePane.revalidate();
  }

  public ViewNode checkSelection(int xPos, int yPos) {
    int xScrolled = m_basePane.getHorizontalScrollBar().getValue();
    int yScrolled = m_basePane.getVerticalScrollBar().getValue();
    return checkSelection(m_model.getRuleRoot(), xScrolled + xPos, yScrolled + yPos);
  }

  private ViewNode checkSelection(ViewNode node, int xPos, int yPos) {
    if (node == null || node.contains(xPos, yPos))
      return node;
    for (ViewNode viewNode : node.getViewNodes()) {
      ViewNode tmp = checkSelection(viewNode, xPos, yPos);
      if (tmp != null)
        return tmp;
    }
    return null;
  }

  private class DiagramPane extends JPanel {

    public void paintComponent(Graphics gc) {
      setBackground(new Color(214, 243, 87));
      super.paintComponent(gc);

      Graphics2D gc2 = (Graphics2D) gc;
      gc2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
      gc2.setColor(Color.BLACK);
      gc2.drawString("Fuck", 50, 50);

      ViewNode ruleRoot = m_model.getRuleRoot();
      if (ruleRoot != null)
        drawAllNodes(gc2, ruleRoot);

      ViewNode selectedNode = m_model.getSelectedNode();
      if (selectedNode != null) {
        gc2.setStroke(new BasicStroke(1));
        gc2.setColor(Color.RED);
        int xPos = selectedNode.getNodeXPos() - 2;
        int yPos = selectedNode.getNodeYPos() - 2;
        gc2.drawRect(xPos, yPos, S_NODEWIDTH + 4, S_NODEHEIGHT + 4);
        if (ruleRoot != selectedNode) {  // dont draw operand for first node (ruleRoot)
          xPos = selectedNode.getOperXPos() - 2;
          yPos = selectedNode.getOperYPos() - 2;
          gc2.drawRect(xPos, yPos, S_OPERWIDTH + 4, S_OPERHEIGHT + 4);
        }
      }
    }

    private void drawAllNodes(Graphics2D gc2, ViewNode node) {
      List<ViewNode> viewNodes = node.getViewNodes();
      ViewNode firstBorn = viewNodes.isEmpty() ? null : viewNodes.get(0);
      ViewNode lastBorn = viewNodes.isEmpty() ? null : viewNodes.get(viewNodes.size() - 1);

      if (firstBorn != null && firstBorn.isExpanded())
        drawOperandLine(gc2, node, firstBorn);
      if (lastBorn != null && firstBorn.isExpanded())
        drawChildLine(gc2, node, lastBorn);

      drawBox(gc2, node);
      for (ViewNode viewNode : viewNodes) {
        if (viewNode.isCollapsed()) continue;
        drawAllNodes(gc2, viewNode);
        drawEdgeLine(gc2, viewNode);
        drawOperand(gc2, viewNode);
      }
    }

    private void drawOperandLine(Graphics2D gc2, ViewNode node, ViewNode firstBorn) {
      gc2.setStroke(new BasicStroke(2));
      int xPos_1 = node.getNodeXPos() + S_NODEWIDTH;
      int xPos_2 = firstBorn.getNodeXPos();
      int yPos = node.getNodeYPos() + S_NODEHEIGHT / 2;
      gc2.drawLine(xPos_1, yPos, xPos_2, yPos);
    }

    private void drawEdgeLine(Graphics2D gc2, ViewNode viewNode) {
      gc2.setStroke(new BasicStroke(2));
      int xPos_1 = viewNode.getNodeXPos() - S_EDGESTART;
      int xPos_2 = viewNode.getNodeXPos();
      int yPos = viewNode.getNodeYPos() + S_NODEHEIGHT / 2;
      gc2.drawLine(xPos_1, yPos, xPos_2, yPos);
    }

    private void drawChildLine(Graphics2D gc2, ViewNode node, ViewNode lastBorn) {
      gc2.setStroke(new BasicStroke(2));
      int xPos = lastBorn.getNodeXPos() - S_EDGESTART;
      int yPos_1 = node.getNodeYPos() + S_NODEHEIGHT / 2;
      int yPos_2 = lastBorn.getNodeYPos() + S_NODEHEIGHT / 2;
      gc2.drawLine(xPos, yPos_1, xPos, yPos_2);
    }

    protected void drawBox(Graphics2D gc2, ViewNode viewNode) {
      int xPos = viewNode.getNodeXPos();
      int yPos = viewNode.getNodeYPos();

      gc2.setPaint(viewNode.isLeaf() ? new Color(190, 200, 80) : S_NODECOLORS[S_NODECOLOR]);

      gc2.fillRoundRect(xPos, yPos, S_NODEWIDTH, S_NODEHEIGHT, 16, 16);
      gc2.setColor(Color.GRAY);
      gc2.drawRoundRect(xPos, yPos, S_NODEWIDTH, S_NODEHEIGHT, 16, 16);

      gc2.setColor(Color.BLACK);
      drawNodeString(gc2, xPos, yPos + 8, S_RULEFONT, viewNode.getNodeName());
      drawNodeString(gc2, xPos, yPos - 12, S_LEVELFONT, viewNode.getLevel());
    }

    private void drawOperand(Graphics2D gc2, ViewNode viewNode) {
      int xPos = viewNode.getOperXPos();
      int yPos = viewNode.getOperYPos();

      gc2.setPaint(new Color(240, 240, 240)); //Color.lightGray.brighter());

      gc2.fillRoundRect(xPos, yPos, S_OPERWIDTH, S_OPERHEIGHT, 10, 10);
      gc2.setColor(Color.GRAY);
      gc2.drawRoundRect(xPos, yPos, S_OPERWIDTH, S_OPERHEIGHT, 10, 10);

      gc2.setColor(Color.BLACK);
      char aChar = viewNode.getOperand().getChar();
      xPos -= aChar == '*' ? 0 : 1;
      yPos -= aChar == '*' ? 7 : 10;
      String op = Character.toString(aChar);
      drawNodeString(gc2, xPos - 40, yPos, S_OPERFONT, op);
    }

    private void drawNodeString(Graphics2D gc2, int xPos, int yPos, Font font, String label) {
      gc2.setFont(font);
      Rectangle2D bounds = gc2.getFontMetrics().getStringBounds(label, gc2);
      int xMarg = (int) Math.round((S_NODEWIDTH - bounds.getWidth()) / 2.0);
      int yMarg = (int) Math.round((S_NODEHEIGHT - bounds.getHeight()) / 2.0);
      gc2.drawString(label, xPos + xMarg, yPos + yMarg + gc2.getFontMetrics().getAscent());
    }
  }
}
