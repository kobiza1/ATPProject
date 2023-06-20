package Model;

import Client.Client;
import IO.MyDecompressorInputStream;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import Server.*;
import Client.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.search.AState;
import algorithms.search.Solution;
import org.apache.logging.log4j.core.config.Configurator;


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
    private boolean serversAreUp;
    private static Logger logger;

    public MyModel() {
        logger = LogManager.getLogger("Maze logger");
        Configurator.setRootLevel(Level.DEBUG);
        maze = null;
        rowChar =0;
        colChar =0;
        serversAreUp = false;
        mazeGeneratorServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        mazeSolverServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
    }

    public void startServers() {
        serversAreUp = true;
        mazeGeneratorServer.start();
        mazeSolverServer.start();
        logger.info(mazeGeneratorServer +  " is up");
        logger.info(mazeSolverServer +  " is up");

    }

    public void stopServers() {
        if(serversAreUp){
            mazeGeneratorServer.stop();
            mazeSolverServer.stop();
            logger.info(mazeGeneratorServer +  " is down");
            logger.info(mazeSolverServer +  " is down");
        }
    }
    public void updateCharacterLocation(int direction)
    {
        /*
            direction = 1 -> Up
            direction = 2 -> Down
            direction = 3 -> Right
            direction = 4 -> Left
            direction = 5 -> Up - Right
            direction = 6 -> Up - Left
            direction = 7 -> Down - Right
            direction = 8 -> Down - Left
         */
        if(maze != null){
            int[][] maze_board = maze.getMaze_board();
            boolean right_conditions = colChar!=maze.getMaze_board()[0].length-1 && maze_board[rowChar][colChar+1] == 0;
            boolean left_conditions = colChar!=0 && maze_board[rowChar][colChar-1] == 0;
            boolean up_conditions = rowChar!=0 && maze_board[rowChar-1][colChar] == 0;
            boolean down_conditions = rowChar!=maze_board.length - 1 && maze_board[rowChar+1][colChar] == 0;
            boolean up_right_conditions = (up_conditions || right_conditions) && colChar!=maze.getMaze_board()[0].length-1 && rowChar!=0 && maze_board[rowChar-1][colChar+1] == 0;
            boolean up_left_conditions = (up_conditions || left_conditions) && rowChar!=0 && colChar!=0 && maze_board[rowChar-1][colChar-1] == 0;
            boolean down_right_conditions = (down_conditions || right_conditions) && rowChar!=maze_board.length - 1 && colChar!=maze.getMaze_board()[0].length-1 && maze_board[rowChar+1][colChar+1] == 0 ;
            boolean down_left_conditions = (down_conditions || left_conditions) && rowChar!=maze_board.length - 1 && colChar!=0 && maze_board[rowChar+1][colChar-1] == 0;

            switch(direction)
            {
                case 1: // Up
                    if(up_conditions)
                        rowChar--;
                    break;

                case 2: // Down
                      if(down_conditions)
                            rowChar++;
                    break;
                case 3: // Right
                    if(right_conditions)
                        colChar++;
                     break;
                case 4: // Left
                    if(left_conditions)
                        colChar--;
                    break;
                case 5: // Up - Right
                    if(up_right_conditions){
                        colChar++;
                        rowChar--;
                    }
                    break;
                case 6: // Up - Left
                    if(up_left_conditions){
                        colChar--;
                        rowChar--;
                    }
                    break;
                case 7: // Down - Right
                    if(down_right_conditions){
                        colChar++;
                        rowChar++;
                    }
                    break;
                case 8: // Down - Left
                    if(down_left_conditions){
                        colChar--;
                        rowChar++;
                    }
                    break;
            }
            setChanged();
            notifyObservers(2);

            if(colChar == maze_board[0].length -1 && rowChar == maze_board.length -1){
                logger.info("The player finished the maze");
                setChanged();
                notifyObservers(10); // maze is done!!
            }
        }
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
        if(maze != null)
        {
            if(!serversAreUp)
                startServers();
            CommunicateWithServer_SolveSearchProblem();
            logger.info("The player asked for help");
            setChanged();
            notifyObservers(3);
        }
    }

    @Override
    public ArrayList<AState> getSolution() {
        return solution.getSolutionPath();
    }


    public void generateMaze(int row, int col)
    {
        if(!serversAreUp)
            startServers();
        CommunicateWithServer_MazeGenerating(row, col);
        rowChar =0;
        colChar =0;
        setChanged();
        notifyObservers(1);
    }

    public int[][] getMaze() {
        if(maze != null)
            return maze.getMaze_board();
        return null;
    }

    public void save_maze(String Path, String name){
        try {
            File MazeFile = new File(Path);
            File newFile_maze = new File(MazeFile, name);
            FileOutputStream fileOutput_maze = new FileOutputStream(newFile_maze.getPath());
            ObjectOutputStream objectOutput_maze = new ObjectOutputStream(fileOutput_maze);
            objectOutput_maze.writeObject(maze);
            logger.info("The maze has been saved in the computer in: " + Path + name);
            setChanged();
            notifyObservers(4);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void load_maze(String Path){
        try{
        File file = new File(Path);
        FileInputStream fileInput = new FileInputStream(file.getPath());
        ObjectInputStream objectInput = new ObjectInputStream(fileInput);
        Object retrievedObject = objectInput.readObject();
        if(retrievedObject instanceof Maze){
            maze = (Maze)retrievedObject;
            logger.info("The maze has been loaded from the computer.");
            setChanged();
            notifyObservers(5);
        }
    } catch (IOException e) {
            setChanged();
            notifyObservers(11);
    } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void exit(){
        if(serversAreUp){
            stopServers();
        }
        logger.info("The program shut down.");
        System.exit(0);
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            client.communicateWithServer();
            logger.info(client + " is connected to the maze generator server and generate maze in size: [" + row + "," + col + "] with start point at [0,0] and end point in [" + (row-1) + "," + (col-1) + "]");
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
            logger.info(client + " is connected to the solve search problem server and solve the maze: " + solution.getSolutionPath());
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
    }
}

