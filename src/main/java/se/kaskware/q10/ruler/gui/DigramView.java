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
  private static final Font S_RULEFONT  = new Font("Arial", Font.BOLD, 20);
  private static final Font S_OPERFONT  = new Font("Arial", Font.PLAIN, 20);
  private static final Font S_LEVELFONT = new Font("Arial", Font.BOLD, 10);
  private static final int  S_EDGESTART = 75;

  private JScrollPane m_basePane;
  private final DiagramPane m_diagPane;
  private String m_diagramName;
  private DiagramModel m_model;

  public DigramView(String name) {
    m_diagramName = name;
    m_diagPane = new DiagramPane();
    m_diagPane.setPreferredSize(new Dimension(1500, 800));
    m_basePane = new JScrollPane(m_diagPane);
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

  private  class DiagramPane extends JPanel {

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

      if (firstBorn != null)
        drawOperandLine(gc2, node, firstBorn);
      if (lastBorn != null)
        drawChildLine(gc2, node, lastBorn);

      drawBox(gc2, node);
      for (ViewNode viewNode : viewNodes) {
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

      gc2.setPaint(viewNode.isLeaf() ? new Color(190, 200, 80) : Color.lightGray);

      gc2.fillRoundRect(xPos, yPos, S_NODEWIDTH, S_NODEHEIGHT, 16, 16);
      gc2.setColor(Color.GRAY);
      gc2.drawRoundRect(xPos, yPos, S_NODEWIDTH, S_NODEHEIGHT, 16, 16);

      gc2.setColor(Color.BLACK);
      drawNodeString(gc2, xPos, yPos, S_RULEFONT, viewNode.getNodeName());
      drawNodeString(gc2, xPos, yPos-18, S_LEVELFONT, viewNode.getLevel());
    }

    private void drawOperand(Graphics2D gc2, ViewNode viewNode) {
      int xPos = viewNode.getOperXPos();
      int yPos = viewNode.getOperYPos();

      gc2.setPaint(Color.lightGray.brighter());

      gc2.fillRoundRect(xPos, yPos, 25, 25, 16, 16);
      gc2.setColor(Color.GRAY);
      gc2.drawRoundRect(xPos, yPos, 25, 25, 16, 16);

      gc2.setColor(Color.BLACK);
      String op = Character.toString(viewNode.getOperand().getChar());
      drawNodeString(gc2, xPos - 37, yPos - 12, S_OPERFONT, op);
    }

    private void drawNodeString(Graphics2D gc2, int xPos, int yPos, Font font, String label) {
      gc2.setFont(font);
      Rectangle2D bounds = gc2.getFontMetrics().getStringBounds(label, gc2);
      int xMarg = (int) Math.round((S_NODEWIDTH - bounds.getWidth()) / 2.0);
      int yMarg = (int) Math.round((S_NODEHEIGHT - bounds.getHeight()) / 2.0);
      gc2.drawString(label, xPos+xMarg, yPos+yMarg+gc2.getFontMetrics().getAscent());
    }
  }
}
