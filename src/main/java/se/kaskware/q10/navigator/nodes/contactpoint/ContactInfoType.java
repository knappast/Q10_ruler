package se.kaskware.q10.navigator.nodes.contactpoint;

/**
 * @author Per Leander
 * @version 1.0
 * @created 15-mar-2016 17:24:47
 */
public class ContactInfoType {

  /**
   * Tex
   * <ul>
   * <li>Folkbokföring    </li>
   * <li>Annan    </li>
   * </ul>
   * <ul>
   * <li>CareOf   </li>
   * <li>Grannens mobil  </li>
   * </ul>
   */
  public enum infoType {
    registered, careOf, other
  }

  private infoType m_infoType;
  private String m_otherDescription;

  public ContactInfoType() {

  }
}