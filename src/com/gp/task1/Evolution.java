package com.gp.task1;
/**
 * @author Pavlo Rozbytskyi
 * @version 2.0.0
 */
public class Evolution{
    public static void main(String[] args) throws InterruptedException {
        startSimulation();
    }

    public static void startSimulation() throws InterruptedException {
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