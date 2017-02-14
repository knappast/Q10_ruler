package se.kaskware.q10.ruler.nodes;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static se.kaskware.q10.ruler.nodes.RuleNode.operand.andRule;
import static se.kaskware.q10.ruler.nodes.RuleNode.operand.getOperand;

/** Proudly produced by APLE02 on 2016-03-18 15:54. */
public class RuleNode extends PleMongoObject {
  private RuleNode m_parent;
  private String   m_name;
  private operand  m_ruleType = andRule;
  private String   m_description;
  private int m_level = 1;
  List<RuleNode> m_children = new ArrayList();

  public enum operand {
    andRule('*'), orRule('+');

    private final char m_opChar;

    operand(char ch) {
      m_opChar = ch;
    }

    public char getChar() {
      return m_opChar;
    }

    public static operand getOperand(char ch) {
      switch (ch) {
        case '*': return andRule;
        case '+': return orRule;
      }
      return andRule;
    }
  }

  public RuleNode(RuleNode parent, String name) {
    m_parent = parent;
    m_name = name;
    setLevel();
  }

  public RuleNode(Document dbo) {
    super(dbo);
    expandDBObject();
  }

  public String getName() {
    return m_name;
  }

  public void setName(String name) {
    m_name = name;
  }

  public boolean isLeaf() {
    return getRules().isEmpty();
  }

  public operand getRuleType() {
    return m_ruleType;
  }

  public void setRuleType(operand oper) {
    m_ruleType = oper;
  }

  public void removeRule() {
    for (RuleNode node : getRules()) {
      node.removeRule();
    }
    m_parent.getRules().remove(this);
  }

  public List<RuleNode> getRules() {
    return m_children;
  }

  public RuleNode getRule(int index) {
    return m_children.get(index);
  }

  public int getLevel() {
    return m_level;
  }

  public String getLevelAsString() {
    StringBuilder res = new StringBuilder();
    String level = Integer.toString(m_level);
    res.append(level.charAt(0));
    for (int i = 1; i < level.length(); i++) {
      res.append('.').append(level.charAt(i));
    }
    return res.toString();
  }

  private void setLevel() {
    if (m_parent instanceof RuleNode) {
      int pLevel = m_parent.getLevel();
      int sz = m_parent.getRules().size();
      int num = sz == 0 ? pLevel * 10 : (m_parent.getRule(sz - 1)).getLevel();
      m_level = num + 1;
    }
  }

  @Override
  public String toString() {
    return "RuleNode{" +
           "m_ruleType=" + m_ruleType +
           ", m_level=" + m_level +
           '}';
  }

  //-----------------

  public Document getDBObject() {
    Document dobj = new Document();
    dobj.put("name", m_name);
    dobj.put("ruleType", m_ruleType.getChar());
    dobj.put("description", m_description);
    dobj.put("level", m_level);
    ArrayList<Document> dbList = new ArrayList<>();
    for (RuleNode child : m_children) {
      dbList.add(child.getDBObject());
    }
    dobj.put("subRules", dbList);
    return dobj;
  }

  protected void expandDBObject() {
    m_name = getString("name");
    m_ruleType = getOperand(getString("ruleType").charAt(0));
    m_description = getString("description");
    m_level = getInt("level");
    Iterator tmp = ((ArrayList) get("subRules")).iterator();
    m_children = new ArrayList<>();
    while (tmp.hasNext()) {
      Document dobj = (Document) tmp.next();
      RuleNode ruleNode = new RuleNode(dobj);
      m_children.add(ruleNode);
      ruleNode.m_parent = this;
    }
  }
}
