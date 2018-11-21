package test.gp.task1;

import com.gp.task1.Constants;
import com.gp.task1.Main;
import org.junit.Assert;
import org.junit.Test;

public class MainTest {
    @Test
    public void calculatePointsToProcessTest(){
        int pointsNum = (int)((((Constants.PM_MAX - Constants.PM_MIN) / Constants.PM_STEP) *
                ((Constants.PC_MAX - Constants.PC_MIN) / Constants.PC_STEP)) + 0.5f);
        Main.calculatePointsToProcess(pointsNum);
        Assert.assertEquals(pointsNum, Main.points.length);
    }
}
