package com.company;

import java.util.Arrays;

public class Main {

    private static Boolean isNumeric;
    private static Boolean isIncrease = true;
    private static Boolean isIncreaseAuto = false;
    private static String outFilename;
    private static String[] inFilenames;
    private static Boolean isError = false;

    private static void isIntOrStringParsing(String arg){
        switch (arg) {
            case ("-s") -> isNumeric = false;
            case ("-i") -> isNumeric = true;
            default -> {
                System.out.println("First parameter is -a for increase and -d for decrease (optional)");
                System.out.println("Second parameter is -i for int and -s for string");
                isError = true;
            }
        }

    }
    private static void argumentsParsing(String[] args){
        if(args.length < 3){
            isError = true;
        }
        switch (args[0]) {
            case ("-a") -> {
                isIncrease = true;
                isIntOrStringParsing(args[1]);
            }
            case ("-d") -> {
                isIncrease = false;
                isIntOrStringParsing(args[1]);
            }
            default -> {
                isIncreaseAuto = true;
                isIntOrStringParsing(args[0]);
            }
        }
        int minusOptionalArgument = isIncreaseAuto ? 1 : 0;
        outFilename = args[2 - minusOptionalArgument];
        inFilenames = Arrays.copyOfRange(args, 3 - minusOptionalArgument, args.length);
    }
    public static void main(String[] args) {
        argumentsParsing(args);
        if(isError){
            System.out.println("Error in parsing arguments. Please, check if the arguments are correct.");
        }
        FileSorter fs = new FileSorter(isIncrease, isNumeric, outFilename, inFilenames);
        fs.mergeSort();
    }
}
