package se.kaskware.gui;

import com.surelogic.NonNull;
import com.surelogic.Nullable;
import org.jdesktop.core.animation.i18n.I18N;
import org.jdesktop.core.animation.timing.Interpolator;
import org.jdesktop.core.animation.timing.KeyFrames;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

/** Proudly produced by APLE02 on 2017-02-21 15:48. */
public class PropertyFinder {
  private final Object    m_object;
  private final String    m_propertyName;
  private final Method    m_propertyGetter;
  private final Method    m_propertySetter;
  private final KeyFrames m_keyFrames;

  private <T> PropertyFinder(Object object, String propName, Method propGetter, Method propSetter,
                             KeyFrames<T> keyFrames) {
    m_object = object;
    m_propertyName = propName;
    m_propertyGetter = propGetter;
    m_propertySetter = propSetter;
    m_keyFrames = keyFrames;
  }

  public Object getObject() {
    return m_object;
  }

  public String getPropertyName() {
    return m_propertyName;
  }

  public Method getPropertyGetter() {
    return m_propertyGetter;
  }

  public Method getPropertySetter() {
    return m_propertySetter;
  }

  public KeyFrames getKeyFrames() {
    return m_keyFrames;
  }

  public static <T> PropertyFinder getHandler(Object object, String propertyName, T... values) {
    final KeyFrames<T> keyFrames = new KeyFrames.Builder<T>().addFrames(values).build();
    return getHandler(object, propertyName, keyFrames);
  }

  public static <T> PropertyFinder getHandler(Object object, String propertyName, Interpolator interpolator,
                                             T... values) {
    final KeyFrames<T> keyFrames = new KeyFrames.Builder<T>().setInterpolator(interpolator).addFrames(values).build();
    return getHandler(object, propertyName, keyFrames);
  }

  public static <T> PropertyFinder getHandler(Object object, String propertyName, KeyFrames<T> keyFrames) {
    return getTargetHelper(object, propertyName, keyFrames);
  }

  private static <T> PropertyFinder getTargetHelper(final Object object, final String propertyName,
                                            final KeyFrames<T> keyFrames) {
    if (object == null)
      throw new IllegalArgumentException(I18N.err(1, "object"));
    if (propertyName == null)
      throw new IllegalArgumentException(I18N.err(1, "propertyName"));
    if (keyFrames == null)
      throw new IllegalArgumentException(I18N.err(1, "keyFrames"));
    @SuppressWarnings("unchecked") final KeyFrames<Object> objectKeyFrames = (KeyFrames<Object>) keyFrames;
    /*
     * Find the setter method for the property.
     */
    final String firstChar = propertyName.substring(0, 1);
    final String remainder = propertyName.substring(1);
    final String propertySetterName = "set" + firstChar.toUpperCase(Locale.ENGLISH) + remainder;
    final Class<?> argType = keyFrames.getClassOfValue();
    final ArrayList<Method> potentials = new ArrayList<Method>();
    final Method propertySetter;
    try {
      for (Method m : object.getClass().getMethods()) {
        if (m.getName().equals(propertySetterName)) {
          final Class<?>[] parameterTypes = m.getParameterTypes();
          if (parameterTypes.length == 1) {
            potentials.add(m);
          }
        }
      }
      propertySetter = bestSetterMatch(argType, potentials);
      if (propertySetter == null) {
        throw new IllegalArgumentException(I18N.err(30, propertySetterName, propertyName, object.toString()));
      }
    }
    catch (SecurityException e) {
      throw new IllegalArgumentException(I18N.err(30, propertySetterName, propertyName, object.toString()), e);
    }

    // Find the getter method for the property if this is a "to" animations
    final String propertyGetterName = "get" + firstChar.toUpperCase(Locale.ENGLISH) + remainder;
    Method propertyGetter = null;
    try {
      for (Method m : object.getClass().getMethods()) {
        if (m.getName().equals(propertyGetterName)) {
          if (m.getParameterTypes().length == 0) {
            propertyGetter = m;
            break;
          }
        }
      }
      if (propertyGetter == null) {
        throw new IllegalArgumentException(I18N.err(30, propertyGetterName, propertyName, object.toString()));
      }
    }
    catch (SecurityException e) {
      throw new IllegalArgumentException(I18N.err(30, propertyGetterName, propertyName, object.toString()), e);
    }
    return new PropertyFinder(object, propertyName, propertyGetter, propertySetter, keyFrames);
  }

  /**
   * Heuristically picks the best potential setter. See doc for
   * {@link org.jdesktop.core.animation.timing.PropertySetter} about the heuristic and how a choice is made.
   *
   * @param argType    the type of the value being passed to the setter method.
   * @param potentials methods found that are called <tt>setX</tt> with one argument.
   * @return the best potential setter, or {@code null} if none.
   */
  @Nullable
  private static Method bestSetterMatch(@NonNull Class<?> argType, @NonNull ArrayList<Method> potentials) {
    if (potentials.isEmpty())
      return null;
    else if (potentials.size() == 1)
      return potentials.get(0);
    else {
      // try for exact match on primitive formal
      for (Method m : potentials) {
        final Class<?> formalType = m.getParameterTypes()[0];
        if (formalType.isPrimitive()) {
          if ((int.class.equals(formalType) && Integer.class.equals(argType)) || (long.class.equals(formalType)
                                                                                  && Long.class.equals(argType)) || (
                  double.class.equals(formalType) && Double.class.equals(argType)) || (float.class.equals(formalType)
                                                                                       && Float.class.equals(argType))
              || (boolean.class.equals(formalType) && Boolean.class.equals(argType)) || (char.class.equals(formalType)
                                                                                         && Character.class.equals(
              argType)) || (byte.class.equals(formalType) && Byte.class.equals(argType)) || (void.class.equals(
              formalType) && Void.class.equals(argType)) || (short.class.equals(formalType) && Short.class.equals(
              argType))) {
            return m;
          }
        }
      }
      // try for exact match on reference formal
      for (Method m : potentials) {
        final Class<?> formalType = m.getParameterTypes()[0];
        if (!formalType.isPrimitive()) {
          if (formalType.equals(argType)) {
            return m;
          }
        }
      }
      // try for assignable match on primitive formal
      for (Method m : potentials) {
        final Class<?> formalType = m.getParameterTypes()[0];
        if (formalType.isPrimitive()) {
          if ((short.class.equals(formalType) && Byte.class.equals(argType)) || (int.class.equals(formalType) && (
              Byte.class.equals(argType) || Short.class.equals(argType) || Character.class.equals(argType))) || (
                  long.class.equals(formalType) && (Byte.class.equals(argType) || Short.class.equals(argType)
                                                    || Integer.class.equals(argType) || Character.class.equals(
                      argType))) || (float.class.equals(formalType) && (Byte.class.equals(argType)
                                                                        || Short.class.equals(argType)
                                                                        || Integer.class.equals(argType)
                                                                        || Long.class.equals(argType)
                                                                        || Character.class.equals(argType))) || (
                  double.class.equals(formalType) && (Byte.class.equals(argType) || Short.class.equals(argType)
                                                      || Integer.class.equals(argType) || Long.class.equals(argType)
                                                      || Character.class.equals(argType) || Float.class.equals(
                      argType)))) {
            return m;
          }
        }
      }
      // try for assignable match on reference formal
      for (Method m : potentials) {
        final Class<?> formalType = m.getParameterTypes()[0];
        if (!formalType.isPrimitive()) {
          if (formalType.isAssignableFrom(argType)) {
            return m;
          }
        }
      }
      // just return the first (old behavior)
      return potentials.get(0);
    }
  }
}

