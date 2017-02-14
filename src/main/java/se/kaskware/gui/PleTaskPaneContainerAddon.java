package se.kaskware.gui;

import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.plaf.AbstractComponentAddon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;

import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright © 2007, Dogon IT
 * Created by: pele01, Date: 2007-apr-21 11:43:03
 */
public class PleTaskPaneContainerAddon extends AbstractComponentAddon //JXTaskPaneContainerAddon
{
  public PleTaskPaneContainerAddon(String name) {
    super(name);
  }

  public void initialize(LookAndFeelAddons addon) {
    addon.loadDefaults(getMyDefaults(addon));
  }

  private Object[] getMyDefaults(LookAndFeelAddons addon) {
    List<Object> defaults = new ArrayList<Object>();
    addBasicDefaults(addon, defaults);
    return defaults.toArray();
  }

  //  @Override
  protected void addBasicDefaults(LookAndFeelAddons addon, List<Object> defaults) {
    //    String xpStyle = OS.getWindowsVisualStyle();
    ColorUIResource background;
    ColorUIResource backgroundGradientStart;
    ColorUIResource backgroundGradientEnd;

    final Toolkit toolkit = Toolkit.getDefaultToolkit();
    //    background = new ColorUIResource((Color) toolkit.getDesktopProperty("win.3d.backgroundColor"));
    //    backgroundGradientStart = new ColorUIResource((Color) toolkit.getDesktopProperty("win.frame
    // .activeCaptionColor"));
    //    backgroundGradientEnd = new ColorUIResource((Color) toolkit.getDesktopProperty("win.frame
    // .inactiveCaptionColor"));
    defaults.addAll(Arrays.asList(
        JXTaskPaneContainer.uiClassID, "se.kaskware.gui.PleTaskPaneContainerUI",
        //        "TaskPaneContainer.backgroundGradientStart", backgroundGradientStart,
        //        "TaskPaneContainer.backgroundGradientEnd", backgroundGradientEnd,
        //        "TaskPaneContainer.background", new Color(245, 246, 170),
        "TaskPaneContainer.background", new Color(220, 220, 100),
        //        "TaskPaneContainer.background", background,
        "TaskPaneContainer.useGradient", Boolean.FALSE));
  }
}

