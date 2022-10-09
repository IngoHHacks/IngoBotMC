package net.ingoh.util;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.conversions.Bson;

import net.ingoh.minecraft.plugins.ingobotmc.Config;
import net.ingoh.minecraft.plugins.ingobotmc.discord.DiscordInterface;

public class Mongo implements Runnable {

    Config config;
    Queue<MongoData> queue;
    DiscordInterface discord;
    boolean end = false;

    public Mongo(Config config, DiscordInterface discord) {
        queue = new LinkedList<>();
        this.config = config;
        this.discord = discord;
    }

    @Override
    public void run() {

        MongoClient c = MongoClients.create("mongodb://" + config.getDbName() + ":" + config.getDbPwd() + "@" + config.getDbConnection());
        MongoDatabase db = c.getDatabase("mc");

        while (!end) {
            while (queue.size() > 0) {
                try {
                    MongoData mData = queue.poll();
                    MongoCollection<Document> collection = db.getCollection(mData.getCollection());
                    if (mData instanceof MongoUpdateData) {
                        MongoUpdateData muData = (MongoUpdateData) mData;
                        UpdateResult result;
                        if (muData.isMultiple()) {
                            result = collection.updateMany(muData.getFilter(), muData.getUpdate(), new UpdateOptions().upsert(true));
                        } else {
                            result = collection.updateOne(muData.getFilter(), muData.getUpdate(), new UpdateOptions().upsert(true));
                        }
                        if (muData.hasFunction()) {
                            muData.getFunction().apply(result);
                        }
                    } else if (mData instanceof MongoFindData) {
                        MongoFindData mfData = (MongoFindData) mData;
                        FindIterable<Document> found = collection.find(mfData.getFilter());
                        mfData.getFunction().apply(found);
                    }
                } catch (Exception e) {
                    discord.sendDebug("Unhandled Exception: " + e.toString());
                    discord.printStackTrace(e.getStackTrace());
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
    }

    public void find(String collection, Bson filter, Function<FindIterable<Document>, String> function) {
        queue.add(new MongoFindData(collection, filter, function));
    }

    public void update(String collection, Bson filter, Bson update, boolean multiple) {
        queue.add(new MongoUpdateData(collection, filter, update, multiple));
    }

    public void update(String collection, Bson filter, Bson update, boolean multiple, Function<UpdateResult, String> function) {
        queue.add(new MongoUpdateData(collection, filter, update, multiple, function));
    }

    public Queue<MongoData> getQueue() {
        return queue;
    }

    public void end() {
        end = true;
    }
}
