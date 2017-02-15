package se.kaskware.q10.navigator.nodes.rule;

import se.kaskware.q10.annotations.TermAlias;
import se.kaskware.q10.navigator.PleGroupNode;
import se.kaskware.q10.navigator.PleNode;

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
