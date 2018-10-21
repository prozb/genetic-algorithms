package com.gp.task1;

/**
 * @author Pavlo Rozbytskyi
 * @version 0.0.1
 */
public class DNAPool {
    /**
     * DNA Pool constructor
     * @param geneLen               length of one gene
     * @param geneCnt               count of genes in one generation
     * @param maxGenerations        maximum number of generations
     * @param recombinationRate
     * @param mutationRate          mutation rate in percent
     * @param initRate              how much numbers in gene are set to 1
     * @param crossoverMethod
     * @param replicationScheme
     */

    //current generation of dna's
    private DNA [] generation;
    //here will be stored next generation of genes
    private DNA [] newGeneration;

    private int maxGenerations;
    private int recombinationRate;
    private int mutationRate;
    private int crossoverMethod;
    private int replicationScheme;
    private int geneCount;
    private int geneLen;

    public DNAPool(int geneLen, int geneCnt, int maxGenerations, int recombinationRate, int mutationRate, int initRate,
                   int crossoverMethod, int replicationScheme){
        this.maxGenerations     = maxGenerations;
        this.crossoverMethod    = crossoverMethod;
        this.recombinationRate  = recombinationRate;
        this.mutationRate       = mutationRate;
        this.crossoverMethod    = crossoverMethod;
        this.replicationScheme  = replicationScheme;
        this.geneCount          = geneCnt;
        this.geneLen            = geneLen;

        this.generation         = new DNA[geneCnt];
        this.newGeneration      = new DNA[geneCnt];

        createDNAGeneration(generation, geneLen, initRate);
        createDNAGeneration(newGeneration, geneLen);
    }

    private void createDNAGeneration(DNA [] gen, int geneLen, int initRate){
        for(int i = 0; i < gen.length; i++){
            gen[i] = new DNA(geneLen, initRate);
        }
    }

    private  void createDNAGeneration(DNA [] gen, int geneLen){
        for(int i = 0; i < gen.length; i++) {
            gen[i] = new DNA(geneLen);
        }
    }

    /**
     * generates two new genes and saves this two on the next free position in new generation
     * @param gene1
     * @param gene2
     * @param positionInNewGeneration pointer to the next free place in new generation
     */
    public void crossOver(DNA gene1, DNA gene2, int positionInNewGeneration){
        int pos           = (int)(Math.random() * geneLen);
        boolean firstGene = true;

        newGeneration[positionInNewGeneration++] = gene1.crossOverAnotherGene(gene2, pos);
        newGeneration[positionInNewGeneration++] = gene2.crossOverAnotherGene(gene1, pos);
    }

    public void processMutation() throws Exception {
        int mutationsCount = getMutationCount();

        int testCount = 0; //test purposes

        while (mutationsCount > 0){
            int x = (int)(Math.random() * geneCount);
            int y = (int)(Math.random() * geneLen);

            generation[x].invertCellOfDNA(y);

            mutationsCount--;
            testCount++; //test purposes
        }

        //test purposes
        if(testCount != getMutationCount()){
            throw new Exception("check your loop counter!");
        }
    }


    private int getMutationCount(){
        return  (int)((mutationRate / 100.0) * geneLen * geneCount);
    }
}
