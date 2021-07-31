package tv.ingoh.minecraft.plugins.ingobotcore.web;

import tv.ingoh.minecraft.plugins.ingobotcore.web.AsyncWebThread.Type;

public class Query {

    public Type type;
    public String[] args;
    public boolean isPublic;
    public String user;

    public Query(Type type, String user, String[] args, boolean isPublic) {
        this.type = type;
        this.args = args;
        this.isPublic = isPublic;
        this.user = user;
    }

}
