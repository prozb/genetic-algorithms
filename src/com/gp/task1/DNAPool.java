package com.gp.task1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

/**
 * @author Pavlo Rozbytskyi
 * @version 2.0.0
 */
// after each generation loop you must recalculate fitness off all dna's and
// figure out maximal and minimal fitness
public class DNAPool{
    private DNA [] currentGeneration;
    private int maxFitness;
    private int minFitness;
    private int generationsCount;
    private int generationLen;
    private int geneLen;
    private float mutationRate;
    private float recombinationRate;
    private int replicationSchema;
    private int crossOverSchema;
    private boolean finished;

    public DNAPool(){

    }

    // calculating fitness after each loop and after creating new generation
    public DNAPool(int generationLen, int geneLen, int initRate, float mutationRate, int replicationSchema, int crossOverSchema, float recombinationRate){
        this.replicationSchema = replicationSchema;
        this.crossOverSchema   = crossOverSchema;
        this.recombinationRate = recombinationRate;

        this.mutationRate  = mutationRate;
        this.generationLen = generationLen;

        createGenerations(generationLen, geneLen, initRate);
    }

    private void createGenerations(int generationLen, int geneLen, int initRate){
        this.currentGeneration = new DNA[generationLen];
        this.geneLen           = geneLen;
        this.generationsCount  = 0;

        for(int i = 0; i < currentGeneration.length; i++){
            currentGeneration[i] = new DNA(geneLen, initRate);
        }

        calcMaxFitnessOfGeneration();
        calcMinFitnessOfGeneration();
    }

    /**
     * calculating max fitness of gen
     */
    public void calcMaxFitnessOfGeneration(){
        Optional<DNA> dnaMaxFitness = Arrays.stream(currentGeneration).max(Comparator.comparing(DNA::getFitness));
        dnaMaxFitness.ifPresent(dna -> maxFitness = dna.getFitness());

        if(maxFitness == geneLen) {
            finished = true;
            printInfo();
        }
    }

    /**
     * calculating min fitness of gen
     */
    public void calcMinFitnessOfGeneration(){
        Optional<DNA> dnaMinFitness = Arrays.stream(currentGeneration).min(Comparator.comparing(DNA::getFitness));
        dnaMinFitness.ifPresent(dna -> minFitness = dna.getFitness());
    }

    public void switchToNextGeneration(){
        calcMinFitnessOfGeneration();
        calcMaxFitnessOfGeneration();
        generationsCount++;
    }

    // created for testing reasons
    public void processCrossOver(){
        switch(crossOverSchema){
            case 1:
                crossOverSchemaOne();
                break;
            default:
                throw new RuntimeException("please input replication schema");
        }

        calcMinFitnessOfGeneration();
        calcMaxFitnessOfGeneration();
    }

    public void crossOverSchemaOne(){
        int crossOverCount = (int) (recombinationRate * generationLen);
        int crossOverPerf  = 0;
        int firstGenePos   = 0;
        int secondGenePos  = 0;

        int randPos = 0;

        do {
            firstGenePos  = (int) (Math.random() * generationLen);
            secondGenePos = (int) (Math.random() * generationLen);
            secondGenePos = (int) (Math.random() * generationLen);

            randPos = (int) (Math.random() * geneLen);

            DNA dna1 = crossOver(currentGeneration[firstGenePos], currentGeneration[secondGenePos], randPos);
            DNA dna2 = crossOver(currentGeneration[secondGenePos], currentGeneration[firstGenePos], randPos);

            currentGeneration[firstGenePos]  = dna1;
            currentGeneration[secondGenePos] = dna2;

            crossOverCount--;
            crossOverPerf++;
        }while (crossOverCount > 0);

        assert (int)(currentGeneration.length * recombinationRate) == crossOverPerf;
    }

    public DNA crossOver(DNA dna1, DNA dna2, int randPos){
        DNA newDna         = new DNA(dna1.getGene().length);
        Integer [] newGene = new Integer[dna1.getGene().length];

        System.arraycopy(dna1.getGene(), 0, newGene, 0, randPos);
        System.arraycopy(dna2.getGene(), randPos, newGene, randPos, dna1.getGene().length - randPos);

        newDna.setGene(newGene);
        return newDna;
    }

    public void sortGeneration(){
        Arrays.sort(currentGeneration, Comparator.comparing(DNA::getFitness));
    }

    public void processMutation(){
        int mutationCount     = (int) (mutationRate * geneLen * generationLen) + 1;
        int mutationPerformed = 0;

        int randGen = 0;
        int randPos = 0;

        do{
            randGen = (int) (Math.random() * generationLen);
            randPos = (int) (Math.random() * geneLen);

            currentGeneration[randGen].invertCell(randPos);

            mutationCount--;
            mutationPerformed++;
        }while (mutationCount > 0);

        // testing reasons
        if((int) (mutationRate * geneLen * generationLen) != --mutationPerformed){
            throw new RuntimeException();
        }

        calcMaxFitnessOfGeneration();
        calcMinFitnessOfGeneration();
    }

    public void processReplication(){
//        sortGeneration();
        switch(replicationSchema){
            case 1:
                replicationSchemaOne();
                break;
            default:
                throw new RuntimeException("please input replication schema");
        }

        calcMinFitnessOfGeneration();
        calcMaxFitnessOfGeneration();
    }

    private void replicationSchemaOne(){
        int selectionPercent = 10;

        DNA [] bestDNAs = getBestGenes(selectionPercent);

        for(int i = 0; i < currentGeneration.length; i++){
            currentGeneration[i] = bestDNAs[i / 10];
        }
    }

    public DNA [] getBestGenes(int selectionPercent){
        int selectCount = (int) (selectionPercent / 100.0f * generationLen);

        return Arrays.copyOfRange(currentGeneration, currentGeneration.length - selectCount, currentGeneration.length);
    }

    public int getGenerationsCount(){
        return generationsCount;
    }

    public int getMaxFitness(){
        return maxFitness;
    }

    public int getMinFitness() {
        return minFitness;
    }

    public DNA [] getGeneration(){
        return currentGeneration;
    }

    public boolean isFinished(){
        return finished;
    }

    public void printInfo(){
        System.out.println("max fitness: " + maxFitness + " min fitness: " + minFitness +
                " gen number: " + generationsCount + " \n=========================================================");
    }

    public void printGeneration(){
        System.out.println("---------------------------------------------------------");
        System.out.println(Arrays.toString(currentGeneration));
        System.out.println("---------------------------------------------------------");
    }
}