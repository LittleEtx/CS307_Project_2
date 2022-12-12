package com.littleetx.cs307_project_2.file_reader;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public static void runSQL(String sqlFile, Connection con) {
        File file = new File(sqlFile);
        SQLReader reader = new SQLReader(file);
        try {
            Statement stmt = con.createStatement();
            for (String sql : reader) {
                stmt.execute(sql);
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to run sql", e);
        }
    }

    public static List<ResultSet> runQuery(String sqlFile, Connection con) {
        File file = new File(sqlFile);
        SQLReader reader = new SQLReader(file);
        List<ResultSet> result = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            for (String sql : reader) {
                result.add(stmt.executeQuery(sql));
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to run sql", e);
        }
        return result;
    }
}
