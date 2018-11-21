package com.gp.task1;

public class Test {
    public static void main(String[] args) {
        for(float i = Constants.PC_MIN; i < Constants.PC_MAX; i += Constants.PC_STEP){
            for(float j = Constants.PM_MIN; j < Constants.PM_MAX; j += Constants.PM_STEP){
                System.out.printf("(%.2f|%.3f) , ", i, j);
            }
            System.out.println();
        }
    }
}
