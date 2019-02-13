package ru.spbau.gbarto.parser;

public class ParsedCommand {
    public enum Type {COMMAND, ASSIGNMENT}

    public Type type;

    public String command;
    public String[] args;

    public String variable;
    public String value;

    ParsedCommand(Type type) {
        this.type = type;
    }
}
