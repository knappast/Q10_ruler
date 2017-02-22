package se.kaskware.kardan.gui;

/**
 * Created by: APLE02 - Date: 2013-11-21; Time: 13:46
 */
public class GroupNode extends FolkNode {

  public GroupNode(String name) {
    super(name);
  }

  @Override
  public boolean getAllowsChildren() {
    return true;
  }

  @Override
  public boolean isLeaf() {
    return false;
  }

  public FolkNode addNode(FolkNode node) {
    return super.addNode(node);
  }
}
