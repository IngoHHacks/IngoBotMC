package tv.ingoh.minecraft.plugins.ingobotcore.command;

public enum ResultType {
    SUCCESS("%%0"),
    NOTEXISTEXCEPTION("The command %%0 does not exist, idiot."),
    TOOFEWARGUMENTSEXCEPTION("Too few arguments, idiot. Given: %%0, expected: %%1"),
    TOOMANYARGUMENTSEXCEPTION("Too many arguments, idiot. Given: %%0, expected: %%1"),
    EXECUTIONXCEPTION("An exception occured when executing this command.");
    

    String text;
    ResultType(String text) {
        this.text = text;
    }
}
