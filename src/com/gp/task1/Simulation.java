package com.gp.task1;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @author Pavlo Rozbytskyi
 * @version 3.0.1
 */

public class Simulation implements Callable<String>{
    public int simulationsCount;
    public static int counter;
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
    private boolean isGraph;

    private int [] statArr;
    private int resPosition;
    private StringBuilder sb;
    private Point point;

    static {
        counter = 0;
    }

    public Simulation(int geneLen, int generationCount, float mutationRate, float recombinationRate, int runsNum,
                      int replicationSchema, int crossOverSchema, int maxGenerations, int initRate, boolean protect,
                      Point point, boolean isGraph){

        this.generationCount   = generationCount;
        this.recombinationRate = recombinationRate;
        this.replicationSchema = replicationSchema;
        this.crossOverSchema   = crossOverSchema;
        this.maxGenerations    = maxGenerations;
        this.mutationRate      = mutationRate;

        this.point    = point;
        this.isGraph  = isGraph;
        this.runsNum  = runsNum;
        this.initRate = initRate;
        this.protect  = protect;
        this.geneLen  = geneLen;
        this.statArr  = new int[runsNum];
        this.sb       = new StringBuilder();
    }
    // TESTING PARAMETERS: --pm=0.02 --pc=0.5 --genecount=200 --genelen=200 --maxgen=1000 --runs=10 --protect=best --initrate=5 --crossover_scheme=1 --replication_scheme=1
    public String call() {
        try {
            if(isGraph)
                graphSimulation();
            else {
                startSimulation();
                printStatistics();
            }
        }catch (Exception e){
            Main.printError("Cannot execute simulation " + e.toString());
        }
        Main.logger.info("Thread #" + counter + " finished");

        return sb.toString();
    }


    private void graphSimulation(){
        sb.setLength(0);
        //average generations count to achieve max fitness
        float genCount = 0;

        mutationRate      = point.getPm();
        recombinationRate = point.getPc();

        startSimulation();

        genCount = calcStatistics();
        exportToBuffer(mutationRate, recombinationRate, genCount);
        counter++;
        Main.logger.debug("===> Thread $" + Thread.currentThread().getId() + " simulation #" + counter + " finished");

        sb.append("\n");
    }

    private void exportToBuffer(float pc, float pm, float averCount){
        sb.append(pm);
        sb.append("\t");
        sb.append(pc);
        sb.append("\t");
        sb.append(averCount);
        sb.append("\t\n");
    }

    //process runsNum simulations to calculate average
    private void startSimulation() {
        int localRunsNum = runsNum;
        int runsCounter  = 0;
        while (localRunsNum > 0){
            runSimulation();
            Main.logger.debug("Thread $" + Thread.currentThread().getId()  + " simulation #" + counter +
                              " run #" + runsCounter + " finished");
            localRunsNum--;
            runsCounter++;
        }
    }

    //process just one simulation
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
        push(pool.getGenerationsCount() - 1);
    }

    private float calcStatistics(){
        int count = Arrays.stream(statArr).sum();
        clear();
        return count / statArr.length;
    }

    public void printStatistics(){
        Main.logger.info("Average " + calcStatistics() % 1000 + " generations to achieve max fitness");
    }

    // pushes result of the generation into statistics array
    private void push(int res){
        statArr[resPosition++] = res;
    }

    private void clear(){
        resPosition = 0;
        Arrays.fill(statArr, 0);
    }
}