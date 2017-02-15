package se.kaskware.q10.navigator.nodes.contactpoint;

/**
 * @author Per Leander
 * @version 1.0
 * @created 15-mar-2016 17:25:12
 */
public class PostalAddress extends ContactPoint {

  /**
   * folkbokföring, careOf, annan
   */
  private enum addressType {}

  private String m_address_1;
  private String m_address_2;
  private String m_address_3;

  private String  m_city;
  private String  m_zipCode;
  public  Country m_country;

  public PostalAddress() {

  }
}