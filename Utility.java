package com.tagsofproject;

import java.util.Scanner;
import java.util.InputMismatchException;

public class Utility {
    private final Scanner console; 

    public Utility() {
        console = new Scanner(System.in);
    }

    public static void clearScreen() {  
        /*
         * from
         * https://stackoverflow.com/questions/2979383/how-to-clear-the-console-using-java
         */

        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    public String getString() {
        return console.nextLine();
    }

    public void invalid() {
        invalid("Invalid input");
    }

    public void invalid(String msg) {
        System.out.println(msg+". Please try again.");
        System.out.flush();
        console.nextLine();
    }

    public int getInt(String msg, int min, int max) {
        boolean isValid = false;
        int input = 0;

        while (!isValid) {
            Utility.clearScreen();
            System.out.print(msg);

            try {
                input = console.nextInt();
                console.nextLine(); // flush buffer

                if (input >= min && (max == 0 || input <= max)) { // if max == 0 then ignore
                    isValid = true;
                } else {
                    invalid("Input out of range");
                }
            } catch (InputMismatchException e) {
                invalid();
            }
        }

        return input;
    }
}
