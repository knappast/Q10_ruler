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
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import static se.kaskware.q10.ruler.nodes.RuleNode.operand.getOperand;

/**
 * Created by User: per on: 2014-10-31 at: 23:12
 */
public class MainGui implements Runnable {
  private JTextField m_nameField;
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
  private JFrame      m_mainFrame;
  private Spacer      m_spacer;

  private SettingsPane      m_settingsPane;
  private JXCollapsiblePane m_collapsePane;
  private DigramView        m_currentDiagram;
  private DiagramModel      m_diagramModel;
  private TableModel        m_tableModel;
  private PleGroupNode      m_root, m_rulesDefs, m_prodDefs;
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
    m_prodDefs = new PleGroupNode("Products");
    m_root = new PleGroupNode("Product Setups");
    m_root.getChildren().add(m_rulesDefs);
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
      TreePath path = tse.getPath();
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
        m_break = false;
      }

      m_diagramModel.setSelectedNode(viewNode);
      m_digramTabs.updateUI();
    });

    m_diagramModel.addTreeModelListener(getTreeModelListener());
    m_currentDiagram.getBasePane().addMouseListener(getMouseListener());
    m_nameField.addKeyListener(getKeyAdapter());

    m_createRuleButton.addActionListener(getActionListener());
    m_linkRuleButton.addActionListener(getActionListener());
    m_removeRuleButton.addActionListener(getActionListener());
    m_operandType.addItemListener(getItemListener());

    m_createProductButton.addActionListener(getActionListener());
    m_removeProductButton.addActionListener(getActionListener());
  }

  private ViewNode findRuleRoot(PleGroupNode viewNode) {
    PleGroupNode pNode = viewNode.getParent();
    if (pNode == m_rulesDefs)
      return (ViewNode) viewNode;
    return findRuleRoot(pNode);

  }

  private ItemListener getItemListener() {
    return new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent ie) {
        Object src = ie.getSource();
        if (src == m_operandType && !m_break) {
          ViewNode node = m_diagramModel.getSelectedNode();
          if (node == null) return;
          char ch = ((String) m_operandType.getSelectedItem()).charAt(0);
          node.setOperand(getOperand(ch));
          m_digramTabs.updateUI();
        }
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
        ViewNode vNode = checkSelection(m_diagramModel.getRuleRoot(), xPos, yPos);
        m_diagramModel.setSelectedNode(vNode == null || m_diagramModel.getSelectedNode() == vNode ? null : vNode);
        if (vNode != null) {
          m_break = true;
          m_operandType.setSelectedItem(Character.toString(vNode.getOperand().getChar()));
          m_break = false;
        }
        TreePath path = new TreePath(m_root);
        path = m_rulesDefs.find(vNode, path);
        m_tree.setSelectionPath(path);
        m_tree.updateUI();
        m_digramTabs.updateUI();
      }

      private ViewNode checkSelection(ViewNode node, int xPos, int yPos) {
        if (node.contains(xPos, yPos))
          return node;
        for (ViewNode viewNode : node.getViewNodes()) {
          ViewNode tmp = checkSelection(viewNode, xPos, yPos);
          if (tmp != null)
            return tmp;
        }
        return null;
      }
    };
  }

  private ActionListener getActionListener() {
    return new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();
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
                m_rulesDefs.getChildren().add(newNode);
                m_diagramModel.setRuleRoot(newNode);
//              }
            }
          }
          if (src == m_removeRuleButton) {
            if (selected == null) return;
            selected.removeNode();
            m_diagramModel.setSelectedNode(null);
          }
          m_diagramModel.getRuleRoot().computePosition();
        }
        if (src == m_createProductButton) {
          ViewNode newNode = new ViewNode(null);     // stupid shortcut to avoid unnecessary casting later on
          newNode.setParent(m_prodDefs);             // stupid shortcut to avoid unnecessary casting later on
          m_prodDefs.getChildren().add(newNode);
//          m_diagramModel.setRuleRoot(newNode);
        }
        if (src == m_removeProductButton && m_diagramModel.getSelectedNode() != null) {
        }
        m_tree.updateUI();
        m_digramTabs.updateUI();
      }
    };
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
    private JButton   m_loadProductBookButton;
    private JButton   m_saveProductBookButton;
    private JPanel    m_basePane;
    private JComboBox m_setupBooks;

    public SettingsPane() {
      setupPanes();
      setupSettingsListeners();
    }

    private void setupSettingsListeners() {
      m_loadProductBookButton.addActionListener(getActionListener());
      m_saveProductBookButton.addActionListener(getActionListener());
      m_setupBooks.addItemListener(getItemListener());
    }

    private ItemListener getItemListener() {
      return null;
    }

    private void setupPanes() {
    }

    ActionListener getActionListener() {
      return new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
          Object src = ae.getSource();
          if (src == m_loadProductBookButton) {
            List<PleNode> vNodes = m_rulesDefs.getChildren();
            m_book = Persister.loadRules(vNodes); // should only return the root rule for now
            for (RuleNode pleNode : m_book.getRules()) {
              ViewNode newNode = new ViewNode(m_rulesDefs, pleNode); // takes first one
              vNodes.add(newNode);
            }
            m_setupBooks.addItem(m_book.getName());
            m_modelName.setText(m_book.getName());
            m_diagramModel.setRuleRoot((ViewNode) vNodes.get(0));

            m_tree.updateUI();
            m_digramTabs.updateUI();
          }
          if (src == m_saveProductBookButton) {
            if (m_rulesDefs.getChildren().isEmpty()) return;
            // make more general to save whole tree
            Persister.dumpRules(m_rulesDefs, m_prodDefs);  // always only one root
          }
        }
      };
    }
  }

  class TreeModelAdapter implements TreeModelListener {
    @Override
    public void treeNodesChanged(TreeModelEvent e) {}

    @Override
    public void treeNodesInserted(TreeModelEvent e) {}

    @Override
    public void treeNodesRemoved(TreeModelEvent e) {}

    @Override
    public void treeStructureChanged(TreeModelEvent e) {}
  };
}
