/* Generated By:JJTree: Do not edit this line. ASTValueble.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package se.kaskware.q10.parser;

public
class ASTValueble extends SimpleNode {
  private ASTValue m_value;
  private ASTVariable m_variable;

  public ASTValueble(int id) {
    super(id);
  }

  public ASTValueble(PLE_Parser parser, int valueble) {
    super(parser, valueble);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(PLE_ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }

  public void setValue(ASTValue value) {
    m_value = value;
  }

  public void setVariable(ASTVariable variable) {
    m_variable = variable;
  }
}
/* JavaCC - OriginalChecksum=31c36560253151e46d61062ba6f1a7bb (do not edit this line) */
