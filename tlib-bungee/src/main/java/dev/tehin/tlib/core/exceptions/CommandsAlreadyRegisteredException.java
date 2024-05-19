package dev.tehin.tlib.core.exceptions;

public class CommandsAlreadyRegisteredException extends Exception {

    public CommandsAlreadyRegisteredException() {
        super("Your plugin tried to register the commands twice, please check: https://docs.com/commands");
    }

}
