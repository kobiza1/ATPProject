package ViewModel;

import Model.IModel;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class MyViewModel extends Observable implements Observer {

    private IModel model;

    private ArrayList<AState> solution;
    private int [][] maze;
    private int rowChar;
    private int colChar;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
        this.maze = null;
    }


    public int[][] getMaze() {
        return maze;
    }


    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    @Override
    public void update(Observable o, Object arg) {
        /*
        1 --> generate maze
        2 --> move player
        3 --> give the user solution
        4 --> save the maze into file
        5 --> load the  maze from path
        .
        .
        .
       10 --> user finished the maze
         */
        int action_num = 0;
        String str_num = arg.toString();
        action_num = Integer.parseInt(str_num);

        if(action_num == 1) {
            this.maze = model.getMaze();
        }
        else if (action_num == 2){
            rowChar = model.getRowChar();
            colChar = model.getColChar();
        }
        else if(action_num == 3)
            solution = model.getSolution();

        setChanged();
        notifyObservers(action_num);
    }

    public void generateMaze(int row,int col)
    {
        model.generateMaze(row,col);
    }

    public void moveCharacter(KeyEvent keyEvent)
    {
        int direction = -1;

        switch (keyEvent.getCode()){
            case NUMPAD8:
                direction = 1;
                break;
            case NUMPAD2:
                direction = 2;
                break;
            case NUMPAD6:
                direction = 3;
                break;
            case NUMPAD4:
                direction = 4;
                break;
            case NUMPAD9:
                direction = 5;
                break;
            case NUMPAD7:
                direction = 6;
                break;
            case NUMPAD3:
                direction = 7;
                break;
            case NUMPAD1:
                direction = 8;
                break;
        }

        model.updateCharacterLocation(direction);
    }

    public void solveMaze(int [][] maze)
    {
        model.solveMaze(maze);
    }

    public ArrayList<AState> getSolution()
    {
        return model.getSolution();
    }
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }
}


