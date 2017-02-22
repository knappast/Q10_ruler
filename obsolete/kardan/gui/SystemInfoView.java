package se.kaskware.kardan.gui;

import se.kaskware.kardan.bo.FolkSystem;

import javax.swing.*;

/** Created by: Per Leander (APLE02) at: 2010-aug-03, 14:00:19 */
public class SystemInfoView {
  private JLabel        m_systemName;
  private JTextField    m_owner;
  private JTextField    m_maintainer;
  private JTextField    m_dependsFrom;
  private JTextField    m_dependsOn;
  private JTextPane     m_description;
  private JPanel        m_basePane;
  private MainGuiKardan m_mainFrame;

  public SystemInfoView(MainGuiKardan mainGui) {
    m_mainFrame = mainGui;
  }

  public JPanel getBasepane() {
    return m_basePane;
  }

  public void setInfo(ViewObject selectedVO) {
    if (selectedVO == null) {
      clearFields();
      return;
    }
    FolkSystem folkSystem = selectedVO.getSystem();
    m_systemName.setText(folkSystem.getName());
    m_description.setText(folkSystem.getDescription());
  }

  private void clearFields() {
    m_systemName.setText("Inget valt");
    m_owner.setText("");
    m_maintainer.setText("");
    m_dependsFrom.setText("");
    m_dependsOn.setText("");
    m_description.setText("");
  }
}
