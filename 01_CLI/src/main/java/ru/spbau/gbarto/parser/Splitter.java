package ru.spbau.gbarto.parser;

import ru.spbau.gbarto.exception.ParserException;

import java.util.ArrayList;
import java.util.function.Predicate;

class Splitter {
    private static ArrayList<String> split(String s, Predicate<Character> predicate) throws ParserException {
        ArrayList<String> result = new ArrayList<>();

        int pos = 0;
        while (pos < s.length()) {
            StringBuilder snip = new StringBuilder();

            for (; pos < s.length(); pos++) {
                char c = s.charAt(pos);
                if (predicate.test(c)) {
                    pos++;
                    break;
                }

                if (Utils.isQuote(c)) {
                    int posR = Utils.findRightQuotePos(s, pos);
                    snip.append(s, pos, posR + 1);

                    pos = posR;
                } else {
                    snip.append(c);
                }
            }

            if (snip.length() != 0) {
                result.add(snip.toString());
            }
        }

        return result;
    }

    static ArrayList<String> splitBySpace(String s) throws ParserException {
        return split(s, Character::isSpaceChar);
    }

    static ArrayList<String> splitByPipe(String s) throws ParserException {
        return split(s, c -> c == '|');
    }
}
