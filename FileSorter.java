package com.company;

import java.io.*;

public class FileSorter {
    private BufferedReader[] lineReaders;
    private BufferedWriter lineWriter;
    private Integer numberOfFiles;
    private String[] actualElement;
    private FileWriter fileWriter;
    private Boolean isNumeric;
    private Boolean isIncrease;

    private void creatingReaders(String[] inputFilenames){
        FileReader[] fileReaders;
        fileReaders = new FileReader[inputFilenames.length];
        lineReaders = new BufferedReader[inputFilenames.length];
        for (int i = 0; i < inputFilenames.length; ++i) {
            try {
                fileReaders[i] = new FileReader(inputFilenames[i]);
                lineReaders[i] = new BufferedReader(fileReaders[i]);
            }
            catch (FileNotFoundException ex){
                System.out.println("File " + inputFilenames[i] + " not found");
                lineReaders[i] = null;
            }
        }
    }

    private void creatingWriter(String outputFilename){
        //FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(outputFilename);
            lineWriter = new BufferedWriter(fileWriter);
        }
        catch (IOException ex) {
            System.out.println("Error in create out file");
        }
    }

    public FileSorter(Boolean isIncrease, Boolean isNumeric, String outFilename, String[] inFilenames){
        this.isNumeric = isNumeric;
        this.isIncrease = isIncrease;
        numberOfFiles = inFilenames.length;
        creatingWriter(outFilename);
        creatingReaders(inFilenames);
        if (0 == numberOfFiles){
            System.out.println("No one file found");
        }
    }

    private void readFromFile(int index){
        if(null == lineReaders[index]){
            return;
        }
        String previousElement = actualElement[index];
        try {
            actualElement[index] = lineReaders[index].readLine();
        }
        catch (IOException ex){
            System.out.println("Error in read input file");
        }
        if(null != actualElement[index]) {
            if (isNumeric) {
                if ((isIncrease && (toNumeric(previousElement) > toNumeric(actualElement[index]))) ||
                        (!isIncrease && (toNumeric(previousElement) < toNumeric(actualElement[index])))) {
                    System.out.println("Error! Sort order broken");
                }
            } else {
                if ((isIncrease && (actualElement[index].compareTo(previousElement) <= 0)) ||
                        (!isIncrease && (actualElement[index].compareTo(previousElement) >= 0))) {
                    System.out.println("Error! Sort order broken");
                }
            }
        }
    }
    private void writeInOut(String result){
        try {
            lineWriter.write(result+System.lineSeparator());
            System.out.println(result);
        }
        catch (IOException ex){
            System.out.println("Error in write in out file");
        }
    }

    private int toNumeric(String string) {
        if(string == null || string.equals("")) {
            System.out.println("String cannot be parsed, it is null or empty.");
            return -1;
        }
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            System.out.println("Input string cannot be parsed to integer.");
        }
        return -1;
    }

    private int fillActualElements(){
        int numOfNullElements = 0;
        actualElement = new String[numberOfFiles];
        try {
            for (int i = 0; i < numberOfFiles; ++i) {
                if(null == lineReaders[i]){
                    continue;
                }
                actualElement[i] = lineReaders[i].readLine();
                if(null == actualElement[i]){
                    numOfNullElements += 1;
                }
            }
        }
        catch(IOException ex){
            System.out.println("Error in read in-file");
        }
        return numOfNullElements;
    }
    public void mergeSort(){
        int numOfNullElements = fillActualElements();
        while (numOfNullElements != numberOfFiles){
            String min = null;
            int minIndex = -1;
            for(int i = 0; i < numberOfFiles; ++i){
                if(null == min && null != actualElement[i]){
                    min = actualElement[i];
                    minIndex = i;
                    continue;
                }
                if(null == actualElement[i] || null == min){
                    continue;
                }
                if(isIncrease){
                    if(isNumeric){
                        if(toNumeric(actualElement[i]) <= toNumeric(min)){
                            min = actualElement[i];
                            minIndex = i;
                        }
                    }
                    else{
                        if(actualElement[i].compareTo(min) <= 0){
                            min = actualElement[i];
                            minIndex = i;
                        }
                    }
                }
                else{
                    if(isNumeric){
                        if(toNumeric(actualElement[i]) >= toNumeric(min)){
                            min = actualElement[i];
                            minIndex = i;
                        }
                    }
                    else{
                        if(actualElement[i].compareTo(min) >= 0){
                            min = actualElement[i];
                            minIndex = i;
                        }
                    }
                }

            }
            writeInOut(min);
            readFromFile(minIndex);
            numOfNullElements = 0;
            for (String el: actualElement) {
                if(null == el){
                    numOfNullElements += 1;
                }
            }
        }
        try{
            lineWriter.close();
            fileWriter.close();
        }
        catch (IOException ex){
            System.out.println("Error in close writers");
        }

    }
}
