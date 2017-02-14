package se.kaskware.q10.ruler.gui;

import se.kaskware.gui.PleGroupNode;
import se.kaskware.gui.PleNode;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

/** Proudly produced by APLE02 on 2016-03-18 16:07. */
public class DiagramModel implements TreeModel {
  private PleGroupNode m_root;
  private ViewNode m_currentNode;
  private ViewNode m_ruleRoot;
  private List<TreeModelListener> m_modelListeners = new ArrayList<>();

  public DiagramModel(PleGroupNode root) {
    m_root = root;
  }

  public ViewNode getSelectedNode() {
    return m_currentNode;
  }

  public void setSelectedNode(ViewNode currentRule) {
    m_currentNode = currentRule;
  }

  @Override
  public Object getRoot() {
    return m_root;
  }

  @Override
  public Object getChild(Object parent, int index) {
    return ((PleGroupNode) parent).getChild(index);
  }

  @Override
  public int getChildCount(Object parent) {
    return ((PleGroupNode) parent).getChildren().size();
  }

  @Override
  public boolean isLeaf(Object node) {
    return ((PleNode) node).isLeaf();
  }

  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {
    ViewNode node = (ViewNode) path.getLastPathComponent();
    if (node != m_currentNode) {
      System.out.println("Should not happen");
      return;
    }
    if (node.getName().equals(newValue)) return;
    node.setName((String) newValue);
    for (TreeModelListener treeModelListener : m_modelListeners) {
      treeModelListener.treeNodesChanged(new TreeModelEvent(node, path));
    }
  }

  @Override
  public int getIndexOfChild(Object parent, Object child) { return 0; }

  @Override
  public void addTreeModelListener(TreeModelListener tml) {
    m_modelListeners.add(tml);
  }

  @Override
  public void removeTreeModelListener(TreeModelListener l) { }

  public ViewNode getRuleRoot() {
    return m_ruleRoot;
  }

  public void setRuleRoot(ViewNode node) {
    m_ruleRoot = node;
    m_ruleRoot.computePosition();
  }
}
