package com.gp.task1;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Pavlo Rozbytskyi
 * @version 2.0.0
 */

public class Main {
    private static int NUM_OF_ARGS = 9;
    private static int RUNS_COUNT  = 100;

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

    private static int [] statArr;
    private static int resPosition = 0;

    // TESTING PARAMETERS: --pm=0.02 --pc=0.5 --genecount=200 --genelen=200 --maxgen=1000 --runs=10 --initrate=5 --crossover_scheme=1 --replication_scheme=1
    public static void main(String[] args) throws InterruptedException {
        arguments = args;

        processUserInput();
        startSimulation();
        showStatistics();
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

            statArr = new int [runsNum];
        }
    }

    private static float getValue(String arg){
        Optional<String> geneLenStringOptional = Arrays.stream(arguments).filter(elem -> elem.contains(arg)).findFirst();
        String geneLenString = "";

        if(geneLenStringOptional.isPresent()){
            geneLenString = geneLenStringOptional.get().trim();
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
                "[--runs] number of runs (integer number)");
    }

    private static void startSimulation() throws InterruptedException {
        while (runsNum > 0){
            run();
            runsNum--;
        }
    }
    private static void run() {

        int runsCount = maxGenerations;

        DNAPool pool = new DNAPool(generationCount, geneLen, initRate, mutationRate, replicationSchema, crossOverSchema, recombinationRate);
        while(!pool.isFinished() && runsCount > 0){
            pool.processCrossOver();
            pool.sortGeneration();
            pool.processMutation();
            pool.sortGeneration();
            pool.processReplication();
            pool.switchToNextGeneration();
//            pool.printInfo();

            runsCount--;
        }
        push(pool.getGenerationsCount() - 1);
    }

    private static void showStatistics(){
        int count     = Arrays.stream(statArr).sum();
        float average = count / statArr.length;
        System.out.printf("\n==================================================\n" +
                "to achieve complete fitness, you need average %.4f generations", average);
    }

    // pushes result of the generation into statistics array
    private static void push(int res){
        statArr[resPosition++] = res;
    }
}