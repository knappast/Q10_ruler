package se.kaskware.q10.navigator.nodes.contactpoint;

/**
 * @author Per Leander
 * @version 1.0
 * @created 15-mar-2016 17:24:49
 */
public class ContactPoint {

  /**
   * f�r alla
   * endast f�r vissa handl�ggare
   * endast f�r handl�ggare
   * endast f�r vissa handl�ggare
   */
  public enum visibility {
  }

  private long m_uniqueID;

  private visibility    m_visibility;
  private QualityAspect m_qualityAspect;
  private State         m_state;

  public ContactPoint() {

  }
}