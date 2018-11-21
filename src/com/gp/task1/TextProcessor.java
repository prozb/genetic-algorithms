package com.gp.task1;

import java.io.*;
import java.util.Arrays;

public class TextProcessor {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("plot.txt"));

        StringBuilder sb = new StringBuilder();

        String read = reader.readLine();
        while (read != null){
            String s = processLine(read);
            s = changeFirstTwoCols(s);
            s = s.replace(",", "\t\t");
            System.out.println(s);

            sb.append(s);
            sb.append("\n");

            read = reader.readLine();
        }

        System.out.println(sb.toString());

        reader.close();

        BufferedWriter writer = new BufferedWriter(new FileWriter("plot1.txt"));
        writer.write(sb.toString());
        writer.flush();
        writer.close();
    }

    public static String changeFirstTwoCols(String s){
        String[] sArr = s.split(" ");
        if(!(sArr.length <= 1)) {
            String change = sArr[0];
            sArr[0] = sArr[1];
            sArr[1] = change;

            String res = Arrays.toString(sArr);
            res = res.replace("]", "");
            res = res.replace("[", "");
            res = res.replace(" ", "");
            res = res.trim();

            return res;
        }
        return s;
    }

    public static String processLine(String s){
        s = s.replaceAll("\t+", " ");
//        s = s.replaceAll("\\s+", ",");
//        s = s.replaceAll(",", "\t\t");
        return s;
    }
}
