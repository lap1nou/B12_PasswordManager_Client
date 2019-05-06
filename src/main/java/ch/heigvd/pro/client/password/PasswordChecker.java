package ch.heigvd.pro.client.password;

import ch.heigvd.pro.client.structure.Entry;

import java.util.ArrayList;
import java.util.Arrays;

public class PasswordChecker {

    private static HaveIBeenPwned pwnChecker = new HaveIBeenPwned();
    private final static int MIN_LENGTH = 8;
    private final static int MAX_POINT_LENGTH = 35;
    private final static int MAX_POINT_UPPERCASE = 24;
    private final static int MAX_POINT_LOWERCASE = 12;
    private final static int MAX_POINT_NUMERIC = 13;
    private final static int MAX_POINT_SPECIAL_CHAR = 16;
    private final static double RATIO_LENGTH = 2.5;
    private final static int POINT = 8;

    private static ArrayList<Character> specialCharacter = new ArrayList<Character>(Arrays.asList('~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-'));

    /**
     * This function will check all new password who will be assign to an entry and will define
     * the password score to know if the password is secure or not.
     * /!\ This information isn't quantifiable, so it's indicative information. To be carefull /!\
     * The scores begin at 0 until 100
     * source: https://www.javacodeexamples.com/check-password-strength-in-java-example/668
     * https://en.wikipedia.org/wiki/Password_strength#cite_note-23
     *
     * @param password password to check
     * @return the password score
     */
    public static int checkStrong(char[] password) {
        int lengthPoint = 0;
        int upperCasePoint = 0;
        int lowerCasePoint = 0;
        int numericPoint = 0;
        int specialCharPoint = 0;

        // Check if the password length is bigger than 8 and that the password is not leaked
        //if(password.length < MIN_LENGTH || pwnChecker.isLeaked(password)){
        if (password.length < MIN_LENGTH || pwnChecker.isLeaked(password)) {
            return lengthPoint;
        }

        /*
         * It will define the point of password length
         * Min: 13 pts (8 characters) | Max: 35 pts (< 13 character)
         */
        if (password.length * RATIO_LENGTH > MAX_POINT_LENGTH) {
            lengthPoint += MAX_POINT_LENGTH;
        } else {
            lengthPoint += password.length * RATIO_LENGTH;
        }

        for (int i = 0; i < password.length; ++i) {

            /*
             * It will define the point if the password contains uppercase letter
             * 3 characters: 24 pts | 2 characters: 16 pts | 1 characters: 8 pts
             */
            if (Character.isUpperCase(password[i])) {
                if (upperCasePoint < MAX_POINT_UPPERCASE) {
                    upperCasePoint += POINT;
                }
            }

            /*
             * It will define the point if the password contains special characters
             * 2 special characters: 16 pts | 1 special character: 8 pts
             */
            if (specialCharacter.contains(password[i])) {
                if (specialCharPoint < MAX_POINT_SPECIAL_CHAR) {
                    specialCharPoint += POINT;
                }
            }

            //It will define the point if the password contains 1 or more lowercase character
            if (Character.isLowerCase(password[i])) {
                if (lowerCasePoint == 0) {
                    lowerCasePoint += MAX_POINT_LOWERCASE;
                }
            }

            //It will define the point if the password contains 1 or more lowercase character
            if (Character.isDigit(password[i])) {
                if (numericPoint == 0) {
                    numericPoint += MAX_POINT_NUMERIC;
                }
            }
        }

        return upperCasePoint + lowerCasePoint + specialCharPoint + numericPoint + lengthPoint;
    }

}
