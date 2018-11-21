package com.gp.task1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Pavlo Rozbytskyi
 * @version 3.0.1
 */

public class Simulation implements Runnable{
    private int generationCount;
    private int geneLen;
    private int replicationSchema;
    private int crossOverSchema;
    private int maxGenerations;
    private int initRate;
    private int runsNum;
    private float mutationRate;
    private float recombinationRate;
    private boolean protect;

    private int [] statArr;
    private int resPosition;
    private StringBuilder sb;
    private BufferedWriter writer;

    private volatile boolean ready;

    public Simulation(int geneLen, int generationCount, float mutationRate, float recombinationRate, int runsNum,
                      int replicationSchema, int crossOverSchema, int maxGenerations, int initRate, boolean protect){
        this.sb = new StringBuilder();
        this.statArr = new int[runsNum];

        this.runsNum = runsNum;
        this.geneLen = geneLen;
        this.generationCount = generationCount;
        this.mutationRate = mutationRate;
        this.recombinationRate = recombinationRate;
        this.replicationSchema = replicationSchema;
        this.crossOverSchema = crossOverSchema;
        this.maxGenerations = maxGenerations;
        this.initRate = initRate;
        this.protect = protect;
    }
    // TESTING PARAMETERS: --pm=0.02 --pc=0.5 --genecount=200 --genelen=200 --maxgen=1000 --runs=10 --protect=best --initrate=5 --crossover_scheme=1 --replication_scheme=1
    public void run() {
        try {
//        startSimulation();
            graphSimulation();
            printStatistics();
//            exportBufferToFile();
        }catch (Exception e){

        }
    }


    public void graphSimulation() throws InterruptedException, IOException {
        sb.setLength(0);

//        float genCount = 0;
//        for(float i = Constants.PC_MIN; i <= Constants.PC_MAX; i += Constants.PC_STEP){
//            for(float j = Constants.PM_MIN; j < Constants.PM_MAX; j += Constants.PM_STEP){
//                mutationRate = j;
//                recombinationRate = i;
//
//                System.err.println("pc = " + i + " pm = " + j);
//                startSimulation();
//
//                genCount = calcStatistics();
//                exportToBuffer(mutationRate, recombinationRate, genCount);
//            }
            sb.append("asdasdasdasdasd");
            sb.append("\n");
//        }

        setReady();
//        exportBufferToFile();
    }

    private void exportBufferToFile() throws IOException {
            writer = new BufferedWriter(new FileWriter("plot.txt"));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
    }

    private void exportToBuffer(float pc, float pm, float averCount){
        sb.append(pm);
        sb.append("\t");
        sb.append(pc);
        sb.append("\t");
        sb.append(averCount);
        sb.append("\t\n");
    }

    private void startSimulation() throws InterruptedException {
        int localRunsNum = runsNum;

        while (localRunsNum > 0){
            runSimulation();
            localRunsNum--;
        }
    }

    private void runSimulation() {
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

    private float calcStatistics(){
        int count = Arrays.stream(statArr).sum();
        clear();
        return count / statArr.length;
    }

    public void printStatistics(){
        System.out.printf("\n==================================================\n" +
                "to achieve complete fitness, you need average %.4f generations", calcStatistics());
    }

    // pushes result of the generation into statistics array
    private void push(int res){
        statArr[resPosition++] = res;
    }

    private void clear(){
        resPosition = 0;
        Arrays.fill(statArr, 0);
    }

    public boolean isReady(){
        return ready;
    }

    public void setReady(){
        this.ready = true;
    }

    public StringBuilder getStringBuilder(){
        return sb;
    }

    public void setArguments(String [] s){
        arguments = s;
    }


//    private void processUserInput(){
//        if(arguments.length <= 1){
//            printHelpMessage();
//            printError("No parameters!");
//        }else if(arguments[1].equals("--help")){
//            printHelpMessage();
//            System.exit(99);
//        }else if(arguments.length < NUM_OF_ARGS){
//            printHelpMessage();
//            printError("Not enough parameters!");
//        }else if(arguments.length > NUM_OF_ARGS){
//            printHelpMessage();
//            printError("To many parameters!");
//        }else {
//            generationCount   = (int) getValue("--genecount");
//            geneLen           = (int) getValue("--genelen");
//            crossOverSchema   = (int) getValue("--crossover_scheme");
//            replicationSchema = (int) getValue("--replication_scheme");
//            initRate          = (int) getValue("--initrate");
//            maxGenerations    = (int) getValue("--maxgen");
//            runsNum           = (int) getValue("--runs");
//            mutationRate      = getValue("--pm");
//            recombinationRate = getValue("--pc");
//            protect = getStringValue("--protect").equals("best");
//
//            statArr = new int [runsNum];
//        }
//    }

//    private float getValue(String arg){
//        Optional<String> geneLenStringOptional = Arrays.stream(arguments).filter(elem -> elem.contains(arg)).findFirst();
//        String geneLenString = "";
//
//        if(geneLenStringOptional.isPresent()){
//            geneLenString = geneLenStringOptional.get().trim().replace(" ", "");
//        }
//        int pos = geneLenString.indexOf(arg) + 1 + arg.length();
//        String num = geneLenString.substring(pos);
//
//        float val = 0;
//        try {
//            val = Float.parseFloat(num);
//        }catch (NumberFormatException e){
//            printError("Number \"" + arg + "\" not found!");
//        }
//        return val;
//    }
//
//    public String getStringValue(String arg){
//        Optional<String> geneLenStringOptional = Arrays.stream(arguments).filter(elem -> elem.contains(arg)).findFirst();
//        String geneLenString = "";
//
//        if(geneLenStringOptional.isPresent()){
//            geneLenString = geneLenStringOptional.get().trim().replace(" ", "");
//        }
//        int pos = geneLenString.indexOf(arg) + 1 + arg.length();
//        String val = geneLenString.substring(pos).trim();
//
//        if(val.equals("")){
//            printError("protect option can not be empty!");
//        }
//
//        return val.trim();
//    }

//    private void printError(String err){
//        System.out.println("Error: " + err);
//        System.exit(99);
//    }
//
//    private void printHelpMessage(){
//        System.out.println("\nusage:\n--------------------------------------------" +
//                " \n [--help]\nto process simulation you must enter all following args\n" +
//                "[--pc] recombination rate (float number)\n" +
//                "[--pm] mutations rate (float number)\n" +
//                "[--initrate] initiations rate (integer number)\n" +
//                "[--genecnt] count of genes in generation (integer number)\n" +
//                "[--genelen] length of the gene (integer number)\n" +
//                "[--crossover_scheme] (integer number)\n" +
//                "[--replication_scheme] (integer number)\n" +
//                "[--maxgen] maximum generations (integer number)\n" +
//                "[--runs] number of runs (integer number)\n" +
//                "[--protect] the best gene is crossover and mutation protected (best|none)");
//    }
}