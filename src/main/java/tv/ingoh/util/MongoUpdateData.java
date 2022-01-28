package tv.ingoh.util;

import java.util.function.Function;

import com.mongodb.client.result.UpdateResult;

import org.bson.conversions.Bson;

public class MongoUpdateData implements MongoData {
    String collection;
    Bson filter;
    Bson update;
    boolean multiple;
    Function<UpdateResult, String> function;

    public MongoUpdateData(String collection, Bson filter, Bson update, boolean multiple, Function<UpdateResult, String> function) {
        this.collection = collection;
        this.filter = filter;
        this.update = update;
        this.multiple = multiple;
        this.function = function;
    }

    public MongoUpdateData(String collection, Bson filter, Bson update, boolean multiple) {
        this.collection = collection;
        this.filter = filter;
        this.update = update;
        this.multiple = multiple;
    }


    @Override
    public String getCollection() {
        return collection;
    }

    public Bson getFilter() {
        return filter;
    }

    public Bson getUpdate() {
        return update;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public boolean hasFunction() {
        return (function != null);
    }

    public Function<UpdateResult, String> getFunction() {
        return function;
    }
}
