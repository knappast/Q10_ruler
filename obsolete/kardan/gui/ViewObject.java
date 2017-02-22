package se.kaskware.kardan.gui;

import se.kaskware.kardan.bo.FolkSystem;
import se.kaskware.kardan.setups.FolkConnection;

import java.awt.*;
import java.util.ArrayList;

/** Created by User: per at: 2010-apr-02, 21:30:27 */
public class ViewObject {

  public enum sides {top, right, bottom, left, topleft, topright, bottomleft, bottomright};

  private FolkSystem m_system;
  private sides m_viewSide;
  private Font m_font;
  private Color m_color;
  private int m_xPos, m_yPos;
  private int m_xTextPos, m_yTextPos;

  public ViewObject(FolkSystem app) {
    m_system = app;
  }

  public String getName() {
    return m_system.getName();
  }

  public FolkSystem getSystem() {
    return m_system;
  }

  public Font getFont() {
    return m_font;
  }

  public int getXPos() {
    return m_xPos;
  }

  public int getYPos() {
    return m_yPos;
  }

  public int getXTextPos() {
    return m_xTextPos;
  }

  public int getYTextPos() {
    return m_yTextPos;
  }

  public Color getColor() {
    return m_color;
  }

  public void setFont(Font font) {
    m_font = font;
  }

  public void setColor(Color color) {
    m_color = color;
  }

  public void setPos(int x, int y) {
    m_xPos = x;
    m_yPos = y;
  }

  public void setTextPos(int x, int y) {
    m_xTextPos = x;
    m_yTextPos = y;
  }

  public ArrayList<FolkConnection> getToConnections() {
    return m_system.getToConnections();
  }

  public ArrayList<FolkConnection> getFromConnections() {
    return m_system.getFromConnections();
  }

  public ArrayList<FolkConnection> getChain() {
    return m_system.getChain();
  }

  public sides getSide() {
    return m_viewSide;
  }

  public void setSide(sides side) {
    m_viewSide = side;
  }

  public boolean contains(Point point) {
    return (m_xPos <= point.x && point.x <= m_xPos + SystemView.BOXWIDTH)
        && (m_yPos <= point.y && point.y <= m_yPos + SystemView.BOXHEIGHT);
  }

  public String toString() {
    return getName() + ' ' + m_xPos + '@' + m_yPos + " at: " + m_viewSide;
  }
}
