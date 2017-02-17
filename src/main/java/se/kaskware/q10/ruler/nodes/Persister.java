package se.kaskware.q10.ruler.nodes;

import se.kaskware.gui.PleGroupNode;
import se.kaskware.gui.PleNode;
import se.kaskware.q10.ruler.gui.ViewNode;

import com.mongodb.MongoTimeoutException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created with pride by per on 2016-04-01.
 */
public class Persister {
  private static MyMongo m_yMongoDB = new MyMongo();

  static String getTimeStamp() {
    return String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:1%tS", Calendar.getInstance());
  }

  public static void createSetupBook(List<SetupBook> bookNames) {
    long start = System.currentTimeMillis();
    try {
      MongoCollection<Document> startCollection = m_yMongoDB.getCollection("Pers_Definitions");

      startCollection.drop();

      ArrayList<Document> books = new ArrayList<>();
      Document bookCatalog = new Document().append("Setup Books", books).append("created", getTimeStamp()).append(
          "updated", getTimeStamp());

      for (SetupBook book : bookNames) {
        Document doc = new Document().append("name", book.getName()).append("uid", System.currentTimeMillis()).append(
            "created", book.getCreated());
        books.add(doc);
      }
      startCollection.insertOne(bookCatalog);
    }
    catch (MongoTimeoutException mte) {
      JOptionPane.showMessageDialog(null, "Mongo not started!",
                                    "Mongo not started!", JOptionPane.ERROR_MESSAGE);
    }
    System.out.printf("Time taken: %d ms%n", (System.currentTimeMillis() - start));
  }

  public static List<SetupBook> loadSetupBooks() {
    ArrayList<SetupBook> allBooks = new ArrayList<>();

    long start = System.currentTimeMillis();
    try {
      MongoCollection<Document> startCollection = m_yMongoDB.getCollection("Pers_Definitions");
      Document result = startCollection.find().iterator().next();

      ArrayList<Document> document = (ArrayList<Document>) result.get("Setup Books");
      for (Document book : document) {
        allBooks.add(new SetupBook(book));
      }
    }
    catch (MongoTimeoutException mte) {
      JOptionPane.showMessageDialog(null, "Mongo not started!", "Mongo not started!", JOptionPane.ERROR_MESSAGE);
    }
    catch (NoSuchElementException e) {
      System.err.println("No definitions yet");
    }
    System.out.printf("Time taken: %d ms%n", (System.currentTimeMillis() - start));

    return allBooks;
  }

  public static void dumpRules(PleGroupNode ruleDefs, PleGroupNode prods, SetupBook book) {
    long start = System.currentTimeMillis();
    try {
      MongoCollection<Document> startCollection = m_yMongoDB.getCollection("ruleDefinitions");
      startCollection.drop();

      ArrayList<Document> catalog = new ArrayList<>();
      Document persBook = book.getDBObject();
      catalog.add(persBook);
      Document productCatalog = new Document().append("Product Catalog", catalog).append("created", getTimeStamp());

      startCollection.insertOne(productCatalog);
    }
    catch (MongoTimeoutException mte) {
      JOptionPane.showMessageDialog(null, "Mongo not started!",
                                    "Mongo not started!", JOptionPane.ERROR_MESSAGE);
    }

    System.out.printf("Time taken: %d ms%n", (System.currentTimeMillis() - start));
  }

  public static SetupBook loadRules(List<PleNode> vNodes, SetupBook setupBook) {
    // to avoid cast and concurrent access conflict when removing nodes
    List<ViewNode> toAvoidCast = new ArrayList<>();
    for (PleNode pleNode : vNodes) {
      toAvoidCast.add((ViewNode) pleNode);
    }

    for (ViewNode node : toAvoidCast) {
      node.removeNode();
    }

    MongoCollection<Document> startCollection = m_yMongoDB.getCollection("ruleDefinitions");

    long start = System.currentTimeMillis();
    Document productCatalog = new Document().append("Product Catalog.name", setupBook.getName());

    try {
      Document result = startCollection.find(productCatalog).iterator().next();
      return new ProductCatalog(result, setupBook).getSetupBooks().get(0);  // just for now
    }
    catch (NoSuchElementException e) {
      System.err.println("No definitions yet");
    }
    catch (MongoTimeoutException mte) {
    JOptionPane.showMessageDialog(null, "Mongo not started!",
                                  "Mongo not started!", JOptionPane.ERROR_MESSAGE);
  }
    System.out.printf("Time taken: %d ms%n", (System.currentTimeMillis() - start));
    return null;

//    db.getCollection('ruleDefinitions').find({"Product Catalog.Name" : "Pers Setup Book"})

//    startCollection.find().forEach((Block<Document>) document -> {
//      topNodes.add(new RuleNode((Document) document.get("Rule templates")));
//    });
  }
}
