package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.util.Observable;
import java.util.Observer;


public class MyModel extends Observable implements IModel{

    private Maze maze;
    private int rowChar;
    private int colChar;

    public MyModel() {
        maze = null;
        rowChar =0;
        colChar =0;

    }

    public void updateCharacterLocation(int direction)
    {
        /*
            direction = 1 -> Up
            direction = 2 -> Down
            direction = 3 -> Left
            direction = 4 -> Right
         */

        switch(direction)
        {
            case 1: //Up
                //if(rowChar!=0)
                rowChar--;
                break;

            case 2: //Down
                //  if(rowChar!=maze.length-1)
                rowChar++;
                break;
            case 3: //Left
                //  if(colChar!=0)
                colChar--;
                break;
            case 4: //Right
                //  if(colChar!=maze[0].length-1)
                colChar++;
                break;

        }

        setChanged();
        notifyObservers();
    }

    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze(int[][] maze) {
        //Solving maze
        setChanged();
        notifyObservers();
    }

    @Override
    public void getSolution() {
        //return this.solution;
    }


    public void generateMaze(int row, int col)
    {
        MyMazeGenerator mg = new MyMazeGenerator();
        this.maze = mg.generate(row, col);
        setChanged();
        notifyObservers();
    }

    public int[][] getMaze() {
        return maze.getMaze_board();
    }
}

