package se.kaskware.q10.ruler.nodes;

import se.kaskware.gui.PleGroupNode;
import se.kaskware.gui.PleNode;
import se.kaskware.q10.ruler.gui.ViewNode;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with pride by per on 2016-04-01.
 */
public class Persister {
  private static MyMongo m_yMongoDB = new MyMongo();

  public static void dumpRules(PleGroupNode ruleDefs, PleGroupNode prods) {

    MongoCollection<Document> startCollection = m_yMongoDB.getCollection("ruleDefinitions");

    long start = System.currentTimeMillis();
    startCollection.drop();
    ArrayList<Document> ruleList = new ArrayList<>();
    for (PleNode node : ruleDefs.getChildren()) {
      ruleList.add(((ViewNode) node).getUserNode().getDBObject());
    }

    Document persBook = new Document()
        .append("Name", "Pers Setup Book")
        .append("Created", "2016-01-02")
        .append("Rule templates", ruleList)
        .append("Products", new ArrayList<>())
        ;

    ArrayList<Document> catalog = new ArrayList<>();
    catalog.add(persBook);
    Document productCatalog = new Document().append("Product Catalog", catalog).append("Created", "2016-01-02");

    startCollection.insertOne(productCatalog);
    System.out.printf("Time taken: %d ms%n", (System.currentTimeMillis() - start));
  }

  public static SetupBook loadRules(List<PleNode> vNodes) {
    for (PleNode node : vNodes) {
      ((ViewNode)node).removeNode();
    }

    MongoCollection<Document> startCollection = m_yMongoDB.getCollection("ruleDefinitions");

    long start = System.currentTimeMillis();
    Document result = startCollection.find().iterator().next();
    System.out.printf("Time taken: %d ms%n", (System.currentTimeMillis() - start));
    return new ProductCatalog(result).getSetupBooks().get(0);  // just for now

//    startCollection.find().forEach((Block<Document>) document -> {
//      topNodes.add(new RuleNode((Document) document.get("Rule templates")));
//    });
  }
}
