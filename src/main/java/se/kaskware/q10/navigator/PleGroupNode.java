package se.kaskware.q10.navigator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/** Created by User: konplr; Date: 2008-okt-21, 13:44:52 */
public class PleGroupNode extends PleNode {
  List<PleNode> m_children = new ArrayList<PleNode>();

  public PleGroupNode(String name) {
    this(null, name);
  }

  public PleGroupNode(PleGroupNode parent, String name) {
    super(parent, name);
  }

  public boolean isLeaf() {
    return false;
  }

  public PleNode getChild(int index) {
    return m_children.get(index);
  }

  public List<PleNode> getChildren() {
    return m_children;
  }

  public void sortChildren() {
    TreeSet<PleNode> tset = new TreeSet<PleNode>(new Comparator<PleNode>() {

      public int compare(PleNode o1, PleNode o2) {
        String name_1 = o1.getName().toLowerCase(), name_2 = o2.getName().toLowerCase();
        name_1 = checkLeading(name_1);
        name_2 = checkLeading(name_2);

        return name_1.equals(name_2) ? o1.getName().compareTo(o2.getName()) : name_1.compareTo(name_2);
      }
    });
    tset.addAll(m_children);
    m_children.clear();
    m_children.addAll(tset);
  }

  private String checkLeading(String name) {
    try {
      if (name.length() < 4) return name;
      return ((name.substring(0, 4).equalsIgnoreCase("the ")
          || name.substring(0, 2).equalsIgnoreCase("a ")
          || name.substring(0, 3).equalsIgnoreCase("an ")))
             ? name.substring(name.indexOf(' ') + 1)
             : name;
    }
    catch (Exception e) {
      System.out.println("name = " + name);
    }
    return null;
  }
}
