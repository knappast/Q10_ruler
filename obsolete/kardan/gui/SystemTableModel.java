package se.kaskware.kardan.gui;

import se.kaskware.kardan.setups.SystemFieldNames;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/** Created by: APLE02 - Date: 2013-11-21; Time: 15:26 */
public class SystemTableModel implements TableModel {
  private static final String COLUMNNAMES[] = new String[]{"Attribut", "Värde"};

  private SystemFieldNames.areaName                   m_area;
  private FolkNode                                    m_node;
  private ArrayList<Map.Entry>                        m_data;
  private TreeMap<SystemFieldNames.fieldName, String> m_attribut;

  public SystemTableModel(SystemFieldNames.areaName area) {
    m_area = area;
  }

  @Override
  public int getRowCount() {
    return m_data != null ? m_data.size() : 0;
  }

  @Override
  public int getColumnCount() {
    return COLUMNNAMES.length;
  }

  @Override
  public String getColumnName(int columnIndex) {
    return COLUMNNAMES[columnIndex];
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Map.Entry<SystemFieldNames.fieldName, String> entry = m_data.get(rowIndex);
    switch (columnIndex) {
      case 0:
        return SystemFieldNames.getTuple(entry.getKey()).getLabel(); //SystemFieldNames.getLabel(rowIndex);
      case 1:
        return m_data.get(rowIndex).getValue(); // m_attribut.get(tuple.getField()); // entry.getValue();
    }
    return "Gurka";
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

  }

  @Override
  public void addTableModelListener(TableModelListener l) {

  }

  @Override
  public void removeTableModelListener(TableModelListener l) {

  }

  public void setNode(FolkNode node) {
    m_node = node;
    m_data = new ArrayList<Map.Entry>();
    if (node == null || node.getSystem() == null) return;

    m_attribut = node.getSystem().getSystemAttr();
    TreeMap<SystemFieldNames.fieldName,SystemFieldNames.Tuple> labels = SystemFieldNames.getAllLabels();
    if (m_attribut != null) // if from old files
      for (Map.Entry entry : m_attribut.entrySet()) {
        if (labels.get(entry.getKey()).getArea().equals(m_area)) {
          m_data.add(entry);
        }
      }
//    attribut = node.getSystem().getMiscAttr();
//    if (attribut != null) // if from old files
//      for (Map.Entry entry : attribut.entrySet()) {
//        m_data.add(entry);
//      }
  }
}
