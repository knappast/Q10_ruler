package se.kaskware.kardan.gui;

import se.kaskware.kardan.bo.FolkGroup;
import se.kaskware.kardan.bo.FolkSystem;
import se.kaskware.kardan.setups.FolkSystemFactory;
import se.kaskware.kardan.setups.SystemFieldNames;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jdesktop.swingx.JXTaskPane;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;

import static com.intellij.uiDesigner.core.GridConstraints.*;

/** Created by: Per Leander (10APLE02) at 2010-feb-26, Time 12:47:17 */
public class MainGuiKardan extends MainGui {
  private        JTextField               m_statusField;
  private        JButton                  m_showStats;
  private        JPanel                   m_bugger;
  private        JPanel                   m_mainPane;
  private        JButton                  m_showInfo;
  private        JPanel                   m_top;
  private        JTable[]                 m_rawDataModels;
  private        JTree                    m_systemTree;
  private        JScrollPane              m_treeScroller;
  private        JPanel                   m_infoPane;
  private        SystemInfoView           m_infoView;
  private static DefaultTableCellRenderer s_renderer;

  public MainGuiKardan() {
    m_infoView = new SystemInfoView(this);
    m_top.add(m_infoView.getBasepane());

    m_showInfo.addActionListener(getActionListener());
    m_showStats.addActionListener(getActionListener());
    setCommonPanes(m_statusField, m_mainPane);

    final TreeMap<String, FolkSystem> systems = FolkSystemFactory.getAllSystems();
    TreeMap<String, FolkGroup> groups = FolkSystemFactory.getAllGroups();

    m_bugger.add(new SystemView(this, systems, groups));

    SystemFieldNames.areaName[] areaNames = SystemFieldNames.areaName.values();
    m_infoPane.setLayout(new GridLayoutManager(areaNames.length + 1, 1, new Insets(0, 0, 0, 0), -1, -1));

    int row = 0;
    boolean first = false;
    TreeMap<SystemFieldNames.areaName, String> groupLabels = SystemFieldNames.getGroupLabels();
    m_rawDataModels = new JTable[groupLabels.size()];
    for (SystemFieldNames.areaName area : areaNames) {
      SystemTableModel tableModel = new SystemTableModel(area);
      JTable table = new JTable(tableModel);
      table.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
      table.setDefaultRenderer(String.class, getDefaultRenderer());
      table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);
      TableColumnModel columnModel = table.getColumnModel();
      columnModel.getColumn(0).setPreferredWidth(150);
      columnModel.getColumn(1).setPreferredWidth(780);
      columnModel.getColumn(0).setCellRenderer(getDefaultRenderer());
      columnModel.getColumn(1).setCellRenderer(getDefaultRenderer());
      m_rawDataModels[row] = table;

      JXTaskPane tpane = new JXTaskPane(); //groupLabels.get(area));
      tpane.setCollapsed(first);
      first = true;
      tpane.setFont(new Font("Chalkduster", Font.BOLD, 15));
      tpane.add(new JScrollPane(table));
      tpane.getContentPane().getInsets().set(3,4,4,4);
      m_infoPane.add(tpane, new GridConstraints(row, 0, 1, 1,
                                                ANCHOR_CENTER, FILL_BOTH,
                                               SIZEPOLICY_CAN_SHRINK | SIZEPOLICY_WANT_GROW,
                                               SIZEPOLICY_CAN_SHRINK | SIZEPOLICY_WANT_GROW,
                                                null, null, null, 0, false));
      tpane.add(new Spacer(), new GridConstraints(row, 0, 1, 1, ANCHOR_CENTER, FILL_VERTICAL, 1,
                                                  SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
      ++row;
    }

    m_infoPane.add(new Spacer(), new GridConstraints(row, 0, 1, 1, ANCHOR_CENTER, FILL_VERTICAL, 1,
                                                     SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
  }

  private TableCellRenderer getDefaultRenderer() {
    if (s_renderer == null)
     s_renderer = new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                     int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        setBackground(isSelected ? Color.lightGray.brighter() : Color.white);
        setForeground(isSelected ? Color.red.darker() : Color.black);

        // Configure the component with the specified value
        setText(value.toString());

        // Since the renderer is a component, return itself
        return this;
      }
    };
    return s_renderer;
  }

  @Override
  public String getWindowLabel() {
    return "Kardan ver. 0.1";
  }

  public void setSelectedVO(ViewObject vo) {
    super.setSelectedVO(vo);
    m_infoView.setInfo(vo);
    m_bugger.updateUI();
  }

  protected ActionListener getActionListener() {
    ActionListener listener = new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equalsIgnoreCase("Visa Info")) {
//          JDialog dialog = new JDialog(m_mainframe, "System Information", true);
//          dialog.setLayout(new BorderLayout());
//          dialog.add(m_, "Center");
//          dialog.setVisible(true);
        }
        if (ae.getActionCommand().equalsIgnoreCase("Banner"))
          showBanner(ae);
      }
    };
    return listener;
  }

  public static void main(String args[]) {
    SwingUtilities.invokeLater(new MainGuiKardan());
  }

  private void createUIComponents() {
    GroupNode root = new GroupNode("All Systems"), charGrp = null;
    GroupNode alpha = new GroupNode("Alfabet i skåning");
    root.addNode(alpha);
    char firstChar = ' ';
    TreeMap<String, FolkSystem> allSystems = FolkSystemFactory.getAllSystems();
    for (Map.Entry<String, FolkSystem> entry : allSystems.entrySet()) {
      String key = entry.getKey();
      if (key.charAt(0) != firstChar) {
        firstChar = key.charAt(0);
        charGrp = new GroupNode(Character.toString(Character.toUpperCase(key.charAt(0))) + " ...");
        alpha.addNode(charGrp);
      }
      charGrp.addNode(new FolkNode(entry));
    }

    DefaultTreeModel treeModel = new DefaultTreeModel(root);
    m_systemTree = new JTree(treeModel);
    m_systemTree.addTreeSelectionListener(new TreeSelectionListener() {
      @Override
      public void valueChanged(TreeSelectionEvent tse) {
        FolkNode src = null;
        TreePath path = tse.getNewLeadSelectionPath();
        if (path != null) {
          src = (FolkNode) path.getLastPathComponent();
        }
        int row = 0;
        SystemFieldNames.areaName[] areaNames = SystemFieldNames.areaName.values();
        for (JTable table : m_rawDataModels) {
          ((SystemTableModel) table.getModel()).setNode(src);
          Container parent = table; //.getParent(); //.getParent(); //.getParent();
          table.setFillsViewportHeight(true);
          parent.setPreferredSize(new Dimension(parent.getWidth(),
                                       (table.getRowCount() + 2) * table.getRowHeight()));
          table.updateUI();
          ++row;
        }
      }
    });
  }
}
