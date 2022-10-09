package net.ingoh.minecraft.plugins.ingobotmc.web;

public class Query {

    public AsyncWebThread.Type type;
    public String[] args;
    public boolean isPublic;
    public String user;

    public Query(AsyncWebThread.Type type, String user, String[] args, boolean isPublic) {
        this.type = type;
        this.args = args;
        this.isPublic = isPublic;
        this.user = user;
    }

}
