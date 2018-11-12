package test.gp.task1;

import com.gp.task1.DNA;
import com.gp.task1.DNAPool;
import com.gp.task1.Main;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MainTest {
    @Test
    public void getStringValueTest(){
        String option = "--protect";
        String value  = "best";

        String concatenated  = option + "=" + value;
        String concatenated1 = option + "= " + value;
        String concatenated2 = option + " = " + value;
        String concatenated3 = option + " =     " + value;

        Main.setArgs(new String[] {concatenated});
        String stringValue  = Main.getStringValue(option);
        Main.setArgs(new String[] {concatenated1});
        String stringValue1 = Main.getStringValue(option);
        Main.setArgs(new String[] {concatenated2});
        String stringValue2 = Main.getStringValue(option);
        Main.setArgs(new String[] {concatenated3});
        String stringValue3 = Main.getStringValue(option);

        Assert.assertEquals(value, stringValue);
        Assert.assertEquals(value, stringValue1);
        Assert.assertEquals(value, stringValue2);
        Assert.assertEquals(value, stringValue3);
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
}
