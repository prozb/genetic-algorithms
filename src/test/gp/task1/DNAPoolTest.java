package test.gp.task1;

import com.gp.task1.DNAPool;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DNAPoolTest {
    private static DNAPool pool;

    @BeforeClass
    public static void setupStatic(){
        pool = new DNAPool(200, 200, 10, 1,
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
}
