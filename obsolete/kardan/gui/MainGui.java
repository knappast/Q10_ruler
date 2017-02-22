package se.kaskware.kardan.gui;

import se.kaskware.gui.Stacker;
import se.kaskware.kardan.gui.ViewObject;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/** Created by: Per Leander (10APLE02) at 2010-feb-26, Time 12:47:17 */
public abstract class MainGui implements Runnable {
  private JTextField m_statusField;
  private JFrame     m_mainframe;
  private Stacker    m_dataPanel;
  private boolean    m_overlayIsShowing;
  private ViewObject m_selectedVO;

  /** stupid gui generation */
  protected void setCommonPanes(JTextField field, JPanel mainPane) {
    m_statusField = field;
    m_dataPanel = new Stacker(mainPane);
  }

  public abstract String getWindowLabel();

  public ViewObject getSelectedVO() {
    return m_selectedVO;
  }

  public void setSelectedVO(ViewObject vo) {
    m_selectedVO = vo;
    m_statusField.setText("m_selected = " + m_selectedVO);
  }

  protected void showBanner(ActionEvent ae) {
    if (!m_overlayIsShowing && m_selectedVO != null)
      showCredits();
    else
      hideMessageLayer();
  }

  protected void showTaginfo() {
    hideMessageLayer();
    m_dataPanel.showMessageLayer(new JPanel(), .95f);
    m_overlayIsShowing = true;
  }

  protected void showCredits() {
    hideMessageLayer();
//    JLabel credits = new JLabel("<html><p align=\"center\">Product Provided<br>courtesy of Per Leander</p></html>");
    String text = "<html><p align=\"center\">Folksam System " + m_selectedVO.getName()
                  + "<br><br><i><b>Kardan</b></i><br>Provided courtesy<br>of Per Leander</p></html>";
    JLabel credits = new JLabel(text);
    credits.setFont(UIManager.getFont("Table.font").deriveFont(24f));
    credits.setHorizontalAlignment(JLabel.CENTER);
    credits.setBorder(new CompoundBorder(new TitledBorder(""), new EmptyBorder(20, 20, 20, 20)));
    m_dataPanel.showMessageLayer(credits, .75f);
  }

  protected void hideMessageLayer() {
    m_dataPanel.hideMessageLayer();
    m_overlayIsShowing = false;
    m_dataPanel.revalidate();
  }

  protected int getDefaultHeight() {return 1000;}
  protected int getDefaultWidth() {return 1200;}

  public void run() {
    m_mainframe = new JFrame(getWindowLabel());
    m_mainframe.setContentPane(m_dataPanel);
    m_mainframe.pack();
    m_mainframe.setSize(getDefaultWidth(), getDefaultHeight());

    Rectangle screenRect = m_mainframe.getGraphicsConfiguration().getBounds();
    Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(m_mainframe.getGraphicsConfiguration());

    // Make sure we don't place the demo off the screen.
    int centerWidth = screenRect.width < m_mainframe.getSize().width
        ? screenRect.x
        : screenRect.x + screenRect.width / 2 - m_mainframe.getSize().width / 2;
    int centerHeight = screenRect.height < m_mainframe.getSize().height
        ? screenRect.y
        : screenRect.y + screenRect.height / 2 - m_mainframe.getSize().height / 2;

    centerHeight = centerHeight < screenInsets.top ? screenInsets.top : centerHeight;

    m_mainframe.setLocation(centerWidth, centerHeight);
    m_mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    m_mainframe.setVisible(true);
  }
}
