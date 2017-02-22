package se.kaskware.gui;

import com.surelogic.ThreadSafe;
import com.surelogic.Vouch;
import org.jdesktop.core.animation.i18n.I18N;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.KeyFrames;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

/** Proudly produced by APLE02 on 2017-02-21 16:16. */
public class StackerBacker {

  public static <T> TimingTargetAdapter getTarget(PropertyFinder propFinder) {
    return new StackerBacker.PropertySetterTimingTarget(propFinder);   // Setup animation.
  }

  public static <T> TimingTargetAdapter getTargetTo(PropertyFinder propFinder) {
    return new StackerBacker.PropertySetterToTimingTarget(propFinder);  // Setup "to" animation.
  }

  @ThreadSafe
  static class PropertySetterTimingTarget extends TimingTargetAdapter {
    protected final AtomicReference<KeyFrames<Object>> f_keyFrames = new AtomicReference<KeyFrames<Object>>();
    @Vouch("ThreadSafe")
    protected final Object f_object;
    @Vouch("ThreadSafe")
    protected final Method f_propertySetter;

//    PropertySetterTimingTarget(KeyFrames<Object> keyFrames, Object object, Method propertySetter, String propertyName) {
    PropertySetterTimingTarget(PropertyFinder finder) {
      f_keyFrames.set(finder.getKeyFrames());
      f_object = finder.getObject();
      f_propertySetter = finder.getPropertySetter();
      f_debugName = finder.getPropertyName();
    }

    @Override
    public void timingEvent(Animator source, double fraction) {
      try {
        f_propertySetter.invoke(f_object, f_keyFrames.get().getInterpolatedValueAt(fraction));
      }
      catch (Exception e) {
        throw new IllegalStateException(I18N.err(31, f_propertySetter.getName(), f_object.toString()), e);
      }
    }

    @Override
    public void begin(Animator source) {
      final double fraction = source.getCurrentDirection() == Animator.Direction.FORWARD ? 0.0 : 1.0;
      timingEvent(source, fraction);
    }
  }

  @ThreadSafe
  static class PropertySetterToTimingTarget extends PropertySetterTimingTarget {
    @Vouch("ThreadSafe")
    final Method f_propertyGetter;

    PropertySetterToTimingTarget(PropertyFinder backer) {
      super(backer);
      f_propertyGetter = backer.getPropertyGetter();
    }

    @Override
    public void begin(Animator source) {
      try {
        final Object startValue = f_propertyGetter.invoke(f_object);
        final KeyFrames.Builder<Object> builder = new KeyFrames.Builder<Object>(startValue);
        boolean first = true;
        for (KeyFrames.Frame<Object> frame : f_keyFrames.get()) {
          if (first)
            first = false;
          else
            builder.addFrame(frame);
        }
        f_keyFrames.set(builder.build());
      }
      catch (Exception e) {
        throw new IllegalStateException(I18N.err(31, f_propertyGetter.getName(), f_object.toString()), e);
      }
      super.begin(source); // set the initial value
    }
  }
}
