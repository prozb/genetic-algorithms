package com.gp.task1;

/**
 * @author Pavlo Rozbytskyi
 * @version 0.0.1
 */
public class DNAPool {
    /**
     * DNA Pool constructor
     * @param geneLen
     * @param geneCnt
     * @param maxGenerations
     * @param recombinationRate
     * @param mutationRate
     * @param initRate
     * @param crossoverMethod
     * @param replicationScheme
     */

    private DNA [][] generation;
    private DNA [][] newGeneration;

    private int maxGenerations;
    private int recombinationRate;
    private int mutationRate;
    private int crossoverMethod;
    private int replicationScheme;

    public DNAPool(int geneLen, int geneCnt, int maxGenerations, int recombinationRate, int mutationRate, int initRate,
                   int crossoverMethod, int replicationScheme){
        this.maxGenerations     = maxGenerations;
        this.crossoverMethod    = crossoverMethod;
        this.recombinationRate  = recombinationRate;
        this.mutationRate       = mutationRate;
        this.crossoverMethod    = crossoverMethod;
        this.replicationScheme  = replicationScheme;

        this.generation         = new DNA[geneCnt][geneCnt];
        this.newGeneration      = new DNA[geneCnt][geneCnt];

        createDNAGeneration(generation, geneLen, initRate);
        createDNAGeneration(newGeneration, geneLen);
    }

    private void createDNAGeneration(DNA [][] gen, int geneLen, int initRate){
        for(int i = 0; i < gen.length; i++){
            for(int j = 0; j < gen[0].length; j++){
                gen[i][j] = new DNA(geneLen, initRate);
            }
        }
    }

    private  void createDNAGeneration(DNA [][] gen, int geneLen){
        for(int i = 0; i < gen.length; i++) {
            for (int j = 0; j < gen[0].length; j++) {
                gen[i][j] = new DNA(geneLen);
            }
        }
    }


    //TODO: test this function
    /**
     * generates two new genes and saves this two on the next free position in new generation
     * @param gene1
     * @param gene2
     * @param positionInNewGeneration pointer to the next free place in new generation
     */
    public void crossOver(DNA [] gene1, DNA[] gene2, int positionInNewGeneration){
        int pos = (int)(Math.random() * gene1.length);

        DNA [] nextGene1 = new DNA[gene1.length];
        DNA [] nextGene2 = new DNA[gene2.length];

        for(int i = 0; i < gene1.length; i++){
            if(i < pos){
                nextGene1[i] = gene1[i];
            }else{
                nextGene1[i] = gene2[i];
            }
        }

        for(int i = 0; i < gene2.length; i++){
            if(i < pos){
                nextGene2[i] = gene2[i];
            }else{
                nextGene2[i] = gene1[i];
            }
        }

        newGeneration[positionInNewGeneration++] = nextGene1;
        newGeneration[positionInNewGeneration++] = nextGene2;
    }
}
