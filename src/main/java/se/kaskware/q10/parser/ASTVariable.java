/* Generated By:JJTree: Do not edit this line. ASTVariable.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package se.kaskware.q10.parser;

public
class ASTVariable extends SimpleNode {
  private String m_variable;

  public ASTVariable(int id) {
    super(id);
  }

  public ASTVariable(PLE_Parser parser, int id) {
    super(parser, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(PLE_ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }

  public void setVariable(String variable) {
    m_variable = variable;
  }

  public void setFunction(String func, String key, ASTValueble valueble) {

  }
}
/* JavaCC - OriginalChecksum=876e9afe23ea57a55b5cbe4a85f922cf (do not edit this line) */
