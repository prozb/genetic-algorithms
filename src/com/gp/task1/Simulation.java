package com.gp.task1;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

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

    private float pcMin;
    private float pcMax;
    private float pcStep;
    private float pmMin;
    private float pmMax;
    private float pmStep;

    private int [] statArr;
    private int resPosition;
    private StringBuilder sb;
    private BufferedWriter writer;

    private volatile boolean ready;

    public Simulation(int geneLen, int generationCount, float mutationRate, float recombinationRate, int runsNum,
                      int replicationSchema, int crossOverSchema, int maxGenerations, int initRate, boolean protect,
                      float pcMin, float pcMax, float pcStep, float pmMin, float pmMax, float pmStep){

        this.generationCount   = generationCount;
        this.recombinationRate = recombinationRate;
        this.replicationSchema = replicationSchema;
        this.crossOverSchema   = crossOverSchema;
        this.maxGenerations    = maxGenerations;
        this.mutationRate      = mutationRate;

        this.statArr  = new int[runsNum];
        this.initRate = initRate;
        this.protect  = protect;
        this.runsNum  = runsNum;
        this.geneLen  = geneLen;

        this.sb     = new StringBuilder();
        this.pcMin  = pcMin;
        this.pcMax  = pcMax;
        this.pcStep = pcStep;
        this.pmMax  = pmMax;
        this.pmMin  = pmMin;
        this.pmStep = pmStep;
    }
    // TESTING PARAMETERS: --pm=0.02 --pc=0.5 --genecount=200 --genelen=200 --maxgen=1000 --runs=10 --protect=best --initrate=5 --crossover_scheme=1 --replication_scheme=1
    public void run() {
        try {
//        startSimulation();
            graphSimulation();
            printStatistics();
//            exportBufferToFile();
        }catch (Exception e){
            Main.printError("Cannot execute simulation " + e.toString());
        }
        System.err.println("Thread finished");
    }


    public void graphSimulation() throws InterruptedException, IOException {
        sb.setLength(0);

        float genCount = 0;
        for(float i = pcMin; i <= pcMax; i += pcStep){
            for(float j = pmMin; j < pmMax; j += pmStep){
                mutationRate = j;
                recombinationRate = i;

//                System.err.println("pc = " + i + " pm = " + j);
                startSimulation();

                genCount = calcStatistics();
                exportToBuffer(mutationRate, recombinationRate, genCount);
            }
            sb.append("\n");
        }
        System.err.println(sb.toString());
//        setReady();
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
}