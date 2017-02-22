package se.kaskware.kardan.bo;

import se.kaskware.kardan.setups.FolkConnection;
import se.kaskware.kardan.setups.FolkSystemFactory;
import se.kaskware.kardan.setups.SystemFieldNames;

import java.util.ArrayList;
import java.util.TreeMap;

import static se.kaskware.kardan.setups.SystemFieldNames.fieldName.*;

/**
 * Created by: Per Leander (10APLE02) at 2010-apr-01, Time 11:28:13
 */
public class FolkSystem extends FolkObject {
  private TreeMap<SystemFieldNames.fieldName, String> m_systemAttr;
  private TreeMap<String, String>                     m_miscAttr;
  private FolkGroup                                   m_group;
  private ArrayList<FolkConnection> m_toConnections   = new ArrayList<FolkConnection>();
  private ArrayList<FolkConnection> m_fromConnections = new ArrayList<FolkConnection>();
  private String m_activity;
  private boolean isKalleDingDong = false;

  public FolkSystem(FolkGroup group, String name, String slogan) {
    super(name, slogan);
    m_group = group;
    m_group.addSystem(this);
  }

  public FolkSystem(String name, String descr, String activity) {
    m_group = FolkSystemFactory.getAppGroup(Character.toString(name.charAt(0)).toUpperCase() + "-apps");
    m_group.addSystem(this);
    m_activity = activity;

    setName(name);
    setDescription(descr);
  }

  public FolkSystem(TreeMap<SystemFieldNames.fieldName, String> systemAttr, TreeMap<String, String> miscAttr) {
    m_systemAttr = systemAttr;
    m_miscAttr = miscAttr;

    String systemName = systemAttr.get(fldSystemName);
    systemName = systemName == null ? miscAttr.get(fldSystemName.toString()).toString() + "_bogus" : systemName;
    setName(systemName);
    setDescription(systemAttr.get(fldSystemPurpose));

    m_group = FolkSystemFactory.getAppGroup(Character.toString(systemName.charAt(0)).toUpperCase() + "-apps");
    m_group.addSystem(this);

    isKalleDingDong = true;
  }

  public FolkGroup getGroup() {
    return m_group;
  }

  public boolean isKalleDingDong() { return isKalleDingDong; }

  public TreeMap<SystemFieldNames.fieldName, String> getSystemAttr() { return m_systemAttr; }

  public TreeMap<String, String> getMiscAttr() { return m_miscAttr; }

  public ArrayList<FolkConnection> getChain() {
    ArrayList<FolkConnection> chain = new ArrayList<FolkConnection>();
    ArrayList<FolkSystem> visited = new ArrayList<FolkSystem>();
    visited.add(this);
    for (FolkConnection toConn : m_fromConnections) {
      ArrayList<FolkConnection> list = getChain(visited, toConn);
      if (list.size() > chain.size()) chain = list;
    }

    return chain;
  }

  public ArrayList<FolkConnection> getChain(ArrayList<FolkSystem> visited, FolkConnection startConn) {
    ArrayList<FolkConnection> chain = new ArrayList<FolkConnection>();
    chain.add(startConn);
    FolkSystem fromApp = startConn.getToApp();
    ArrayList<FolkConnection> fromConns = fromApp.getFromConnections();
    if (visited.contains(fromApp) || fromConns.isEmpty()) return chain;
    visited.add(fromApp);

    ArrayList<FolkConnection> newChain = new ArrayList<FolkConnection>();
    for (FolkConnection toConn : fromConns) {
      ArrayList<FolkConnection> list = getChain(visited, toConn);
      if (list.size() > newChain.size()) newChain = list;
    }
    chain.addAll(newChain);
    return chain;
  }

  public ArrayList<FolkConnection> getToConnections() {
    return m_toConnections;
  }

  public ArrayList<FolkConnection> getFromConnections() {
    return m_fromConnections;
  }

  public void addToConnection(FolkConnection conn) {
    m_toConnections.add(conn);
  }

  public void addFromConnection(FolkConnection conn) {
    m_fromConnections.add(conn);
  }
}
