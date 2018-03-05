import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Main extends Application{
    //controls the total number of rooms, permits, and threads active.
    final static int totalNum = 4;
    //number of iterations all the threads will go through
    final static int iterations = 10000;
    //Number of Rows and Columns for the rooms
    static int rowNum = 6;
    static int colNum = 6;
    static int counter = 0;

    //UI Elements
    GridPane grid;
    Scene scene;

    //starting room
    static Classroom startRoom = new Classroom(rowNum, colNum);
    //the best room from previous generations
    static Classroom previousBest =  new Classroom(startRoom, -1);

    public static void main(String[]args){
        launch(args);
    }

    public void start(Stage primaryStage) throws InterruptedException {
        //GUI stuff
        grid = new GridPane();
        scene = new Scene(grid, colNum * 75, rowNum * 75);
        grid.setVgap(5);
        grid.setHgap(5);

        //print out original fitness
        System.out.println("Original fitness: " + startRoom.fitness);

        //array to keep track of rooms
        Classroom[] roomList = new Classroom[totalNum];
        GeneticAlgorithm[] gaList = new GeneticAlgorithm[totalNum];
        //instantiate all rooms
        for (int i = 0; i < totalNum; i++) {
            roomList[i] = new Classroom(startRoom, i);
        }
        loadGAList(roomList, gaList);

        primaryStage.setScene(scene);
        primaryStage.show();
        updateGUI(primaryStage, previousBest);


        for (int i = 0; i < iterations; i++) {
            for(int j = 0; j < totalNum; j++){
                gaList[j].swap();
            }

            Classroom temp = new Classroom(bestRoom(roomList), -1);

            if(temp.getFitness() < previousBest.getFitness()){
                loadRooms(roomList, temp);
                loadGAList(roomList, gaList);
                previousBest = new Classroom(temp, -1);
                updateGUI(primaryStage, previousBest);
                System.out.println("test");
            }
            else{
                loadRooms(roomList, previousBest);
                loadGAList(roomList, gaList);
            }
        }
        System.out.println("Final fitness: " + previousBest.getFitness());
    }
    public void updateGUI(Stage p, Classroom c) throws InterruptedException{
        //seperate the colors
        //Platform.runLater(() -> {
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                Paint paint = c.students[row][col].color;
                Rectangle rec = new Rectangle(60, 60, paint);
                GridPane.setRowIndex(rec, row);
                GridPane.setColumnIndex(rec, col);

                grid.getChildren().addAll(rec);
            }
        }
        p.setTitle("Grid Random");
        p.setScene(scene);
        //});
    }

    public void loadGAList(Classroom[] rooms, GeneticAlgorithm[] gaList){
        for(int i = 0; i < totalNum; i++) {
            gaList[i] = new GeneticAlgorithm(rooms[i]);
        }
    }

    public static synchronized Classroom bestRoom(Classroom[] cRooms){
        int bestIndex = 0;
        for(int i = 0; i < cRooms.length; i++){
            if(cRooms[bestIndex].getFitness() > cRooms[i].getFitness()){
                bestIndex = i;
            }
        }
        return cRooms[bestIndex];
    }

    public void loadRooms(Classroom[] rooms, Classroom best){
        for(int i = 0; i < totalNum; i++){
            rooms[i] = new Classroom(best, i);
        }
    }
}
