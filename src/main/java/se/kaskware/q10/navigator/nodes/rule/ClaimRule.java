package se.kaskware.q10.navigator.nodes.rule;

import se.kaskware.q10.annotations.TermAlias;
import se.kaskware.q10.navigator.PleGroupNode;

/**
 * Created with pride by: per on: 2016-01-30 at: 13:32
 */
@TermAlias(alias="SkadeRegel", lang = "se", createdBy = "Per")
public class ClaimRule extends Rule {
  public ClaimRule(String name) {
    super(name);
  }

  public ClaimRule(PleGroupNode parent, String name) {
    super(parent, name);
  }
}
