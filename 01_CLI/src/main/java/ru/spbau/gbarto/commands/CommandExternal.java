package ru.spbau.gbarto.commands;

import ru.spbau.gbarto.exception.CommandException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Runs an external command, passing arguments and input stream to it.
 */
public class CommandExternal extends AbstractCommand {
    private String command;

    public CommandExternal(String command) {
        this.command = command;
    }

    private void setInput(Process process, InputStream input) throws IOException {
        OutputStream output = process.getOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        while ((read = input.read(buffer)) > 0) {
            output.write(buffer, 0, read);
        }
        output.close();
    }

    private ByteArrayOutputStream getOutput(Process process) throws IOException {
        InputStream input = process.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }
        output.flush();
        return output;
    }

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
            List<String> allArgs = new ArrayList<>();
            allArgs.add(command);
            allArgs.addAll(Arrays.asList(args));

            ProcessBuilder processBuilder = new ProcessBuilder(allArgs);
            Process process = processBuilder.start();
            setInput(process, input);
            process.waitFor();
            return getOutput(process);
        } catch (IOException e) {
            throw new CommandException("Problem reading or writing data to stream");
        } catch (InterruptedException e) {
            throw new CommandException("Command was interrupted");
        }
    }
}
