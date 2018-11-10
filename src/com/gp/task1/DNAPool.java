package com.gp.task1;

public class DNAPool{
    private DNA [] currentGeneration;
    private DNA [] nextGeneration;

    public DNAPool(){

    }

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
    }
}