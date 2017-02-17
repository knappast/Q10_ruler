package se.kaskware.q10.ruler.nodes;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.Set;

/** Created by User: per on: 2014-04-20 at: 20:51 */
public class MyMongo {
  private static MongoClient m_mongoClient;
  private final static String s_myUserName = "per";
  private final static char[] s_myPassword = "pelle".toCharArray();
  private MongoDatabase m_db;

  public MyMongo() {

    // To directly connect to a single MongoDB server (note that this will not auto-discover the primary even
    // if it's a member of a replica set:
//      m_mongoClient = new MongoClient();
    // or
//      m_mongoClient = new MongoClient("localhost");
    // or
    m_mongoClient = new MongoClient("localhost", 27017);
    m_db = m_mongoClient.getDatabase("pleMongoDB");
    // or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
//      m_mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),
//          new ServerAddress("localhost", 27018),
//          new ServerAddress("localhost", 27019)),
//          Arrays.asList(MongoCredential.createMongoCRCredential(s_myUserName, "", s_myPassword)));

  }

  public MongoCollection<Document> getCollection(String collection) {
    return m_db.getCollection(collection);
  }

  public void printCollections() {
    MongoIterable<String> colls = m_db.listCollectionNames();

    for (String s : colls) {
      System.out.println(s);
    }

//   BasicDBObject doc = new BasicDBObject("name", "MongoDB").
//       append("type", "database").
//       append("count", 1).
//       append("info", new BasicDBObject("x", 203).append("y", 102));
//
//   DBCollection coll = m_db.getCollection("mycollection");
//   coll.insert(doc);
  }
}
