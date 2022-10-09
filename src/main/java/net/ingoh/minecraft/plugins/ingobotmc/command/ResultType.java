package net.ingoh.minecraft.plugins.ingobotmc.command;

public enum ResultType {
    SUCCESS("%%0"),
    NOTEXISTEXCEPTION("The command '%%0' does not exist, idiot."),
    TOOFEWARGUMENTSEXCEPTION("Too few arguments, idiot. Given: %%0, expected: %%1"),
    TOOMANYARGUMENTSEXCEPTION("Too many arguments, idiot. Given: %%0, expected: %%1"),
    EXECUTIONFAILUREEXCEPTION("Failed to execute command: %%0, idiot."),
    EXECUTIONERROREXCEPTION("An exception occured when executing this command, idiot."),
    VALUEOUTOFRANGEEXCEPTION("Value %%0 is out of range for %%1, idiot. Expected: %%2"),
    COOLDOWNEXCEPTION("You need to wait %%0 seconds to execute this command again, idiot.");
    

    String text;
    ResultType(String text) {
        this.text = text;
    }
}
