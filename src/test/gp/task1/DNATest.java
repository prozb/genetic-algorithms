package test.gp.task1;

import com.gp.task1.DNA;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DNATest {
    private static DNA testDNA;

    @BeforeClass
    public static void setUpStatic(){
        testDNA = new DNA(200, 5);
    }

    @Before
    public void setUp(){

    }

    @Test
    public void getOnesMustHaveDNATest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getOnesMustHaveDNA = testDNA.getClass().getDeclaredMethod("getOnesMustHaveDNA");
        getOnesMustHaveDNA.setAccessible(true);

        int result = (int)getOnesMustHaveDNA.invoke(testDNA);

        Assert.assertTrue(result == 200 * (5 / 100.0));
    }

    @Test
    public void generateRandomDNATest_notFirstGeneration() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int dnaLength = 200;
        DNA dna = new DNA(dnaLength);

        Method generateRandomDNA = dna.getClass().getDeclaredMethod("generateRandomDNA", Integer.class);
        generateRandomDNA.setAccessible(true);
        generateRandomDNA.invoke(dna, 200);

        int [] result = dna.getGene();

        Assert.assertNotNull(result);
        Assert.assertEquals(dnaLength, result.length);
    }

    @Test
    public void generateRandomDNATest_firstGeneration() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int dnaLength = 300;
        int initrate = 5;
        int countOfOnesMustBe = (int) (dnaLength * (initrate / 100.0));
        DNA dna = new DNA(dnaLength);
        dna.printDNA();
        dna.generateRandomDNA(dnaLength, initrate);
        dna.printDNA();
//        Method generateRandomDNA = dna.getClass().getDeclaredMethod("generateRandomDNA", Integer.class, Integer.class);
//        generateRandomDNA.setAccessible(true);
//        generateRandomDNA.invoke(dna, dnaLength, initrate);

        int [] result = dna.getGene();
//        dna.printDNA();
        int countOfOnes = 0;
        for(int i = 0; i < result.length; i++){
            if(result[i] == 1){
                countOfOnes++;
            }
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(countOfOnes, countOfOnesMustBe);
    }
}
