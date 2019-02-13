package ru.spbau.gbarto.commands;

import org.junit.jupiter.api.Test;
import ru.spbau.gbarto.exception.CommandException;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;

class CommandEchoTest {
    @Test
    void testNoParams() throws CommandException, IOException {
        CommandEcho echo = new CommandEcho();
        OutputStream output = echo.execute(null, null);
        output.close();

        assertEquals(System.lineSeparator(), output.toString());
    }

    @Test
    void testSingleParam() throws CommandException, IOException {
        CommandEcho echo = new CommandEcho();
        OutputStream output = echo.execute(new String[]{"word"}, null);
        output.close();

        assertEquals("word" + System.lineSeparator(), output.toString());
    }

    @Test
    void testSeveralParams() throws CommandException, IOException {
        CommandEcho echo = new CommandEcho();
        OutputStream output = echo.execute(new String[]{"8 800", "555 35 35"}, null);
        output.close();

        assertEquals("8 800 555 35 35" + System.lineSeparator(), output.toString());
    }
}