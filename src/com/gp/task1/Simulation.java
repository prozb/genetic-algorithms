package com.gp.task1;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Pavlo Rozbytskyi
 * @version 3.0.1
 */

public class Simulation implements Runnable{
    public int simulationsCount;

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

    private float pcMin;
    private float pcMax;
    private float pcStep;
    private float pmMin;
    private float pmMax;
    private float pmStep;

    private int [] statArr;
    private int resPosition;
    private int simulationNum;
    private StringBuilder sb;
    private Point [] points;

    private volatile boolean ready;

    public Simulation(int geneLen, int generationCount, float mutationRate, float recombinationRate, int runsNum,
                      int replicationSchema, int crossOverSchema, int maxGenerations, int initRate, boolean protect,
                      int simulationNum, Point [] points){

        this.generationCount   = generationCount;
        this.recombinationRate = recombinationRate;
        this.replicationSchema = replicationSchema;
        this.crossOverSchema   = crossOverSchema;
        this.maxGenerations    = maxGenerations;
        this.mutationRate      = mutationRate;
        this.simulationNum     = simulationNum;

        this.points   = points;
        this.runsNum  = runsNum;
        this.initRate = initRate;
        this.protect  = protect;
        this.geneLen  = geneLen;
        this.statArr  = new int[runsNum];
        this.sb       = new StringBuilder();
    }
    // TESTING PARAMETERS: --pm=0.02 --pc=0.5 --genecount=200 --genelen=200 --maxgen=1000 --runs=10 --protect=best --initrate=5 --crossover_scheme=1 --replication_scheme=1
    public void run() {
        try {
//        startSimulation();
            graphSimulation();
//            printStatistics();
//            exportBufferToFile();
        }catch (Exception e){
            Main.printError("Cannot execute simulation " + e.toString());
        }
        System.err.println("Thread #" + simulationNum + " finished");
    }


    public void graphSimulation() throws InterruptedException, IOException {
        sb.setLength(0);

        int counter    = 0;
        float genCount = 0;

        for(int i = 0; i < points.length; i++){
                System.err.println("===> Thread $" + simulationNum + " simulation #" + counter + " started");
                mutationRate      = points[i].getPm();
                recombinationRate = points[i].getPc();

                startSimulation();

                genCount = calcStatistics();
                exportToBuffer(mutationRate, recombinationRate, genCount);
                System.err.println("===> Thread $" + simulationNum + " simulation #" + counter + " finished");
                counter++;
                simulationsCount++;
        }
        sb.append("\n");
        System.err.println("===> Thread $" + simulationNum + " | " + simulationsCount +  " simulations finished");
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
        int runsCounter  = 0;
        while (localRunsNum > 0){
            System.err.println("Thread $" + simulationNum +  " run #" + runsCounter + " started");
            runSimulation();
            System.err.println("Thread $" + simulationNum +  " run #" + runsCounter + " finished");
            localRunsNum--;
            runsCounter++;
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
//        pool.printInfo();
        push(pool.getGenerationsCount() - 1);
    }

    private float calcStatistics(){
        int count = Arrays.stream(statArr).sum();
        clear();
        return count / statArr.length;
    }

    public void printStatistics(){
        System.out.printf("\n" + new String(new char[100]).replace("\0", "=")+ "\n" +
                "\nto achieve complete fitness, you need average %.4f generations", calcStatistics());
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
}