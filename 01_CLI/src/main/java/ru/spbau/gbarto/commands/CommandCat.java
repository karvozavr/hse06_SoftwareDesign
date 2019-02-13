package ru.spbau.gbarto.commands;

import ru.spbau.gbarto.exception.CommandException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Prints passed arguments to output. If there are no arguments, prints input to output.
 */
public class CommandCat extends AbstractCommand {
    /**
     * Executes command.
     *
     * @param args is a String array of arguments
     * @param input is an input stream of data
     * @return output stream
     * @throws CommandException if something goes wrong
     */
    @Override
    public ByteArrayOutputStream execute(String[] args, InputStream input) throws CommandException {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            if (args != null) {
                for (String arg : args) {
                    byte[] bytes = Files.readAllBytes(Paths.get(arg));
                    output.write(bytes);
                    output.flush();
                }
            }

            if ((args == null || args.length == 0) && input != null) {
                byte[] buffer = new byte[1024];
                int read;
                while ((read = input.read(buffer)) > 0) {
                    output.write(buffer, 0, read);
                }
                output.flush();
            }

            return output;
        } catch (IOException e) {
            throw new CommandException("Problem reading or writing data to stream");
        }
    }
}
