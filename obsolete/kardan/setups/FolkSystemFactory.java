package se.kaskware.kardan.setups;

import se.kaskware.kardan.bo.FolkGroup;
import se.kaskware.kardan.bo.FolkSystem;

import java.util.TreeMap;

/** Created by: Per Leander (10APLE02) at 2010-apr-01, Time 11:34:36 */
public class FolkSystemFactory {
  private TreeMap<String, FolkSystem> m_allSystems = new TreeMap<String, FolkSystem>(), m_parsedSystems;
  private TreeMap<String, FolkGroup> m_allGroups = new TreeMap<String, FolkGroup>();
  private static FolkSystemFactory s_instance;

  private static FolkSystemFactory getInstance() {
    if (s_instance == null) {
      s_instance = new FolkSystemFactory();
      s_instance.initialize();
    }
    return s_instance;
  }

  public static TreeMap<String, FolkGroup> getAllGroups() {
    return getInstance().m_allGroups;
  }

  public static TreeMap<String, FolkSystem> getAllSystems() {
    return getInstance().m_parsedSystems;
  }

  public static FolkGroup getAppGroup(String groupName) {
    return getInstance().getGroup(groupName);
  }

  private FolkGroup getGroup(String groupName) {
    FolkGroup group = m_allGroups.get(groupName);
    if (group == null) {
      group = new FolkGroup(groupName);
      m_allGroups.put(groupName, group);
    }
    return group;
  }

  private FolkSystemFactory() {
  }

  private void initialize() {
//    setupApps();
//    setupRelations();

    KardanParser parser = new KardanParser();
//    m_parsedSystems = parser.parseApplicationsFile("Systemsamband v0.1.skv");
    m_parsedSystems = parser.parseSystemFiles("User Data/kalledingdong.txt",
                                              "User Data/systemsamband_V02_system.csv",
                                              "User Data/systemsamband_V02_samband.csv");
  }

  private void setupRelations() {
//    FolkSystem syst = m_allSystems.get("S70");
//    syst.addToConnection(m_allSystems.get("D90"));

    FolkSystem syst = m_allSystems.get("L89");
    syst.addToConnection(new FolkConnection(m_allSystems.get("L87")));
//    syst.addToConnection(m_allSystems.get("D21")));
//    syst.addToConnection(m_allSystems.get("Y80")));
//    syst.addToConnection(m_allSystems.get("Q80")));
    syst.addToConnection(new FolkConnection(m_allSystems.get("SAP-**")));
    syst.addToConnection(new FolkConnection(m_allSystems.get("L90")));
    syst.addToConnection(new FolkConnection(m_allSystems.get("Y02")));
    syst.addToConnection(new FolkConnection(m_allSystems.get("Y05")));
    syst.addToConnection(new FolkConnection(m_allSystems.get("G70")));

    syst = m_allSystems.get("G51");
    syst.addToConnection(new FolkConnection(m_allSystems.get("G50")));
    syst.addToConnection(new FolkConnection(m_allSystems.get("Gx6?")));
    syst.addToConnection(new FolkConnection(m_allSystems.get("Y90")));

    syst = m_allSystems.get("Gx5?");
    syst.addToConnection(new FolkConnection(m_allSystems.get("Gx6?")));
    syst.addToConnection(new FolkConnection(m_allSystems.get("D14")));

    syst = m_allSystems.get("Y10");
    syst.addToConnection(new FolkConnection(m_allSystems.get("Y09")));
    syst.addToConnection(new FolkConnection(m_allSystems.get("SAP-CD")));
    syst.addToConnection(new FolkConnection(m_allSystems.get("D21")));

    syst = m_allSystems.get("H10");
    syst.addToConnection(new FolkConnection(m_allSystems.get("Gx4?")));
    syst.addToConnection(new FolkConnection(m_allSystems.get("H20")));
    syst.addToConnection(new FolkConnection(m_allSystems.get("L87")));
    syst.addToConnection(new FolkConnection(m_allSystems.get("Y70")));
    syst.addToConnection(new FolkConnection(m_allSystems.get("L86")));

    syst = m_allSystems.get("Y80");
    syst.addToConnection(new FolkConnection(m_allSystems.get("D21")));

    syst = m_allSystems.get("L87");
    syst.addToConnection(new FolkConnection(m_allSystems.get("L86")));
    syst.addToConnection(new FolkConnection(m_allSystems.get("L85")));
  }

