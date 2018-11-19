package com.gp.task1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

/**
 * @author Pavlo Rozbytskyi
 * @version 2.0.1
 */
// after each generation loop you must recalculate fitness off all dna's and
// figure out maximal and minimal fitness
public class DNAPool {
    private DNA[] currentGeneration;
    private int maxFitness;
    private int minFitness;
    private int generationsCount;
    private int generationLen;
    private int geneLen;
    private float mutationRate;
    private float recombinationRate;
    private int replicationSchema;
    private int crossOverSchema;
    private boolean finished;
    private boolean protect;

    public DNAPool(){

    }

    // calculating fitness after each loop and after creating new generation
    public DNAPool(int generationLen, int geneLen, int initRate, float mutationRate, int replicationSchema, int crossOverSchema, float recombinationRate, boolean protect){
        this.replicationSchema = replicationSchema;
        this.crossOverSchema   = crossOverSchema;
        this.recombinationRate = recombinationRate;

        this.mutationRate  = mutationRate;
        this.generationLen = generationLen;
        this.protect       = protect; // protecting best gene from mutation and cross over

        createGenerations(generationLen, geneLen, initRate);
    }

    private void createGenerations(int generationLen, int geneLen, int initRate){
        this.currentGeneration = new DNA[generationLen];
        this.geneLen           = geneLen;
        this.generationsCount  = 0;

        for(int i = 0; i < currentGeneration.length; i++){
            currentGeneration[i] = new DNA(geneLen, initRate);
        }

        calcMaxFitnessOfGeneration();
        calcMinFitnessOfGeneration();
    }

    /**
     * calculating max fitness of gen
     */
    public void calcMaxFitnessOfGeneration(){
        Optional<DNA> dnaMaxFitness = Arrays.stream(currentGeneration).max(Comparator.comparing(DNA::getFitness));
        dnaMaxFitness.ifPresent(DNA -> maxFitness = DNA.getFitness());

        if(maxFitness == geneLen) {
            finished = true;
            printInfo();
        }
    }

    /**
     * calculating min fitness of gen
     */
    public void calcMinFitnessOfGeneration(){
        Optional<DNA> dnaMinFitness = Arrays.stream(currentGeneration).min(Comparator.comparing(DNA::getFitness));
        dnaMinFitness.ifPresent(DNA -> minFitness = DNA.getFitness());
    }

    public void switchToNextGeneration(){
        calcMinFitnessOfGeneration();
        calcMaxFitnessOfGeneration();
        generationsCount++;
    }

    // created for testing reasons
    public void processCrossOver(){
        setBestGene();
        switch(crossOverSchema){
            case 1:
                crossOverSchemaOne();
                break;
            default:
                throw new RuntimeException("please input replication schema");
        }
        unsetBestGene();
        calcMinFitnessOfGeneration();
        calcMaxFitnessOfGeneration();
    }

    public void crossOverSchemaOne(){
        int crossOverCount = (int) (recombinationRate * generationLen);
        int crossOverPerf  = 0;
        int firstGenePos   = 0;
        int secondGenePos  = 0;
        int randPos = 0;

        int bestPos = getBestGenePos();

        do {
            firstGenePos  = (int) (Math.random() * generationLen);
            secondGenePos = (int) (Math.random() * generationLen);

            if(!(firstGenePos == bestPos || secondGenePos == bestPos)){
                randPos = (int) (Math.random() * geneLen);

                DNA DNA1 = crossOver(currentGeneration[firstGenePos], currentGeneration[secondGenePos], randPos);
                DNA DNA2 = crossOver(currentGeneration[secondGenePos], currentGeneration[firstGenePos], randPos);

                currentGeneration[firstGenePos] = DNA1;
                currentGeneration[secondGenePos] = DNA2;

                crossOverCount--;
                crossOverPerf++;
            }
        }while (crossOverCount > 0);

        assert (int)(currentGeneration.length * recombinationRate) == crossOverPerf;
    }

    public DNA crossOver(DNA DNA1, DNA DNA2, int randPos){
        DNA newDNA = new DNA(DNA1.getGene().length);
        Integer [] newGene = new Integer[DNA1.getGene().length];

        System.arraycopy(DNA1.getGene(), 0, newGene, 0, randPos);
        System.arraycopy(DNA2.getGene(), randPos, newGene, randPos, DNA1.getGene().length - randPos);

        newDNA.setGene(newGene);
        return newDNA;
    }

    public void sortGeneration(){
        Arrays.sort(currentGeneration, Comparator.comparing(DNA::getFitness));
    }

    public void processMutation(){
        setBestGene();

        int mutationCount     = (int) (mutationRate * geneLen * generationLen) + 1;
        int mutationPerformed = 0;

        int randGen = 0;
        int randPos = 0;

        int bestGenePos = getBestGenePos();

        do{
            randGen = (int) (Math.random() * generationLen);

            if(randGen != bestGenePos) {
                randPos = (int) (Math.random() * geneLen);

                currentGeneration[randGen].invertCell(randPos);

                mutationCount--;
                mutationPerformed++;
            }
        }while (mutationCount > 0);

        // testing reasons
        if((int) (mutationRate * geneLen * generationLen) != --mutationPerformed){
            throw new RuntimeException();
        }

        unsetBestGene();
        calcMaxFitnessOfGeneration();
        calcMinFitnessOfGeneration();
    }

