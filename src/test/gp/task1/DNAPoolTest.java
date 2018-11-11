package test.gp.task1;

import com.gp.task1.DNAPool;
import org.junit.Assert;
import org.junit.Test;

public class DNAPoolTest {
    @Test
    public void calcMaxFitnessOfGenerationTest(){
        int generationLen = 200;
        int geneLen       = 200;
        int initRate      = 5;

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate);

        int expectedMax = 10;
        int actualMax   = pool.getMaxFitness();

        Assert.assertEquals(expectedMax, actualMax);
    }

    @Test
    public void switchToNextGenerationTest(){
        int generationLen = 200;
        int geneLen       = 200;
        int initRate      = 5;

        DNAPool pool = new DNAPool(generationLen, geneLen, initRate);
        pool.switchToNextGeneration();

        int expectedNewFitn  = 0;
        int expectedGenCount = 1;
        int actual           = pool.getGenerationsCount();
        Assert.assertEquals(expectedGenCount, actual);
        Assert.assertEquals(expectedNewFitn, pool.getMaxFitness());
    }
}
