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
    private int initrate;

    public DNA(int lenght){
        generateRandomDNA(lenght);
        calculateFitness();
    }

    public DNA(int length, int initrate){
        this.initrate = initrate;

        generateRandomDNA(length, initrate);
        calculateFitness();
    }


    /*************************************************
     *                  SETUP METHODS                *
     *************************************************/
    private void calculateFitness(){
        for(int i = 0; i < gene.length; i++){
            if(gene[i] == 1){
                fitness++;
            }
        }
    }


    public void generateRandomDNA(int length, int initrate){
        this.initrate = initrate;

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

    // returns number of ones, must be in new DNA in first generation
    private int getOnesMustHaveDNA(){
//        System.out.println("rate: " + (int)((initrate / 100.0) * gene.length) + "\n");

        return (int)((initrate / 100.0) * gene.length);
    }

    /*************************************************
     *                  GETTERS/SETTERS              *
     *************************************************/
    public void printDNA(){
        System.out.print("DNA = ");

        System.out.println(Arrays.toString(this.gene));
    }

    public int [] getGene(){
        return this.gene;
    }

    public int getFitness(){
        return this.fitness;
    }
}