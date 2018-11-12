package test.gp.task1;

import com.gp.task1.DNA;
import com.gp.task1.DNAPool;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Pavlo Rozbytskyi
 * @version 2.0.0
 */
public class DNAPoolTest {
    @Test
    public void calcMaxFitnessOfGenerationTest(){
        int generationLen = 200;
        int geneLen       = 200;
        int initRate      = 5;

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate, 0, 0, 0, 0, false);

        int expectedMax = 10;
        int actualMax   = pool.getMaxFitness();

        Assert.assertEquals(expectedMax, actualMax);
    }

    @Test
    public void switchToNextGenerationTest(){
        int generationLen = 200;
        int geneLen       = 200;
        int initRate      = 5;

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate, 0, 0, 0, 0, false);
        pool.switchToNextGeneration();

        int expectedNewFitn  = 0;
        int expectedGenCount = 1;
        int actual           = pool.getGenerationsCount();
        Assert.assertEquals(expectedGenCount, actual);
    }

    @Test
    public void crossOverTest(){
        DNAPool pool = new DNAPool();

        int pos  = 50;
        int len  = 200;
        int init = 5;

        DNA DNA1 = new DNA(len, init);
        DNA DNA2 = new DNA(len, init);

        DNA newDNA = pool.crossOver(DNA1, DNA2, pos);

        Integer [] gene  = newDNA.getGene();
        Integer [] part1 = Arrays.copyOfRange(DNA1.getGene(), 0, pos);
        Integer [] part2 = Arrays.copyOfRange(DNA2.getGene(), pos, DNA2.getGene().length);

        Assert.assertArrayEquals(part1, Arrays.copyOfRange(gene, 0, pos));
        Assert.assertArrayEquals(part2, Arrays.copyOfRange(gene, pos, DNA2.getGene().length));
    }

    @Test
    public void processMutationTest(){
        int generationLen  = 200;
        int geneLen        = 200;
        int initRate       = 5;
        float mutationRate = 0.02f;
        boolean thrown     = false;

        try {
            DNAPool pool = new DNAPool(generationLen, geneLen, initRate, mutationRate, 0, 0, 0, false);
            pool.processMutation();
        }catch (Exception e){
            thrown = true;
        }

        Assert.assertFalse(thrown);
    }

    @Test
    public void sortGenerationTest(){
        int generationLen  = 200;
        int geneLen        = 200;
        int initRate       = 5;
        float mutationRate = 0.02f;
        boolean thrown     = false;

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate, mutationRate, 0, 0, 0, false);

        pool.processMutation();
        pool.processMutation();
        pool.processMutation();
        pool.processMutation();

        pool.sortGeneration();

        try {
//            for (int i = 1; i < pool.getGeneration().length; i++) {
            for (int i = pool.getGeneration().length; i > 1; i++) {
                if (pool.getGeneration()[i].getFitness() < pool.getGeneration()[i - 1].getFitness())
                    throw new RuntimeException();
            }
        }catch (Exception e){
            thrown = true;
        }
        Assert.assertFalse(thrown);
    }

    @Test
    public void getBestGenesTest(){
        int generationLen  = 200;
        int geneLen        = 200;
        int initRate       = 5;
        float mutationRate = 0.02f;
        int replicationSch = 1;

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate, mutationRate, replicationSch, 0, 0, false);

        pool.processMutation();
        pool.processMutation();
        pool.processMutation();
        pool.processMutation();
        pool.sortGeneration();

        DNA[] bestTwenty = pool.getBestGenes(10);

        Assert.assertEquals(20, bestTwenty.length);
        Assert.assertTrue(bestTwenty[bestTwenty.length - 1].getFitness() >= pool.getGeneration()[bestTwenty.length].getFitness());
    }

    @Test
    public void replicationSchemaOneTest(){
        int generationLen  = 200;
        int geneLen        = 200;
        int initRate       = 5;
        float mutationRate = 0.02f;
        int replicationSch = 1;
        boolean thrown     = false;

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate, mutationRate, replicationSch, 0, 0, false);

        pool.processMutation();
        pool.processMutation();
        pool.processMutation();
        pool.processMutation();
        pool.sortGeneration();

        DNA[] bestGenes  = pool.getBestGenes(10);
        pool.processReplication();
        DNA[] generation = pool.getGeneration();

        try{
            for(int i = 0; i < generation.length; i++){
                if(!generation[i].getFitness().equals(bestGenes[i % 10].getFitness()))
                    throw new RuntimeException();
            }
        }catch (Exception e){
            thrown = true;
        }
        Assert.assertFalse(thrown);
    }

    @Test
    public void crossOverSchemaOneTest(){
        int generationLen  = 200;
        int geneLen        = 200;
        int initRate       = 5;
        float mutationRate = 0.02f;
        int replicationSch = 1;
        int crossOverSch   = 1;

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate, mutationRate, replicationSch, crossOverSch, 0.5f, false);

        pool.processMutation();
        pool.processMutation();
        pool.processMutation();
        pool.processMutation();
        pool.processCrossOver();
    }

    @Test
    public void setBestGeneTest(){
        int generationLen = 200;
        int geneLen       = 200;
        int initRate      = 5;
        int bestGenePos   = 10;

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate, 0, 0, 0, 0, false);

        Integer [] bestArr = new Integer[geneLen];
        Arrays.fill(bestArr, 1);
        pool.getGeneration()[bestGenePos].setGene(bestArr);
        pool.setBestGene();

        boolean set = pool.getGeneration()[bestGenePos].isBest();

        Assert.assertTrue(set);
    }

    @Test
    public void unsetBestGene(){
        int generationLen = 200;
        int geneLen       = 200;
        int initRate      = 5;
        int bestGenePos   = 10;

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate, 0, 0, 0, 0, false);

        Integer [] bestArr = new Integer[geneLen];
        Arrays.fill(bestArr, 1);
        pool.getGeneration()[bestGenePos].setGene(bestArr);
        pool.setBestGene();
        pool.unsetBestGene();

        boolean unSet = pool.getGeneration()[bestGenePos].isBest();

        Assert.assertFalse(unSet);
    }
}
