import java.util.ArrayList;
import java.util.Random;

public class Classroom {
    Student[][] students;
    int row, col;
    double fitness;
    Random rand;
    ArrayList<Student> studentList;
    ArrayList<int[]> swaps;
    int studentPopulation;
    int roomNum;

    public Classroom(int row, int col){
        this.row = row;
        this.col = col;
        fitness = 0;
        studentPopulation = row*col;
        studentList = new ArrayList<>();
        swaps = new ArrayList<>();
        rand = new Random();

        initializeClassroom();
    }

    public Classroom(Classroom c, int num){
        this.row = c.row;
        this.col = c.col;
        this.studentPopulation = c.studentPopulation;
        this.students = c.cloneStudents();
        studentList = new ArrayList<>();
        setStudentList();
        calcFitness();
        rand = new Random();
        this.swaps = new ArrayList<>();
        roomNum = num;
    }

    public double getFitness(){
        return fitness;
    }

    public void initializeClassroom(){
        students = new Student[row][col];
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                students[i][j] = new Student();
                students[i][j].setPosition(i,j);
            }
        }
        calcFitness();
        setStudentList();
    }

    public void calcFitness(){
        double totalFitness = 0;
        for(int i = 0; i < this.row; i++){
            for(int j = 0; j < this.col; j++){

                ArrayList<Student> neighbors = getAdjacentStudents(i, j);
                double localFitness = 0;
                for(int k = 0; k < neighbors.size(); k++){
                    localFitness += Math.abs(students[i][j].colorTotal - neighbors.get(k).colorTotal);
                }
                students[i][j].fitness = localFitness;
                totalFitness += localFitness;
            }
        }
        fitness = totalFitness;
    }

    //return all neighbors of the inputted coordinates
    public ArrayList<Student> getAdjacentStudents(int r, int c){
        ArrayList<Student> temp = new ArrayList<>();
        int[] colCombos = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] rowCombos = {-1, 1, 0, 1, -1, -1, 0, 1};
        for(int i = 0; i < 8; i++){
            try{
                temp.add(students[r+rowCombos[i]][c+colCombos[i]]);
            }
            catch(ArrayIndexOutOfBoundsException e){

            }
        }
        return temp;
    }

    //make a new copy of the students array
    public Student[][] cloneStudents(){
        Student[][] temp = new Student[row][col];
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                temp[r][c] = students[r][c];
            }
        }
        return temp;
    }

    //picks two random students as swaps
    public synchronized int[] roulette(){
        int[] studentPos = new int[4];
        do{
            int swapIndex = 0;
            for(int i = 0; i < 2; i++){
                double total = 0;
                double slice = rand.nextDouble() * this.fitness;
                for(Student student: studentList){
                    total+= student.getFitness();
                    if(total > slice){
                        studentPos[swapIndex++] = student.getX();
                        studentPos[swapIndex++] = student.getY();
                        break;
                    }
                }
            }
        }while(studentPos[0] == studentPos[2] && studentPos[1] == studentPos[3]);
        return studentPos;
    }

    //clear the old arraylist of students, and then add all the students into the list
    public void setStudentList(){
        studentList.clear();

        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                studentList.add(students[i][j]);
            }
        }
    }

    //generate new swaps equal to the number of students in the room
    public void generateSwaps(){
        swaps.clear();
        //orderStudents();
        //int swapNum = rand.nextInt(studentPopulation);
        for(int i = 0; i < studentPopulation/row; i++){
            swaps.add(roulette());
        }
    }

    public ArrayList<int[]> getSwaps(){
        return swaps;
    }

    /*public void updateCrossover(List<int[]> newSwaps){
        swaps.subList(0, newSwaps.size()).clear();
        swaps.addAll(0, newSwaps);
    }*/

    public synchronized void swapStudents(){
        for(int[] swap: swaps){
            Student temp = students[swap[0]][swap[1]];

            students[swap[0]][swap[1]] = students[swap[2]][swap[3]];
            students[swap[2]][swap[3]] = temp;
        }
        setStudentPos();
    }

    public synchronized void setStudentPos(){
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                students[i][j].setPosition(i,j);
            }
        }
        setStudentList();
    }


    public ArrayList<Student> getStudentList(){
        return studentList;
    }

    public int getStudentPop(){
        return studentPopulation;
    }
}