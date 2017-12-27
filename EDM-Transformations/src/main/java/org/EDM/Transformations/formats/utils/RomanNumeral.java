package org.EDM.Transformations.formats.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RomanNumeral {

    private static int num;

    private static int[] numbers = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

    private static String[] letters = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    /**
     *
     * @param arabic
     */
    private RomanNumeral(int arabic) {
        if (arabic < 1)
            throw new NumberFormatException("Value of RomanNumeral must be positive.");
        if (arabic > 3999)
            throw new NumberFormatException("Value of RomanNumeral must be 3999 or less.");
        num = arabic;
    }


    /**
     *
     * @param roman
     */
    private RomanNumeral(String roman) {

        if (roman.length() == 0)
            throw new NumberFormatException("An empty string does not define a Roman numeral.");

        roman = roman.toUpperCase();

        int i = 0;
        int arabic = 0;

        while (i < roman.length()) {
            char letter = roman.charAt(i);
            int number = letterToNumber(letter);

            i++;

            if (i == roman.length())    arabic += number;
            else {
                int nextNumber = letterToNumber(roman.charAt(i));
                if (nextNumber > number) {
                    arabic += (nextNumber - number);
                    i++;
                }else arabic += number;
            }
        }
        if (arabic > 3999)
            throw new NumberFormatException("Roman numeral must have value 3999 or less.");
        num = arabic;
    }


    /**
     *
     * @param letter
     * @return
     */
    private int letterToNumber(char letter) {
        switch (letter) {
            case 'I':  return 1;
            case 'V':  return 5;
            case 'X':  return 10;
            case 'L':  return 50;
            case 'C':  return 100;
            case 'D':  return 500;
            case 'M':  return 1000;
            default:   throw new NumberFormatException(
                    "Illegal character \"" + letter + "\" in Roman numeral");
        }
    }


    /**
     * Return the standard representation of this Roman numeral.
     */
    public static String toRoman(int numeral) {
        new RomanNumeral(numeral);
        String roman = "";  // The roman numeral.
        int N = num;        // N represents the part of num that still has
        //   to be converted to Roman numeral representation.
        for (int i = 0; i < numbers.length; i++) {
            while (N >= numbers[i]) {
                roman += letters[i];
                N -= numbers[i];
            }
        }
        return roman;
    }


    /**
     * Return the value of this Roman numeral as an int.
     */
    public static int toNumerical(String roman) {
        new RomanNumeral(roman);
        return num;
    }

    public static boolean isRoman(String input){
        Pattern pattern = Pattern.compile("[MDCLXVI]+");
        Matcher m = pattern.matcher(input);
        return m.find();
    }

    /**
     *
     *
     * @param input
     * @return
     */
    public static String format(String input){
        Pattern pattern = Pattern.compile("[MDCLXVI]+");
        Matcher m = pattern.matcher(input);
        Set<String> builder = new HashSet<>();
        while (m.find())    builder.add(m.group());
        return !builder.isEmpty() ? builder.stream().collect(Collectors.joining("-")) : null;
    }
}
