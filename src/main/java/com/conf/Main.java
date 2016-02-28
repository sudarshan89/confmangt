package com.conf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

/**
 * Author: Nthdimenzion
 */

public class Main {

    public static void main(String[] args) throws IOException {
        int numberOfTracks = 2;
        String fileName = args.length == 1 ? args[0] : "demo.txt";
        System.out.println("If you want to specify a custom input file java -jar confmangt.jar mypath\\myfile.txt");
        System.out.println("Input file processed is " + fileName);
        System.out.println("Enter the number of tracks in Conference (Will assume 2 tracks if input is invalid :) : ");
        Scanner sc = new Scanner(System.in);
        try {
            numberOfTracks = sc.nextInt();
        } catch (Exception ex) {
            System.out.println("Too bad invalid input, lets go with 2 tracks");
        }
        System.out.println(numberOfTracks);
        final List<String> lines = readFile(fileName);
        Conference.Kickstart(lines, numberOfTracks);

        test(() -> new String("Hello"));
    }

    public static void test(Supplier<String> helloworld){

    }

    private static List<String> readFile(String fileName) throws IOException {
        return Files.readAllLines(Paths.get(fileName));

    }
}
