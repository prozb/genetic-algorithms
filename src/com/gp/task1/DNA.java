package com.gp.task1;

import java.util.Arrays;

/**
 * @author Pavlo Rozbytskyi
 * @version 2.0.0
 */
public class DNA{
    private int len;
    private Integer fitness;
    private Integer [] gene;

    // fitness will be calculated after creating gene
    public DNA(int len, int initRate){
        this.len = len;

        initGene(initRate);
        calcFitness();
    }

    public DNA(int len){
        this.len = len;
        this.fitness = 0;
        initGene();
    }

    private void initGene(){
        this.gene = new Integer[len];
        Arrays.fill(gene, 0);
    }

    public void calcFitness(){
        this.fitness = (int) Arrays.stream(gene).filter(elem -> 1 == elem).count();
    }
    /**
     *  initRate represents percent of all cells set on 1
     * @param initRate integer represents percent
     */
    private void initGene(int initRate){
        initGene();

        int initCount = (int) ((initRate / 100.0) * len);
        int randPos   = 0;

        while(initCount > 0){
            randPos = (int) (Math.random() * 200);

            if(setCell(randPos))
                initCount--;
        }

        calcFitness();
    }

    public void invertCell(int pos){
        gene[pos] = gene[pos] == 0 ? 1 : 0;
    }
    // returns true if cell can be set
    private boolean setCell(int pos){
        try {
            if(gene[pos] == 0){
                gene[pos] = 1;

                return true;
            }else{
                return false;
            }
        }catch (ArrayIndexOutOfBoundsException e){
            return false;
        }
    }

    /**
     * sets gene and recalculates fitness
     * @param gene gene must be set
     */
    public void setGene(Integer [] gene){
        this.gene = gene;

        calcFitness();
    }

    public Integer[] getGene() {
        return gene;
    }

    public Integer getFitness() {
        calcFitness();

        return fitness;
    }

    @Override
    public String toString() {
        return Arrays.toString(gene) + "\n";
    }
}