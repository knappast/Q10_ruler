package se.kaskware.q10.ruler.nodes;

/**
 * Created with pride by per on 2017-01-07.
 */
public class ValueTuple {
  public enum e_valueType {
    t_integer("Integer"), t_string("String"), t_date("Date"), t_time("Time"), t_period("Period");

    private final String m_name;

    e_valueType(String name) {
      m_name = name;
    }

    public String getName() { return m_name; }
  }

  private e_valueType m_type;
  private String m_value;

  public ValueTuple(e_valueType type, String value) {
    m_type = type;
    m_value = value;
  }

  public String getValue() {
    return m_value;
  }

  public void setValue(String value) {
    m_value = value;
  }

  public String getType() {
    return m_type.getName();
  }
}
