package com.gp.task1;

public class Evolution {
    private static int geneCount;
    private static int geneLength;
    private static int initRate;
    private static int maxGenerations;
    private static int mutationRate;
    private static int crossoverMethod;
    private static int replicationSchema;
    private static float recombinationCountPercent;

    public static void main(String[] args) throws Exception {
        maxGenerations = 5;
        initRate = 5;
        geneCount = 200;
        geneLength = 200;
        mutationRate = 2;
        replicationSchema = 1;
        recombinationCountPercent = 0.5f;
        crossoverMethod = 1;

//        processCommands();
        startSimulation();
    }

    public static void processCommands(){
        //TODO: read commands from console
    }

    public static void startSimulation() throws Exception {
        while (maxGenerations > 0){
            runGenerations();

            maxGenerations--;
        }
    }

    private static void runGenerations() throws Exception {
        DNAPool dnaPool = new DNAPool(geneLength, geneCount, recombinationCountPercent,
                mutationRate, initRate, crossoverMethod, replicationSchema);
        int countOfGeneratios = 0;

        while(!dnaPool.isFinished()) {
            dnaPool.processMutation();
            dnaPool.processReplication();
            dnaPool.processRecombination();
            dnaPool.switchToNextGeneration();

            System.err.println("gen count: " + countOfGeneratios + " best fitness: " + dnaPool.getGeneration()[0].getFitness());
            countOfGeneratios++;
//            Thread.sleep(100);
        }

        System.err.println("count of generations: " + countOfGeneratios);
        //TODO: think about where to store data
    }
}
