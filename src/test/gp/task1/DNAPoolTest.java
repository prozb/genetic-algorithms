package test.gp.task1;

import com.gp.task1.DNA;
import com.gp.task1.DNAPool;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Pavlo Rozbytskyi
 * @version 2.0.1
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
            for (int i = 0; i < pool.getGeneration().length; i++) {
                if (pool.getGeneration()[i].getFitness() > pool.getGeneration()[i].getFitness())
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

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate, 0, 0, 0, 0, true);

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

    @Test
    public void protectMutationTest(){
        int generationLen  = 200;
        int geneLen        = 200;
        int initRate       = 5;
        float mutationRate = 0.02f;

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate, mutationRate, 0, 0, 0, false);

        Arrays.stream(pool.getGeneration()).forEach(DNA::setBest);
        pool.getGeneration()[0].unsetBest();

        DNA [] beforeMutation = Arrays.copyOfRange(pool.getGeneration(), 1, pool.getGeneration().length);
        pool.processMutation();
        pool.processMutation();
        pool.processMutation();
        pool.processMutation();
        DNA [] afterMutation = Arrays.copyOfRange(pool.getGeneration(), 1, pool.getGeneration().length);

        Assert.assertArrayEquals(beforeMutation, afterMutation);
    }

    @Test
    public void getBestGenePosTest(){
        int generationLen  = 200;
        int geneLen        = 200;
        int initRate       = 5;
        float mutationRate = 0.02f;

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate, mutationRate, 0, 0, 0, false);

        int expected = (int) (Math.random() * pool.getGeneration().length);
        pool.getGeneration()[expected].setBest();

        int result = pool.getBestGenePos();

        Assert.assertEquals(result, expected);
    }

    @Test
    public void processRankingTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int generationLen  = 20;
        int geneLen        = 20;
        int initRate       = 5;
        float mutationRate = 0.02f;

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate, mutationRate, 0, 0, 0, false);
        Method processRanking = pool.getClass().getDeclaredMethod("processRanking");
        processRanking.setAccessible(true);
        processRanking.invoke(pool);

        boolean thrown = false;
        DNA [] dnas = pool.getGeneration();

        double prev = 0;

        try {
            for (int i = 0; i < dnas.length; i++) {
                if (dnas[i].getPs() + prev != dnas[i].getPsCum())
                    throw new RuntimeException();
                prev = dnas[i].getPsCum();
            }
        }catch (Exception e){
            thrown = true;
        }

        Assert.assertFalse(thrown);
    }
}


