import javafx.scene.paint.Color;

import java.util.Random;

public class Student implements Comparable<Student>{
    double fitness;
    int x;
    int y;

    Color  color;
    double colorTotal;
    public Student(){
        initializeStudent();
    }

    public void initializeStudent(){
        Random rand = new Random();
        int r, g, b;
        /*r = rand.nextInt(256);
        g = rand.nextInt(256);*/
        b = rand.nextInt(256);
        color = color.rgb(0,0,b);

        //colorTotal = r+g+b;
        colorTotal = b;
        fitness = 0;

    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int[] getPosition(){
        int[] pos = new int[2];
        pos[0] = x;
        pos[1] = y;
        return pos;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public double getFitness(){
        return fitness;
    }

    @Override
    public int compareTo(Student s) {
        return Double.compare(s.getFitness(), this.getFitness());
    }
}
