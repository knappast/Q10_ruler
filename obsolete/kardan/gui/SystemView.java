package se.kaskware.kardan.gui;

import se.kaskware.kardan.bo.FolkGroup;
import se.kaskware.kardan.bo.FolkSystem;
import se.kaskware.kardan.setups.FolkConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;

import static se.kaskware.kardan.gui.ViewObject.sides.*;

/** Created by: Per Leander (10APLE02) at 2010-feb-26, Time 13:26:54 */
public class SystemView extends JPanel {
  public static final int BOXWIDTH = 50, X_GAP = 2, BOXHEIGHT = 25, Y_GAP = 5;

  private TreeMap<String, FolkSystem> m_systems;
  private TreeMap<String, ViewObject> m_allVOs;
  private TreeMap<String, FolkGroup> m_groups;
  private boolean m_isInitalized;
  private MainGuiKardan m_maingui;
  private boolean m_isShiftDown, m_isCtrlDown;
  private ArrayList<ViewObject> m_selectedVO = new ArrayList<ViewObject>();
  private TreeMap<String, FolkSystem> m_currentSystems;
  private TreeMap<String, FolkGroup> m_currentGroups = new TreeMap<String, FolkGroup>();

  public SystemView(MainGuiKardan gui, TreeMap<String, FolkSystem> systems, TreeMap<String, FolkGroup> groups) {
    m_maingui = gui;
    m_systems = systems;
    m_groups = groups;

    addMouseListener(getMouseListener());
  }

  public Dimension getPreferredSize() {
    int size = getSystems().size();
    int sideSize = size / 4 + 4 + ((size % 4) == 0 ? 1 : 0);

    return new Dimension(25 + sideSize * (BOXWIDTH + X_GAP), 10 + sideSize * (BOXHEIGHT + Y_GAP));
  }

  private TreeMap<String, FolkGroup> getGroups() {
    if (m_selectedVO != null) {
      if (m_currentGroups == null) {
        m_currentGroups = new TreeMap<String, FolkGroup>();
        TreeMap<String, FolkSystem> systems = getSystems();
        for (FolkSystem system : systems.values()) {
          FolkGroup group = system.getGroup();
          m_currentGroups.put(group.getName(), group);
        }
        for (String key : m_currentGroups.keySet()) {
          System.out.println("group = " + key);
        }
      }
    }
    else
      m_currentGroups = m_groups;

    return m_currentGroups;
  }

  /** get selected Systems connections */
  private TreeMap<String, FolkSystem> getSystems() {
    if (m_selectedVO.isEmpty()) {
      m_currentSystems = m_systems;
    }

    if (m_currentSystems != null)
      return m_currentSystems;

    m_currentSystems = new TreeMap<String, FolkSystem>(new Comparator() {
      public int compare(Object o1, Object o2) {
        String f1 = (String) o1, f2 = (String) o2;
        return f1.compareTo(f2);
      }
    });
    for (ViewObject vo : m_selectedVO) {
      m_currentSystems.put(vo.getName(), vo.getSystem());
      for (FolkConnection connection : vo.getFromConnections()) {
        m_currentSystems.put(connection.getToApp().getName(), connection.getToApp());
      }
      for (FolkConnection connection : vo.getToConnections()) {
        m_currentSystems.put(connection.getToApp().getName(), connection.getToApp());
      }
      m_currentGroups = null;
    }

    return m_currentSystems;
  }

