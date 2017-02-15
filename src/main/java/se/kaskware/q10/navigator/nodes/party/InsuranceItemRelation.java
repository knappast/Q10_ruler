package se.kaskware.q10.navigator.nodes.party;

/**
 * @author Per Leander
 * @version 1.0
 * @created 15-mar-2016 13:38:30
 */
public class InsuranceItemRelation {

  private String insuranceItemID;
  private String presentationName;

  private enum relationType {}

  public Person        m_Person;
  public PartyRole     m_PartyRole;
  public InsuranceItem m_InsuranceItem;

  public InsuranceItemRelation() {

  }
}