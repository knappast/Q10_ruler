package se.kaskware.q10.navigator.nodes.party;

import se.kaskware.q10.navigator.nodes.contactpoint.ContatInfo;
import se.kaskware.q10.navigator.nodes.contactpoint.Preferences;

/**
 * @author Per Leander
 * @version 1.0
 * @created 15-mar-2016 13:38:40
 */
public class Party {

  /**
   * person  eller organisationsnummer
   */
  private String        partyIdentification;
  private String        presentationName;
  private long          uniqueID;
  public  PartyRole     m_PartyRole;
  public  PartyRelation m_PartyRelation;
  public  Preferences   m_Preferences;
  public  ContatInfo    m_ContatInfo;

  public Party() {

  }
}