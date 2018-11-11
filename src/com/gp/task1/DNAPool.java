package com.gp.task1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class DNAPool{
    private DNA [] currentGeneration;
    private DNA [] nextGeneration;
    private int maxFitness;

    public DNAPool(){

    }

    // calculating fitness after each loop and after creating new generation
    public DNAPool(int generationLen, int geneLen, int initRate){
        createGenerations(generationLen, geneLen, initRate);
    }

    private void createGenerations(int generationLen, int geneLen, int initRate){
        this.currentGeneration = new DNA[generationLen];
        this.nextGeneration    = new DNA[generationLen];

        for(int i = 0; i < currentGeneration.length; i++){
            currentGeneration[i] = new DNA(geneLen, initRate);
            nextGeneration[i]    = new DNA(geneLen);
        }

        calcMaxFitnessOfGeneration();
    }

    public void calcMaxFitnessOfGeneration(){
        Optional<DNA> dnaMaxFitness = Arrays.stream(currentGeneration).max(Comparator.comparing(DNA::getFitness));
        dnaMaxFitness.ifPresent(dna -> maxFitness = dna.getFitness());
    }

    public int getMaxFitness(){
        return maxFitness;
    }
}