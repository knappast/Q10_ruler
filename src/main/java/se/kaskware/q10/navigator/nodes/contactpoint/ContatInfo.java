package se.kaskware.q10.navigator.nodes.contactpoint;

import java.util.List;

/**
 * @author Per Leander
 * @version 1.0
 * @created 15-mar-2016 17:24:51
 */
public class ContatInfo {

  private List<PostalAddress> m_addresses;
  private List<Device>        m_devices;
  private List<Email>         m_emails;

  private ContactInfoType m_ContactInfoType;
  private PostalAddress   m_PostalAddress;
  private Email           m_Email;
  private Device          m_Device;

  public ContatInfo() {

  }

  public void finalize() throws Throwable {

  }
}