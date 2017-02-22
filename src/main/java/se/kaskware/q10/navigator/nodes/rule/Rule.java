package se.kaskware.q10.navigator.nodes.rule;

import se.kaskware.gui.PleGroupNode;
import se.kaskware.gui.PleNode;
import se.kaskware.q10.annotations.TermAlias;

/**
 * Created with pride by: per on: 2016-01-30 at: 13:32
 */
@TermAlias(alias="Regel", lang = "se", createdBy = "Per")
public abstract class Rule extends PleNode {
  public Rule(String name) {
    super(name);
  }

  public Rule(PleGroupNode parent, String name) {
    super(parent, name);
  }
}
