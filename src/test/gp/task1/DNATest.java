package test.gp.task1;

import com.gp.task1.DNA;
import org.junit.Assert;
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

//    @Before
//    public void setUp(){
//
//    }

    @Test
    public void getOnesMustHaveDNATest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getOnesMustHaveDNA = testDNA.getClass().getDeclaredMethod("getOnesMustHaveDNA");
        getOnesMustHaveDNA.setAccessible(true);

        int result = (int)getOnesMustHaveDNA.invoke(testDNA);

        Assert.assertTrue(result == 200 * (5 / 100.0));
    }

    @Test
    public void generateRandomDNATest_notFirstGeneration(){
        int dnaLength = 200;
        DNA dna = new DNA(dnaLength);

        dna.generateRandomDNA(200);
        int [] result = dna.getGene();

        Assert.assertNotNull(result);
        Assert.assertEquals(dnaLength, result.length);
    }

    @Test
    public void generateRandomDNATest_firstGeneration(){
        int dnaLength = 300;
        int initrate = 5;
        int countOfOnesMustBe = (int) (dnaLength * (initrate / 100.0));

        DNA dna = new DNA(dnaLength);
        dna.generateRandomDNA(dnaLength, initrate);

        int [] result = dna.getGene();
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
