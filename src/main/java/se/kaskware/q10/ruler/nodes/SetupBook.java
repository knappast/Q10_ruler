package se.kaskware.q10.ruler.nodes;

import se.kaskware.gui.PleNode;
import se.kaskware.q10.ruler.gui.ViewNode;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with pride by per on 2016-04-02.
 */
public class SetupBook extends PleMongoObject {
  public enum bookStatus {
    development("development"), test("test"), production("production");

    private final String m_name;

    bookStatus(String name) {
      m_name = name;
    }

    static bookStatus getStatus(String name) {
      if (name != null)
        switch (name) {
          case "development": return development;
          case "test":        return test;
          case "production":  return production;
        }
      return development;
    }
  }

  private long   m_uid;
  private String m_name;
  private String m_owner;
  private String m_version;
  private String m_created;
  private Date   m_updated;
  private bookStatus m_status = bookStatus.development;

  private ArrayList<RuleNode>  m_ruleDefs = new ArrayList<>();
  private ArrayList<ProdNodes> m_products = new ArrayList<>();

  public SetupBook(String bookName) {
    m_name = bookName;
    m_uid = System.currentTimeMillis();
    m_created = Persister.getTimeStamp();
  }

  public SetupBook(Document doc) {
    super(doc);
    expandDBObject();
  }

  public void loadRules(Document doc) {
    ArrayList<Document> tmp = (ArrayList<Document>) doc.get("Rule templates");
    for (Document document : tmp) {
      m_ruleDefs.add(new RuleNode(document));
    }
    tmp = (ArrayList<Document>) doc.get("Products");
    for (Document document : tmp) {
      m_products.add(new ProdNodes(document));
    }
  }

  public String getName() {
    return m_name;
  }

  public long getUID() {
    return m_uid;
  }

  public String getCreated() {
    return m_created;
  }

  public ArrayList<RuleNode> getRules() {
    return m_ruleDefs;
  }

  public String toString() { return getName(); }


  //-----------------

  public Document getDBObject() {
    ArrayList<Document> ruleList = new ArrayList<>();
    for (RuleNode node : m_ruleDefs)
      ruleList.add(node.getDBObject());

    Document dobj = new Document()
        .append("name", m_name)
        .append("uid",  m_uid)
        .append("created", m_created)
        .append("Rule templates", ruleList)
        .append("Products", new ArrayList<>());
    return dobj;
  }

  protected void expandDBObject() {
    m_name = getString("name");
    m_uid = getLong("uid");
    m_created = getString("created");
    m_updated = getDateFromString("updated");
    m_owner = getString("owner");
    m_version = getString("version");
    m_status = bookStatus.getStatus(getString("status"));
  }
}
