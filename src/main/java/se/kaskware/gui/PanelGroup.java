package se.kaskware.gui;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/** Created by: per; Date: 2012-10-21 23:27 */
  public class PanelGroup extends CardPanel implements TreeSelectionListener
  {
    private static final String s_defaultName = "Info";

    public PanelGroup() {
      setupPanel();
    }

    void setupPanel() {
      setOpaque(false);
//      add(new InfoPanel(), s_defaultName);
//      add(new ProgramsPanel(), Program.class.getName());
//      add(new SetupsPanel(), Setup.class.getName());
//      add(new KeymapPanel(), Keymap.class.getName());
//      add(new KDFXStudioPanel(), KdfxStudio.class.getName());
//      add(new KDFXPresetPanel(), KdfxPreset.class.getName());
//      add(new KDFXAlgorithmPanel(), KdfxAlgorithm.class.getName());
//      add(new K25GrouperPanel(), K25Grouper.class.getName());
//      add(new K25TablePanel(), K2Table.class.getName());
//      add(new EffectPanel(), Effect.class.getName());
//      add(new SamplePanel(), Sample.class.getName());

      add(Box.createVerticalGlue());
    }


//    public void resetNode(K3TreeNode node) {
//      K25Object kobj = node.getUserObject();
//      String nName = kobj.getClass().getName();
//
//      KurzPanel kcard = (KurzPanel) findCard(nName);
//      kcard = (kcard == null ? (KurzPanel)findCard(s_defaultName) : kcard);
//      kcard.initFrom(node);
//      showCard(kcard);
//    }

    public void valueChanged(TreeSelectionEvent tsev) {
//      K3TreeNode node = (K3TreeNode) tsev.getPath().getLastPathComponent();
//      if (node == null) return;
//      resetNode(node);
    }
  }

