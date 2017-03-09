import msrcpsp.scheduling.Schedule;
import msrcpsp.scheduling.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by GreggJakubiak on 09.03.2017.
 */
public class GA {

    private Population population;
    private int crossoverRate;
    private int mutationRate;

    public GA(Population population,int crossoverRate, int mutationRate){
        this.population = population;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
    }

    public Schedule roulette(){
        Schedule schedule = null;
        int populationSize = population.getPopulationSize();
        Schedule[] individuals = population.getIndividuals();
        double[] fitnesses = population.getIndividualFitnesses();
        Random random = new Random();

        for(int i = 0; i < populationSize; i++){
            if(random.nextDouble() >= fitnesses[i]){
                schedule = individuals[i];
                break;
            }
        }

        return schedule;
    }

    public Schedule crossover(Schedule firstParent, Schedule secondParent){
        Task[] firstParentTasks = firstParent.getTasks();
        Task[] secondParentTasks = secondParent.getTasks();

        int crossoverPoint = crossoverPoint(firstParentTasks.length);


        return null;
    }

    private int crossoverPoint(int numberOfTasks){
        Random random = new Random();
        int point = 0;
        for (int i = 0; i < numberOfTasks; i++){
            if(random.nextDouble() > crossoverRate){
                point = i;
                break;
            }
        }

        return point;
    }

    private List<Task> getTasksBeforeCrossoverPoint(Task[] tasks,int crossoverPoint){
        List<Task> tasksBefore = new ArrayList<>();
        for(int i = 0; i <= crossoverPoint; i++){
            tasksBefore.add(tasks[i]);
        }
        return tasksBefore;
    }

    private List<Task> getTasksAfterCrossoverPoint(Task[] tasks, int crossoverPoint){
        List<Task> tasksAfter = new ArrayList<>();
        for(int i = crossoverPoint; i < tasks.length; i++){
            tasksAfter.add(tasks[i]);
        }
        return  tasksAfter;
    }
}
