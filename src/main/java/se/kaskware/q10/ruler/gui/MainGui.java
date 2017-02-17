package se.kaskware.q10.ruler.gui;

import se.kaskware.gui.ApplicationGui;
import se.kaskware.gui.PleGroupNode;
import se.kaskware.gui.PleNode;
import se.kaskware.q10.ruler.nodes.Persister;
import se.kaskware.q10.ruler.nodes.RuleNode;
import se.kaskware.q10.ruler.nodes.SetupBook;

import com.intellij.uiDesigner.core.Spacer;
import org.jdesktop.swingx.JXCollapsiblePane;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import static se.kaskware.q10.ruler.nodes.RuleNode.operand.getOperand;

/**
 * Created by User: per on: 2014-10-31 at: 23:12
 */
public class MainGui implements Runnable {
  private JTextField  m_nameField;
  private JLabel      m_status;
  private JTree       m_tree;
  private JButton     m_toggleButton;
  private JPanel      m_topPane;
  private JPanel      m_mainPain;
  private JTabbedPane m_digramTabs;
  private JLabel      m_modelName;
  private JSplitPane  m_centerPane;
  private JComboBox   m_operandType;
  private JButton     m_createRuleButton;
  private JButton     m_removeRuleButton;
  private JButton     m_linkRuleButton;
  private JButton     m_createProductButton;
  private JButton     m_removeProductButton;
  private JTable      m_attributeTable;
  private JButton     m_expandButton;
  private JButton     m_collapseButton;
  private JButton     m_expandAllButton;
  private JFrame      m_mainFrame;
  private Spacer      m_spacer;

  private SettingsPane      m_settingsPane;
  private JXCollapsiblePane m_collapsePane;
  private DigramView        m_currentDiagram;
  private DiagramModel      m_diagramModel;
  private AttributeModel    m_tableModel;
  private PleGroupNode      m_root, m_rulesDefs, m_coefficients, m_dataDicts, m_prodDefs;
  private SetupBook m_book;
  private boolean   m_break;

  public MainGui() {
    initiateRuleModel();
    initiateTableModel();

    m_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    m_tree.setCellRenderer(new DefaultTreeCellRenderer() {
      @Override
      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                    boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (value instanceof ViewNode)
          setIcon(((ViewNode) value).getImage());
        return this;
      }
    });
    m_tree.setModel(m_diagramModel);
//    m_tree.setFont(new Font("DejaVu Sans", 0, 16));

