package se.kaskware.q10.navigator.nodes.contactpoint;

import java.util.Date;

/**
 * @author Per Leander
 * @version 1.0
 * @created 15-mar-2016 17:25:18
 */
public class QualityAspect {

  private Date creationDate;

  /**
   * Skapad
   * Avslutad
   * Senast inl�st
   */
  private enum dateType {
    created, ended, lastRead
  }

  /**
   * Service
   * S�lj
   * Skadereglering
   */
  private enum fromProcess {
    service, sale, claim
  }

  /**
   * <u>V�rdef�rr�d: K�lla </u>
   */
  private enum source {
  }

  /**
   * automatisk
   * manuell
   */
  private enum sourceType {
    automatic, manuel
  }

  public QualityAspect() {

  }
}