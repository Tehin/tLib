package dev.tehin.tlib.utilities;

import java.util.Arrays;
import java.util.Collection;

public class AlgorithmicUtil {

    public static <T> String getBestMatch(Collection<T> collection, String regex, String[] target) {
        String bestMatch = null;
        int bestMatchLength = 0;

        for (T object : collection) {
            String asString = object.toString();
            String[] asArray = asString.split(regex);

            int matches = getMatchesCountInOrder(asArray, target);

            /*
             * NOTE: We can't skip the matches count even if it's the first one,
             * since we need to check for better options
             */
            if (bestMatch == null) {
                bestMatch = asString;
                bestMatchLength = matches;
                continue;
            }

            // Ignore this match since does not meet the last threshold
            if (matches <= bestMatchLength) continue;

            // Set the new threshold
            bestMatch = asString;
            bestMatchLength = matches;
        }

        return bestMatch;
    }

    /**
     * NOTE: We do not use a set since we have to compare them in order
     *
     * @param first First array to be compared
     * @param second Second array to be compared with
     * @return How many matches, in order, are found between the two arrays (NOT case-sensitive)
     */
    public static int getMatchesCountInOrder(String[] first, String[] second) {
        // This prevents us from going out of bounds
        final int max = Math.min(first.length, second.length);

        if (max == 0) return 0;

        int matches = 0;
        for (int i = 0; i < max; i++) {
            String firstMatch = first[i];
            String secondMatch = second[i];

            if (firstMatch.equalsIgnoreCase(secondMatch)) matches++;
        }

        return matches;
    }

}
