package ru.spbau.gbarto;

import ru.spbau.gbarto.commands.*;
import ru.spbau.gbarto.exception.CommandException;
import ru.spbau.gbarto.parser.ParsedCommand;
import ru.spbau.gbarto.parser.Parser;
import ru.spbau.gbarto.exception.ParserException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * CLI is a class that allows you to execute shell commands.
 */
public class CLI {
    private HashMap<String, String> variables;

    public CLI() {
        variables = new HashMap<>();
    }

    private AbstractCommand getExeCommand(String c) {
        switch (c) {
            case "cat":
                return new CommandCat();

            case "echo":
                return new CommandEcho();

            case "wc":
                return new CommandWordCount();

            case "pwd":
                return new CommandPwd();

            case "exit":
                return new CommandExit();

            case "grep":
                return new CommandGrep();

            default:
                return new CommandExternal(c);
        }
    }

    private ByteArrayOutputStream executeCommand(ParsedCommand command, InputStream input) throws CommandException {
        if (command.type.equals(ParsedCommand.Type.ASSIGNMENT)) {
            String var = command.variable;
            String val = command.value;
            variables.put(var, val);
            return new ByteArrayOutputStream();
        } else {
            String c = command.command;
            String[] args = command.args;
            return getExeCommand(c).execute(args, input);
        }
    }

    private void redirectStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = input.read(buffer)) > 0) {
            output.write(buffer, 0, read);
        }
        output.flush();
    }

    /**
     * Processes the string by receiving consecutive commands with arguments passed to them and executing them.
     *
     * @param s the string to be processed
     * @param output output stream
     * @throws ParserException if something goes wrong during the parsing
     * @throws CommandException if something goes wrong during the execution of the command
     */
    public void execute(String s, OutputStream output) throws ParserException, CommandException {
        ArrayList<ParsedCommand> commands = Parser.parse(s, variables);

        try {
            InputStream commandInput = null;
            for (ParsedCommand command : commands) {
                ByteArrayOutputStream commandOutput = executeCommand(command, commandInput);

                if (commandInput != null) {
                    commandInput.close();
                }
                commandOutput.close();
                commandInput = new ByteArrayInputStream(commandOutput.toByteArray());
            }

            if (commandInput != null) {
                redirectStream(commandInput, output);
                commandInput.close();
            }
        } catch (IOException e) {
            throw new CommandException("Problem reading or writing data to stream");
        }
    }
}
