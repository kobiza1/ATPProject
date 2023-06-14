package Model;

import Client.Client;
import IO.MyDecompressorInputStream;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import Server.*;
import Client.*;

import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.search.AState;
import algorithms.search.Solution;


import java.io.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class MyModel extends Observable implements IModel{

    private Maze maze;
    private Solution solution;
    private int rowChar;
    private int colChar;
    private Server mazeGeneratorServer;
    private Server mazeSolverServer;

    public MyModel() {
        maze = null;
        rowChar =0;
        colChar =0;
        mazeGeneratorServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        mazeSolverServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());

    }

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
                if(rowChar!=0)
                    rowChar--;
                break;

            case 2: //Down
                  if(rowChar!=maze_board.length - 1)
                        rowChar++;
                break;
            case 3: //Left
                if(colChar!=0)
                    colChar--;
                 break;
            case 4: //Right
                if(colChar!=maze.getMaze_board()[0].length-1)
                    colChar++;
                break;

        }

        setChanged();
        notifyObservers(2);
    }

    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    @Override
    public void assignObserver(Observer o) {
        if(o instanceof MyViewModel)
            this.addObserver(o);
    }

    @Override
    public void solveMaze(int[][] maze) {
        CommunicateWithServer_SolveSearchProblem();
        setChanged();
        notifyObservers(3);
    }

    @Override
    public Solution getSolution() {
        return solution;
    }


    public void generateMaze(int row, int col)
    {
        CommunicateWithServer_MazeGenerating(row, col);
        setChanged();
        notifyObservers(1);
    }

    public int[][] getMaze() {
        return maze.getMaze_board();
    }

    private void CommunicateWithServer_MazeGenerating(int row, int col)  {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {

                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])fromServer.readObject();
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[row*col + 8];
                        is.read(decompressedMaze);
                        maze = new Maze(decompressedMaze);
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }
                }
            });

            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
    }

    private void CommunicateWithServer_SolveSearchProblem() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze);
                        toServer.flush();
                        solution = (Solution) fromServer.readObject();
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
    }
}

