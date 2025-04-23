package se.skynet.skyblock;

public class Util {
    public static String intToRoman(int num) {
        // Define the values and symbols
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder result = new StringBuilder();

        // Convert by finding the largest value that fits
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num -= values[i];
                result.append(symbols[i]);
            }
        }

        return result.toString();
    }

    public static String formatNumber(float progress, int i) {
        // Format the number to a string with the specified number of decimal places
        String format = "%." + i + "f";
        return String.format(format, progress);
    }

    public static String repeatCharecter(String s, int i) {
        // Repeat the character 's' for 'i' times
        StringBuilder result = new StringBuilder();
        for (int j = 0; j < i; j++) {
            result.append(s);
        }
        return result.toString();
    }

    public static String insertAt(String string, String insert, int index) {
        // Insert a string at a specific index
        return string.substring(0, index) + insert + string.substring(index);
    }

}
