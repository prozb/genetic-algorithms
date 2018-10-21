package com.gp.task1;

public class Evolution {
    private static int geneCount;
    private static int geneLength;
    private static int initRate;
    private static int maxGenerations;
    private static int mutationRate;
    private static int crossoverMethod;
    private static int replicationSchema;
    private static int recombinationCountPercent;

    public static void main(String[] args) {
        processCommands();
        startSimulation();
    }

    public static void processCommands(){
        //TODO: read commands from console
    }

    public static void startSimulation(){
        while (maxGenerations > 0){
            runGenerations();

            maxGenerations--;
        }
    }

    private static void runGenerations(){
        DNAPool dnaPool = new DNAPool(geneLength, geneCount, recombinationCountPercent,
                mutationRate, initRate, crossoverMethod, replicationSchema);
        int countOfGeneratios = 0;

        while(!dnaPool.isFinished()) {


            countOfGeneratios++;
        }

        //TODO: think about where to store data
    }
}
