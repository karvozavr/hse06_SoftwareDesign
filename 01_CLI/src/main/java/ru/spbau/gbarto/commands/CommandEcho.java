package ru.spbau.gbarto.commands;

import ru.spbau.gbarto.exception.CommandException;

import java.io.*;

/**
 * Prints passed arguments to output. Ignores input.
 */
public class CommandEcho extends AbstractCommand {
    /**
     * Executes command.
     *
     * @param args is a String array of arguments
     * @param input is an input stream of data (ignored)
     * @return output stream
     * @throws CommandException if something goes wrong
     */
    @Override
    public ByteArrayOutputStream execute(String[] args, InputStream input) throws CommandException {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            StringBuilder str = new StringBuilder();
            if (args != null) {
                str.append(String.join(" ", args));
            }
            str.append(System.lineSeparator());

            output.write(str.toString().getBytes());
            return output;
        } catch (IOException e) {
            throw new CommandException("Problem reading or writing data to stream");
        }
    }
}
