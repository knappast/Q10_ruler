package se.kaskware.kardan.gui;

import se.kaskware.kardan.bo.FolkSystem;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

/**
 * Created by: APLE02 - Date: 2013-11-21; Time: 13:46
 */
public class FolkNode implements TreeNode {
  private FolkSystem          m_system;
  private String              m_name;
  private ArrayList<FolkNode> m_nodes;

  public FolkNode(String name) {
    m_name = name;
    m_nodes = new ArrayList<FolkNode>();
  }

  public FolkNode(Map.Entry<String, FolkSystem> entry) {
    m_name = entry.getKey();
    m_system = entry.getValue();
  }

  public FolkSystem getSystem() {
    return m_system;
  }

  @Override
  public TreeNode getChildAt(int childIndex) {
    return m_nodes.get(childIndex);
  }

  @Override
  public int getChildCount() {
    return m_nodes.size();
  }

  @Override
  public TreeNode getParent() {
    return null;
  }

  @Override
  public int getIndex(TreeNode node) {
    return m_nodes.indexOf(node);
  }

  @Override
  public boolean getAllowsChildren() {
    return false;
  }

  @Override
  public boolean isLeaf() {
    return true;
  }

  @Override
  public Enumeration children() {
    return new Vector(m_nodes).elements();
  }

  public String toString() {
    return m_name;
  }

  protected FolkNode addNode(FolkNode node) {
    m_nodes.add(node);
    return node;
  }
}
