package se.kaskware.q10.parser;

import se.kaskware.q10.parser.helper.Reference;
import se.kaskware.q10.parser.helper.RuleReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with pride by per on 2017-01-06.
 */
public abstract class IdentifyableNode extends SimpleNode {
  private ASTLagret        m_lagret;
  private String           m_identifier;
  private ASTDocumentation m_documentation;
  private RuleReference    m_ruleReference;

  public IdentifyableNode(int id) {
    super(id);
  }

  public IdentifyableNode(PLE_Parser parser, int id) {
    super(parser, id);
  }

  public String getIdentifier() {
    return m_identifier;
  }
  protected void setIdentifier(String identifier) {
    m_identifier = identifier;
  }

  protected ASTLagret getLagret() {
    return m_lagret;
  }
  protected void setLager(ASTLagret lager) {
    m_lagret = lager;
  }

  public void setDocumentation(ASTDocumentation documentation) {
    m_documentation = documentation;
  }

  public void setReference(RuleReference reference) {
    m_ruleReference = reference;
  }

  protected List<Reference> resolveReferences() {
    List<Reference> aList = new ArrayList<>();
    if (m_ruleReference != null)  // really unneeded when mand and optional introd
      if (!getLagret().referenceExists(m_ruleReference)) aList.add(m_ruleReference);
    return aList;
  }

  public void localToString(StringBuilder buf, String prefix) {
    buf.append(' ').append(m_identifier).append("\n");
    if (m_ruleReference != null)  // really unneeded when mand and optional introd
      buf.append(prefix).append("(Rule ").append(m_ruleReference.getIdentifier()).append(")\n");
  }
}
