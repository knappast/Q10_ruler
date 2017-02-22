package se.kaskware.kardan.bo;

import java.util.ArrayList;

/** Created by: Per Leander (10APLE02) at 2010-apr-01, Time 11:37:12 */
public class FolkGroup extends FolkObject {
  private ArrayList<FolkSystem> m_systems = new ArrayList<FolkSystem>();

  public FolkGroup(String name) {
    super(name);
  }

  public ArrayList<FolkSystem> getSystems() {
    return m_systems;
  }

  public void addSystem(FolkSystem app) {
    m_systems.add(app);
  }
}
