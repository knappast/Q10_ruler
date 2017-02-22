package se.kaskware.q10.navigator.nodes.product;

import se.kaskware.q10.annotations.TermAlias;
import se.kaskware.gui.PleGroupNode;

/**
 * Created with pride by: per on: 2016-01-30 at: 13:32
 */
@TermAlias(alias="Moment", lang="se", createdBy = "Per")
public class Coverage extends PleGroupNode {
  private boolean isSubcoverage;

  public Coverage(String name) {
    super(name);
  }

  public Coverage(PleGroupNode parent, String name) {
    super(parent, name);
  }
}
