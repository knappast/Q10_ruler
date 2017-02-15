package se.kaskware.q10.ruler.gui;

import se.kaskware.gui.PleGroupNode;
import se.kaskware.gui.PleNode;
import se.kaskware.q10.ruler.nodes.RuleNode;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Proudly produced by APLE02 on 2016-03-18 15:59. */
public class ViewNode extends PleGroupNode {
  static final int S_MARGIN     = 10;
  static final int S_NODEWIDTH  = 100;
  static final int S_NODEHEIGHT = 40;
  static final int S_OPERWIDTH  = 20;
  static final int S_OPERHEIGHT = 20;
  static final int S_HEIGHTGAP  = 10;

  private static int s_maxX;   // used to determine pane size
  private static int s_maxY;   // used to determine pane size

  private RuleNode m_userNode;
  private int m_xNodePos = S_MARGIN, m_yNodePos = S_MARGIN;
  private int m_xOperPos, m_yOperPos;
  private boolean m_collapsed, m_visible;
  private int m_depth;

  /** used to determine the viewable size of the diagram with respect max number of "rows" and "columns" */
  public static Dimension getExtent() {
    return new Dimension(s_maxX, s_maxY);
  }

  /** remodel to use only viewNode renamed to RuleNode or ... */
  public ViewNode(ViewNode parent) {
    super(parent, "No name");

    RuleNode ruleParent = null;
    if (parent != null) {
      ruleParent = parent.getUserNode();
      parent.addNode(this);
    }
    m_userNode = new RuleNode(ruleParent, "No name");
    if (ruleParent != null)
      ruleParent.getRules().add(m_userNode);

    computePosition();
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
    m_userNode.getRules().remove(m_userNode);
    m_userNode = null;
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

  public boolean isCollapsed() {
    return m_collapsed;
  }

  public boolean isExpanded() {
    return !m_collapsed;
  }

  public void isExpanded(boolean expanded) {
    m_collapsed = !expanded;
  }

  public void expandAllNode() {
    isExpanded(true);
    for (ViewNode viewNode : getViewNodes())
      viewNode.expandAllNode();
  }

  public void expandNode() {
    expandNode(true);
  }

  public void collapseNode() {
    expandNode(false);
  }

  protected void expandNode(boolean expand) {
    for (ViewNode viewNode : getViewNodes()) {
      if (expand)
        viewNode.isExpanded(expand);
      else {
        viewNode.isExpanded(expand);
        viewNode.expandNode(expand); // always collapse all the subnodes
      }
    }
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
    return m_userNode.getLevelAsString();
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
    return ((m_xNodePos < xPos && xPos < m_xNodePos + S_NODEWIDTH)
        && (m_yNodePos < yPos && yPos < m_yNodePos + S_NODEHEIGHT))
        || ((m_xOperPos < xPos && xPos < m_xOperPos + S_OPERWIDTH)
        && (m_yOperPos < yPos && yPos < m_yOperPos + S_OPERHEIGHT));
  }

  public void computePosition() {
    s_maxX = 0;
    s_maxY = 0;

    m_xNodePos = S_MARGIN;
    m_yNodePos = S_MARGIN;

    int yPos = m_yNodePos;
    List<ViewNode> myNodes = getViewNodes();
    ViewNode prevNode = null;
    for (ViewNode viewNode : myNodes) {
      yPos = viewNode.computePosition(prevNode, m_xNodePos, yPos);
      prevNode = viewNode;
      yPos += S_NODEHEIGHT + S_HEIGHTGAP;
    }
  }

  private int computePosition(ViewNode prevNode, int xPos, int yPos) {
    m_xNodePos = xPos + S_NODEWIDTH + 110 + S_MARGIN;
    m_yNodePos = yPos;

    m_xOperPos = m_xNodePos - 30;
    m_yOperPos = m_yNodePos + 9;

    if (isLeaf()) {
      s_maxX = Math.max(m_xNodePos + S_NODEWIDTH + 110 + S_MARGIN, s_maxX);
      s_maxY = Math.max(m_yNodePos + S_NODEHEIGHT + S_HEIGHTGAP, s_maxY);

      return m_yNodePos;
    }

    if (prevNode != null && !prevNode.isLeaf()) {
      List<ViewNode> nLevel_1 = prevNode.getViewNodes();

      // default to yPos for last node for "level 1" on prevNode if only leafs on level 2
      yPos = nLevel_1.get(nLevel_1.size() - 1).getNodeYPos() + S_NODEHEIGHT + S_HEIGHTGAP;
      m_yNodePos = yPos;
      m_yOperPos = m_yNodePos + 9;

      // search for yPos on last node for "level 2" on prevNode
      for (int i = nLevel_1.size() - 1; i > 0; i--) {
        ViewNode nextNode = nLevel_1.get(i);
        if (nextNode.isLeaf()) continue;
        List<ViewNode> nLevel_2 = nextNode.getViewNodes();
        if (nLevel_2.size() > 1) {
          ViewNode lastNode = nLevel_2.get(nLevel_2.size() - 1);
          yPos = lastNode.getNodeYPos() + S_NODEHEIGHT + S_HEIGHTGAP;
          // first level 2 node with children i still "higher up" than that the last level 1 leaf node
          yPos = yPos <= m_yNodePos ? m_yNodePos : yPos;
          m_yNodePos = yPos;
          m_yOperPos = m_yNodePos + 9;
          break;
        }
      }
    }

    prevNode = null;  // reset
    List<ViewNode> myNodes = getViewNodes();
    for (ViewNode viewNode : myNodes) {
      yPos = viewNode.computePosition(prevNode, m_xNodePos, yPos);
      prevNode = viewNode;
      yPos += S_NODEHEIGHT + S_HEIGHTGAP;
    }

    s_maxX = Math.max(m_xNodePos + S_NODEWIDTH + 110 + S_MARGIN, s_maxX);
    s_maxY = Math.max(m_yNodePos + S_NODEHEIGHT + S_HEIGHTGAP, s_maxY);

    return m_yNodePos;
  }

  public String toString() {
    return getName();
  }
}
