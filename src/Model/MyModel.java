package Model;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.AState;
import algorithms.search.Solution;

import java.util.ArrayList;
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

    public int getRowChar() {
        return rowChar;
    }
    public int getColChar() {
        return colChar;
    }

//    @Override
//    public void assignObserver(Observer o) {
//        if(o instanceof MyViewModel)
//            this.addObserver(o);
//    }

    public void updateCharacterLocation(int direction)
    {
        /*
            direction = 1 -> Up
            direction = 2 -> Down
            direction = 3 -> Left
            direction = 4 -> Right
         */
        int[][] maze_board = maze.getMaze_board();
        switch(direction)
        {
            case 1: //Up
                if(rowChar!=0 && maze_board[rowChar-1][colChar] == 0)
                    rowChar--;
                break;

            case 2: //Down
                if(rowChar!=maze_board.length - 1 && maze_board[rowChar+1][colChar] == 0)
                    rowChar++;
                break;
            case 3: //Left
                if(colChar!=0 && maze_board[rowChar][colChar-1] == 0)
                    colChar--;
                break;
            case 4: //Right
                if(colChar!=maze.getMaze_board()[0].length-1 && maze_board[rowChar][colChar+1] == 0)
                    colChar++;
                break;

        }

        setChanged();
        notifyObservers(2);
    }


    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze(int[][] maze) {
        setChanged();
        notifyObservers(3);
    }

    @Override
    public ArrayList<AState> getSolution() {
        return null;
    }


    public void generateMaze(int row, int col)
    {
        MyMazeGenerator mg = new MyMazeGenerator();
        this.maze = mg.generate(row, col);
        setChanged();
        notifyObservers(1);
    }

    public int[][] getMaze() {
        return maze.getMaze_board();
    }
}


