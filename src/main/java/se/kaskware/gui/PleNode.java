package se.kaskware.gui;

import javax.swing.*;
import javax.swing.tree.TreePath;

/** Created by User: konplr; Date: 2008-okt-21, 13:44:52 */
public class PleNode {
  public enum imageSize {small, medium, large}

  private String       m_name;
  private PleGroupNode m_parent;  // always a groupnode
  private int          m_index;

  public PleNode() {}

  public PleNode(String name) {
    m_name = name;
  }

  public PleNode(PleGroupNode parent, String name) {
    m_name = name;
    m_parent = parent;
  }

  public String getName() { return m_name; }

  public void setName(String name) { m_name = name; }

  public PleGroupNode getParent() { return m_parent; }
  public void setParent(PleGroupNode parent) { m_parent = parent; }

  public TreePath find(PleNode tmp, TreePath path) {
    TreePath foundPath = path.pathByAddingChild(this);
    if (tmp == this) {
      return foundPath;
    }
    return path;
  }

  protected int getIndex() { return m_index; }

  public void setIndex(int index) {
    m_index = index;
  }

//  public String[] getFilterName(ArrayList<Mp3MC.TAG> filterTags) { // urk
//    return null;
//  }

  public boolean isWanted() { return false; }

  public Object getValueAt(int columnIndex) {
    switch (columnIndex) {
      case 0:
        return m_index;
      case 2:
        return m_name;
      default:
        return "";
    }
  }

//  public void setValueAt(Mp3MC.TAG tag, Object value) {
//    System.out.println("in PleNode.setValueAt setting " + tag + " to " + value);
//  }

  public boolean isLeaf() {
    return true;
  }

  public ImageIcon getImage() {
    return null;
  }

  public String getFullname() {
    return m_name;
  }

  public String toString() {
    return m_name;
  }
}
