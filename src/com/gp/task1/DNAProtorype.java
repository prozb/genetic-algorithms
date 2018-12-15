package com.gp.task1;

import java.util.Arrays;

public abstract class DNAProtorype {
    int len;
    int initRate;
    double ps;      // probability to be chosen
    double psCum;   // cumulated probability
    Integer fitness;
    Integer [] gene;
    boolean best;

    DNAProtorype(int len, int initRate){
        this.len      = len;
        this.fitness  = 0;
        this.initRate = initRate;

        initGene(initRate);
    }

    DNAProtorype(int len){
        this.len = len;
        this.fitness = 0;
        initGene(0);
    }

    /*****************************************************
                            METHODS
     *****************************************************/
    // returns true if cell can be set
    boolean setCell(int pos){
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

    public void invertCell(int pos){
        gene[pos] = gene[pos] == 0 ? 1 : 0;
    }

    void initGene(){
        this.gene = new Integer[len];
        Arrays.fill(gene, 0);
    }

    /**
     *  initRate represents percent of all cells set on 1
     * @param initRate integer represents percent
     */
    void initGene(int initRate){
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
    /*****************************************************
                        GETTERS / SETTERS
     *****************************************************/
    public void calcProbability(int r, int n){
        this.ps = ((2 - Constants.S) / n) + (2.0f * r * (Constants.S - 1)) / (n * (n - 1));
    }

    public double getPs(){
        return ps;
    }

    public void calcCumulProbability(double prevPs){
        psCum = prevPs + ps;
    }

    public double getPsCum(){
        return psCum;
    }

    public void clearPs(){
        ps = 0;
        psCum = 0;
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

    // please unset best gene after each generation
    public void setBest(){
        this.best = true;
    }

    public void unsetBest(){
        this.best = false;
    }

    public boolean isBest() {
        return best;
    }

    /*****************************************************
                        ABSTRACT METHODS
     *****************************************************/
    public abstract void calcFitness();
}
