package se.kaskware.q10.navigator.nodes.contactpoint;

/**
 * @author Per Leander
 * @version 1.0
 * @created 15-mar-2016 17:25:07
 */
public class PhoneDevice extends Device {

  private int    countryPrefix;
  /**
   * utan landskod men med riktnummer med nolla
   */
  private String number;

  public PhoneDevice() {

  }
}