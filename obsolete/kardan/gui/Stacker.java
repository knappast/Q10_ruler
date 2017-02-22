package se.kaskware.gui;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import org.jdesktop.core.animation.timing.interpolators.AccelerationInterpolator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Copyright ? 2008, Per Leander, Dogon IT
 * Created by User: per at: 2010-apr-27, 06:55:14
 */
public class Stacker extends JLayeredPane {
  private Component master; // dictates sizing, scrolling
  private JPanel messageLayer;
  private JXPanel messageAlpha;

  public Stacker(Component master) {
    this.master = master;
    setLayout(null);
    add(master, JLayeredPane.DEFAULT_LAYER);

    TimingSource ts = new SwingTimerTimingSource();
    Animator.setDefaultTimingSource(ts);
  }

  @Override
  public Dimension getPreferredSize() {
    return master.getPreferredSize();
  }

  @Override
  public void doLayout() {
    // ensure all layers are sized the same
    Dimension size = getSize();
    Component layers[] = getComponents();
    for (Component layer : layers) {
      layer.setBounds(0, 0, size.width, size.height);
    }
  }

  /**
   * Fades in the specified message component in the top layer of this
   * layered pane.
   *
   * @param message    the component to be displayed in the message layer
   * @param finalAlpha the alpha value of the component when fade in is complete
   */
  public void showMessageLayer(JComponent message, final float finalAlpha) {
    messageLayer = new JPanel();
    messageLayer.setOpaque(false);
    GridBagLayout gridbag = new GridBagLayout();
    messageLayer.setLayout(gridbag);
    GridBagConstraints c = new GridBagConstraints();
    c.anchor = GridBagConstraints.CENTER;

    messageAlpha = new JXPanel();
    messageAlpha.setOpaque(false);
    messageAlpha.setAlpha(0.0f);
    gridbag.addLayoutComponent(messageAlpha, c);
    messageLayer.add(messageAlpha);
    messageAlpha.add(message);

    add(messageLayer, JLayeredPane.POPUP_LAYER);
    revalidate();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Animator.getDefaultTimingSource().init();
        Animator.Builder builder = new Animator.Builder();
        builder.setDuration(2000,TimeUnit.MILLISECONDS).setStartDelay(200, TimeUnit.MILLISECONDS);
        builder.setInterpolator(new AccelerationInterpolator(0.2, 0.5));
        PropertyFinder propFinder = PropertyFinder.getHandler(messageAlpha, "alpha", messageAlpha.getAlpha(), 0.0f);
        TimingTargetAdapter alphaTarget = new StackerBacker.PropertySetterTimingTarget(propFinder);
        Animator animator = builder.addTarget(alphaTarget).build();
        animator.start();

//      public void run() {
//        Animator animator = new Animator(2000, new PropertySetter(messageAlpha, "alpha", 0.0f, finalAlpha));
//        animator.setStartDelay(200);
//        animator.setAcceleration(.2f);
//        animator.setDeceleration(.5f);
//        animator.start();
      }
    });

  }

  /** Fades out and removes the current message component */
  public void hideMessageLayer() {
    if (messageLayer != null && messageLayer.isShowing()) {
      Animator.Builder builder = new Animator.Builder();
      builder.setDuration(500, TimeUnit.MILLISECONDS).setStartDelay(300, TimeUnit.MILLISECONDS);
      builder.setInterpolator(new AccelerationInterpolator(0.2, 0.5));
      PropertyFinder propFinder = PropertyFinder.getHandler(messageAlpha, "alpha", messageAlpha.getAlpha(), 0.0f);
      TimingTargetAdapter alphaTarget = new StackerBacker.PropertySetterToTimingTarget(propFinder){
        public void end(Animator source) {
          remove(messageLayer);
          revalidate();
          messageLayer = null;
        }
      };
      Animator animator = builder.addTarget(alphaTarget).build();
      animator.start();

//      Animator animator = new Animator(500,
//          new PropertySetter(messageAlpha, "alpha", messageAlpha.getAlpha(), 0.0f) {
//            public void end() {
//              remove(messageLayer);
//              revalidate();
//              messageLayer = null;
//            }
//          });
//      animator.setStartDelay(300);
//      animator.setAcceleration(.2f);
//      animator.setDeceleration(.5f);
//      animator.start();
    }
  }
}
