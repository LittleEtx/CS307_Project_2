package com.littleetx.cs307_project_2.file_reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileOperator_CSV extends FileOperator {


    public FileOperator_CSV(String fileName) {
        super(fileName);
    }
    private final List<String> results = new ArrayList<>();

    @Override
    protected boolean hasNextRow(BufferedReader reader) {
        if (!results.isEmpty()) {
            return true;
        }

        try {
            if (!reader.ready())
                return false;
            getValues(reader);
            return !results.isEmpty();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String[] getNextRow(BufferedReader reader) {
        if (!hasNextRow(reader)) {
            throw new IllegalStateException("No more rows");
        }
        String[] values = results.toArray(new String[0]);
        results.clear();
        return values;
    }


    private void getValues(BufferedReader reader) {
        boolean isInQuote = false;
        StringBuilder value = new StringBuilder();
        try {
            String line = reader.readLine();
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (isInQuote) {
                    //in quote only ends with single quoteMark
                    if (c == '"') {
                        if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                            value.append('"');
                            i++;
                        } else {
                            isInQuote = false;
                            results.add(value.toString());
                            value = new StringBuilder();
                        }
                    } else {
                        value.append(c);
                    }
                }
                else { //not in quote
                    if (c == '"' && value.length() == 0) {
                        isInQuote = true;
                    } else if (c == ',') {
                        //null value
                        if (value.length() == 0) {
                            results.add(null);
                        } else {
                            results.add(value.toString());
                        }
                        value = new StringBuilder();
                    } else {
                        value.append(c);
                    }
                }

                if (i == line.length() - 1) {
                    //deal with multi-line value
                    if (isInQuote) {
                        value.append(System.getProperty("line.separator"));
                        i = -1;
                        line = reader.readLine();
                    } else {
                        //insert the last value
                        if (value.length() == 0 && line.charAt(i) != '"') {
                            results.add(null);
                        } else {
                            results.add(value.toString());
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        FileOperator fo = new FileOperator_CSV("data.csv");
        try (FileOperatorReader reader = fo.getReader()) {
            Iterator< String[]> iter = reader.iterator();
            for (String str : iter.next()) {
                System.out.print(str + ",");
            }
            System.out.println();
            for (String str : iter.next()) {
                System.out.print(str + ",");
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}



