package se.kaskware.q10.ruler.gui;

import se.kaskware.gui.PleGroupNode;
import se.kaskware.q10.ruler.nodes.ValueTuple;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeMap;

/** Proudly produced by APLE02 on 2016-04-01 16:15. */
public class AttributeModel implements TableModel {
  private static final String[] S_COLUMNNAMES = {"Name", "Value", "Type"};

  private ViewNode                      m_selectedNode;
  private HashMap<String, ValueTuple> m_attributes;
  private ArrayList<String> m_keys = new ArrayList();

  public AttributeModel(PleGroupNode prodDefs) {}

  public ViewNode getSelectedNode() {
    return m_selectedNode;
  }

  public void setSelectedNode(ViewNode node) {
    m_selectedNode = node;
    if (node == null) {
      m_keys = new ArrayList();
      m_attributes = null;
    }
    else {
      m_attributes = m_selectedNode.getUserNode().getAttributes();
      m_keys = new ArrayList(m_attributes.keySet());
    }
  }

  @Override
  public int getRowCount() {
    return m_keys.size();
  }

  @Override
  public int getColumnCount() {
    return 3;
  }  // if node is rule 3 else 2

  @Override
  public String getColumnName(int columnIndex) {
    return S_COLUMNNAMES[columnIndex];
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  } // fix

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return columnIndex == 1;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    String val = null;

    switch (columnIndex) {
      case 0:
        val = m_keys.get(rowIndex);
        break;
      case 1:
        val = m_attributes.get(m_keys.get(rowIndex)).getValue();
        break;
      case 2:
        val = m_attributes.get(m_keys.get(rowIndex)).getType();
        break;
    }
    return val;
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    if (columnIndex == 1) m_attributes.get(m_keys.get(rowIndex)).setValue((String) aValue);
  }

  @Override
  public void addTableModelListener(TableModelListener l) {

  }

  @Override
  public void removeTableModelListener(TableModelListener l) {

  }
}
