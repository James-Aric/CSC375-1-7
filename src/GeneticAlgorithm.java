import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm{
    Random rand = new Random();
    Classroom classroom;
    final double mutationRate = 0.001;

    public GeneticAlgorithm(Classroom c){
        classroom = c;
    }

    public List<int[]> crossover(Classroom c){
        //creates an index for the crossover between swaps
        int crossover = rand.nextInt(classroom.getStudentPop()/classroom.row);
        //returns a sublist of the inputted classrooms swaps to crossover
        return new ArrayList<int[]>(c.getSwaps());

    }

    //potential for one mutation
    public void mutate(Classroom c){
        for(int i = 0; i < c.getSwaps().size(); i++){
            if(rand.nextDouble() < mutationRate){
                c.getSwaps().set(i, c.roulette());
            }
        }
    }

    //run the full genetic alg once through
    public void swap(){
        try{
            classroom.calcFitness();
            //System.out.println("Room num: " + classroom.roomNum + "  Previous Fitness: " + classroom.fitness);
            classroom.generateSwaps();
            //only works with more than one thread active?
            //classroom.updateCrossover(exchanger.exchange(crossover(classroom)));
            //classroom.updateCrossover(crossover(classroom));
            mutate(classroom);
            classroom.swapStudents();
            classroom.calcFitness();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



}
