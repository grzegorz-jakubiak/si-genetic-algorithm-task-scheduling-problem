import msrcpsp.evaluation.BaseEvaluator;
import msrcpsp.evaluation.DurationEvaluator;
import msrcpsp.evaluation.EvaluatorType;
import msrcpsp.io.MSRCPSPIO;
import msrcpsp.scheduling.BaseIndividual;
import msrcpsp.scheduling.Resource;
import msrcpsp.scheduling.Schedule;
import msrcpsp.scheduling.Task;
import msrcpsp.scheduling.greedy.Greedy;

import java.util.List;
import java.util.Random;

/**
 * Created by GreggJakubiak on 09.03.2017.
 */
public class Population {
    private Schedule[] individuals;
    private int[] individualDuration;
    private double[] individualFitness;
    private MSRCPSPIO reader = new MSRCPSPIO();

    //constructor for creating population of a given size and using given def file
    public Population(int popSize, String fileName){
        individuals = new Schedule[popSize];
        individualDuration = new int[popSize];
        individualFitness = new double[popSize];
        for(int i = 0; i < individuals.length; i++) {
            individuals[i] = reader.readDefinition(fileName);
            individuals[i].setEvaluator(new DurationEvaluator(individuals[i]));
        }
    }
    //population from schedule array
    public Population(Schedule[] schedules){
        this.individuals = schedules;
        this.individualDuration = new int[individuals.length];
        for (int i = 0; i < individuals.length; i++){
            individualDuration[i] = calculateIndividualDuration(individuals[i]);
        }
    }

    //initializing population at random and setting task times
    public void initializeRandomPopulation(){
        for(int i = 0; i < individuals.length; i++){
            initializeRandomIndividual(individuals[i]);
            initializeTaskTime(individuals[i]);
            individualDuration[i] = calculateIndividualDuration(individuals[i]);
        }
    }

    //initializing particular individual taking into account task constraints
    private void initializeRandomIndividual(Schedule schedule){
        List<Resource> capableResources;
        Random random = new Random(System.currentTimeMillis());
        int[] upperBounds = schedule.getUpperBounds(schedule.getTasks().length);
        Task[] tasks = schedule.getTasks();
        for(int i = 0; i < tasks.length; i++){
            capableResources = schedule.getCapableResources(tasks[i]);
            schedule.assign(tasks[i], capableResources.get((int)(random.nextDouble() * upperBounds[i])));
        }
    }

    private void initializeTaskTime(Schedule schedule){
        Greedy greedy = new Greedy(schedule.getSuccesors());
        greedy.buildTimestamps(schedule);
    }

    //calcualte duration for individual schedule using BaseIndividual
    private int calculateIndividualDuration(Schedule schedule){
        BaseIndividual baseIndividual = new BaseIndividual(schedule, schedule.getEvaluator());
        baseIndividual.setDurationAndCost();
        return baseIndividual.getDuration();
    }

    public Schedule[] getIndividuals(){
        return individuals;
    }

    public int[] getIndividualDuration(){
        return individualDuration;
    }

    public int getSumOfDurations(){
        int sum = 0;
        for(int i = 0; i < individualDuration.length; i++){
            sum += individualDuration[i];
        }
        return sum;
    }

    public void calculateFitnesses(){
        for (int i = 0; i < individualDuration.length; i++){
            individualFitness[i] = individualDuration[i] / getSumOfDurations();
        }
    }
}
