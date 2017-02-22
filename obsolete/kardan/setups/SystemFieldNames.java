package se.kaskware.kardan.setups;

import java.util.TreeMap;

import static se.kaskware.kardan.setups.SystemFieldNames.areaName.*;
import static se.kaskware.kardan.setups.SystemFieldNames.fieldName.*;

/**
 * Created by: APLE02 - Date: 2013-11-18; Time: 16:28
 */
public class SystemFieldNames {
  private static TreeMap<areaName, String> s_groupLabels;
  private static TreeMap<areaName, String> s_groupsOrder;
  private static TreeMap<fieldName, Tuple> s_labels;

  //-------------------------------------------------
  public enum areaName {
    systemInfo, techInfo, ekoInfo_forvaltning, ekoInfo_utveckling, volumeInfo, documentation, sourcing,
    legalDependancies, continuityPlans, infoClassification, systemHealth, roadMaps,
    documentRevisions, miscUncertain
  }

  public enum fieldName {
    fldSystemName, fldSystemDesc, fldSystemPurpose, fldSystemArea, fldMaintPort,
    fldMaintObject, fldFamilyObject, fldEstablishedObject, fldSystemResponsIT, fldSystemResponsVH,
    fldSystemGroup, fldSlaClass,

    fldSystemPlatform, fldSystemDetails, fldSystemLang, fldSystemDb, fldSystemTrans,
    fldSystemMiddle, fldSystemIntegrations, fldSystemIntegrationsExt, fldSystemIntegratesWith,

    fldSystemMaint, fldSystemPower, fldSystemLicenses, fldSystemKeepin, fldSystemSmallProjects, fldSystemBigProjects,

    fldSystemUsers, fldSystemGeografic, fldSystemPublic, fldSystemUsers_External, fldSystemTransactions,
    fldSystemIncidents, fldSystemCustomers, fldSystemContracts, fldSystemValue,

    fldDocLang, fldDocComplete,

    fldSourcing1, fldSourcingCompany1, fldSourcing2, fldSourcingCompany2, fldSourcing3, fldSourcingCompany3,
    fldObjectPortfolio, fldObjectGroup,

    fldPUL, fldLegal,

    fldProcess, fldLogg, fldComplete,

    fldKonf, fldRight, fldAvible, fldSecure,

    fldHealth1, fldHealth2, fldHealth3, fldHealth4, fldHealth5,

    fldHealth6, fldHealthYear, fldHealth2011, fldHealth2012, fldHealthComment,

    fldDocumentHistory,

    // misc
    fldSystemStatus, fldSystemClient, fldHealthPart01DocId, fldHealthPart02DocId,

    fldDocId, fldHealthProcessStatus, fldReader, fldAuthor, $UpdatedBy, $Revisions, fldLastUpdated
    }

  public static String getLabel(int row) {
    return s_labels.get(fieldName.values()[row]).getLabel();
  }

  public static Tuple getTuple(fieldName row) {
    return s_labels.get(row);
  }
  public static TreeMap<areaName, String> getGroupLabels() { return s_groupLabels; }
  public static TreeMap<fieldName, Tuple> getAllLabels() { return s_labels; }

  public static String getGroup(int row) {
    return s_groupsOrder.get(s_labels.get(fieldName.values()[row]).getArea());
  }

