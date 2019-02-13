package ru.spbau.gbarto.parser;

import ru.spbau.gbarto.exception.ParserException;

import java.util.HashMap;
import java.util.LinkedList;

class Substitutor {
    private HashMap<String, String> variables;

    Substitutor(HashMap<String, String> variables) {
        this.variables = variables;
    }

    String substitute(String s) throws ParserException {
        StringBuilder res = new StringBuilder();
        LinkedList<Character> stack = new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '$' && (stack.isEmpty() || stack.getFirst() == '\"')) {
                int endOfVar = i + 1;
                while (endOfVar < s.length()) {
                    char current = s.charAt(endOfVar);
                    if (!Character.isLetter(current) && current != '_') {
                        break;
                    }
                    endOfVar++;
                }
                String variable = s.substring(i + 1, endOfVar);
                if (variables.containsKey(variable)) {
                    String value = variables.get(variable);
                    res.append(value);
                } else {
                    throw new ParserException("could not find variable with name \'" + variable + "\'");
                }
                i = endOfVar - 1;
            } else {
                if (Utils.isQuote(c)) {
                    if (!stack.isEmpty() && stack.getLast() == c) {
                        stack.pop();
                    } else {
                        stack.add(c);
                    }
                }
                res.append(c);
            }
        }

        return res.toString();
    }
}