  private MouseListener getMouseListener() {
    MouseListener listener = new MouseAdapter() {
      public void mouseClicked(MouseEvent me) {
        ViewObject selected = null;
        boolean isShift = false, isAlt = false, isCtrl = false;
        for (ViewObject vo : m_allVOs.values()) {
          Point point = me.getPoint();
          point.translate(-25, -10);
          if (vo.contains(point)) {
            selected = vo;
            isShift = me.isShiftDown();
            isAlt = me.isAltDown();
            isCtrl = me.isControlDown();
            m_maingui.setSelectedVO(vo);
            break;
          }
        }
        m_currentSystems = null;
        // to keep viewing selected in graphview but able to see info about it in mainview
        if (selected == null) {
          m_selectedVO = new ArrayList<ViewObject>();  // empty the selectionlist
        }
        else if (!isAlt) {  // if alt do nothing
          if (isCtrl) {  // only add a new node to selection if its not there else remove it
            int index = 0;
            for (ViewObject vo : m_selectedVO) {
              if (vo.getName().equals(selected.getName())) break;
              index++;
            }
            if (index < m_selectedVO.size())
              m_selectedVO.remove(index);
            else
              m_selectedVO.add(selected);
          }
          else {// just one selection
            m_selectedVO = new ArrayList<ViewObject>();  // empty the selectionlist
            m_selectedVO.add(selected);
          }
        }
//        m_isShiftDown = isShift;
        m_isCtrlDown = isCtrl;
        // always set m_mainview to last selection for displaying info
        // unless it meant selected was "removed" thereby emptying the selectionlist
        m_maingui.setSelectedVO(m_selectedVO.isEmpty() ? null : selected);
        m_isInitalized = false;
      }
    };
    return listener;
  }

  private void initViewObjects(Graphics2D gc2) {
    if (m_selectedVO == null)
      initAll(gc2);
    else
      initSelected(gc2);
  }

  private void initAll(Graphics2D gc2) {
    int size = getSystems().size(), total = 0, bogg = 0;
    int sideSize = size / 4 + 2 + ((size % 4) == 0 ? 1 : 0), col = 0, row = 0;

    m_allVOs = new TreeMap<String, ViewObject>();
    Font font = gc2.getFont().deriveFont(10.0f);
    Object params[] = new Object[]{row, col, sideSize, font, null};
    for (FolkGroup group : getGroups().values()) {
      int r = 255 - (bogg * 10);
      int g = Math.min(255, 125 + (bogg * 8));
      int b = Math.max(0, 255 - (bogg++ * 5));
      params[4] = new Color(bogg % 3 == 0 ? r : g, bogg % 2 == 0 ? g : b, bogg % 2 == 0 ? b : g);
      params = init(group.getSystems(), gc2, false, params);
    }
    System.out.println("------------------------------");
    m_isInitalized = true;
  }

  private void initSelected(Graphics2D gc2) {
    int size = getSystems().size();
    int sideSize = size / 4 + 2 + ((size % 4) == 0 ? 1 : 0), col = 0, row = 0;
    Font font = gc2.getFont().deriveFont(10.0f);
    Object params[] = new Object[]{row, col, sideSize, font, null};

    m_allVOs = new TreeMap<String, ViewObject>();

    init(m_currentSystems.values(), gc2, true, params);
    m_isInitalized = true;
  }

  private Object[] init(Collection<FolkSystem> systems, Graphics2D gc2, boolean changeColor, Object[] params) {
    int row = (Integer) params[0], col = (Integer) params[1], sideSize = (Integer) params[2], bogg = 0;
    Font font = (Font) params[3];
    Color color = (Color) params[4];
    FolkGroup curGroup = null;

    for (FolkSystem app : systems) {
      ViewObject vo = new ViewObject(app);
      m_allVOs.put(app.getName(), vo);
      FolkGroup group = app.getGroup();
      if (changeColor && group != curGroup) {  // used when not all systems are visible
        int r = Math.max(0, 255 - (bogg * 10));
        int g = Math.min(255, 125 + (bogg * 8));
        int b = Math.max(0, 255 - (bogg * 5));
        bogg++;
        color = new Color(bogg % 3 == 0 ? r : g, bogg % 2 == 0 ? g : b, bogg % 2 == 0 ? b : g);
        curGroup = group;
      }
      vo.setFont(font);
      vo.setColor(color);

      gc2.setFont(font);
      Rectangle2D bnd = gc2.getFontMetrics().getStringBounds(app.getName(), gc2);

      int position = row * 1000 + col;
      position = checkPlace(vo, position, sideSize);
      row = position / 1000;
      col = position % 1000;
      vo.setPos((BOXWIDTH + X_GAP) * col, (BOXHEIGHT + Y_GAP) * row);
      vo.setTextPos((BOXWIDTH + X_GAP) * col + (int) (BOXWIDTH - bnd.getWidth()) / 2, (BOXHEIGHT + Y_GAP) * row + 16);
    }
    params[0] = row;
    params[1] = col;
    return params;
  }

