package ru.spbau.gbarto.commands;

import ru.spbau.gbarto.exception.CommandException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Returns current path.
 */
public class CommandPwd extends AbstractCommand {

    private Path currentDirectory;

    public CommandPwd(Environment environment) {
        currentDirectory = environment.getCurrentDirectory();
    }

    /**
     * Executes command.
     *
     * @param args  is a String array of arguments
     * @param input is an input stream of data (ignored)
     * @return output stream
     * @throws CommandException if something goes wrong
     */
    @Override
    public ByteArrayOutputStream execute(String[] args, InputStream input) throws CommandException {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            String pwd = currentDirectory.toString() + System.lineSeparator();
            output.write(pwd.getBytes());

            return output;
        } catch (IOException e) {
            throw new CommandException("Problem reading or writing data to stream");
        }
    }
}
