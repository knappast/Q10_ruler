package se.kaskware.gui;

import java.util.HashMap;

/**
 * Created with pride by: per on: 2015-06-11 at: 22:58
 */
public class Base {
  public static final int     REVISION     = 101;
  /** This might be replaced by main() if there's a lib/version.txt file. */
  static              String  VERSION_NAME = "0101";
  /** Set true if this a proper release rather than a numbered revision. */
  static public       boolean RELEASE      = false;

  static HashMap<Integer, String> platformNames = new HashMap<Integer, String>();

  static final int WINDOWS = 1;
  static final int MACOSX = 2;
  static final int LINUX = 3;

  static {
    platformNames.put(WINDOWS, "windows");
    platformNames.put(MACOSX, "macosx");
    platformNames.put(LINUX, "linux");
  }

  static HashMap<String, Integer> platformIndices = new HashMap<String, Integer>();

  static {
    platformIndices.put("windows", WINDOWS);
    platformIndices.put("macosx",  MACOSX);
    platformIndices.put("linux",   LINUX);
  }

//  static Platform platform;
//
//  static public Platform getPlatform() {
//    return platform;
//  }

  static public String getPlatformName() {
    String osname = System.getProperty("os.name");

    if (osname.contains("Mac"))
      return "macosx";
    if (osname.contains("Windows"))
      return "windows";
    if (osname.equals("Linux"))  // true for the ibm vm
      return "linux";

    return "other";
  }

  /**
   * Map a platform constant to its name.
   *
   * @param which PConstants.WINDOWS, PConstants.MACOSX, PConstants.LINUX
   * @return one of "windows", "macosx", or "linux"
   */
  static public String getPlatformName(int which) {
    return platformNames.get(which);
  }

  static public int getPlatformIndex(String what) {
    Integer entry = platformIndices.get(what);
    return (entry == null) ? -1 : entry;
  }

  // These were changed to no longer rely on PApplet and PConstants because
  // of conflicts that could happen with older versions of core.jar, where
  // the MACOSX constant would instead read as the LINUX constant.

  /** returns true if Processing is running on a Mac OS X machine. */
  static public boolean isMacOS() {
    //return PApplet.platform == PConstants.MACOSX;
    return System.getProperty("os.name").contains("Mac");
  }

  /** returns true if running on windows. */
  static public boolean isWindows() {
    //return PApplet.platform == PConstants.WINDOWS;
    return System.getProperty("os.name").contains("Windows");
  }

  /** true if running on linux. */
  static public boolean isLinux() {
    //return PApplet.platform == PConstants.LINUX;
    return System.getProperty("os.name").contains("Linux");
  }
}
