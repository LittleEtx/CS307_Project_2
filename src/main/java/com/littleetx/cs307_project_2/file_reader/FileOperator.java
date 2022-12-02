package com.littleetx.cs307_project_2.file_reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public abstract class FileOperator {
    protected final String filePath;

    FileOperator(String fileName) {
        this.filePath = fileName;
    }

    abstract protected boolean hasNextRow(BufferedReader reader);


    abstract protected String[] getNextRow(BufferedReader reader);

    public FileOperatorReader getReader() throws FileNotFoundException {
        return new FileOperatorReader() {
            private final BufferedReader reader = new BufferedReader(new FileReader(filePath));

            @Override
            public Iterator<String[]> iterator () {
                return new Iterator<>() {

                    @Override
                    public boolean hasNext() {
                        return hasNextRow(reader);
                    }

                    @Override
                    public String[] next() {
                        return getNextRow(reader);
                    }
                };
            }

            @Override
            public void close() throws IOException {
                reader.close();
            }
        };
    }
}
