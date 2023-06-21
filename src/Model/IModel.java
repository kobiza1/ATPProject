package Model;

import algorithms.search.AState;

import java.util.ArrayList;
import java.util.Observer;

public interface IModel {
    void generateMaze(int row, int col);
    int[][] getMaze();
    void updateCharacterLocation(int direction);
    int getRowChar();
    int getColChar();
    void assignObserver(Observer o);
    void solveMaze(int [][] maze);
    void save_maze(String path, String name);
    void load_maze(String path);
    ArrayList<AState> getSolution();
    void exit();
}
