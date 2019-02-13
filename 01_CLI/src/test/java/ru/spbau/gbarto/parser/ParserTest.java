package ru.spbau.gbarto.parser;

import org.junit.jupiter.api.Test;
import ru.spbau.gbarto.exception.ParserException;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    @Test
    void testSplitBySpaceCommand() throws ParserException {
        ArrayList<ParsedCommand> res = Parser.parse("cat file1.txt \"file2.json  \"", new HashMap<>());

        assertEquals(1, res.size());

        ParsedCommand parsedCommand = res.get(0);
        assertEquals(ParsedCommand.Type.COMMAND, parsedCommand.type);
        assertEquals("cat", parsedCommand.command);
        assertNotEquals(null, parsedCommand.args);
        assertEquals(2, parsedCommand.args.length);
        assertEquals("file1.txt", parsedCommand.args[0]);
        assertEquals("file2.json  ", parsedCommand.args[1]);
    }

    @Test
    void testSplitBySpaceAssignment() throws ParserException {
        ArrayList<ParsedCommand> res = Parser.parse("x=\"lol kek\"", new HashMap<>());

        assertEquals(1, res.size());

        ParsedCommand parsedCommand = res.get(0);
        assertEquals(ParsedCommand.Type.ASSIGNMENT, parsedCommand.type);
        assertEquals("x", parsedCommand.variable);
        assertEquals("lol kek", parsedCommand.value);
    }

    @Test
    void testSplitByPipe() throws ParserException {
        ArrayList<ParsedCommand> res = Parser.parse("cat \"file2.json  \" | wc", new HashMap<>());

        assertEquals(2, res.size());

        ParsedCommand parsedCommand = res.get(0);
        assertEquals(ParsedCommand.Type.COMMAND, parsedCommand.type);
        assertEquals("cat", parsedCommand.command);
        assertNotEquals(null, parsedCommand.args);
        assertEquals(1, parsedCommand.args.length);
        assertEquals("file2.json  ", parsedCommand.args[0]);

        parsedCommand = res.get(1);
        assertEquals(ParsedCommand.Type.COMMAND, parsedCommand.type);
        assertEquals("wc", parsedCommand.command);
        assertNull(parsedCommand.args);
    }

    @Test
    void testSubstitute() throws ParserException {
        HashMap<String, String> variables = new HashMap<>();
        variables.put("a", "8");
        variables.put("b", "800");
        variables.put("c", "555");
        variables.put("d", "35 35");

        ArrayList<ParsedCommand> res = Parser.parse("echo \"$a $b $c $d\"", variables);

        assertEquals(1, res.size());

        ParsedCommand parsedCommand = res.get(0);
        assertEquals(ParsedCommand.Type.COMMAND, parsedCommand.type);
        assertEquals("echo", parsedCommand.command);
        assertNotEquals(null, parsedCommand.args);
        assertEquals(1, parsedCommand.args.length);
        assertEquals("8 800 555 35 35", parsedCommand.args[0]);
    }
}