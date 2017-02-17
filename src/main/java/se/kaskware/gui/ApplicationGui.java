package se.kaskware.gui;

import com.apple.eawt.Application;
import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with pride by per on 2016-03-16.
 */
public class ApplicationGui {
  public static void setup() {
    try {
      if (Base.isMacOS()) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Ple Guitar Efx");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      else {
        try {
          UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (Exception e) {
          // If Nimbus is not available, you can set the GUI to another look and feel.
          for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Metal".equals(info.getName())) {
              UIManager.setLookAndFeel(info.getClassName());
              break;
            }
          }
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void setupRunner(final JFrame m_mainFrame) {
    // macosx already has its own about menu
    if (Base.isMacOS()) {
      Application application = Application.getApplication();
      application.setEnabledPreferencesMenu(true);
      application.setEnabledAboutMenu(true);
      application.addApplicationListener(new ApplicationAdapter() {
        @Override
        public void handleAbout(ApplicationEvent e) {
          setupAboutPane(m_mainFrame);
          // code goes here to show your About Dialog
          e.setHandled(true);
        }

        @Override
        public void handleOpenApplication(ApplicationEvent e) {
          // your code here
        }

        @Override
        public void handleOpenFile(ApplicationEvent e) {
        }

        @Override
        public void handlePreferences(ApplicationEvent e) {
//          PreferencesDialog dialog = new PreferencesDialog(m_mainFrame, new Preferences());
//          dialog.setVisible(true);
        }

        @Override
        public void handlePrintFile(ApplicationEvent e) {
        }

        @Override
        public void handleQuit(ApplicationEvent e) {
          m_mainFrame.dispose();
          System.exit(1);
        }
      });
    }

    JMenuBar bar = new JMenuBar();
    m_mainFrame.setJMenuBar(bar);
    bar.add(new JMenu("File"));
    bar.add(Box.createHorizontalGlue());
    JMenu menu = new JMenu("Help");
    JMenuItem item = new JMenuItem("About");
    bar.add(menu);
    menu.add(item);
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setupAboutPane(m_mainFrame);
      }
    });
//
//    m_mainPane.addComponentListener(new ComponentListener() {
//      public void componentResized(ComponentEvent e) {
////          if (m_objectTree.getSelectionPath() != null)
////            m_panelGroup.resetNode((K3TreeNode) m_objectTree.getSelectionPath().getLastPathComponent());
//      }
//
//      public void componentMoved(ComponentEvent e) { }
//
//      public void componentShown(ComponentEvent e) { }
//
//      public void componentHidden(ComponentEvent e) { }
//    });

  }

  protected static void setupAboutPane(JFrame m_mainFrame) {
    AboutPilsner about = new AboutPilsner(m_mainFrame);
    Rectangle bounds = about.getParent().getBounds();
    Rectangle abounds = about.getBounds();

    about.setLocation(bounds.x + (bounds.width - abounds.width) / 2,
                      bounds.y + (bounds.height - abounds.height) / 2 - 50);
    about.setSize(240, 315);
    about.setResizable(false);
    about.setVisible(true);
  }
}
