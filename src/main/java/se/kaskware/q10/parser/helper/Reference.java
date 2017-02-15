package se.kaskware.q10.parser.helper;

/**
 * Created with pride by per on 2017-01-06.
 */
public class Reference {
  private String m_identifier;
  private boolean m_resolved;

  public Reference(String id) {
    m_identifier = id;
  }

  public String getIdentifier() {
    return m_identifier;
  }
}