  private void setupApps() {
    String systems[][] = new String[][]{
        {"D14", "Transaktionssystem"}, {"D21", "Tabellhant pnr/FK"},
        {"D77", "Transaktionssystem"}, {"D90", "Inbetalningar"}
    };
    addSystems("D-apps", systems);

    systems = new String[][]{
        {"E06", "Utbetalningar"}, {"E15", "Kontoplan"},
        {"E33", "MVS - SAP"}, {"E66 (D90)", "Reg inbetalning"}
    };
    addSystems("E-apps", systems);

    systems = new String[][]{
        {"G50", "Kampanjer"}, {"G51", "Enk?tsvar"}, {"G52", "Erbjudanden"}, {"G60", "M?lgrupp"},
        {"G70", "Kampanjer (data)"}, {"G71", "Ensamble"}, {"G72", "Smartpath"}, {"G73", "Epiphany"},
        {"G74", "Aprimo"}, {"G80", "Holger"}, {"Gx1?", "Dialer"}, {"Gx2?", "Cognos"},
        {"Gx3?", "Teleopti"}, {"Gx4?", "Scanner"}, {"Gx5?", "Solidus"}, {"Gx6?", "S?kes"},
        {"Gx7?", "Netwise"}
    };
    addSystems("G-apps", systems);

    systems = new String[][]{
        {"H10", "Utskrift KU-r?nta"}, {"H20", "Djur"}, {"H40", "Provision Sak"},
        {"H50", "Fordon"}, {"H60", "Kalkyl Bil "}, {"H80", "Sv Konsument"},
        {"H82", "Folksam F?retag"}, {"H84", "Tre Kronor"}
    };
    addSystems("H-apps", systems);

    systems = new String[][]{
        {"I35", "Kundsupport.se"}
    };
    addSystems("I-apps", systems);

    systems = new String[][]{
        {"K60", "F?rs?kringsfall "}, {"K80/86", "Kalkyl Byggnad"}, {"K85", "Kalkyl Person"}
    };
    addSystems("K-apps", systems);

    systems = new String[][]{
        {"L00", "Liv generellt"}, {"L13", "Ber?kningar"}, {"L25", "Liv utskrift"},
        {"L35", "Liv skador/utbet"}, {"L73", "Liv provision"}, {"L81", "Liv utf?rda"},
        {"L82", "Liv-avi/redovisn"}, {"L85", "Liv-offert"}, {"L86", "Liv ?ndra"},
        {"L87", "Liv-underh?ll"}, {"L88", "Fond-avtal"}, {"L89", "Aktur ber?kningar"},
        {"L90", "Gruppavtal"}, {"L97", "Avtalshantering"}
    };
    addSystems("L-apps", systems);

    systems = new String[][]{
        {"M11", "M?klare"}, {"M51", "OmbudsWeb"}
    };
    addSystems("M-apps", systems);

    systems = new String[][]{
        {"O01/O02", "Anslutningsavtal"}, {"O03", "Filmning dok"}, {"O21/MTH", "Erbjud/M?lgrupp"},
        {"O24", "Grupp -Avtal"}, {"O29", "Statistik"}, {"O36", "Grupp Skadesystem"},
        {"O45/46", "Licensf?rs?kr"}, {"O63", "Khem/M?lgrupp"}
    };
    addSystems("O-apps", systems);

    systems = new String[][]{
        {"P05", "Personal-f?rsystem"}, {"P10", "Personal"}
    };
    addSystems("P-apps", systems);

    systems = new String[][]{
        {"Q00", "Tabeller"}, {"Q08", "Liv skaderegl"}, {"Q80", "Leverant?rswebb"}
    };
    addSystems("Q-apps", systems);

    systems = new String[][]{
        {"S50", "Gamla Civilf?rs"}, {"S70", "Civilf?rs"}
    };
    addSystems("S-apps", systems);

    systems = new String[][]{
        {"SAP-CD", "Betalning"}, {"SAP-FI", "Bokf?ring"}, {"SAP-HR", "Personal"}, {"SAP-**", "?vrig SAP"}
    };
    addSystems("SAP-apps", systems);

    systems = new String[][]{
        {"V02", "Arvoden tillf. anst."}
    };
    addSystems("V-apps", systems);

    systems = new String[][]{
        {"Y02", "Fint"}, {"Y05", "KIS+ FolksamF?rs"}, {"Y07", "Is?k"},
        {"Y08", "Admin Is?k"}, {"Y09", "Fullmakt"}, {"Y10", "Ekonomisk Trans"},
        {"Y12", "Landskod"}, {"Y22", "Skattemodulen"}, {"Y25", "Betalf?rmedling"},
        {"Y30", "Betalning - f?rsyst"}, {"Y40", "Medgivande"}, {"Y50", "CBR"},
        {"Y60", "GSR"}, {"Y70", "DW"}, {"Y80", "Apios"}, {"Y90", "Samband"}
    };
    addSystems("Y-apps", systems);
    System.out.println("m_allSystems = " + m_allSystems.size());
    System.out.println("m_allGroups = " + m_allGroups.size());
  }

  private void addSystems(String groupName, String systems[][]) {
    FolkGroup group = getGroup(groupName);
    for (String[] app : systems) {
      FolkSystem folkSystem = new FolkSystem(group, app[0], app[1]);
      m_allSystems.put(folkSystem.getName(), folkSystem);
    }
  }
}
