package com.gp.task1;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Pavlo Rozbytskyi
 * @version 2.0.0
 */
public class Simulation {
    private static int NUM_OF_ARGS = 8;

    private static String [] arguments;
    private static int generationCount;
    private static int geneLen;
    private static int replicationSchema;
    private static int crossOverSchema;
    private static int maxGenerations;
    private static int initRate;
    private static float mutationRate;
    private static float recombinationRate;

    private static int averageRuns;

    public static void main(String[] args) throws InterruptedException {
        arguments = args;

        processUserInput(args);
        startSimulation();
    }

    private static void processUserInput(String [] args){
//        if(args.length <= 1){
//            printHelpMessage();
//            printError("No parameters!");
//        }else if(args[1].equals("--help")){
//            printHelpMessage();
//            System.exit(99);
//        }else if(args.length < NUM_OF_ARGS){
//            printHelpMessage();
//            printError("Not enough parameters!");
//        }else if(args.length > NUM_OF_ARGS){
//            printHelpMessage();
//            printError("To many parameters!");
//        }else {
            generationCount   = getIntegerValue("--genecount");
            geneLen           = getIntegerValue("--genelen");
            crossOverSchema   = getIntegerValue("--crossover_scheme");
            replicationSchema = getIntegerValue("--replication_scheme");
            initRate          = getIntegerValue("--initrate");

//        }
    }

    private static int getIntegerValue(String arg){
        Optional<String> geneLenStringOptional = Arrays.stream(arguments).filter(elem -> elem.contains(arg)).findFirst();
        String geneLenString = "";

        if(geneLenStringOptional.isPresent()){
            geneLenString = geneLenStringOptional.get();
        }
        int pos = geneLenString.indexOf(arg) + 1 + arg.length();
        String num = geneLenString.substring(pos);

        int geneLen = 0;
        try {
            geneLen = Integer.parseInt(num);
        }catch (NumberFormatException e){
            printError("Integer \"" + arg + "\" not found!");
        }
        return geneLen;
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
                "[--replication_scheme] (integer number)");
    }

    private static void startSimulation() throws InterruptedException {
        int generationLen  = 200;
        int geneLen        = 200;
        int initRate       = 5;
        float mutationRate = 0.02f;
        int replicationSch = 1;
        int crossOverSch   = 1;
        float recombRate   = 0.5f;

        int runsCount = 2000;

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate, mutationRate, replicationSch, crossOverSch, recombRate);
        while(!pool.isFinished() && runsCount > 0){
            pool.processCrossOver();
            pool.sortGeneration();
            pool.processMutation();
            pool.sortGeneration();
            pool.processReplication();
            pool.switchToNextGeneration();
            pool.printInfo();

            runsCount--;
        }
    }
}