  private int checkPlace(ViewObject vo, int position, int sideSize) {
    int row = position / 1000, col = position % 1000;
    if (row == 0 && col < sideSize - 1) {
      vo.setSide(top);
      col++;
      return row * 1000 + col;
    }
    if (row == 0 && col == sideSize - 1) { col++; }
    if (col == sideSize && row < sideSize - 1) {
      vo.setSide(right);
      row++;
      return row * 1000 + col;
    }
    if (col == sideSize && row == sideSize - 1) { row++; }
    if (col > 1) {
      vo.setSide(bottom);
      col--;
      return row * 1000 + col;
    }
    vo.setSide(left);
    col = 0;
    row--;

    return row * 1000 + col;
  }

  public void paintComponent(Graphics gc) {
    Graphics2D gc2 = (Graphics2D) gc;
    gc2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    gc2.setColor(new Color(255, 255, 204));
    gc2.fillRect(0, 0, (int) getBounds().getWidth(), (int) getBounds().getHeight());
    gc2.setStroke(new BasicStroke(1));
    if (!m_isInitalized) initViewObjects(gc2);

    int xStart = 25, yStart = 10;
    for (ViewObject fromVO : m_allVOs.values()) {
      int fromX = fromVO.getXPos(), fromY = fromVO.getYPos();
      gc2.setColor(Color.BLACK);

      BasicStroke stroke = new BasicStroke(2);
      gc2.setStroke(stroke);
      if (m_selectedVO.isEmpty()) {
        drawConnections(gc2, xStart, yStart, fromVO, true);
      }
      else
        for (ViewObject vo : m_selectedVO) {
          if (vo.getName().equalsIgnoreCase(fromVO.getName())) {
            if (m_isShiftDown)
              drawChain(gc2, xStart, yStart, fromVO);
            else {
              drawConnections(gc2, xStart, yStart, fromVO, true);
              drawConnections(gc2, xStart, yStart, fromVO, false);
            }
          }
        }

      gc2.setStroke(new BasicStroke(1));
      gc2.setColor(fromVO.getColor());
      gc2.fillRoundRect(xStart + fromX, yStart + fromY, BOXWIDTH, BOXHEIGHT, 10, 10);
      gc2.setColor(Color.BLACK);
      gc2.drawRoundRect(xStart + fromX + 1, yStart + fromY + 1, BOXWIDTH - 2, BOXHEIGHT - 2, 10, 10);

      Color comps = fromVO.getColor();    // to enhance contrast, not scientific
      if (comps.getBlue() > 200 && comps.getGreen() < 150 && comps.getRed() < 150)
        gc2.setColor(Color.white);
      gc2.setFont(fromVO.getFont());
      gc2.drawString(fromVO.getName(), xStart + fromVO.getXTextPos(), yStart + fromVO.getYTextPos());
    }
//    gc2.setColor(new Color(180, 120, 120));
//    gc2.setColor(new Color(120, 180, 120));
  }

  private void drawChain(Graphics2D gc2, int xStart, int yStart, ViewObject fromVO) {
    gc2.setColor(Color.YELLOW.darker());
    ViewObject fromApp = fromVO;
    for (FolkConnection conn : fromVO.getChain()) {
      ViewObject toApp = m_allVOs.get(conn.getToApp().getName());
      drawConn(gc2, xStart, yStart, fromApp, toApp, true);
      fromApp = toApp;
    }
  }

  private void drawConnections(Graphics2D gc2, int xStart, int yStart, ViewObject fromVO, boolean toApp) {
    gc2.setColor(toApp ? Color.RED : Color.BLUE);
    ArrayList<FolkConnection> list = toApp ? fromVO.getToConnections() : fromVO.getFromConnections();
    for (FolkConnection conn : list) {
      ViewObject toVO = m_allVOs.get(conn.getToApp().getName());
      drawConn(gc2, xStart, yStart, fromVO, toVO, toApp);
    }
  }

