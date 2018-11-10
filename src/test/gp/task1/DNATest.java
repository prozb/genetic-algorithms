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

        int [] geneArr = gene.getGene();
        int occurrence = (int) Arrays.stream(geneArr).filter(elem -> 1 == elem).count();
        Assert.assertEquals(occurrence, countInit);

        DNA gene1 = new DNA(200);
        int [] geneArr1 = gene1.getGene();
        int occurrence1  = (int) Arrays.stream(geneArr1).filter(elem -> 1 == elem).count();
        Assert.assertEquals(0, occurrence1);
    }
}
