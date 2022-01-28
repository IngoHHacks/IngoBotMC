package tv.ingoh.util;

import java.util.function.Function;

import com.mongodb.client.FindIterable;

import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoFindData implements MongoData {
    String collection;
    Bson filter;
    boolean multiple;
    Function<FindIterable<Document>, String> function;

    public MongoFindData(String collection, Bson filter, Function<FindIterable<Document>, String> function) {
        this.collection = collection;
        this.filter = filter;
        this.function = function;
    }

    @Override
    public String getCollection() {
        return collection;
    }

    public Bson getFilter() {
        return filter;
    }

    public Function<FindIterable<Document>, String> getFunction() {
        return function;
    }
}