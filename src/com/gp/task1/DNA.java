package com.gp.task1;

import java.util.Arrays;

/**
 * Class represents DNA
 *
 * @author Pavlo Rozbytskyi
 * @version 0.0.1
 */
public class DNA{
    private int [] gene;
    private int fitness;
    private int initRate;

    public DNA(int length){
        this.fitness = 0;

        generateRandomDNA(length);
        calculateFitness();
    }

    public DNA(int length, int initrate){
        this.initRate = initrate;

        generateRandomDNA(length, initrate);
        calculateFitness();
        printDNA();
    }


    /*************************************************
     *                  SETUP METHODS                *
     *************************************************/
    public void calculateFitness(){
        this.fitness = 0;
        for(int i = 0; i < gene.length; i++){
            this.fitness += gene[i];
        }
    }


    public void generateRandomDNA(int length, int initrate){
        this.initRate = initrate;

        generateRandomDNA(length);

        int onesMustHave = getOnesMustHaveDNA();
        //print("onesMustHave: " + onesMustHave);

        int countOfOnes = 0;
        int pos = 0;

        while(countOfOnes < onesMustHave){
            pos = (int)(Math.random() * gene.length);
            //println("rand: " + pos);

            if(gene[pos] != 1){
                gene[pos] = 1;

                countOfOnes++;
            }
        }
    }

    public void generateRandomDNA(int length){
        this.gene = new int [length];

        for(int i = 0; i < gene.length; i++){
            gene[i] = 0;
        }
    }

    /**
     * Method inverts current cell value
     * @param x position
     */
    public void invertCellOfDNA(int x){
        if(gene[x] == 0){
            gene[x] = 1;
        }else{
            gene[x] = 0;
        }
    }

    /**
     * ATTENTION!
     * gene2 DNA will be always concatenated to the end of this (current gene)!!!
     * please use this function careful
     *
     * Example:
     * crossOverPoint = 4
     * this gene  = [1001 0000]
     *      gene2 = [0010 1111]
     *
     * offspring  = [1001 1111] => gene2 is in the end
     *
     * @param gene2
     * @param crossOverPoint crossing over position
     * @return returns the offspring
     */
    public DNA crossOverAnotherGene(DNA gene2, int crossOverPoint) {
        DNA offspring = new DNA(gene.length);

        for(int i = 0; i < gene.length; i++){
            if(i < crossOverPoint){
                offspring.getGene()[i] = gene[i];
            }else{
                offspring.getGene()[i] = gene2.getGene()[i];
            }
        }

        return offspring;
    }

    // returns number of ones, must be in new DNA in first generation
    private int getOnesMustHaveDNA(){
        return (int)((initRate / 100.0) * gene.length);
    }

    /*************************************************
     *                  GETTERS/SETTERS              *
     *************************************************/
    public void printDNA(){
        calculateFitness();

//        System.err.print("DNA fitness = " + this.fitness + " ");
//        System.err.println(Arrays.toString(this.gene));
    }

    public int [] getGene(){
        return this.gene;
    }

    public int getFitness(){
        return this.fitness;
    }

    public void setFitness(int fitness){
        this.fitness = fitness;
    }

    public void setGene(int [] gene){
        this.gene = gene;
    }
}