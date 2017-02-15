package se.kaskware.q10.navigator.nodes.party;

/**
 * @author Per Leander
 * @version 1.0
 * @created 15-mar-2016 13:38:46
 */
public class Person extends Party {

  private String                customerNumber;
  private String                firstName;
  private String                givenName;
  /**
   * Anger om person har enskild firma.
   */
  private boolean               hasEnskildFirma;
  private String                lastName;
  public  InsuranceItemRelation m_InsuranceItemRelation;

  public Person() {

  }
}