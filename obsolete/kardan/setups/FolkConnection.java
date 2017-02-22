package se.kaskware.kardan.setups;

import se.kaskware.kardan.bo.FolkObject;
import se.kaskware.kardan.bo.FolkSystem;

/** Created by: Per Leander (APLE02) at: 2010-maj-06, 13:23:34 */
public class FolkConnection extends FolkObject {
  private FolkSystem m_toApp;
  private String m_info;

  public FolkConnection(FolkSystem toApp) {
    this(toApp, "No Info");
  }

  public FolkConnection(FolkSystem toApp, String info) {
    m_toApp = toApp;
    m_info = info;
  }

  public FolkSystem getToApp() {
    return m_toApp;
  }

  public String toString() {
    return "toConn: " + m_toApp;
  }
}
