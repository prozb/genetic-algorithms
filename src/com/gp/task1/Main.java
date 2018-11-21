package com.gp.task1;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Main {
    private static int generationCount;
    private static int geneLen;
    private static int replicationSchema;
    private static int crossOverSchema;
    private static int maxGenerations;
    private static int initRate;
    private static int runsNum;
    private static float mutationRate;
    private static float recombinationRate;
    private static boolean protect;

    private static int [] statArr;
    private static StringBuilder sBuilder;
    private static BufferedWriter writer;
    public static String [] arguments;
    public static Point [] points;
    public static int pointsPos;

    public static boolean threaded = true;
    public static long startTime;
    public static long relativeTime;

    // TESTING PARAMETERS: --pm=0.02 --pc=0.5 --genecount=200 --genelen=200 --maxgen=1000 --runs=10 --protect=best --initrate=5 --crossover_scheme=1 --replication_scheme=1
    public static void main(String[] args) throws InterruptedException, IOException {
        arguments = args;
        sBuilder  = new StringBuilder();

        //process time
        startTime    = System.nanoTime();
        relativeTime = startTime;

        processUserInput();

        if(threaded) {
            int permNumber          =  (int)((((Constants.PM_MAX - Constants.PM_MIN) / Constants.PM_STEP) *
                                    ((Constants.PC_MAX - Constants.PC_MIN) / Constants.PC_STEP)) + 0.5f);
            int threadsNum          = permNumber / 30;
            int simulationPerThread = permNumber / threadsNum;
            //creates array with points with pm and pc to pass into each simulation
            calculatePointsToProcess(permNumber);

            ExecutorService executors = Executors.newFixedThreadPool(threadsNum);
            Simulation [] simulations = new Simulation[threadsNum];

            System.err.println("start " + threadsNum + " threads");

            int simulationPos  = 0;

            int startPos = 0;
            int endPos   = simulationPerThread;

            for (int i = 0; i < threadsNum; i++) {

                Simulation simulation = new Simulation(geneLen, generationCount, mutationRate, recombinationRate,
                        runsNum, replicationSchema, crossOverSchema, maxGenerations, initRate, protect, i,
                        Arrays.copyOfRange(points, startPos, endPos));

                startPos = endPos;
                endPos  += simulationPerThread;

                simulations[simulationPos++] = simulation;
                executors.execute(simulation);
            }
            System.err.println("started " + threadsNum + " threads");
            executors.shutdown();
            while (!executors.isTerminated()){
//                System.err.println("#Main Thread waiting");
//                Thread.sleep(1000);
//                if(relativeTime >= System.nanoTime() / 10000){
//                    System.err.println("Time: duration = " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime) + "s");
//                    relativeTime = System.nanoTime();
//                }
            }

//            System.err.println("Simulations count: " + Simulation.simulationsCount);
            for(Simulation simulation : simulations){
                sBuilder.append(simulation.getStringBuilder().toString());
            }
            exportBufferToFile();
            System.err.println("Time: complete program duration = " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime) + "s");
        }else {
//            System.err.println("start thread");
//            Simulation simulation = new Simulation(geneLen, generationCount, mutationRate, recombinationRate, runsNum,
//                    replicationSchema, crossOverSchema, maxGenerations, initRate, protect, Constants.PC_MIN, Constants.PC_MAX,
//                    Constants.PC_STEP, Constants.PM_MIN, Constants.PM_MIN, Constants.PM_STEP, 0);
//
//            Thread thread = new Thread(simulation);
//            thread.start();
//            System.err.println("started thread");
//
//            //TODO: isReady deprecated
//            while (!simulation.isReady()){
//                Thread.sleep(2000);
//            }
//            sBuilder.append(simulation.getStringBuilder().toString());
//            exportBufferToFile();
        }
    }

    public static void calculatePointsToProcess(int pointsNum){
        points = new Point[pointsNum];
        //scale factors just to iterate over integers, not floats
        for(int i = (int) (Constants.PC_MIN * Constants.SCALE_FACTOR); i < (int)(Constants.PC_MAX * Constants.SCALE_FACTOR); i += (int)(Constants.PC_STEP * Constants.SCALE_FACTOR)){
            for(int j = (int)(Constants.PM_MIN * Constants.SCALE_FACTOR); j < (int)(Constants.PM_MAX * Constants.SCALE_FACTOR); j += (int)(Constants.PM_STEP * Constants.SCALE_FACTOR)){
                points[pointsPos++] = new Point(j / (Constants.SCALE_FACTOR + 0.0f),
                                                 i / (Constants.SCALE_FACTOR + 0.0f));
            }
        }
    }
    private static void exportBufferToFile() throws IOException {
        writer = new BufferedWriter(new FileWriter("plot.txt"));
        writer.write(sBuilder.toString());
        writer.flush();
        writer.close();
    }

    private static void processUserInput(){
        if(arguments.length <= 1){
            printHelpMessage();
            printError("No parameters!");
        }else if(arguments[1].equals("--help")){
            printHelpMessage();
            System.exit(99);
        }else if(arguments.length < Constants.NUM_OF_ARGS){
            printHelpMessage();
            printError("Not enough parameters!");
        }else if(arguments.length > Constants.NUM_OF_ARGS){
            printHelpMessage();
            printError("To many parameters!");
        }else {
            generationCount   = (int) getValue("--genecount");
            geneLen           = (int) getValue("--genelen");
            crossOverSchema   = (int) getValue("--crossover_scheme");
            replicationSchema = (int) getValue("--replication_scheme");
            initRate          = (int) getValue("--initrate");
            maxGenerations    = (int) getValue("--maxgen");
            runsNum           = (int) getValue("--runs");
            mutationRate      = getValue("--pm");
            recombinationRate = getValue("--pc");
            protect = getStringValue("--protect").equals("best");

            statArr = new int [runsNum];
        }
    }

    private static float getValue(String arg){
        Optional<String> geneLenStringOptional = Arrays.stream(arguments).filter(elem -> elem.contains(arg)).findFirst();
        String geneLenString = "";

        if(geneLenStringOptional.isPresent()){
            geneLenString = geneLenStringOptional.get().trim().replace(" ", "");
        }
        int pos = geneLenString.indexOf(arg) + 1 + arg.length();
        String num = geneLenString.substring(pos);

        float val = 0;
        try {
            val = Float.parseFloat(num);
        }catch (NumberFormatException e){
            printError("Number \"" + arg + "\" not found!");
        }
        return val;
    }

    public static String getStringValue(String arg){
        Optional<String> geneLenStringOptional = Arrays.stream(arguments).filter(elem -> elem.contains(arg)).findFirst();
        String geneLenString = "";

        if(geneLenStringOptional.isPresent()){
            geneLenString = geneLenStringOptional.get().trim().replace(" ", "");
        }
        int pos = geneLenString.indexOf(arg) + 1 + arg.length();
        String val = geneLenString.substring(pos).trim();

        if(val.equals("")){
            printError("protect option can not be empty!");
        }

        return val.trim();
    }

    public static void printError(String err){
        System.out.println("Error: " + err);
        System.exit(99);
    }

    private static void printHelpMessage(){
        System.out.println("\nusage:\n--------------------------------------------" +
                " \n [--help]\nto process simulation you must enter all following args\n" +
                "[--pc] recombination rate (float number)\n" +
                "[--pm] mutations rate (float number)\n" +
                "[--initrate] initiations rate (integer number)\n" +
                "[--genecnt] count of genes in generation (integer number)\n" +
                "[--genelen] length of the gene (integer number)\n" +
                "[--crossover_scheme] (integer number)\n" +
                "[--replication_scheme] (integer number)\n" +
                "[--maxgen] maximum generations (integer number)\n" +
                "[--runs] number of runs (integer number)\n" +
                "[--protect] the best gene is crossover and mutation protected (best|none)");
    }
}
