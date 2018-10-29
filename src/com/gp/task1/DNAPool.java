package com.gp.task1;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Pavlo Rozbytskyi
 * @version 0.0.1
 */
public class DNAPool {
    /**
     * DNA Pool constructor
     * @param geneLen               length of one gene
     * @param geneCnt               count of genes in one generation
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
    private DNA [] replicationGeneration;

    private float recombinationRate;
    private int mutationRate;
    private int crossoverMethod;
    private int replicationScheme;
    private int geneCount;
    private int geneLen;

    private static boolean SPEED_UP = true;

    private boolean finished;

    public DNAPool(int geneLen, int geneCnt, float recombinationRate, int mutationRate, int initRate,
                   int crossoverMethod, int replicationScheme){
        this.crossoverMethod    = crossoverMethod;
        this.recombinationRate  = recombinationRate;
        this.mutationRate       = mutationRate;
        this.crossoverMethod    = crossoverMethod;
        this.replicationScheme  = replicationScheme;
        this.geneCount          = geneCnt;
        this.geneLen            = geneLen;
        this.finished           = false;

        this.generation         = new DNA[geneCnt];
        this.newGeneration      = new DNA[geneCnt];
        this.replicationGeneration = new DNA[geneCnt];

        createDNAGeneration(generation, geneLen, initRate);
//        createDNAGeneration(newGeneration, geneLen);

        recalculateFitnessOfGeneration(generation);
        checkFitness();
    }

    private void checkFitness(){
        for(int i = 0; i < generation.length; i++){
            if(generation[i].getFitness() == generation[i].getGene().length){
                this.finished = true;
                break;
            }
        }
    }

    public void processRecombination() throws Exception {
        int recombinationCount;
        int recombinationHappened;
        int posInNewGeneration;

        if(SPEED_UP){
            recombinationCount = generation.length;
            recombinationHappened = 0;
            posInNewGeneration = 0;

            int firstGenePos = 0;

            while (recombinationCount > 0){
                int secondGenePos = (int)(Math.random() * geneCount);

                crossOver(generation[firstGenePos], generation[secondGenePos], posInNewGeneration);
                firstGenePos++;
                recombinationHappened++;
                recombinationCount--;
                posInNewGeneration++;
            }
        }else {
            recombinationCount = getRecombinationsCount();
            recombinationHappened = 0; //Testing purposes
            posInNewGeneration = 0;

            while (recombinationCount > 0) {
                int firstRandomGenePos = (int) (Math.random() * geneCount);
                int secondRandomGenePos = (int) (Math.random() * geneCount);

                crossOver(generation[firstRandomGenePos], generation[secondRandomGenePos], posInNewGeneration);

                posInNewGeneration++;
                posInNewGeneration++;
                recombinationCount--;
                recombinationHappened++;
            }

            if(recombinationHappened != getRecombinationsCount()) {
                throw new Exception();
            }
        }

    }

    /**
     * Method processes replication
     */
    public void processReplication(){
        replicationGeneration = Arrays.copyOfRange(newGeneration, 0, generation.length);

        if(replicationScheme == 1){
            sortGeneration(replicationGeneration);

            DNA [] bestTenDNAs = getBestTenDNAs(replicationGeneration);

            for(int i = 0; i < geneCount; i++){
                replicationGeneration[i] = bestTenDNAs[i % 10];
            }
        }
    }

    // getting best ten genes
    private DNA [] getBestTenDNAs(DNA [] newGeneration){
        return Arrays.copyOfRange(newGeneration, (newGeneration.length - 10), newGeneration.length);
    }

    // sorting dnas by fitness in increasing order
    private void sortGeneration(DNA [] generation){
//        Arrays.sort(generation, Comparator.comparingInt(DNA::getFitness));
        Arrays.sort(generation, Comparator.comparingInt(DNA::getFitness));
    }

    private void recalculateFitnessOfGeneration(DNA [] generation){
        for(int i = 0; i < generation.length; i++){
            generation[i].calculateFitness();
        }
    }

    /**
     * generates two new genes and saves this two on the next free position in new generation
     * @param gene1
     * @param gene2
     * @param positionInNewGeneration pointer to the next free place in new generation
     */
    public void crossOver(DNA gene1, DNA gene2, int positionInNewGeneration){
        if(crossoverMethod == 1) {
            //newGeneration = Arrays.copyOfRange(generation, 0, generation.length);

            int pos = (int) (Math.random() * geneLen);
//            int pos = geneLen / 2;
//            System.err.println("genes to cross over pos " + pos +  " ");
//            gene1.printDNA();
//            gene2.printDNA();
//            System.err.println("=====================");
            if(SPEED_UP){
                DNA firstGene  = gene1.crossOverAnotherGene(gene2, pos);
                DNA secondGene = gene2.crossOverAnotherGene(gene1, pos);

                firstGene.calculateFitness();
                secondGene.calculateFitness();

                if(firstGene.getFitness() > secondGene.getFitness()){
                    newGeneration[positionInNewGeneration++] = firstGene;
                }else {
                    newGeneration[positionInNewGeneration++] = secondGene;
                }
            }else {
                newGeneration[positionInNewGeneration++] = gene1.crossOverAnotherGene(gene2, pos);
                newGeneration[positionInNewGeneration++] = gene2.crossOverAnotherGene(gene1, pos);
            }

//            System.err.println("genes crossed over pos " + pos +  " ");
//            newGeneration[positionInNewGeneration - 2].printDNA();
//            newGeneration[positionInNewGeneration - 1].printDNA();
//            System.err.println("=====================");
        }
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

    public void switchToNextGeneration(){
        newGeneration = replicationGeneration;
        generation = newGeneration;
        recalculateFitnessOfGeneration(generation);
    }

    private int getRecombinationsCount(){
        return (int)(recombinationRate * geneCount);
    }

    private int getMutationCount(){
        return  (int)((mutationRate / 100.0) * geneLen * geneCount);
    }

    private void createDNAGeneration(DNA [] gen, int geneLen, int initRate){
        for(int i = 0; i < gen.length; i++){
            gen[i] = new DNA(geneLen, initRate);
        }
    }

//    private  void createDNAGeneration(DNA [] gen, int geneLen){
//        gen = new DNA[geneLen];
//    }

    public boolean isFinished() {
        return finished;
    }

    public DNA [] getGeneration(){
        return this.generation;
    }

    public void setGeneration(DNA [] generation){
        this.generation = generation;
    }
    // TODO: testing purposes
    public void printDNAPool(){
        System.err.println("DNA pool: ");
        for(int i = 0; i < generation.length; i++){
            generation[i].printDNA();
        }
    }

    public int getBestFitness(){
        int best = 0;

        for(int i = 0; i < generation.length; i++){
            if(generation[i].getFitness() > best){
                best = generation[i].getFitness();
            }
        }

        return best;
    }
}
