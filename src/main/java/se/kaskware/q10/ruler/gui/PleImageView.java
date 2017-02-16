package se.kaskware.q10.ruler.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by User: per on: 2014-11-04 at: 20:51
 */
public class PleImageView extends JPanel {
  private Image m_image;

  public void setImage(ImageIcon image) {
    m_image = image == null ? null : image.getImage();
  }

  public void paintComponent(Graphics gc) {
    super.paintComponent(gc);
    gc.drawRect(0, 0, getWidth(), getHeight());
    if (m_image != null) {
      gc.drawImage(m_image, 0, 0, null);
    }
  }
}
