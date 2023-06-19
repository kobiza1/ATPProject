package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class View extends Canvas {
    private int[][] maze = null;
    private int playerRow = 0;
    private int playerCol = 0;
    private ArrayList<Integer> solution = null;
    // wall and player images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameSolution = new SimpleStringProperty();
    StringProperty imageFileNameFriend = new SimpleStringProperty();

    public int[][] get_maze(){
        return this.maze;
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }
    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }
    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }
    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }
    public String getImageFileNameSolution() {
        return imageFileNameSolution.get();
    }
    public void setImageFileNameSolution(String imageFileNameSolution) {
        this.imageFileNameSolution.set(imageFileNameSolution);
    }
    public String getImageFileNameFriend() {
        return imageFileNameFriend.get();
    }
    public void setImageFileNameFriend(String imageFileNameFriend) {
        this.imageFileNameFriend.set(imageFileNameFriend);
    }

    public int getPlayerRow() {
        return playerRow;
    }
    public int getPlayerCol() {
        return playerCol;
    }
    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }

    public void drawMaze(int[][] maze) {
        this.maze = maze;
        setPlayerPosition(0, 0);
        solution =null;
    }

    private void draw() {
        if(maze != null){
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.length;
            int cols = maze[0].length;

            double cellHeight = canvasHeight / (rows);
            double cellWidth = canvasWidth / (cols);

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            drawPlayer(graphicsContext, cellHeight, cellWidth);
            if(solution != null){
                drawSolution(graphicsContext, cellHeight, cellWidth);
            }
            drawFriend(graphicsContext, cellHeight, cellWidth);
        }
    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        if(maze != null){
            Image wallImage = null;
            try{
                wallImage = new Image(new FileInputStream(getImageFileNameWall()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no wall image file");
            }

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if(maze[i][j] == 1){
                        //if it is a wall:
                        double x = j * cellWidth;
                        double y = i * cellHeight;
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                    }
                }
            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }

    public void drawSolution(GraphicsContext graphicsContext, double cell_height, double cell_width){
        Image SolutionImage = null;
        try{
            SolutionImage = new Image(new FileInputStream(getImageFileNameSolution()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image file");
        }
        double x,y;
        for(int i=0; i<solution.size(); i+=2){
            x = solution.get(i+1)*cell_width;
            y = solution.get(i)*cell_height;
            if(playerCol == solution.get(i+1) && playerRow == solution.get(i)){
                continue;
            }
            graphicsContext.drawImage(SolutionImage, x, y, cell_width, cell_height);
        }
    }

    public void setSolution(ArrayList<Integer> solution) {
        this.solution = solution;
        draw();
    }

    private void drawFriend(GraphicsContext graphicsContext, double cell_height, double cell_width){
        int rows = maze.length;
        int cols = maze[0].length;
        Image friendImage = null;
        try {
            friendImage = new Image(new FileInputStream(getImageFileNameFriend()));
            graphicsContext.drawImage(friendImage, (rows-1) * cell_height, (cols-1) * cell_width, cell_width, cell_height);
        } catch (FileNotFoundException e) {
            System.out.println("There is no friend image file");
        }
    }
}
