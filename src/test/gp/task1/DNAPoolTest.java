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

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate, 0);

        int expectedMax = 10;
        int actualMax   = pool.getMaxFitness();

        Assert.assertEquals(expectedMax, actualMax);
    }

    @Test
    public void switchToNextGenerationTest(){
        int generationLen = 200;
        int geneLen       = 200;
        int initRate      = 5;

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate, 0);
        pool.switchToNextGeneration();

        int expectedNewFitn  = 0;
        int expectedGenCount = 1;
        int actual           = pool.getGenerationsCount();
        Assert.assertEquals(expectedGenCount, actual);
        Assert.assertEquals(expectedNewFitn, pool.getMaxFitness());
    }

    @Test
    public void crossOverTest(){
        DNAPool pool = new DNAPool();

        int pos  = 50;
        int len  = 200;
        int init = 5;

        DNA dna1 = new DNA(len, init);
        DNA dna2 = new DNA(len, init);

        DNA newDna = pool.crossOver(dna1, dna2, pos);

        Integer [] gene  = newDna.getGene();
        Integer [] part1 = Arrays.copyOfRange(dna1.getGene(), 0, pos);
        Integer [] part2 = Arrays.copyOfRange(dna2.getGene(), pos, dna2.getGene().length);

        Assert.assertArrayEquals(part1, Arrays.copyOfRange(gene, 0, pos));
        Assert.assertArrayEquals(part2, Arrays.copyOfRange(gene, pos, dna2.getGene().length));
    }

    @Test
    public void processMutationTest(){
        int generationLen  = 200;
        int geneLen        = 200;
        int initRate       = 5;
        float mutationRate = 0.02f;
        boolean thrown     = false;

        try {
            DNAPool pool = new DNAPool(generationLen, geneLen, initRate, mutationRate);
            pool.processMutation();
        }catch (Exception e){
            thrown = true;
        }

        Assert.assertFalse(thrown);
    }
}
