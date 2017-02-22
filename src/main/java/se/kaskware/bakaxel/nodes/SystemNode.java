package se.kaskware.bakaxel.nodes;

import se.kaskware.gui.PleNode;

/** Proudly produced by APLE02 on 2017-02-21 17:38. */
public class SystemNode extends PleNode {
  private final String m_systemName;
  private final String m_description;
  private final String m_itResponsible;
  private final String m_vhResponsible;
  private final String m_url;

  /**
   * System;Beskrivning;Ansvarig IT;Ansvarig VH;URL A0001;Projektdatabas VIP;Ulf Svensson;;http://wdac.intern.folksam.se/appl/a1175.nsf/24e2bf0785987708c12579c0005441ee/f8f69f5a664614e7c1257be800160c79?OpenDocument
   */
  public SystemNode(String line) {
    String parts[] = line.split(";");  // it truncates if ends with ;;;
    m_systemName = parts.length > 0 ? parts[0] : "";
    m_description = parts.length > 1 ? parts[1] : "";
    m_itResponsible = parts.length > 2 ? parts[2] : "";
    m_vhResponsible = parts.length > 3 ? parts[3] : "";
    m_url =  parts.length > 4 ? parts[4] : "";
  }
}
