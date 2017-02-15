package se.kaskware.q10.navigator.nodes.product;

import se.kaskware.q10.annotations.TermAlias;
import se.kaskware.q10.navigator.PleGroupNode;
import se.kaskware.q10.navigator.PleNode;
import se.kaskware.q10.navigator.nodes.rule.AccountingRule;
import se.kaskware.q10.navigator.nodes.rule.ClaimRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with pride by: per on: 2016-01-30 at: 13:32
 */
@TermAlias(alias="ProduktVariant", lang = "se", createdBy = "Per")
public class LineOfBusiness extends PleNode {
  private List<Coverage> m_coverages = new ArrayList<>();

  private List<AccountingRule> m_accountingRules = new ArrayList<>();
  private List<ClaimRule>      m_claimRules      = new ArrayList<>();

  public LineOfBusiness(String name) {
    super(name);
  }

  public LineOfBusiness(PleGroupNode parent, String name) {
    super(parent, name);
  }
}
