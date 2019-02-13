package ru.spbau.gbarto;

import org.junit.jupiter.api.Test;
import ru.spbau.gbarto.exception.CommandException;
import ru.spbau.gbarto.exception.ParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CLITest {
    @Test
    void testExecuteCommand() throws ParserException, CommandException, IOException {
        CLI cli = new CLI();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        cli.execute("echo \"8 800 55 35 35\"", output);
        output.close();
        assertEquals("8 800 55 35 35" + System.lineSeparator(), output.toString());
    }

    @Test
    void testVariableSubstitute() throws ParserException, CommandException, IOException {
        CLI cli = new CLI();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        cli.execute("a=8", output);
        output.close();
        assertEquals("", output.toString());

        output = new ByteArrayOutputStream();
        cli.execute("b=800", output);
        output.close();
        assertEquals("", output.toString());

        output = new ByteArrayOutputStream();
        cli.execute("c=555", output);
        output.close();
        assertEquals("", output.toString());

        output = new ByteArrayOutputStream();
        cli.execute("d=\"35 35\"", output);
        output.close();
        assertEquals("", output.toString());

        output = new ByteArrayOutputStream();
        cli.execute("echo \"$a $b $c $d\"", output);
        output.close();
        assertEquals("8 800 555 35 35" + System.lineSeparator(), output.toString());
    }

    @Test
    void testPipe() throws ParserException, CommandException, IOException {
        CLI cli = new CLI();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        cli.execute("echo \"8 800" + System.lineSeparator() + "555 35 35\" | wc", output);
        output.close();
        assertEquals("2 5 16" + System.lineSeparator(), output.toString());
    }
}