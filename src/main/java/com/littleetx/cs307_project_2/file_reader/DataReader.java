package com.littleetx.cs307_project_2.file_reader;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

/**
 * An abstract class used as to read files
 */
public abstract class DataReader implements Iterable<String[]>, AutoCloseable, Closeable {
    private final BufferedReader reader;

    public DataReader(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    @Override
    public Iterator<String[]> iterator() {
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

    abstract protected boolean hasNextRow(BufferedReader reader);


    abstract protected String[] getNextRow(BufferedReader reader);

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
