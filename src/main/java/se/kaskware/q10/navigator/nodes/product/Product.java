package se.kaskware.q10.navigator.nodes.product;

import se.kaskware.q10.annotations.TermAlias;
import se.kaskware.gui.PleGroupNode;
import se.kaskware.q10.navigator.nodes.rule.AccountingRule;
import se.kaskware.q10.navigator.nodes.rule.AgreementRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with pride by: per on: 2016-01-30 at: 13:32
 */
@TermAlias(alias="ProduktPaket", lang = "se", createdBy = "Per")
public class Product extends PleGroupNode {
  private List<LineOfBusiness> m_lobs = new ArrayList<>();

  private List<AccountingRule> m_accountingRules = new ArrayList<>();
  private List<AgreementRule>  m_agreementRules  = new ArrayList<>();

  public Product(String name) {
    super(name);
  }

  public Product(PleGroupNode parent, String name) {
    super(parent, name);
  }
}
