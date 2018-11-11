package test.gp.task1;

import com.gp.task1.DNAPool;
import org.junit.Assert;
import org.junit.Test;

public class DNAPoolTest {
    @Test
    public void calcMaxFitnessOfGenerationTest(){
        DNAPool pool = new DNAPool(200, 200, 5);

        int expectedMax = 10;
        int actualMax   = pool.getMaxFitness();

        Assert.assertEquals(expectedMax, actualMax);
    }
}
