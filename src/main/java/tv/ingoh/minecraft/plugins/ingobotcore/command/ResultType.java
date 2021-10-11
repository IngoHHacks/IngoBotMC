package tv.ingoh.minecraft.plugins.ingobotcore.command;

public enum ResultType {
    SUCCESS("%%0"),
    NOTEXISTEXCEPTION("The command '%%0' does not exist, idiot."),
    TOOFEWARGUMENTSEXCEPTION("Too few arguments, idiot. Given: %%0, expected: %%1"),
    TOOMANYARGUMENTSEXCEPTION("Too many arguments, idiot. Given: %%0, expected: %%1"),
    EXECUTIONFAILUREEXCEPTION("Failed to execute command: %%0"),
    EXECUTIONERROREXCEPTION("An exception occured when executing this command."),
    VALUEOUTOFRANGEEXCEPTION("Value %%0 is out of range for %%1. Expected: %%2"),
    COOLDOWNEXCEPTION("You need to wait %%0 seconds to execute this command again.");
    

    String text;
    ResultType(String text) {
        this.text = text;
    }
}
