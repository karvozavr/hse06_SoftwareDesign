package ru.spbau.gbarto.parser;

import ru.spbau.gbarto.exception.ParserException;

import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    private static ParsedCommand parseCommand(ArrayList<String> snippets) {
        if (snippets.size() == 1 && snippets.get(0).indexOf('=') != -1) {
            String s = snippets.get(0);
            int pos = s.indexOf('=');
            String variable = s.substring(0, pos);
            String value = s.substring(pos + 1);

            ParsedCommand parsedCommand = new ParsedCommand(ParsedCommand.Type.ASSIGNMENT);
            parsedCommand.variable = variable;
            parsedCommand.value = value;
            return parsedCommand;
        } else {
            ParsedCommand parsedCommand = new ParsedCommand(ParsedCommand.Type.COMMAND);
            parsedCommand.command = snippets.get(0);
            if (snippets.size() == 1) {
                parsedCommand.args = null;
            } else {
                parsedCommand.args = snippets.subList(1, snippets.size()).toArray(new String[1]);
            }
            return parsedCommand;
        }
    }

    /**
     * Processes the string by receiving consecutive commands with arguments passed to them.
     *
     * @param s the string to be processed
     * @param variables known variable values
     * @return list of ParsedCommand (this structure describes command)
     * @throws ParserException if something goes wrong
     */
    public static ArrayList<ParsedCommand> parse(String s, HashMap<String, String> variables) throws ParserException {
        Substitutor substitutor = new Substitutor(variables);
        s = substitutor.substitute(s);

        ArrayList<String> commands = Splitter.splitByPipe(s);
        ArrayList<ParsedCommand> parsedCommands = new ArrayList<>();
        for (String command : commands) {
            ArrayList<String> snippets = Splitter.splitBySpace(command);
            for (int i = 0; i < snippets.size(); i++) {
                String snippet = snippets.get(i);
                String snippetNoQuotes = Utils.removeQuotes(snippet);
                snippets.set(i, snippetNoQuotes);
            }

            parsedCommands.add(parseCommand(snippets));
        }
        return parsedCommands;
    }
}
