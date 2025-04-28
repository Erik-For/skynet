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

    // for formating coins primarily
    public static String formatNumberShorthand(double number) {
        // Format the number to a string with shorthand notation
        if (number >= 1_000_000_000) {
            return String.format("%.1fB", number / 1_000_000_000.0D);
        } else if (number >= 1_000_000) {
            return String.format("%.1fM", number / 1_000_000.0D);
        } else if (number >= 1_000) {
            return String.format("%.1fK", number / 1_000.0D);
        } else {
            return formatNumber(number, 1);
        }
    }

    public static String formatNumberWithCommas(double number) {
        // Format the number to a string with commas and a dot
        StringBuilder result = new StringBuilder();
        String strNumber = String.valueOf((long) number);
        int length = strNumber.length();

        for (int i = 0; i < length; i++) {
            if (i > 0 && (length - i) % 3 == 0) {
                result.append(",");
            }
            result.append(strNumber.charAt(i));
        }
        // Add the decimal part if it exists and number is less than 1 000 000
        if (number < 1_000_000 && number % 1 != 0) {
            result.append(".");
            String decimalPart = String.valueOf(number).split("\\.")[1];
            result.append(decimalPart.substring(0, 1));
        }

        return result.toString();
    }

    public static String formatNumber(double progress, int i) {
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
