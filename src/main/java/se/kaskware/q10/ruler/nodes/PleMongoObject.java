package se.kaskware.q10.ruler.nodes;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User: per on: 2014-04-25 at: 22:19
 */
public class PleMongoObject {
  private static SimpleDateFormat s_dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  private static SimpleDateFormat s_timeFormat = new SimpleDateFormat("HH:mm:ss");

  protected Document m_dbo;

  public PleMongoObject() {}

  public PleMongoObject(Document dbo) { m_dbo = dbo; }

  protected Object  get(String key)             { return m_dbo.get(key); }
  protected String  getString(String key)       { return m_dbo.getString(key); }
  protected Date    getDate(String key)         { return m_dbo.getDate(key); }
  protected int     getInt(String key)          { return m_dbo.getInteger(key); }
  protected long    getLong(String key)         { return m_dbo.getLong(key); }
  protected double  getDouble(String key)       { return m_dbo.getDouble(key); }
  protected boolean getBoolean(String key)      { return m_dbo.getBoolean(key); }

  protected void put(String key, Object value)  {  m_dbo.put(key, value); }
  protected void put(String key, String value)  {  m_dbo.put(key, value); }
  protected void put(String key, Date value)    {  m_dbo.put(key, value); }
  protected void put(String key, int value)     {  m_dbo.put(key, value); }
  protected void put(String key, long value)    {  m_dbo.put(key, value); }
  protected void put(String key, double value)  {  m_dbo.put(key, value); }
  protected void put(String key, boolean value) {  m_dbo.put(key, value); }

  protected Date getDateFromString(String dateStr) {
    try {
      String date = getString(dateStr);
      if (date == null || date.isEmpty()) return null;
      return s_dateFormat.parse(date);
    }
    catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }
}
