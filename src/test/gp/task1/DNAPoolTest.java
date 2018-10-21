package test.gp.task1;

import com.gp.task1.DNA;
import com.gp.task1.DNAPool;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class DNAPoolTest {
    private static DNAPool pool;

    @BeforeClass
    public static void setupStatic(){
        pool = new DNAPool(200, 200, 1,
                2, 5, 1, 1);
    }

    @Test
    public void getMutationCountTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getMutationCount = pool.getClass().getDeclaredMethod("getMutationCount");
        getMutationCount.setAccessible(true);

        int result = (int)getMutationCount.invoke(pool);

        Assert.assertEquals(800, result);
    }

    @Test
    public void processMutationTest(){
        boolean thrown = false;

        try {
            pool.processMutation();
        } catch (Exception e) {
            thrown = true;
        }

        Assert.assertFalse(thrown);
    }

    @Test
    public void sortGenerationTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method sortGeneration = pool.getClass().getDeclaredMethod("sortGeneration", DNA [].class);
        sortGeneration.setAccessible(true);

        int thrownException = 0;
        DNA [] gen = new DNA[5];

        for(int i = 0; i < gen.length; i++){
            gen[i] = new DNA(10);
            gen[i].setFitness((int) (Math.random() * 10));
        }

        sortGeneration.invoke(pool, (Object) gen);
        int prevFitness = gen[0].getFitness();

        try {
            for (int i = 1; i < gen.length; i++) {
                if (prevFitness > gen[i].getFitness()) {
                    throw new Exception();
                }
                prevFitness = gen[i].getFitness();
            }
        }catch (Exception e){
            thrownException = 1;
        }

        Assert.assertTrue(thrownException == 0);
    }

    @Test
    public void getBestTenDNAsTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method sortGeneration = pool.getClass().getDeclaredMethod("sortGeneration", DNA [].class);
        sortGeneration.setAccessible(true);
        Method getBestTenDNAs = pool.getClass().getDeclaredMethod("getBestTenDNAs", DNA [].class);
        getBestTenDNAs.setAccessible(true);

        DNA [] gen = new DNA[20];
        int thrown = 0;

        for(int i = 0; i < gen.length; i++){
            gen[i] = new DNA(10);
            gen[i].setFitness((int) (Math.random() * 10));
        }

        sortGeneration.invoke(pool, (Object) gen);
        DNA [] bestTen = (DNA[]) getBestTenDNAs.invoke(pool, (Object) gen);

        try {
            for (int i = 0; i < gen.length - 10; i++) {
                if (bestTen[0].getFitness() < gen[i].getFitness()) {
                    throw new Exception();
                }
            }
        }catch (Exception e){
            thrown = 1;
        }

        Assert.assertTrue(bestTen.length == 10);
        Assert.assertTrue(thrown != 1);
    }
}
