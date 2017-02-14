package se.kaskware.q10.ruler.gui;

import se.kaskware.gui.PleGroupNode;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/** Proudly produced by APLE02 on 2016-04-01 16:15. */
public class AttributeModel implements TableModel {
  private static final String[] S_COLUMNNAMES = {"Name", "Value"};
  private static final String[][] S_ROWS      = {
      {"Name", "Kalle"}, {"Age", "42"}, {"Address", "Storgatan 2"},
      {"Name", "Nisse"}, {"Age", "41"}, {"Address", "Storgatan 2"}
  };

  public AttributeModel(PleGroupNode prodDefs) {}

  @Override
  public int getRowCount() {
    return S_ROWS.length;
  }

  @Override
  public int getColumnCount() {
    return 2;
  }

  @Override
  public String getColumnName(int columnIndex) {
    return S_COLUMNNAMES[columnIndex];
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
    return S_ROWS[rowIndex][columnIndex];
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
}
