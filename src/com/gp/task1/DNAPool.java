package com.gp.task1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

// after each generation loop you must recalculate fitness off all dna's and
// figure out maximal and minimal fitness

public class DNAPool{
    private DNA [] currentGeneration;
    private DNA [] nextGeneration;
    private int maxFitness;
    private int minFitness;
    private int generationsCount;
    private int geneLen;

    public DNAPool(){

    }

    // calculating fitness after each loop and after creating new generation
    public DNAPool(int generationLen, int geneLen, int initRate){
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
    
    public int getGenerationsCount(){
        return generationsCount;
    }

    public int getMaxFitness(){
        return maxFitness;
    }
}