package se.kaskware.q10.navigator.nodes.contactpoint;

import java.util.Date;

/**
 * @author Per Leander
 * @version 1.0
 * @created 15-mar-2016 17:25:20
 */
public class Restriction {

  private boolean dm_contact;

  /**
   * samtal
   * sms
   * e-post
   * fysiskt
   * enkät
   * hushåll
   */
  private enum dm_contactType {
    call, sms, email, physical, questionaire, household
  }

  ;
  private Date dm_lastDate;

  public Restriction() {

  }
}