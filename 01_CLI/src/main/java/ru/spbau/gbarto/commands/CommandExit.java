package ru.spbau.gbarto.commands;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Ends the program. Ignores args and input.
 */
public class CommandExit extends AbstractCommand {
    /**
     * Executes command.
     *
     * @param args is a String array of arguments (ignored)
     * @param input is an input stream of data (ignored)
     * @return output stream
     */
    @Override
    public ByteArrayOutputStream execute(String[] args, InputStream input) {
        System.exit(0);
        return new ByteArrayOutputStream();
    }
}
