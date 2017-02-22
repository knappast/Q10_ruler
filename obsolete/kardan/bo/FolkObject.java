package se.kaskware.kardan.bo;

/** Created by: Per Leander (APLE02) at: 2010-maj-05, 16:51:41 */
public class FolkObject {
  private String m_name;
  private String m_slogan;
  private String m_description;

  public FolkObject(String name) {
    m_name = name;
  }

  public FolkObject(String name, String descr) {
    m_name = name;
    m_description = descr;
  }

  public FolkObject() {}

  public String getName() {
    return m_name;
  }

  protected void setName(String name) {
    m_name = name;
  }

  public String getDescription() {
    return m_description;
  }

  protected void setDescription(String desc) {
    m_description = desc;
  }


  @Override
  public String toString() {
    return getName() + ", " + getDescription();
  }
}
