package org.example;

import java.util.Scanner;

public class TextCipher {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        StringBuilder text = new StringBuilder();

        System.out.println("Enter the line (to complete, press enter):");

        while (true) {
            String line = in.nextLine();
            if (line.isEmpty()) {
                break;
            }
            text.append(line).append("\n");
        }
        text.deleteCharAt(text.length() - 1);

        System.out.println(encryptText(text.toString()));


    }

    static String encryptText(String text) {

        StringBuilder result = new StringBuilder();
        for (int mod = 0; mod < 3; mod++) {
            for (int i = mod; i < text.length(); i += 3) {
                result.append(text.charAt(i));
            }
        }
        return result.toString();
    }
}