    public void processReplication(){
//        sortGeneration();
        switch(replicationSchema){
            case 1:
                replicationSchemaOne();
                break;
            case 2:
                replicationSchemaTwo();
                break;
            default:
                throw new RuntimeException("please input replication schema");
        }

        calcMinFitnessOfGeneration();
        calcMaxFitnessOfGeneration();
    }

    // rank based selection
    private void replicationSchemaTwo(){
        sortGeneration();
        processRanking();
        passRankedGenesIntoGeneration();
        clearRankings();
    }

    private void clearRankings(){
        Arrays.stream(currentGeneration).forEach(DNA::clearPs);
    }

    private void processRanking(){
//        printOutRankTable();////////TESTING REASONS
        currentGeneration[0].calcProbability(0, generationLen);

        for(int rank = 1; rank < currentGeneration.length; rank++){
            currentGeneration[rank].calcProbability(rank, generationLen);
            double prev = currentGeneration[rank - 1].getPsCum();
            currentGeneration[rank].calcCumulProbability(prev);
        }
//        printOutRankTable();/////////TESTING REASONS
    }

    private void passRankedGenesIntoGeneration(){
        DNA [] newGeneration = new DNA[generationLen];
        Arrays.fill(newGeneration, new DNA(geneLen));
        for(int i = 0; i < newGeneration.length; i++){
            newGeneration[i] = getBestRankedDNA();
        }

        this.currentGeneration = newGeneration;
    }

    //TODO: How to test this method?????
    //TODO: need refactoring?
    private DNA getBestRankedDNA(){
        double probability = Math.random();

        for(int i = 1; i < currentGeneration.length; i++){
            if(probability == currentGeneration[i - 1].getPsCum()){
//                System.out.println("prob: " + probability + "chosen DNA is Nr." + (i - 1) + " fitness = " + currentGeneration[i - 1].getFitness() +
//                " psCum = " + currentGeneration[i - 1].getPsCum());
                return currentGeneration[i - 1];
            }else if(probability <= currentGeneration[i].getPsCum() && probability > currentGeneration[i - 1].getPsCum()){
//                System.out.println("prob: " + probability +  "chosen DNA is Nr." + (i) + " fitness = " + currentGeneration[i].getFitness() +
//                        " psCum = " + currentGeneration[i].getPsCum());
                return currentGeneration[i];
            }
        }
//        System.out.println("prob: " + probability + "chosen DNA is Nr." + (currentGeneration.length - 1) + " fitness = " + currentGeneration[currentGeneration.length - 1].getFitness() +
//                " psCum = " + currentGeneration[currentGeneration.length - 1].getPsCum());
        return currentGeneration[currentGeneration.length - 1];
    }

    private void replicationSchemaOne(){
        int selectionPercent = 10;

        DNA[] bestDNAS = getBestGenes(selectionPercent);

        for(int i = 0; i < currentGeneration.length; i++){
            currentGeneration[i] = bestDNAS[i / 10];
        }
    }

    public int getBestGenePos(){
        Optional<DNA> bestOpt = Arrays.stream(currentGeneration).filter(DNA::isBest).findFirst();
        int bestPos = 0;
        if(bestOpt.isPresent()) {
            bestPos = Arrays.asList(currentGeneration).indexOf(bestOpt.get());
        }
        return bestPos;
    }

    public DNA[] getBestGenes(int selectionPercent){
        int selectCount = (int) (selectionPercent / 100.0f * generationLen);

        return Arrays.copyOfRange(currentGeneration, currentGeneration.length - selectCount, currentGeneration.length);
    }

    public int getGenerationsCount(){
        return generationsCount;
    }

    public int getMaxFitness(){
        return maxFitness;
    }

    public int getMinFitness() {
        return minFitness;
    }

    public DNA[] getGeneration(){
        return currentGeneration;
    }

    public boolean isFinished(){
        if(maxFitness >= geneLen){
            return true;
        }
        return false;
    }

    public void printInfo(){
        System.out.println("max fitness: " + maxFitness + " min fitness: " + minFitness +
                " gen number: " + generationsCount + " \n---------------------------------------------------");
    }

    public void printGeneration(){
        System.out.println("---------------------------------------------------------");
        System.out.println(Arrays.toString(currentGeneration));
        System.out.println("---------------------------------------------------------");
    }

    public void setBestGene(){
        if(protect) {
            Optional<DNA> bestGene = Arrays.stream(currentGeneration).max(Comparator.comparing(DNA::getFitness));
            bestGene.ifPresent(DNA::setBest);
        }
    }

    public void unsetBestGene(){
        if(protect) {
            Optional<DNA> bestGene = Arrays.stream(currentGeneration).max(Comparator.comparing(DNA::isBest));
            bestGene.ifPresent(DNA::unsetBest);
        }
    }

    //TODO: print out rank based selection table for debugging reasons
    public void printOutRankTable(){
        Arrays.stream(currentGeneration).forEach(DNA::printRank);
    }

}