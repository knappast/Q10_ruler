package se.kaskware.gui;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.plaf.AbstractComponentAddon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright � 2007, Dogon IT
 * Created by: pele01, Date: 2007-apr-21 11:43:03
 */
public class PleTaskPaneAddon extends AbstractComponentAddon {
    public PleTaskPaneAddon(String name) {
      super(name);
    }

    public void initialize(LookAndFeelAddons addon) {
      List<Object> defaults = new ArrayList<Object>();
      addMyDefaults(addon, defaults);
      addon.loadDefaults(defaults.toArray());
    }

    protected void addMyDefaults(LookAndFeelAddons addon, List<Object> defaults) {
      Font taskPaneFont = new Font("Comic Sans MS", Font.BOLD, 12);

      defaults.addAll(Arrays.asList(
          JXTaskPane.uiClassID, "se.kaskware.gui.PleTaskPaneUI",
          "TaskPane.font", new FontUIResource(taskPaneFont),
          "TaskPane.foreground", UIManager.getColor("activeCaptionText"),
          "TaskPane.background", new Color(20, 220, 100),
          "TaskPane.specialTitleBackground", MetalLookAndFeel.getPrimaryControl(),
          "TaskPane.titleBackgroundGradientStart", new Color(250, 250, 170),
          "TaskPane.titleBackgroundGradientEnd", new Color(230, 230, 200),
          "TaskPane.titleForeground", MetalLookAndFeel.getControlTextColor(),
          "TaskPane.specialTitleForeground", MetalLookAndFeel.getControlTextColor(),
          "TaskPane.borderColor", MetalLookAndFeel.getPrimaryControl(),
          "TaskPane.titleOver", MetalLookAndFeel.getControl().darker(),
          "TaskPane.specialTitleOver", MetalLookAndFeel.getPrimaryControlHighlight(),
  //        "TaskPane.animate", Boolean.TRUE,
          "TaskPane.focusInputMap",
          new UIDefaults.LazyInputMap(new Object[]{"ENTER", "toggleExpanded", "SPACE", "toggleExpanded"})));
    }
  }

