package se.kaskware.q10.navigator.nodes.contactpoint;

import java.util.Date;

/**
 * @author Per Leander
 * @version 1.0
 * @created 15-mar-2016 17:25:23
 */
public class State {

  private Date changeDate;
  private Date creationDate;

  /**
   * Created
   * Ended (archived)
   * Changed
   */
  private enum state {
    created, ended, changed
  }

  ;

  public State() {

  }
}