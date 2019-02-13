package ru.spbau.gbarto.parser;

import ru.spbau.gbarto.exception.ParserException;

import java.util.LinkedList;

class Utils {
    static boolean isQuote(char c) {
        return c == '\'' || c == '\"';
    }

    static int findRightQuotePos(String s, int pos) throws ParserException {
        char c = s.charAt(pos);
        if (!isQuote(c)) {
            throw new ParserException("Symbol \'" + c + "\' is not a quote");
        }

        LinkedList<Character> stack = new LinkedList<>();
        stack.add(c);

        for (int i = pos + 1; i < s.length(); i++) {
            c = s.charAt(i);
            if (isQuote(c)) {
                if (stack.getLast() == c) {
                    stack.pop();
                } else {
                    stack.add(c);
                }
            }

            if (stack.isEmpty()) {
                return i;
            }
        }

        throw new ParserException("Could not find closing quote");
    }

    static String removeQuotes(String s) throws ParserException {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (isQuote(c)) {
                int rightQuotePos = findRightQuotePos(s, i);
                res.append(s, i + 1, rightQuotePos);
                i = rightQuotePos;
            } else {
                res.append(c);
            }
        }

        return res.toString();
    }
}