  private void drawConn(Graphics2D gc2, int xStart, int yStart, ViewObject fromVO, ViewObject toVO, boolean toApp) {
    int fromX = fromVO.getXPos(), fromY = fromVO.getYPos();
    CubicCurve2D.Double curve;
    switch (fromVO.getSide()) {
      case top: {
        curve = topCurve(toVO, fromX, fromY, xStart, yStart);
        break;
      }
      case right: {
        curve = rightCurve(toVO, fromX, fromY, xStart, yStart);
        break;
      }
      case bottom: {
        curve = bottomCurve(toVO, fromX, fromY, xStart, yStart);
        break;
      }
      case left: {
        curve = leftCurve(toVO, fromX, fromY, xStart, yStart);
        break;
      }
      default:
        throw new RuntimeException("kalle kalas kula: " + fromVO.getName());
    }
    int xEnd, xCtrl, yEnd, yCtrl;
    if (toApp) {
      xEnd = (int) curve.getX2();
      xCtrl = (int) curve.getCtrlX2();
      yEnd = (int) curve.getY2();
      yCtrl = (int) curve.getCtrlY2();
    }
    else {
      xEnd = (int) curve.getX1();
      xCtrl = (int) curve.getCtrlX1();
      yEnd = (int) curve.getY1();
      yCtrl = (int) curve.getCtrlY1();
    }
    drawArrow(gc2, xCtrl, yCtrl, xEnd, yEnd, 1);
//    gc2.drawLine(xEnd, fromY, xEnd-4, fromY-4);
//    gc2.drawLine(yEnd, fromY, yEnd-4, fromY+4);
    gc2.draw(curve);
  }

  private void drawArrow(Graphics2D g2d, int xCenter, int yCenter, int x, int y, float stroke) {
    double aDir = Math.atan2(xCenter - x, yCenter - y);
    g2d.drawLine(x, y, xCenter, yCenter);
    g2d.setStroke(new BasicStroke(1f));     // make the arrow head solid even if dash pattern has been specified
    Polygon tmpPoly = new Polygon();
    int i1 = 12 + (int) (stroke * 2);
    int i2 = 6 + (int) stroke;              // make the arrow head the same size regardless of the length length
    tmpPoly.addPoint(x, y);                 // arrow tip
    tmpPoly.addPoint(x + xCor(i1, aDir + 0.5), y + yCor(i1, aDir + 0.5));
    tmpPoly.addPoint(x + xCor(i2, aDir), y + yCor(i2, aDir));
    tmpPoly.addPoint(x + xCor(i1, aDir - 0.5), y + yCor(i1, aDir - 0.5));
    tmpPoly.addPoint(x, y);                 // arrow tip
    g2d.drawPolygon(tmpPoly);
    g2d.fillPolygon(tmpPoly);                 // remove this line to leave arrow head unpainted
  }

  private int yCor(int len, double dir) {return (int) (len * Math.cos(dir));}

  private int xCor(int len, double dir) {return (int) (len * Math.sin(dir));}

  private CubicCurve2D.Double topCurve(ViewObject to, int fromX, int fromY, int xStart, int yStart) {
    int toX = to.getXPos() + xStart, toY = to.getYPos() + yStart;
    int adjFromX = 0, adjFromY = 0, adjToX = 0, adjToY = 0;

    int x = fromX + BOXWIDTH / 2 + xStart;
    int y = fromY + BOXHEIGHT + yStart;
    switch (to.getSide()) {
      case left: {
        toX = toX + BOXWIDTH;
        toY = toY + BOXHEIGHT / 2;
        adjFromY = Math.max((toY - y) / 4, BOXHEIGHT);
        break;
      }
      case top: {
        toX = toX + BOXWIDTH / 2;
        toY = toY + BOXHEIGHT;
        adjFromY = BOXHEIGHT / 2;
        adjToY = -BOXHEIGHT / 2;
        break;
      }
      case bottom: {
        toX = toX + BOXWIDTH / 2;
        adjToY = Math.max((toY - y) / 4, BOXHEIGHT / 2);
        break;
      }
      case right: {
        toY = toY + BOXHEIGHT / 2;
        adjFromY = Math.max((toY - y) / 4, BOXHEIGHT);
        break;
      }
    }
    return new CubicCurve2D.Double(x, y, x + adjFromX, y + adjFromY, toX - adjToX, toY - adjToY, toX, toY);
  }

