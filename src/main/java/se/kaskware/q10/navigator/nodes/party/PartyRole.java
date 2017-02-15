package se.kaskware.q10.navigator.nodes.party;

/**
 * @author Per Leander
 * @version 1.0
 * @created 15-mar-2016 13:38:44
 */
public class PartyRole {

  private String  name;
  private boolean primary;

  /**
   * <ul>
   * <li>försäkringstagare </li>
   * <li>försäkrad </li>
   * </ul>
   */
  private enum roleType {insurer, insured}

  public PartyRole() {

  }
}