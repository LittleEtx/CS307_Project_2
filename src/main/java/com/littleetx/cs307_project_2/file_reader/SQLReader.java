package com.littleetx.cs307_project_2.file_reader;

import java.io.*;
import java.util.Iterator;

public class SQLReader implements Iterable<String>, AutoCloseable {
    private final BufferedReader reader;
    private final StringBuilder builder = new StringBuilder();
    public SQLReader(File file) {
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                if (builder.length() > 0) {
                    return true;
                }
                try {
                    int c;
                    while (reader.ready() && (c = reader.read()) != ';') {
                        builder.append((char) c);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return builder.length() > 0;
            }

            @Override
            public String next() {
                if (!hasNext())
                    throw new IllegalStateException();
                String str = builder.toString();
                builder.setLength(0);
                return str;
            }
        };
    }

    @Override
    public void close() throws Exception {
        reader.close();
    }
}