  private CubicCurve2D.Double bottomCurve(ViewObject to, int fromX, int fromY, int xStart, int yStart) {
    int toX = to.getXPos() + xStart, toY = to.getYPos() + yStart;
    int adjFromX = 0, adjFromY = 0, adjToX = 0, adjToY = 0;

    int x = fromX + BOXWIDTH / 2 + xStart;
    int y = fromY + yStart;
    switch (to.getSide()) {
      case left: {
        toX = toX + BOXWIDTH;
        toY = toY + BOXHEIGHT / 2;
        adjFromX = BOXWIDTH;
        adjFromY = -Math.max((toY - y) / 4, BOXHEIGHT / 2);
        adjToX = -BOXWIDTH;
        break;
      }
      case top: {
        toX = toX + BOXWIDTH / 2;
        toY = toY + BOXHEIGHT;
        break;
      }
      case bottom: {
        toX = toX + BOXWIDTH / 2;
        adjFromX = Math.min(BOXWIDTH, (toX - x) / 4);
        adjFromY = -BOXHEIGHT;
        adjToX = Math.min(BOXWIDTH, (toX - x) / 4);
        adjToY = BOXHEIGHT;
        break;
      }
      case right: {
        toY = toY + BOXHEIGHT / 2;
        adjFromX = (toX - x) / 4;
        adjFromY = -Math.max((toY - y) / 4, BOXHEIGHT / 2);
        adjToX = (toX - x) / 4;
        break;
      }
    }
    return new CubicCurve2D.Double(x, y, x + adjFromX, y + adjFromY, toX - adjToX, toY - adjToY, toX, toY);
  }

  private CubicCurve2D.Double rightCurve(ViewObject to, int fromX, int fromY, int xStart, int yStart) {
    int toX = to.getXPos() + xStart, toY = to.getYPos() + yStart;
    int adjFromX = 0, adjFromY = 0, adjToX = 0, adjToY = 0;

    int x = fromX + xStart;
    int y = fromY + BOXHEIGHT / 2 + yStart;
    switch (to.getSide()) {
      case left: {
        toX = toX + BOXWIDTH;
        toY = toY + BOXHEIGHT / 2;
        adjToX = -BOXWIDTH;
        break;
      }
      case top: {
        toX = toX + BOXWIDTH / 2;
        toY = toY + BOXHEIGHT;
        adjFromX = -Math.max(BOXWIDTH, (toX - x) / 4);
        break;
      }
      case bottom: {
        toX = toX + BOXWIDTH / 2;
        adjFromX = -Math.max(BOXWIDTH, (toX - x) / 4);
        adjToY = Math.max((toY - y) / 4, BOXHEIGHT / 2);
        break;
      }
      case right: {
        toY = toY + BOXHEIGHT / 2;
        adjFromX = -BOXWIDTH / 2;
        adjToX = BOXWIDTH / 2;
        break;
      }
    }
    return new CubicCurve2D.Double(x, y, x + adjFromX, y + adjFromY, toX - adjToX, toY - adjToY, toX, toY);
  }

  private CubicCurve2D.Double leftCurve(ViewObject to, int fromX, int fromY, int xStart, int yStart) {
    int toX = to.getXPos() + xStart, toY = to.getYPos() + yStart;
    int adjFromX = 0, adjFromY = 0, adjToX = 0, adjToY = 0;

    int x = fromX + BOXWIDTH + xStart;
    int y = fromY + BOXHEIGHT / 2 + yStart;
    switch (to.getSide()) {
      case left: {
        toX = toX + BOXWIDTH;
        toY = toY + BOXHEIGHT / 2;
        adjFromX = BOXWIDTH / 2;
        adjToX = -BOXWIDTH / 2;
        break;
      }
      case top: {
        toX = toX + BOXWIDTH / 2;
        toY = toY + BOXHEIGHT;
        adjFromX = Math.max(BOXWIDTH, (toX - x) / 4);
        adjToY = -Math.max((toY - y) / 4, BOXHEIGHT / 2);
        break;
      }
      case bottom: {
        toX = toX + BOXWIDTH / 2;
        adjFromX = BOXWIDTH / 2;
        adjToY = Math.max((toY - y) / 4, BOXHEIGHT / 2);
        break;
      }
      case right: {
        toY = toY + BOXHEIGHT / 2;
        adjFromX = (toX - x) / 4;
        adjToX = (toX - x) / 4;
        break;
      }
    }
    return new CubicCurve2D.Double(x, y, x + adjFromX, y + adjFromY, toX - adjToX, toY - adjToY, toX, toY);
  }
}
