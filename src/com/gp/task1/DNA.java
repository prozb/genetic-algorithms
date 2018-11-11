package com.gp.task1;

import java.util.Arrays;

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

    public Integer[] getGene() {
        return gene;
    }

    public Integer getFitness() {
        return fitness;
    }
}