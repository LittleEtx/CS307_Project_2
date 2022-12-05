package com.littleetx.cs307_project_2;

import java.io.PrintStream;

public class Debug {
    public static boolean isOn;
    public static PrintStream out = System.out;
    public static void print(String str) {
        if (isOn)
            out.print(str);
    }
    public static void println(String str) {
        if (isOn)
            out.println(str);
    }
}
