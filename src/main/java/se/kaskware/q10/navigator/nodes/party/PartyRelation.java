package se.kaskware.q10.navigator.nodes.party;

/**
 * @author Per Leander
 * @version 1.0
 * @created 15-mar-2016 13:38:42
 */
public class PartyRelation {

  private String name;
  private String partyIdentification;

  private enum relationType {}

  public Party     m_Party;
  public PartyRole m_PartyRole;

  public PartyRelation() {

  }

  public void finalize() throws Throwable {

  }
}