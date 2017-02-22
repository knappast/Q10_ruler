package se.kaskware.kardan.setups;

import se.kaskware.kardan.bo.FolkSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by: Per Leander (APLE02) at: 2010-maj-05, 13:50:19
 */
public class KardanParser {
  private TreeMap<String, FolkSystem> m_appMap;

  /**
   * num;
   * Från System; Slogan; Tillhörande aktivitet (den med mest interaktioner);
   * Till System; Slogan; Tillhörande aktivitet (huvud);
   * Info som överförs;
   * Övrig kommentar; ;System med oklara /saknade kopplingar
   */
  public TreeMap<String, FolkSystem> parseApplicationsFile(String fName) {
    TreeMap<String, FolkSystem> appMap = new TreeMap<String, FolkSystem>();
    try {
      BufferedReader raf = new BufferedReader(new InputStreamReader(new FileInputStream(fName)));
      String line = raf.readLine();
      int index = 2;
      while (line != null) {
        line = line.trim();
        if (line.length() != 0 && line.charAt(0) != '#') {
          String tmp, items[] = line.split(";");  // if just semicolons items.length == 0
          if (items.length != 0 && (tmp = items[1].trim()).length() != 0) {

            FolkSystem fromApp = appMap.get(items[1]);
            if (fromApp == null) {
              fromApp = new FolkSystem(items[1], items[2].trim(), items[3].trim());
              appMap.put(fromApp.getName(), fromApp);
            }
            FolkSystem toApp = appMap.get(items[4]);
            if (toApp == null) {
              String descr = items.length > 5 ? items[5].trim() : null;
              String activity = items.length > 6 ? items[6].trim() : null;
              toApp = new FolkSystem(items[4], descr, activity);
              appMap.put(toApp.getName(), toApp);
            }
            String info = items.length > 7 ? items[7].trim() : null;
            fromApp.addToConnection(new FolkConnection(toApp, info));
            toApp.addFromConnection(new FolkConnection(fromApp, info));
          }
        }
        line = raf.readLine();
      }
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    return appMap;
  }

  public TreeMap<String, FolkSystem> parseSystemFiles(String systemListFName,
                                                      String systemsFName,
                                                      String relationsFName) {
    m_appMap = new TreeMap<String, FolkSystem>();
    long start = System.currentTimeMillis();
    TreeMap<String, FolkSystem> newAppMap = parseSystemsNew(systemListFName);
    System.out.println("parseSystemsNew took " + ((System.currentTimeMillis() - start)) + " ms");

    start = System.currentTimeMillis();
    TreeMap<String, FolkSystem> oldAppMap = parseSystems(systemsFName);
    System.out.println("parseSystems took " + ((System.currentTimeMillis() - start)) + " ms");

    start = System.currentTimeMillis();
    for (Map.Entry<String, FolkSystem> entry : oldAppMap.entrySet()) {
      String key = entry.getKey();
      if (!newAppMap.containsKey(key)) {
//        System.out.println("oldKey '" + key + "' not among the new systems (removed)");
        m_appMap.put(key, entry.getValue());
      }
    }

    for (Map.Entry<String, FolkSystem> entry : newAppMap.entrySet()) {
      String key = entry.getKey();
//      if (!oldAppMap.containsKey(key))
//        System.out.println("newKey '" + key + "' just among the new systems (added)");
      m_appMap.put(key, entry.getValue());
    }
    System.out.println("checking took " + ((System.currentTimeMillis() - start)) + " ms");

    start = System.currentTimeMillis();
    parseRelations(relationsFName);
    System.out.println("parseRelations took " + ((System.currentTimeMillis() - start)) + " ms");

    return m_appMap;
  }

  private TreeMap<String, FolkSystem> parseSystemsNew(String fName) {
    TreeMap<String, FolkSystem> appMap = new TreeMap<String, FolkSystem>();

    try {
      ArrayList<Object[]> valuePairs = new ArrayList();
      BufferedReader raf = new BufferedReader(new InputStreamReader(new FileInputStream(fName)));
      String line = raf.readLine();
      while (line != null) {
        line = line.trim();
        if (line.length() > 0) {
          String key, value = null;
          int pos = line.indexOf(':');
          if (pos > 0) {
            key = line.substring(0, pos);
            if (pos + 1 <= line.length())
              value = line.substring(pos + 1).trim();
            valuePairs.add(new Object[]{key, value});
          }
        }
        line = raf.readLine();
      }
      raf.close();

      boolean inValidField = false;
      TreeMap<SystemFieldNames.fieldName, String> systemAttr = new TreeMap<SystemFieldNames.fieldName, String>();
      TreeMap<String, String> miscAttr = new TreeMap<String, String>();
      for (Object[] pair : valuePairs) {
        String key = (String) pair[0];
        String value = (String) pair[1];
        if (key.equalsIgnoreCase("FLDMARVALREAD")) {
          inValidField = true;
          continue;
        }
        if (key.equalsIgnoreCase("$Revisions")) {

          systemAttr.put(SystemFieldNames.fieldName.valueOf(key), value);
          FolkSystem system = new FolkSystem(systemAttr, miscAttr);
          appMap.put(system.getName(), system);
          systemAttr = new TreeMap<SystemFieldNames.fieldName, String>();
          miscAttr = new TreeMap<String, String>();
          inValidField = false;
        }
        if (inValidField)
          systemAttr.put(SystemFieldNames.fieldName.valueOf(key), value);
        else
          miscAttr.put(key, value);
      }
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Number of apps in new systemfile = " + appMap.size());
    return appMap;
  }

  protected TreeMap<String, FolkSystem> parseSystems(String fName) {
    TreeMap<String, FolkSystem> appMap = new TreeMap<String, FolkSystem>();

    try {
      BufferedReader raf = new BufferedReader(new InputStreamReader(new FileInputStream(fName)));
      String line = raf.readLine();
      while (line != null) {
        line = line.trim();
        if (line.length() != 0 && line.charAt(0) != '#') {
          String tmp, items[] = line.split(";");  // if just semicolons items.length == 0
          if (items.length == 0) continue;
          tmp = items[0].trim();
          if (tmp.length() == 0) continue;

          int dum = tmp.indexOf("(Dummy");
          int sys = tmp.indexOf('-');
          String sysName;
          if (dum > 0) {
            sysName = tmp.substring(0, dum).trim();
            int index = sysName.indexOf('-');
            tmp = sysName.substring(0, index > 0 ? index : sysName.length()); // duh just lazy
          }
          else {
            sysName = tmp.substring(0, sys).trim();
            tmp = sysName;
          }

          FolkSystem fromApp = appMap.get(tmp);
          if (fromApp == null) {
            fromApp = new FolkSystem(sysName, items.length > 2 ? items[2].trim() : "", items[1].trim());
            appMap.put(tmp, fromApp);
          }
          else {
            System.out.println(fromApp.getName() + " found in kalledingdong: " + fromApp.isKalleDingDong());
          }
        }
        line = raf.readLine();
      }
      raf.close();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Number of apps in old systemfile = " + appMap.size());
    return appMap;
  }

  public void parseRelations(String fName) {
    try {
      BufferedReader raf = new BufferedReader(new InputStreamReader(new FileInputStream(fName)));
      String line = raf.readLine();
      while (line != null) {
        line = line.trim();
        if (line.length() != 0 && line.charAt(0) != '#') {
          String tmp, items[] = line.split(";");  // if just semicolons items.length == 0
          if (items.length == 0) continue;
          tmp = items[0].trim();
          if (tmp.length() == 0) continue;

          // BG-B-INBET-D90-SMB
          int fromIndex = tmp.indexOf('-');
          int toIndex = tmp.lastIndexOf('-');
          String fromSyst = tmp.substring(0, fromIndex);
          tmp = tmp.substring(0, toIndex);
          toIndex = tmp.lastIndexOf('-');
          String slogan = tmp.substring(fromIndex + 1, toIndex);
          String toSyst = tmp.substring(toIndex + 1);
          FolkSystem fromApp = m_appMap.get(fromSyst);
          FolkSystem toApp = m_appMap.get(toSyst);
          if (fromApp == null || toApp == null) {
            System.out.println("missing app in relation: fromApp = " + fromSyst + " toApp = " + toSyst);
            line = raf.readLine();
            continue;
          }
          String info = items.length > 2 ? items[2].trim() : null;
          fromApp.addToConnection(new FolkConnection(toApp, info));
          toApp.addFromConnection(new FolkConnection(fromApp, info));
        }
        line = raf.readLine();
      }
      raf.close();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}
