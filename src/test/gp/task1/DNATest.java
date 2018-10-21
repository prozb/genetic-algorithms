package test.gp.task1;

import com.gp.task1.DNA;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class tests DNA
 *
 * @author Pavlo Rozbytskyi
 * @version 0.0.1
 */
public class DNATest {
    private DNA testDNA;

    @Before
    public void setUpStatic(){
        this.testDNA = new DNA(200, 5);
    }

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
        int initRate = 5;
        int countOfOnesMustBe = (int) (dnaLength * (initRate / 100.0));

        DNA dna = new DNA(dnaLength);
        dna.generateRandomDNA(dnaLength, initRate);

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

    @Test
    public void invertCellOfDNATest(){
        int posToTest  = (int)(Math.random() * testDNA.getGene().length);
        int prevResult = testDNA.getGene()[posToTest];

        testDNA.invertCellOfDNA(posToTest);

        Assert.assertNotEquals(prevResult, testDNA.getGene()[posToTest]);
    }

    @Test
    public void crossOverAnotherGeneTest(){
        int geneLength    = 20;
        int crossOverPos  = (int)(Math.random() * geneLength);
//        int crossOverPos = geneLength / 2;
        int thrownExcept  = 0;

        DNA firstTestDNA  = new DNA(geneLength, 10);
        DNA secondTestDNA = new DNA(geneLength, 10);

//        firstTestDNA.printDNA();
//        secondTestDNA.printDNA();
//        System.err.println("cross over pos: " + crossOverPos);

        DNA offspring1 = firstTestDNA.crossOverAnotherGene(secondTestDNA, crossOverPos);
        DNA offspring2 = secondTestDNA.crossOverAnotherGene(firstTestDNA, crossOverPos);

//        offspring1.printDNA();
//        offspring2.printDNA();

        try {
            crossOverTest(crossOverPos, secondTestDNA, firstTestDNA, offspring1);
            crossOverTest(crossOverPos, firstTestDNA, secondTestDNA, offspring2);
        }catch (Exception e){
            thrownExcept = 1;
        }

        Assert.assertTrue(thrownExcept == 0);
    }

    private void crossOverTest(int crossOverPos, DNA firstTestDNA, DNA secondTestDNA, DNA offspring2) throws Exception {
        for(int i = 0; i < offspring2.getGene().length; i++){
            if(i < crossOverPos){
                if(secondTestDNA.getGene()[i] != offspring2.getGene()[i]){
                    throw new Exception();
                }
            }else{
                if(firstTestDNA.getGene()[i] != offspring2.getGene()[i]){
                    throw new Exception();
                }
            }
        }
    }
}
