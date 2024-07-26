package org.prashant.blog.blogapplicationapi.utils;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {

    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+<,>.";

    private static final int PASSWORD_LENGTH = 12;

    public static String generatePassword() {
        String allCharacters = UPPER_CASE + LOWER_CASE + DIGITS + SPECIAL_CHARACTERS;
        SecureRandom random = new SecureRandom();

        List<Character> passwordCharacters = new ArrayList<>();

        // Ensure the password contains at least one character from each character set
        passwordCharacters.add(UPPER_CASE.charAt(random.nextInt(UPPER_CASE.length())));
        passwordCharacters.add(LOWER_CASE.charAt(random.nextInt(LOWER_CASE.length())));
        passwordCharacters.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        passwordCharacters.add(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        // Fill the rest of the password length with random characters
        for (int i = passwordCharacters.size(); i < PASSWORD_LENGTH; i++) {
            passwordCharacters.add(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        // Shuffle the characters to ensure randomness
        Collections.shuffle(passwordCharacters, random);

        // Convert character list to a string
        StringBuilder password = new StringBuilder();
        for (char ch : passwordCharacters) {
            password.append(ch);
        }

        return password.toString();
    }
}