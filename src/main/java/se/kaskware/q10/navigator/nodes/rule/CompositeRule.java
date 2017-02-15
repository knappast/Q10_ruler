package se.kaskware.q10.navigator.nodes.rule;

import se.kaskware.q10.annotations.TermAlias;
import se.kaskware.q10.navigator.PleGroupNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with pride by: per on: 2016-01-30 at: 13:32
 */
@TermAlias(alias="FlervärdsRegel", lang = "se", createdBy = "Per")
public class CompositeRule extends PleGroupNode {
  private List<Rule> m_rules = new ArrayList();

  public CompositeRule(String name) {
    super(name);
  }

  public CompositeRule(PleGroupNode parent, String name) {
    super(parent, name);
  }
}
