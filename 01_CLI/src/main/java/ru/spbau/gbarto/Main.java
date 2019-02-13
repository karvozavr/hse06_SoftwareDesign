package ru.spbau.gbarto;

import ru.spbau.gbarto.exception.CommandException;
import ru.spbau.gbarto.exception.ParserException;

import java.util.Scanner;

/**
 * CLI is a class that allows you to use CLI.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CLI cli = new CLI();
        while (true) {
            String line = scanner.nextLine();
            try {
                cli.execute(line, System.out);
            } catch (ParserException e) {
                System.out.println("Parser Exception " + e.getMessage());
            } catch (CommandException e) {
                System.out.println("Command Exception " + e.getMessage());
            }
        }
    }
}
