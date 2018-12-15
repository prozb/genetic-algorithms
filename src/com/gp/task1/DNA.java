package com.gp.task1;

import java.util.Arrays;
/**
 * @author Pavlo Rozbytskyi
 * @version 2.0.1
 */
public class DNA extends DNAProtorype{
    // fitness will be calculated after creating gene
    public DNA(int len, int initRate){
        super(len, initRate);
        calcFitness();
    }

    public DNA(int len){
        super(len);
    }

    @Override
    public void calcFitness(){
        this.fitness = (int) Arrays.stream(gene).filter(elem -> 1 == elem).count();
    }

    @Override
    public String toString() {
        return Arrays.toString(gene) + "\n";
    }

    public void printRank(){
        System.out.println("ps: " + ps + " psCum: " + psCum);
    }
}