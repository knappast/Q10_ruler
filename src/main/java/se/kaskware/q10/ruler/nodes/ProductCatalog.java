package se.kaskware.q10.ruler.nodes;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with pride by per on 2016-04-02.
 */
public class ProductCatalog extends PleMongoObject {
  private Date m_created;
  private Date m_updated;

  private ArrayList<SetupBook> m_setupBooks = new ArrayList<>();

  public ProductCatalog(Document doc, SetupBook setupBook) {
    super(doc);

    m_created = getDateFromString("created");
    m_updated = getDateFromString("updated");
    ArrayList<Document> catalog = (ArrayList<Document>) get("Product Catalog");
    for (Document document : catalog) {
      setupBook.loadRules(document);
      m_setupBooks.add(setupBook);
    }
  }

  public ArrayList<SetupBook> getSetupBooks() {
    return m_setupBooks;
  }
}
