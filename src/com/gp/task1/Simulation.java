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
        if(args.length <= 1){
            printHelpMessage();
            printError("No parameters!");
        }else if(args[1].equals("--help")){
            printHelpMessage();
            System.exit(99);
        }else if(args.length < NUM_OF_ARGS){
            printHelpMessage();
            printError("Not enough parameters!");
        }else if(args.length > NUM_OF_ARGS){
            printHelpMessage();
            printError("To many parameters!");
        }else {
            generationCount   = (int) getValue("--genecount");
            geneLen           = (int) getValue("--genelen");
            crossOverSchema   = (int) getValue("--crossover_scheme");
            replicationSchema = (int) getValue("--replication_scheme");
            initRate          = (int) getValue("--initrate");
            maxGenerations    = (int) getValue("--maxgen");
            mutationRate      = getValue("--pm");
            recombinationRate = getValue("pc");
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
                "[--maxgen] maximum generations (integer number)");
    }

    private static void startSimulation() throws InterruptedException {

        int runsCount = maxGenerations;

        DNAPool pool = new DNAPool(generationCount, geneLen, initRate, mutationRate, replicationSchema, crossOverSchema, recombinationRate);
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