  static {
    s_groupLabels = new TreeMap<areaName, String>();

    s_groupLabels.put(systemInfo, "Systeminformation");
    s_groupLabels.put(techInfo, "Teknisk information");
    s_groupLabels.put(ekoInfo_forvaltning, "Ekonomisk information/förvaltning");
    s_groupLabels.put(ekoInfo_utveckling, "Ekonomisk information/utveckling");
    s_groupLabels.put(volumeInfo, "Volyminformation");
    s_groupLabels.put(documentation, "Dokumentation");
    s_groupLabels.put(sourcing, "Sourcing");

    s_groupLabels.put(legalDependancies, "Lagmässiga krav");
    s_groupLabels.put(continuityPlans, "Kontinuitetsplanering");
    s_groupLabels.put(infoClassification, "Informationsklassificering");
    s_groupLabels.put(systemHealth, "Systemhälsa");
    s_groupLabels.put(roadMaps, "Handlingsplaner");

    s_groupLabels.put(documentRevisions, "Dokumenthistorik");
    s_groupLabels.put(miscUncertain, "Övrigt");

    s_groupsOrder = new TreeMap<areaName, String>();
    s_groupsOrder.put(systemInfo, "Systeminformation");
    s_groupsOrder.put(techInfo, "Teknisk information");
    s_groupsOrder.put(ekoInfo_forvaltning, "Ekonomisk information/Förvaltning");
    s_groupsOrder.put(ekoInfo_utveckling, "Ekonomisk information/Utveckling");
    s_groupsOrder.put(volumeInfo, "Volyminformation");
    s_groupsOrder.put(documentation, "Dokumentation");
    s_groupsOrder.put(sourcing, "Sourcing");
    s_groupsOrder.put(legalDependancies, "Lagmässiga krav");
    s_groupsOrder.put(continuityPlans, "Kontinuitetsplaner");
    s_groupsOrder.put(infoClassification, "Informationsklassificering");
    s_groupsOrder.put(systemHealth, "Systemhälsa");
    s_groupsOrder.put(roadMaps, "Handlingsplan");
    s_groupsOrder.put(documentRevisions, "Dokumenthistorik");
    s_groupsOrder.put(miscUncertain, "Diverse ovisat");

//    s_groups = new TreeMap<String, TreeMap<fieldName, Tuple>>();

//         Systeminformation
    s_labels = new TreeMap<fieldName, Tuple>();

    //    s_groups.put("Systeminformation", s_labels);
    addTuple(s_labels, systemInfo, fldSystemName, "Namn");                          // H20
    addTuple(s_labels, systemInfo, fldSystemDesc, "Beskrivning");                   // Djurförsäkring
    addTuple(s_labels, systemInfo, fldSystemPurpose, "Syfte");                      // Försäkringsfall för
    // Djur-försäkringar. Under 2013 ...
    addTuple(s_labels, systemInfo, fldSystemArea, "Förmåga");                       // Försäkringsfallsreglering
    addTuple(s_labels, systemInfo, fldMaintPort, "Förvaltningsportfölj");           // Produkt
    addTuple(s_labels, systemInfo, fldMaintObject, "Förvaltningsobjekt");           // Djur
    addTuple(s_labels, systemInfo, fldFamilyObject, "Förvaltningsobjektsfamilj");   //
    addTuple(s_labels, systemInfo, fldEstablishedObject, "Etablerat objekt");       // Ja/Nej
    addTuple(s_labels, systemInfo, fldSystemResponsIT, "Ansvarig IT");              // Natalia Makhno
    addTuple(s_labels, systemInfo, fldSystemResponsVH, "Ansvarig VH");              // Natalia Makhno
    addTuple(s_labels, systemInfo, fldSystemGroup, "Systemgrupp");                  // SG0150
    addTuple(s_labels, systemInfo, fldSlaClass, "SLA Klass");                       // 3

    //        Teknisk information
//    s_labels = new TreeMap<fieldName, Tuple>();
//    s_groups.put("Teknisk information", s_labels);
    addTuple(s_labels, techInfo, fldSystemPlatform, "Teknisk plattform");            // Windows
    addTuple(s_labels, techInfo, fldSystemDetails, "Utvecklingsverktyg");            // Progress
    addTuple(s_labels, techInfo, fldSystemLang, "Utvecklingsspråk");                 // Progress
    addTuple(s_labels, techInfo, fldSystemDb, "Databashanterare");                   // Progress
    addTuple(s_labels, techInfo, fldSystemTrans, "Transaktionshanterare");           // Progress
    addTuple(s_labels, techInfo, fldSystemMiddle, "Middleware");                     //
    addTuple(s_labels, techInfo, fldSystemIntegrations, "Interna integrationer");    // 1-5
    addTuple(s_labels, techInfo, fldSystemIntegrationsExt, "Externa integrationer"); // 1-5
    addTuple(s_labels, techInfo, fldSystemIntegratesWith, "Integration med");        // Y05

    //        Ekonomisk information
////      Förvaltning
//    s_labels = new TreeMap<fieldName, Tuple>();
//    s_groups.put("Ekonomisk information/Förvaltning", s_labels);
    addTuple(s_labels, ekoInfo_forvaltning, fldSystemMaint, "Underhåll");                // 675 tkr
    addTuple(s_labels, ekoInfo_forvaltning, fldSystemPower, "Datakraft");                // 1 350 tkr
    addTuple(s_labels, ekoInfo_forvaltning, fldSystemLicenses, "Licenser");              // 347 tkr
    addTuple(s_labels, ekoInfo_forvaltning, fldSystemKeepin, "Upprätthålla");            // 624 tkr

    ////      Utveckling
//    s_labels = new TreeMap<fieldName, Tuple>();
//    s_groups.put("Ekonomisk information/Utveckling", s_labels);
    addTuple(s_labels, ekoInfo_utveckling, fldSystemSmallProjects, "Uppdrag");           // 1 681 tkr
    addTuple(s_labels, ekoInfo_utveckling, fldSystemBigProjects, "Projekt");             //

    //        Volyminformation
//    s_labels = new TreeMap<fieldName, Tuple>();
//    s_groups.put("Volyminformation", s_labels);
    addTuple(s_labels, volumeInfo, fldSystemUsers, "Antal interna användare");           // 101-500
    addTuple(s_labels, volumeInfo, fldSystemGeografic, "Geografisk spridning");          // Nationellt
    addTuple(s_labels, volumeInfo, fldSystemPublic, "Exponeras externt");                // Nej
    addTuple(s_labels, volumeInfo, fldSystemUsers_External, "Antal externa användare");  // 0
    addTuple(s_labels, volumeInfo, fldSystemTransactions, "Transaktioner/dag");          // > 10 000
    addTuple(s_labels, volumeInfo, fldSystemIncidents, "Incidenter/år");                 // > 100
    addTuple(s_labels, volumeInfo, fldSystemCustomers, "Antal kunder i systemet");       // > 10 000
    addTuple(s_labels, volumeInfo, fldSystemContracts, "Antal avtal i systemet");        // > 10 000
    addTuple(s_labels, volumeInfo, fldSystemValue, "Värde i systemet");                  // > 10 000 000

    //        Dokumentation
//    s_labels = new TreeMap<fieldName, Tuple>();
//    s_groups.put("Dokumentation", s_labels);
    addTuple(s_labels, documentation, fldDocLang, "Språk");                 // Svenska, Engelska
    addTuple(s_labels, documentation, fldDocComplete, "Komplett");          // Nej

    //        Sourcing
//    s_labels = new TreeMap<fieldName, Tuple>();
//    s_groups.put("Sourcing", s_labels);
    addTuple(s_labels, sourcing, fldSourcing1, "Drift");                    // Insourcing/Outsourcing, Tieto
    addTuple(s_labels, sourcing, fldSourcingCompany1, "Driftsbolag");       // Tieto
    addTuple(s_labels, sourcing, fldSourcing2, "Förvaltning");              // Insourcing/Outsourcing
    addTuple(s_labels, sourcing, fldSourcingCompany2, "Förvaltningsbolag"); //
    addTuple(s_labels, sourcing, fldSourcing3, "Utveckling");               // Insourcing/Outsourcing
    addTuple(s_labels, sourcing, fldSourcingCompany3, "Utvecklingsbolag");  //
    addTuple(s_labels, sourcing, fldObjectPortfolio, "Sourcing object");    // Products - Property
    addTuple(s_labels, sourcing, fldObjectGroup, "Sourcing group");         // Property Core

    //        Lagmässiga krav
//    s_labels = new TreeMap<fieldName, Tuple>();
//    s_groups.put("Lagmässiga krav", s_labels);
    addTuple(s_labels, legalDependancies, fldPUL, "Krav");                 // PUL, Solvens
    addTuple(s_labels, legalDependancies, fldLegal, "Kommentarer");        //

    //        Kontinuitetsplaner
//    s_labels = new TreeMap<fieldName, Tuple>();
//    s_groups.put("Kontinuitetsplaner", s_labels);
    addTuple(s_labels, continuityPlans, fldProcess, "Affärsprocesser");    //
    addTuple(s_labels, continuityPlans, fldLogg, "Logg");                  //
    addTuple(s_labels, continuityPlans, fldComplete, "Komplett");          //

    //        Informationsklassificering
//    s_labels = new TreeMap<fieldName, Tuple>();
//    s_groups.put("Informationsklassificering", s_labels);
    addTuple(s_labels, infoClassification, fldKonf, "Konfidentialitet");     // 2
    addTuple(s_labels, infoClassification, fldRight, "Riktighet");           // 2
    addTuple(s_labels, infoClassification, fldAvible, "Tillgänglighet");     // 1
    addTuple(s_labels, infoClassification, fldSecure, "Säkerhetsnivå");      // 2

    //        Systemhälsa
//    s_labels = new TreeMap<fieldName, Tuple>();
//    s_groups.put("Systemhälsa", s_labels);
    addTuple(s_labels, systemHealth, fldHealth1, "Funktionellt värde"); // Low
    addTuple(s_labels, systemHealth, fldHealth2, "Tekniskt värde");     // Low
    addTuple(s_labels, systemHealth, fldHealth3, "Affärsnytt");         // High
    addTuple(s_labels, systemHealth, fldHealth4, "Strategiskt värde");  // High
    addTuple(s_labels, systemHealth, fldHealth5, "Totalvärde");         // Replace

    //        Handlingsplan
//    s_labels = new TreeMap<fieldName, Tuple>();
//    s_groups.put("Handlingsplan", s_labels);
    addTuple(s_labels, roadMaps, fldHealth6, "Livscykelvärde");         // Freeze
    addTuple(s_labels, roadMaps, fldHealthYear, "Antal år kvar");       //
    addTuple(s_labels, roadMaps, fldHealth2011, "Plan för 2011");       // Försäkringar på väg till S70,
    // skadedelen kvar i H20.
    addTuple(s_labels, roadMaps, fldHealth2012, "Plan för 2012");       // Som 2011. Historiken kvar även efter Halo.
    addTuple(s_labels, roadMaps, fldHealthComment, "Kommentar");        //

    //        Dokumenthistorik
//    s_labels = new TreeMap<fieldName, Tuple>();
//    s_groups.put("Dokumenthistorik", s_labels);
    addTuple(s_labels, documentRevisions, fldDocumentHistory, "När/Användare/Aktivitet");  // time=2011-05-31 9:14:28
    // #user=KerstinSkans
    // #action=Steg ett av systemhälsan slutförd
    // #user=...... #,time=....

    //        Diverse ovisat
//    s_labels = new TreeMap<fieldName, Tuple>();
//    s_groups.put("Diverse ovisat", s_labels);
    addTuple(s_labels, miscUncertain, fldSystemStatus, ""); // 1
    addTuple(s_labels, miscUncertain, fldSystemClient, ""); //
    addTuple(s_labels, miscUncertain, fldHealthPart01DocId, ""); //5D74AFC0D5F67AC0C12578CE002D151B
    addTuple(s_labels, miscUncertain, fldHealthPart02DocId, ""); //02995CBDEEE15745C12578CE002E1468
    addTuple(s_labels, miscUncertain, fldDocId, "");               // 4512B558CA980629C125784D0029385A
    addTuple(s_labels, miscUncertain, fldHealthProcessStatus, ""); // 2
    addTuple(s_labels, miscUncertain, fldReader, ""); // *
    addTuple(s_labels, miscUncertain, fldAuthor, ""); // [System],[Admin],CN=Natalia Makhno/O=FOL/C=SE,
    // CN=Natalia Makhno/O=FOL/C=SE
    addTuple(s_labels, miscUncertain, $UpdatedBy, ""); // CN=Petra Dittlau/O=jbdata,   CN=Petra Dittlau/O=FOL/C=SE,
    // CN=Petra Dittlau/O=jbdata, ...
    addTuple(s_labels, miscUncertain, $Revisions, ""); // 2011-03-08 08:30:13,2011-03-08 08:30:54,
    // 2011-03-09 14:07:28,2011-03-09 14:07:52, ...
  }

  private static void addTuple(TreeMap<fieldName, Tuple> fieldMap, areaName systemInfo,
                               fieldName fieldName, String label) {
    fieldMap.put(fieldName, new Tuple(systemInfo, fieldName, label));                     // H20
  }

  public static class Tuple {
    private areaName  m_area;
    private fieldName m_field;
    private String    m_label;

    private Tuple(areaName group, fieldName field, String label) {
      m_area = group;
      m_field = field;
      m_label = label;
    }

    public areaName getArea() {
      return m_area;
    }

    public fieldName getField() {
      return m_field;
    }

    public String getLabel() {
      return m_label;
    }
  }
}
