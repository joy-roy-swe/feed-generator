package com.solvians.showcase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates valid ISIN codes.
 *
 * Format:
 * - 2 uppercase country letters
 * - 9 alphanumeric characters
 * - 1 check digit
 *
 */
public final class IsinGenerator {

    private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * check digit for the first 11 characters (before the check digit is appended).
     */

    static int computeCheckDigit(String elevenChars) {
        if (elevenChars == null || elevenChars.length() != 11) {
            throw new IllegalArgumentException("Expected exactly 11 characters");
        }
        List<Integer> digits = expandToDigits(elevenChars);
        int n = digits.size();
        int sum = 0;
        for (int i = 0; i < n; i++) {
            int d = digits.get(i);
            int indexFromRight = n - 1 - i;
            if (indexFromRight % 2 == 0) {
                int doubled = d * 2;
                sum += doubled / 10 + doubled % 10;
            } else {
                sum += d;
            }
        }
        int mod = sum % 10;
        return mod == 0 ? 0 : 10 - mod;
    }

    static List<Integer> expandToDigits(String elevenChars) {
        List<Integer> digits = new ArrayList<>();
        for (int i = 0; i < elevenChars.length(); i++) {
            char ch = elevenChars.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                int v = 10 + (ch - 'A');
                digits.add(v / 10);
                digits.add(v % 10);
            } else if (ch >= '0' && ch <= '9') {
                digits.add(ch - '0');
            } else {
                throw new IllegalArgumentException("Invalid character in ISIN base: " + ch);
            }
        }
        return digits;
    }

    /**
     * returns a random 12-character ISIN (11 base + check digit).
     */
    public String generate() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(11);
        for (int i = 0; i < 2; i++) {
            sb.append((char) ('A' + r.nextInt(26)));
        }
        for (int i = 0; i < 9; i++) {
            sb.append(ALPHANUMERIC.charAt(r.nextInt(ALPHANUMERIC.length())));
        }
        int check = computeCheckDigit(sb.toString());
        return sb.append(check).toString();
    }

    static boolean isValid(String isin) {
        if (isin == null || isin.length() != 12) {
            return false;
        }
        for (int i = 0; i < 11; i++) {
            char c = isin.charAt(i);
            if (i < 2) {
                if (c < 'A' || c > 'Z') {
                    return false;
                }
            } else {
                if (!((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z'))) {
                    return false;
                }
            }
        }
        char last = isin.charAt(11);
        if (last < '0' || last > '9') {
            return false;
        }
        int expected = computeCheckDigit(isin.substring(0, 11));
        return expected == (last - '0');
    }
}
