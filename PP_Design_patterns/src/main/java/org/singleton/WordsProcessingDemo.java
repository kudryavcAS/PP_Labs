package org.singleton;

import java.util.Scanner;

public class WordsProcessingDemo {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.println("Enter the line:");
        String line = in.nextLine();

        WordsProcessing first = WordsProcessing.getInstance(line);
        System.out.println("Enter the second line:");
        line = in.nextLine();
        WordsProcessing second = WordsProcessing.getInstance(line);

        System.out.println("First: Line with the first capital letters:");
        System.out.println(first.processingLine());

        System.out.println("Second: Line with the first capital letters:");
        System.out.println(first.processingLine());

    }
}
