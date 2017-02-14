package se.kaskware.q10.ruler.nodes;

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

  private String     m_name;
  private String     m_owner;
  private String     m_version;
  private Date       m_created;
  private Date       m_updated;
  private bookStatus m_status = bookStatus.development;

  private ArrayList<RuleNode>  m_ruleDefs = new ArrayList<>();
  private ArrayList<ProdNodes> m_products = new ArrayList<>();

  public SetupBook(Document doc) {
    super(doc);

    m_name  = getString("Name");
    m_owner = getString("Owner");
    m_version = getString("Version");

    m_created = getDateFromString("Created");
    m_updated = getDateFromString("Updated");
    m_status  = bookStatus.getStatus(getString("Status"));

    ArrayList<Document> tmp = (ArrayList<Document>) get("Rule templates");
    for (Document document : tmp) {
      m_ruleDefs.add(new RuleNode(document));
    }
    tmp = (ArrayList<Document>) get("Products");
    for (Document document : tmp) {
      m_products.add(new ProdNodes(document));
    }
  }

  public String getName() {
    return m_name;
  }

  public ArrayList<RuleNode> getRules() {
    return m_ruleDefs;
  }
}
