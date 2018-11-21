package com.gp.task1;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



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

    public static boolean threaded = true;

    // TESTING PARAMETERS: --pm=0.02 --pc=0.5 --genecount=200 --genelen=200 --maxgen=1000 --runs=10 --protect=best --initrate=5 --crossover_scheme=1 --replication_scheme=1
    public static void main(String[] args) throws InterruptedException, IOException {
        arguments = args;
        sBuilder  = new StringBuilder();

        processUserInput();

        if(threaded) {
            int permNumber =  (int)((((Constants.PM_MAX - Constants.PM_MIN) / Constants.PM_STEP) *
                                    ((Constants.PC_MAX - Constants.PC_MIN) / Constants.PC_STEP)) + 0.5f);
            int threadsNum = permNumber / 30;
            //TODO: how to chose optimal num of threads??
            ExecutorService executors = Executors.newFixedThreadPool(threadsNum);
            Simulation [] simulations = new Simulation[threadsNum];

            System.err.println("start " + threadsNum + " threads");

            int simulPos  = 0;
            float pcStart = Constants.PC_MIN;
            float pmStart = Constants.PM_MIN;
            float pmEnd   = (Constants.PM_MAX - Constants.PM_MIN) / threadsNum;
            float pcEnd   = (Constants.PC_MAX - Constants.PC_MIN) / threadsNum;

            float pmConstStep = pmStart + pmEnd;
            float pcConstStep = pcStart + pcEnd;

            for (int i = 0; i < threadsNum; i++) {

                Simulation simulation = new Simulation(geneLen, generationCount, mutationRate, recombinationRate, runsNum,
                        replicationSchema, crossOverSchema, maxGenerations, initRate, protect, pcStart, pcConstStep,
                        Constants.PC_STEP, pmStart, pmConstStep, Constants.PM_STEP);

                System.err.println("started simulation #" + i + " with pcStart = " + pcStart +
                " pcEnd = " + pcConstStep + " pmStart = " + pmStart + " pmEnd = " + pmConstStep + "\n" +
                        new String(new char[100]).replace("\0", "=") + "\n");
                pcStart = pcConstStep;
                pmStart = pmConstStep;

                pmConstStep += pmEnd;
                pcConstStep += pcEnd;

                simulations[simulPos++] = simulation;

                executors.execute(simulation);
            }
            System.err.println("started " + threadsNum + " threads");
            executors.shutdown();
            while (!executors.isTerminated()){
                System.err.println("waiting");
                Thread.sleep(1000);
            }

            for(Simulation simulation : simulations){
                sBuilder.append(simulation.getStringBuilder().toString());
            }
            exportBufferToFile();
        }else {
            System.err.println("start thread");
            Simulation simulation = new Simulation(geneLen, generationCount, mutationRate, recombinationRate, runsNum,
                    replicationSchema, crossOverSchema, maxGenerations, initRate, protect, Constants.PC_MIN, Constants.PC_MAX,
                    Constants.PC_STEP, Constants.PM_MIN, Constants.PM_MIN, Constants.PM_STEP);

            Thread thread = new Thread(simulation);
            thread.start();
            System.err.println("started thread");

            //TODO: isReady deprecated
            while (!simulation.isReady()){
                System.err.println("waiting");
                Thread.sleep(1000);
            }
//            System.err.println(simulation.getStringBuilder().toString());
            sBuilder.append(simulation.getStringBuilder().toString());
            exportBufferToFile();
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
