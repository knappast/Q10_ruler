package se.kaskware.q10.navigator.nodes.contactpoint;

/**
 * @author Per Leander
 * @version 1.0
 * @created 15-mar-2016 17:24:54
 */
public class Device extends ContactPoint {

  /**
   * talk, sms, mms, chat, screenShare, video, VoIP
   */
  public enum capabilityList  {talk, sms, mms, chat, screenShare, video, voip}

  /**
   * phone, surfBoard, bored, pc
   */
  public enum deviceType {phone, surfBoard, bored, pc}

  private capabilityList m_capabilityList;
  private deviceType m_deviceType;

  public Device() {

  }
}