package com.gp.task1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Pavlo Rozbytskyi
 * @version 2.0.1
 */

public class Main {
    private final static int NUM_OF_ARGS = 10;

    private static String [] arguments;
    private static int generationCount;
    private static int geneLen;
    private static int replicationSchema;
    private static int crossOverSchema;
    private static int maxGenerations;
    private static int initRate;
    private static int runsNum;
    private static float mutationRate;
    private static float recombinationRate;
    private static boolean protect;

    private static int [] statArr;
    private static int resPosition = 0;
    private static StringBuilder sb = new StringBuilder();
    private static BufferedWriter writer;

    // TESTING PARAMETERS: --pm=0.02 --pc=0.5 --genecount=200 --genelen=200 --maxgen=1000 --runs=10 --protect=best --initrate=5 --crossover_scheme=1 --replication_scheme=1
    public static void main(String[] args) throws InterruptedException, IOException {
        arguments = args;
        exportBufferToFile();
        processUserInput();
        startSimulation();
//        graphSimulation();
        printStatistics();
    }


    public static void graphSimulation() throws InterruptedException, IOException {
        sb.setLength(0);

        float genCount = 0;
        for(float i = Constants.PC_MIN; i <= Constants.PC_MAX; i += Constants.PC_STEP){
            for(float j = Constants.PM_MIN; j < Constants.PM_MAX; j += Constants.PM_STEP){
                mutationRate = j;
                recombinationRate = i;

                System.out.println("pc = " + i + " pm = " + j);
                startSimulation();

                genCount = calcStatistics();
                exportToBuffer(mutationRate, recombinationRate, genCount);
            }
            sb.append("\n");
        }
        exportBufferToFile();
    }

    private static void exportBufferToFile() throws IOException {
            writer = new BufferedWriter(new FileWriter("plot.txt"));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
//        }else {
//            printError("Cannot create file!");
//        }
    }

    private static void exportToBuffer(float pc, float pm, float averCount){
        sb.append(pm);
        sb.append("\t");
        sb.append(pc);
        sb.append("\t");
        sb.append(averCount);
        sb.append("\t\n");
    }

    private static void processUserInput(){
        if(arguments.length <= 1){
            printHelpMessage();
            printError("No parameters!");
        }else if(arguments[1].equals("--help")){
            printHelpMessage();
            System.exit(99);
        }else if(arguments.length < NUM_OF_ARGS){
            printHelpMessage();
            printError("Not enough parameters!");
        }else if(arguments.length > NUM_OF_ARGS){
            printHelpMessage();
            printError("To many parameters!");
        }else {
            generationCount   = (int) getValue("--genecount");
            geneLen           = (int) getValue("--genelen");
            crossOverSchema   = (int) getValue("--crossover_scheme");
            replicationSchema = (int) getValue("--replication_scheme");
            initRate          = (int) getValue("--initrate");
            maxGenerations    = (int) getValue("--maxgen");
            runsNum           = (int) getValue("--runs");
            mutationRate      = getValue("--pm");
            recombinationRate = getValue("--pc");
            protect = getStringValue("--protect").equals("best");

            statArr = new int [runsNum];
        }
    }

    private static float getValue(String arg){
        Optional<String> geneLenStringOptional = Arrays.stream(arguments).filter(elem -> elem.contains(arg)).findFirst();
        String geneLenString = "";

        if(geneLenStringOptional.isPresent()){
            geneLenString = geneLenStringOptional.get().trim().replace(" ", "");
        }
        int pos = geneLenString.indexOf(arg) + 1 + arg.length();
        String num = geneLenString.substring(pos);

        float val = 0;
        try {
            val = Float.parseFloat(num);
        }catch (NumberFormatException e){
            printError("Number \"" + arg + "\" not found!");
        }
        return val;
    }

    public static String getStringValue(String arg){
        Optional<String> geneLenStringOptional = Arrays.stream(arguments).filter(elem -> elem.contains(arg)).findFirst();
        String geneLenString = "";

        if(geneLenStringOptional.isPresent()){
            geneLenString = geneLenStringOptional.get().trim().replace(" ", "");
        }
        int pos = geneLenString.indexOf(arg) + 1 + arg.length();
        String val = geneLenString.substring(pos).trim();

        if(val.equals("")){
            printError("protect option can not be empty!");
        }

        return val.trim();
    }

    private static void printError(String err){
        System.out.println("Error: " + err);
        System.exit(99);
    }

    private static void printHelpMessage(){
        System.out.println("\nusage:\n--------------------------------------------" +
                " \n [--help]\nto process simulation you must enter all following args\n" +
                "[--pc] recombination rate (float number)\n" +
                "[--pm] mutations rate (float number)\n" +
                "[--initrate] initiations rate (integer number)\n" +
                "[--genecnt] count of genes in generation (integer number)\n" +
                "[--genelen] length of the gene (integer number)\n" +
                "[--crossover_scheme] (integer number)\n" +
                "[--replication_scheme] (integer number)\n" +
                "[--maxgen] maximum generations (integer number)\n" +
                "[--runs] number of runs (integer number)\n" +
                "[--protect] the best gene is crossover and mutation protected (best|none)");
    }

    private static void startSimulation() throws InterruptedException {
        int localRunsNum = runsNum;

        while (localRunsNum > 0){
            run();
            localRunsNum--;
        }
    }
    private static void run() {
        int runsCount = maxGenerations;

        DNAPool pool = new DNAPool(generationCount, geneLen, initRate, mutationRate,
                                    replicationSchema, crossOverSchema, recombinationRate, protect);
        while(!pool.isFinished() && runsCount > 0){
            pool.processCrossOver();
            pool.processMutation();
            pool.sortGeneration();
            pool.processReplication();
            pool.switchToNextGeneration();
            runsCount--;

        }
        pool.printInfo();
        push(pool.getGenerationsCount() - 1);
    }

    private static float calcStatistics(){
        int count = Arrays.stream(statArr).sum();
        clear();
        return count / statArr.length;
    }

    public static void printStatistics(){
        System.out.printf("\n==================================================\n" +
                "to achieve complete fitness, you need average %.4f generations", calcStatistics());
    }

    // pushes result of the generation into statistics array
    private static void push(int res){
        statArr[resPosition++] = res;
    }

    private static void clear(){
        resPosition = 0;
        Arrays.fill(statArr, 0);
    }

    // Testing purposes
    public static void setArgs(String [] args){
        arguments = args;
    }
}