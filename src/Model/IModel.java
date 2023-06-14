package Model;

import algorithms.search.AState;
import algorithms.search.Solution;

import java.util.ArrayList;
import java.util.Observer;

public interface IModel {
    public void generateMaze(int row, int col);
    public int[][] getMaze();
    public void updateCharacterLocation(int direction);
    public int getRowChar();
    public int getColChar();
    public void assignObserver(Observer o);
    public void solveMaze(int [][] maze);
    public ArrayList<AState> getSolution();
}
