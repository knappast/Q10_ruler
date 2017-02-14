package se.kaskware.q10.ruler.gui;

import se.kaskware.gui.PleGroupNode;
import se.kaskware.gui.PleNode;
import se.kaskware.q10.ruler.nodes.RuleNode;

import java.util.ArrayList;
import java.util.List;

/** Proudly produced by APLE02 on 2016-03-18 15:59. */
public class ViewNode extends PleGroupNode {
  static final int S_MARGIN     = 10;
  static final int S_NODEWIDTH  = 100;
  static final int S_NODEHEIGHT = 50;
  static final int S_OPERWIDTH  = 25;
  static final int S_OPERHEIGHT = 25;

  private RuleNode m_userNode;
  private int      m_xNodePos = S_MARGIN, m_yNodePos = S_MARGIN;
  private int      m_xOperPos, m_yOperPos;

  /** remodel to use only viewNode renamed to RuleNode or ... */
  public ViewNode(ViewNode parent) {
    super(parent, "No name");
    computePosition();

    RuleNode ruleParent = null;
    if (parent != null) {
      ruleParent = parent.getUserNode();
      parent.addNode(this);
    }
    m_userNode = new RuleNode(ruleParent, "No name");
    if (ruleParent != null)
      ruleParent.getRules().add(m_userNode);
  }

  public ViewNode(PleGroupNode parent, RuleNode ruleNode) {
    super(parent, ruleNode.getName());
    m_userNode = ruleNode;

    for (RuleNode subRuleNode : ruleNode.getRules()) {
      ViewNode newNode = new ViewNode(this, subRuleNode);
      addNode(newNode);
    }
  }

  /** do not use getViewNodes() */
  private void addNode(ViewNode viewNode) {
    getChildren().add(viewNode);
  }

  public void removeNode() {
    m_userNode.removeRule();
    for (ViewNode child : getViewNodes()) {
      child.removeNode();
    }
    // do not use getViewNodes()
    getParent().getChildren().remove(this);
  }

  public String getName() {
    return getLevel() + ' ' + m_userNode.getName();
  }

  public String getNodeName() { return m_userNode.getName(); }

  public void setName(String name) {
    super.setName(name);  // redundant ??
    m_userNode.setName(name);
  }

  public RuleNode getUserNode() {
    return m_userNode;
  }

  public boolean isLeaf() {
    return m_userNode.isLeaf();
  }

  /** to simplify calls to ViewNode to avoid cast */
  public List<ViewNode> getViewNodes() {
    List<PleNode> tmpList = super.getChildren();
    List<ViewNode> toAvoidCast = new ArrayList<>();
    for (PleNode pleNode : tmpList) {
      toAvoidCast.add((ViewNode) pleNode);
    }
    return toAvoidCast;
  }

  public RuleNode.operand getOperand() {
    return m_userNode.getRuleType();
  }

  public void setOperand(RuleNode.operand oper) {
    m_userNode.setRuleType(oper);
  }

  public String getLevel() {
    return getUserNode().getLevelAsString();
  }

  public int getNodeXPos() {
    return m_xNodePos;
  }

  public int getNodeYPos() {
    return m_yNodePos;
  }

  public int getOperXPos() {
    return m_xOperPos;
  }

  public int getOperYPos() {
    return m_yOperPos;
  }

  public boolean contains(int xPos, int yPos) {
    return   ( (m_xNodePos < xPos && xPos < m_xNodePos + S_NODEWIDTH)
            && (m_yNodePos < yPos && yPos < m_yNodePos + S_NODEHEIGHT))
          || ( (m_xOperPos < xPos && xPos < m_xOperPos + S_OPERWIDTH)
            && (m_yOperPos < yPos && yPos < m_yOperPos + S_OPERHEIGHT));
  }

  public void computePosition() {
//    if (getParent() == null) {
//      m_xNodePos = S_MARGIN;
//      m_yNodePos = S_MARGIN;
//      System.out.println("kalle kula");
//    }
    int yPos = 0, deepY = 0;
    for (ViewNode viewNode : getViewNodes()) {
      yPos = viewNode.isLeaf() ? yPos : deepY;
      deepY = viewNode.computePosition(m_xNodePos, yPos);
      yPos += S_NODEHEIGHT + 25;
    }
  }

  private int computePosition(int xPos, int yPos) {
    m_xNodePos = xPos + S_NODEWIDTH + 125 + S_MARGIN;
    m_yNodePos = yPos + S_MARGIN;

    m_xOperPos = m_xNodePos - 50;
    m_yOperPos = m_yNodePos + 12;

    int deepY = m_yNodePos;
    List<ViewNode> viewNodes = getViewNodes();
    for (ViewNode viewNode : viewNodes) {
      yPos = viewNode.isLeaf() ? yPos : deepY; //
      deepY = viewNode.computePosition(m_xNodePos, yPos);
      yPos += S_NODEHEIGHT + 25;
    }

    int size = viewNodes.size();
    return size > 0 ? viewNodes.get(size - 1).getNodeYPos() + (S_NODEHEIGHT + 25) : S_NODEHEIGHT + 25;
  }

  public String toString() {
    return getName();
  }
}
