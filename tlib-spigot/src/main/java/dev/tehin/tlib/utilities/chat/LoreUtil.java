package dev.tehin.tlib.utilities.chat;

import java.util.ArrayList;
import java.util.List;

public class LoreUtil {

    // TODO: Mejorar, se lo robé a Elecast
    public static List<String> split(String input) {
        return split(input, "&7");
    }

    public static List<String> split(String input, String color) {
        int maxLines = 6;
        int maxChars = 40;

        List<String> lines = new ArrayList<>();
        String[] words = input.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (lines.size() < (maxLines - 1)) {
                if (currentLine.length() + word.length() > maxChars) {
                    lines.add(color + currentLine.toString().trim());
                    currentLine = new StringBuilder();
                }
                if (currentLine.length() + word.length() == maxChars) {
                    currentLine.append(word);
                } else {
                    currentLine.append(word).append(" ");
                }
            } else {
                currentLine.append(word).append(" ");
            }
        }

        if (!currentLine.isEmpty()) {
            String lastLine = currentLine.toString().trim();
            if (lastLine.length() > maxChars) {
                System.out.println("El string '" + input + "' supera el límite máximo de caracteres.");
                lastLine = lastLine.substring(0, 40);
            }
            lines.add(color + lastLine);
        }

        return lines;
    }

}
