package se.kaskware.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Created by: per; Date: 2012-10-22 21:02 */
public class AboutPilsner extends JDialog {
  private JPanel  contentPane;
  private JButton buttonOK;
  private JLabel  m_titleLabel;
  private JButton m_image;

  public AboutPilsner(JFrame frame) {
    super(frame, "Kaskware Inc");
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(buttonOK);
//    m_titleLabel.setText(frame.getTitle());
    m_titleLabel.setText("Pilsnerdricka");
    buttonOK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {onOK();}
    });
  }

  private void onOK() {
// add your code here
    dispose();
  }
}
