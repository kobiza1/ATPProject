package ViewModel;

import Model.IModel;
import algorithms.search.AState;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private int [][] maze;
    private int rowChar;
    private int colChar;
    private ArrayList<AState> solution;

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

    public void update(Observable o, Object arg) {
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
        this.model.generateMaze(row,col);
    }

    public void moveCharacter(KeyEvent keyEvent)
    {
        int direction = -1;

        switch (keyEvent.getCode()){
            case UP:
                direction = 1;
                break;
            case DOWN:
                direction = 2;
                break;
            case LEFT:
                direction = 3;
                break;
            case RIGHT:
                direction = 4;
                break;
        }

        model.updateCharacterLocation(direction);
    }

    public void solveMaze(int [][] maze)
    {
        model.solveMaze(maze);
    }

    public void getSolution()
    {
        model.getSolution();
    }
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }
}


