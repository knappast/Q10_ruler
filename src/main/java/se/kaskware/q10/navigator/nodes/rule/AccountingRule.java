package se.kaskware.q10.navigator.nodes.rule;

import se.kaskware.q10.annotations.TermAlias;
import se.kaskware.gui.PleGroupNode;

/**
 * Created with pride by: per on: 2016-01-30 at: 13:32
 */
@TermAlias(alias="BokföringsRegel", lang = "se", createdBy = "Per")
public class AccountingRule extends Rule {
  public AccountingRule(String name) {
    super(name);
  }

  public AccountingRule(PleGroupNode parent, String name) {
    super(parent, name);
  }
}
