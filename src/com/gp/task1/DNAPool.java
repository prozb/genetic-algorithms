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
    private DNA [] nextGeneration;
    private int maxFitness;
    private int minFitness;
    private int generationsCount;
    private int generationLen;
    private int geneLen;
    private float mutationRate;

    public DNAPool(){

    }

    // calculating fitness after each loop and after creating new generation
    public DNAPool(int generationLen, int geneLen, int initRate, float mutationRate){
        this.mutationRate  = mutationRate;
        this.generationLen = generationLen;

        createGenerations(generationLen, geneLen, initRate);
    }

    private void createGenerations(int generationLen, int geneLen, int initRate){
        this.currentGeneration = new DNA[generationLen];
        this.nextGeneration    = new DNA[generationLen];
        this.geneLen           = geneLen;
        this.generationsCount  = 0;

        for(int i = 0; i < currentGeneration.length; i++){
            currentGeneration[i] = new DNA(geneLen, initRate);
            nextGeneration[i]    = new DNA(geneLen);
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
    }

    /**
     * calculating min fitness of gen
     */
    public void calcMinFitnessOfGeneration(){
        Optional<DNA> dnaMinFitness = Arrays.stream(currentGeneration).min(Comparator.comparing(DNA::getFitness));
        dnaMinFitness.ifPresent(dna -> minFitness = dna.getFitness());
    }

    public void switchToNextGeneration(){
        currentGeneration = nextGeneration;
        Arrays.fill(nextGeneration, new DNA(geneLen));

        calcMinFitnessOfGeneration();
        calcMaxFitnessOfGeneration();
        generationsCount++;
    }

    // created for testing reasons
    public DNA processCrossOver(DNA dna1, DNA dna2){
        int randPos        = (int) (Math.random() * 200);

        return crossOver(dna1, dna2, randPos);
    }

    public DNA crossOver(DNA dna1, DNA dna2, int randPos){
        DNA newDna         = new DNA(dna1.getGene().length);
        Integer [] newGene = new Integer[dna1.getGene().length];

        System.arraycopy(dna1.getGene(), 0, newGene, 0, randPos);
        System.arraycopy(dna2.getGene(), randPos, newGene, randPos, dna1.getGene().length - randPos);

        newDna.setGene(newGene);
        return newDna;
    }

    public void processMutation(){
        int mutationCount     = (int) (mutationRate * geneLen * generationLen);
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
        if((int) (mutationRate * geneLen * generationLen) != mutationPerformed){
            throw new RuntimeException();
        }
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
}