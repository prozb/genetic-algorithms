package test.gp.task1;

import com.gp.task1.DNA;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class DNATest {
    @Test
    public void initGeneTest(){
        int len  = 200;
        int init = 5;

        DNA gene = new DNA(len, init);

        int should    = 10;
        int countInit = (int) ((init / 100.0) * len);
        Assert.assertEquals(countInit, should);

        Integer [] geneArr = gene.getGene();
        int occurrence = (int) Arrays.stream(geneArr).filter(elem -> 1 == elem).count();
        Assert.assertEquals(occurrence, countInit);

        DNA gene1 = new DNA(200);
        Integer [] geneArr1 = gene1.getGene();
        int occurrence1  = (int) Arrays.stream(geneArr1).filter(elem -> 1 == elem).count();
        Assert.assertEquals(0, occurrence1);
    }

    @Test
    public void calcFitnessTest(){
        int len  = 200;
        int init = 5;

        DNA dna = new DNA(len, init);

        int expected = (int)((init / 100.0) * len);
        int fitness  = dna.getFitness();

        Assert.assertEquals(expected, fitness);
    }

    @Test
    public void calcEmptyFitnessTest(){
        int len  = 200;

        DNA dna = new DNA(len);

        int expected = 0;
        int fitness  = dna.getFitness();

        Assert.assertEquals(expected, fitness);
    }
}