    m_attributeTable.setModel(m_tableModel);
    setupPanes();
    setupListeners();
  }

  protected void initiateRuleModel() {
    m_rulesDefs = new PleGroupNode("Rules");
    m_coefficients = new PleGroupNode("Coefficients");
    m_dataDicts = new PleGroupNode("Data Dictionaries");
    m_prodDefs = new PleGroupNode("Products");
    m_root = new PleGroupNode("Product Catalog");
    m_root.getChildren().add(m_rulesDefs);
    m_root.getChildren().add(m_coefficients);
    m_root.getChildren().add(m_dataDicts);
    m_root.getChildren().add(m_prodDefs);

    PleGroupNode gNode = new PleGroupNode("Grupp Liv");
    gNode.getChildren().add(new PleGroupNode("Product Definitions"));
    gNode.getChildren().add(new PleGroupNode("Product Rules"));
    m_prodDefs.getChildren().add(gNode);

    gNode = new PleGroupNode("Hemförsäkring");
    gNode.getChildren().add(new PleGroupNode("Product Definitions"));
    gNode.getChildren().add(new PleGroupNode("Product Rules"));
    m_prodDefs.getChildren().add(gNode);

    m_diagramModel = new DiagramModel(m_root);
  }

  private void initiateTableModel() {
    m_tableModel = new AttributeModel(m_prodDefs);
  }

  protected void setupPanes() {
    m_spacer = new Spacer();

    m_settingsPane = new SettingsPane();

    m_collapsePane = new JXCollapsiblePane();
    m_collapsePane.setLayout(new BorderLayout());
    m_collapsePane.add("Center", m_settingsPane.m_basePane);
    Action action = m_collapsePane.getActionMap().get("toggle");
    action.putValue("Name", "Settings");
    m_toggleButton.setAction(action);

    m_currentDiagram = new DigramView("Noname");
    m_currentDiagram.setModel(m_diagramModel);
    m_digramTabs.addTab(m_currentDiagram.getName(), m_currentDiagram.getBasePane());
  }

  private void setupListeners() {
    m_tree.addTreeSelectionListener(tse -> {
      TreePath path = tse.getNewLeadSelectionPath();
      if (path == null) return;
      Object node = path.getLastPathComponent();
      ViewNode viewNode = null;
      if (node instanceof ViewNode) {
        viewNode = (ViewNode) node;
        ViewNode pNode = findRuleRoot(viewNode);
        if (pNode != m_diagramModel.getRuleRoot())
          m_diagramModel.setRuleRoot(pNode);
      }
      if (viewNode != null) {
        m_break = true;
        m_operandType.setSelectedItem(Character.toString(viewNode.getOperand().getChar()));

        scrollSelectedIntoView(viewNode);
        m_break = false;
      }

      m_diagramModel.setSelectedNode(viewNode);
      m_tableModel.setSelectedNode(viewNode);
      m_digramTabs.updateUI();
      m_attributeTable.updateUI();
    });

    m_diagramModel.addTreeModelListener(getTreeModelListener());
    m_currentDiagram.getBasePane().addMouseListener(getMouseListener());
    m_nameField.addKeyListener(getKeyAdapter());

    m_createRuleButton.addActionListener(getRuleActionListener());
    m_linkRuleButton.addActionListener(getRuleActionListener());
    m_removeRuleButton.addActionListener(getRuleActionListener());
    m_operandType.addItemListener(getItemListener());

    m_createProductButton.addActionListener(getProductActionListener());
    m_removeProductButton.addActionListener(getProductActionListener());

    m_expandButton.addActionListener(getNodeActionListener());
    m_expandAllButton.addActionListener(getNodeActionListener());
    m_collapseButton.addActionListener(getNodeActionListener());
  }

  private void scrollSelectedIntoView(ViewNode viewNode) {
    JViewport viewport = m_currentDiagram.getBasePane().getViewport();
    Rectangle rect = viewport.getViewRect();
    int nodeXPos = viewNode.getNodeXPos();
    int nodeYPos = viewNode.getNodeYPos();
    if (!rect.contains(nodeXPos, nodeYPos)) {
      // outside of visible area
      int h = (int) (rect.getY() + rect.getHeight()), newY;
      int w = (int) (rect.getX() + rect.getWidth()), newX;
      int rectX = (int) (nodeXPos - rect.getX());
      int rectY = (int) (nodeYPos - rect.getY());
      // right or in or left of view
      newX = w < nodeXPos ? (nodeXPos - w + (2 * ViewNode.S_NODEWIDTH)) : (rectX > 0 ? 0 : rectX - 10);
      // below or in or above view
      newY = h < nodeYPos ? (nodeYPos - h + ViewNode.S_NODEHEIGHT) : (rectY > 0 ? 0 : rectY - 10);
      rect.setLocation(newX, newY);
      viewport.scrollRectToVisible(rect);
    }
  }

  private ViewNode findRuleRoot(PleGroupNode viewNode) {
    PleGroupNode pNode = viewNode.getParent();
    if (pNode == m_rulesDefs)
      return (ViewNode) viewNode;
    return findRuleRoot(pNode);
  }

  private ItemListener getItemListener() {
    return itemEvent -> {
      Object src = itemEvent.getSource();
      if (src == m_operandType && !m_break) {
        ViewNode node = m_diagramModel.getSelectedNode();
        if (node == null) return;
        char ch = ((String) m_operandType.getSelectedItem()).charAt(0);
        node.setOperand(getOperand(ch));
        m_digramTabs.updateUI();
      }
    };
  }

  private TreeModelListener getTreeModelListener() {
    return new TreeModelAdapter() {
      @Override
      public void treeNodesChanged(TreeModelEvent e) {
        m_digramTabs.updateUI();  // current node was renamed using the tree
      }
    };
  }

  private MouseListener getMouseListener() {
    return new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent me) {
        int xPos = me.getX(), yPos = me.getY();
        ViewNode vNode = m_currentDiagram.checkSelection(xPos, yPos);
        m_diagramModel.setSelectedNode(vNode == null || m_diagramModel.getSelectedNode() == vNode ? null : vNode);
        m_tableModel.setSelectedNode(vNode == null || m_tableModel.getSelectedNode() == vNode ? null : vNode);
        if (vNode != null) {
          m_break = true;
          m_operandType.setSelectedItem(Character.toString(vNode.getOperand().getChar()));
          m_break = false;
        }
        TreePath path = new TreePath(m_root);
        path = m_rulesDefs.find(vNode, path);
        m_tree.setSelectionPath(path);
        updateAllViews();
      }
    };
  }

  private ActionListener getNodeActionListener() {
    return event -> {
      Object src = event.getSource();
      ViewNode viewNode = m_diagramModel.getSelectedNode();
      if (src == m_expandButton && viewNode != null) {
        viewNode.expandNode();
        computeNodePositions();
      }
      if (src == m_expandAllButton && viewNode != null) {
        viewNode.expandAllNode();
        computeNodePositions();
      }
      if (src == m_collapseButton && viewNode != null && viewNode.isExpanded()) {
        viewNode.collapseNode();
        computeNodePositions();
      }
      updateAllViews();
    };
  }

  private void computeNodePositions() {
    m_diagramModel.getRuleRoot().computePosition();
    m_currentDiagram.setDiagramViewSize();
  }

  private ActionListener getRuleActionListener() {
    return event -> {
      Object src = event.getSource();
      if (src == m_createRuleButton || src == m_removeRuleButton || src == m_linkRuleButton) {
        ViewNode selected = m_diagramModel.getSelectedNode();
        if (src == m_createRuleButton) {
          if (selected != null) {
            ViewNode newNode = new ViewNode(selected);  // add subRule
          }
          else { // add topRule
//              if (m_rulesDefs.getChildren().isEmpty()) {    // only one top node
            ViewNode newNode = new ViewNode(null);      // stupid shortcut to avoid unnecessary casting later on
            newNode.setParent(m_rulesDefs);             // stupid shortcut to avoid unnecessary casting later on
            m_book.getRules().add(newNode.getUserNode());
            m_rulesDefs.getChildren().add(newNode);
            m_diagramModel.setRuleRoot(newNode);
//              }
          }
        }
        if (src == m_removeRuleButton) {
          if (selected == null) return;
          expandAll(m_tree, new TreePath(selected.getParent()), false);
          m_book.getRules().remove(selected.getUserNode());
          selected.removeNode();
          if (selected == m_diagramModel.getRuleRoot()) {
            List<PleNode> nodes = m_rulesDefs.getChildren();
            m_diagramModel.setRuleRoot(nodes.isEmpty() ? null : (ViewNode) nodes.get(0));
          }
          m_diagramModel.setSelectedNode(null);
          m_tableModel.setSelectedNode(null);
        }
        computeNodePositions();
      }
      updateAllViews();
    };
  }

  private ActionListener getProductActionListener() {
    return event -> {
      Object src = event.getSource();
      if (src == m_createProductButton) {
        ViewNode newNode = new ViewNode(null);     // stupid shortcut to avoid unnecessary casting later on
        newNode.setParent(m_prodDefs);             // stupid shortcut to avoid unnecessary casting later on
        m_prodDefs.getChildren().add(newNode);
//          m_diagramModel.setRuleRoot(newNode);
      }
      if (src == m_removeProductButton && m_diagramModel.getSelectedNode() != null) {
      }
      updateAllViews();
    };
  }

  private void updateAllViews() {
    m_tree.updateUI();
    m_digramTabs.updateUI();
    m_attributeTable.updateUI();
  }

  protected KeyAdapter getKeyAdapter() {
    return new KeyAdapter() {
//      MusicInfo.musicInfoType m_infoType = infoType;

      @Override
      public void keyTyped(KeyEvent ke) {
        String query = m_nameField.getText().trim();
        if (ke.getKeyChar() == KeyEvent.VK_ENTER && query.length() > 0) {
          ViewNode viewNode = m_diagramModel.getSelectedNode();
          if (viewNode != null) {
            viewNode.setName(query);
            m_digramTabs.updateUI();
            m_tree.updateUI();
          }
        }
        else
          super.keyTyped(ke);
      }
    };
  }

  @Override
  public void run() {
    m_mainFrame = new JFrame("Pers Product Definer - ver. 0.1");
    m_mainFrame.setLayout(new BorderLayout());
    m_mainFrame.add("North", m_topPane);
    m_mainPain.add("North", m_collapsePane);
    m_mainFrame.add("Center", m_mainPain);
    m_mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    m_mainFrame.setLocation(50, 50);
    m_mainFrame.setSize(1300, 700);

    ApplicationGui.setupRunner(m_mainFrame);

    m_toggleButton.doClick();
    m_mainFrame.setVisible(true);
  }

  public static void main(String args[]) {
    ApplicationGui.setup();
    SwingUtilities.invokeLater(new MainGui());
  }

  public class SettingsPane {
    private JButton    m_loadSetupBookButton;
    private JButton    m_createSetupBookButton;
    private JPanel     m_basePane;
    private JComboBox  m_setupBooks;
    private JTextField m_bookNameField;
    private JButton    m_saveRulesButton;
    private JButton    m_loadRulesButton;
    private JComboBox  m_nodeColor;

    public SettingsPane() {
      setupPanes();
      setupSettingsListeners();
      loadBooks();
    }

    private void loadBooks() {
      m_setupBooks.removeAllItems();
      List<SetupBook> books = Persister.loadSetupBooks(); // should only return the root rule for now
      for (SetupBook name : books) {
        m_setupBooks.addItem(name);
      }
    }

    private void setupPanes() {
      m_nodeColor.setMaximumRowCount(DigramView.S_NODECOLORS.length);
      m_nodeColor.setRenderer(new MyCellRenderer());
      m_nodeColor.setSelectedIndex(0);
    }

    private void setupSettingsListeners() {
      m_loadSetupBookButton.addActionListener(getActionListener());
      m_createSetupBookButton.addActionListener(getActionListener());
      m_loadRulesButton.addActionListener(getActionListener());
      m_saveRulesButton.addActionListener(getActionListener());
      m_setupBooks.addItemListener(getItemListener());
      m_nodeColor.addItemListener(getItemListener());
    }

    private ItemListener getItemListener() {
      return itemEvent -> {
        Object src = itemEvent.getSource();
        if (src == m_nodeColor) {
          DigramView.setNodeColor(m_nodeColor.getSelectedIndex());
          m_digramTabs.updateUI();
        }
      };
    }

    ActionListener getActionListener() {
      return actionEvent -> {
        Object src = actionEvent.getSource();
        if (src == m_loadRulesButton) {
          List<PleNode> vNodes = m_rulesDefs.getChildren();
          expandAll(m_tree, new TreePath(m_root), false);
          // should only return the root rule for now
          SetupBook selectedItem = (SetupBook) m_setupBooks.getSelectedItem();
          m_modelName.setText(selectedItem.getName());
          m_book = Persister.loadRules(vNodes, selectedItem);
          if (m_book == null) {
            m_diagramModel.setRuleRoot(null);
          }
          else {
            for (RuleNode pleNode : m_book.getRules()) {
              ViewNode newNode = new ViewNode(m_rulesDefs, pleNode); // takes first one
              vNodes.add(newNode);
            }
            m_diagramModel.setRuleRoot(vNodes.isEmpty() ? null : (ViewNode) vNodes.get(0));
          }

          TreePath path = new TreePath(m_root).pathByAddingChild(m_rulesDefs);
          m_tree.setSelectionPath(path);
          updateAllViews();

          m_tree.updateUI();
          m_digramTabs.updateUI();
        }
        if (src == m_saveRulesButton) {
          if (m_rulesDefs.getChildren().isEmpty()) return;
          Persister.dumpRules(m_rulesDefs, m_prodDefs,
                              (SetupBook) m_setupBooks.getSelectedItem());  // always only one root
        }
        if (src == m_createSetupBookButton) {
          // make more general to save whole tree
          String bookName = m_bookNameField.getText().trim();
          if (bookName.isEmpty()) return;
          m_setupBooks.addItem(new SetupBook(bookName));
          List<SetupBook> bookNames = new ArrayList<>();
          for (int i = 0; i < m_setupBooks.getItemCount(); i++) {
            bookNames.add((SetupBook) m_setupBooks.getItemAt(i));
          }
          Persister.createSetupBook(bookNames);  // always only one root
        }
        // should be done automatically
        if (src == m_loadSetupBookButton) {
          loadBooks();
        }
      };
    }

    class MyCellRenderer extends JLabel implements ListCellRenderer {
      public MyCellRenderer() {
        setOpaque(true);
      }

      public Component getListCellRendererComponent(JList list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
        if (index < 0) return this;
        setText(value.toString());
        setBackground(DigramView.S_NODECOLORS[index]);

        return this;
      }
    }
  }

  private void expandAll(JTree tree, TreePath parent, boolean expandAll) {
    PleGroupNode lastNode = (PleGroupNode) parent.getLastPathComponent();
    List<PleNode> nodes = lastNode.getChildren();
    if (nodes.size() >= 0) {
      for (PleNode node : nodes) {
        TreePath path = parent.pathByAddingChild(node);
        expandAll(tree, path, expandAll);
      }
    }
    if (expandAll)
      tree.expandPath(parent);
    else
      tree.collapsePath(parent);
  }

  class TreeModelAdapter implements TreeModelListener {
    @Override
    public void treeNodesChanged(TreeModelEvent e) {
      System.out.println("treeNodesChanged");
    }

    @Override
    public void treeNodesInserted(TreeModelEvent e) {
      System.out.println("treeNodesInserted");
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent e) {
      System.out.println("treeNodesRemoved");
    }

    @Override
    public void treeStructureChanged(TreeModelEvent e) {
      System.out.println("treeStructureChanged");
    }
  }

  ;
}
