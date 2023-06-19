package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.scene.control.TextInputDialog;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.util.Duration;


public class MyViewController implements IView , Initializable, Observer {
    @FXML
    public TextField rowNumber;
    @FXML
    public TextField colNumber;
    @FXML
    public Label playerRow;
    @FXML
    public Label playerCol;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();
    public MyViewModel viewModel;
    @FXML
    public MazeDisplayer mazeDisplayer;
    private static MediaPlayer player;
    public Media mazeSong = new Media(getClass().getResource("../music/mainWindow.mp3").toExternalForm());
    public Media winSong = new Media(getClass().getResource("../music/winWindow.mp3").toExternalForm());

    public MyViewController(){
        MyModel model = new MyModel();
        viewModel = new MyViewModel(model);
        mazeDisplayer = new MazeDisplayer();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
        setMusic(mazeSong);
    }

    public static void setMusic(Media song){
        /*set the mediaPlayer with the media, and call playMusic*/
        if(player!=null)
            player.pause();
        player = new MediaPlayer(song);
        playMusic();
    }

    public static void playMusic(){
        /*set the properties of the mediaPlayer*/
        player.setAutoPlay(true);
        player.setVolume(0.2);
        player.setOnEndOfMedia(new Runnable() { //repeat the music
            public void run() {
                player.seek(Duration.ZERO);
            }
        });
    }

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerRow(String updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow);
    }

    public void setUpdatePlayerCol(String updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol);
    }

    public void generate(){
        int nRow=0;
        int nCol=0;
        try {
            nRow = Integer.parseInt(rowNumber.getText());
            nCol = Integer.parseInt(colNumber.getText());
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Don't mess with me, Insert only numbers.");
            alert.show();
            return;
        }

        if( nRow<0 || nCol<0 ){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Don't mess with me, Insert correct size.");
            alert.show();
        } else {
            viewModel.generateMaze(nRow, nCol);
            mazeDisplayer.drawMaze(viewModel.getMaze());
            setUpdatePlayerRow("" + mazeDisplayer.getPlayerRow());
            setUpdatePlayerCol("" + mazeDisplayer.getPlayerCol());
        }
    }
    public void generateMaze(ActionEvent actionEvent) {
        generate();
    }

    public void solveMaze(ActionEvent actionEvent) {
        viewModel.solveMaze();
    }

    public void keyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

    private void mazeSolved() {
        mazeDisplayer.setSolution(viewModel.getSolution());
    }

    @FXML
    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        int action_num = 0;
        String str_num = arg.toString();
        action_num = Integer.parseInt(str_num);

        if(action_num == 1) {
            mazeDisplayer.drawMaze(viewModel.getMaze());
        }
        else if (action_num == 2){
            int rowChar = viewModel.getRowChar();
            int colChar = viewModel.getColChar();

            setUpdatePlayerRow(""+rowChar);
            setUpdatePlayerCol(""+colChar);
            mazeDisplayer.setPlayerPosition(rowChar, colChar);
        }
        else if(action_num == 3) {
            mazeSolved();
        }
        else if (action_num == 5){
            mazeDisplayer.drawMaze(viewModel.getMaze());
        }
        else if (action_num == 11){
            show_error("Wrong type of file");
        } else if (action_num == 10) {
            openWinWindow();
        }
    }

    public void openWinWindow(){
        try {
            setMusic(winSong);
            Stage newWindow = new Stage();
            FXMLLoader newWindowLoader = new FXMLLoader(getClass().getResource("WinWindow.fxml"));
            Parent root = newWindowLoader.load();
            Scene newWindowScene = new Scene(root);
            newWindow.setTitle("Maze Solved!");
            newWindow.setScene(newWindowScene);
            newWindow.initModality(Modality.NONE);

            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.initOwner(mazeDisplayer.getScene().getWindow());
            newWindow.showAndWait();
            generate();
            setMusic(mazeSong);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void newGame(ActionEvent actionEvent){
        generateMaze(actionEvent);
    }

    public void saveGame(ActionEvent actionEvent){
        if(mazeDisplayer.get_maze() == null){
            show_error("You need to start playing first");
            return;
        }
        String path = ChooseDirectory();
        String name = "";
        if(path != null){
           name = get_name_from_the_user();
        }
        viewModel.saveMaze(path, name);
    }

    private String get_name_from_the_user() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter File Name");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter a file name:");
        String fileName = "";
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
             fileName = result.get();
        }
        return fileName;
    }

    public void loadGame(ActionEvent actionEvent){
        String path = openFileManager();
        viewModel.loadMaze(path);
    }

    public void propertiesGame(ActionEvent actionEvent){
        try {
            Stage newWindow = new Stage();
            FXMLLoader newWindowLoader = new FXMLLoader(getClass().getResource("Properties.fxml"));
            Parent root = newWindowLoader.load();
            Scene newWindowScene = new Scene(root);
            newWindow.setTitle("Properties");
            newWindow.setScene(newWindowScene);
            newWindow.initModality(Modality.NONE);
            newWindow.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void aboutGame(ActionEvent actionEvent) {
        try {
            Stage newWindow = new Stage();
            FXMLLoader newWindowLoader = new FXMLLoader(getClass().getResource("AboutWindow.fxml"));
            Parent root = newWindowLoader.load();
            Scene newWindowScene = new Scene(root);
            newWindow.setTitle("About");
            newWindow.setScene(newWindowScene);
            newWindow.initModality(Modality.NONE);
            newWindow.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void helpGame(ActionEvent actionEvent){
        try {
            Stage newWindow = new Stage();
            FXMLLoader newWindowLoader = new FXMLLoader(getClass().getResource("HelpWindow.fxml"));
            Parent root = newWindowLoader.load();
            Scene newWindowScene = new Scene(root);
            newWindow.setTitle("Help");
            newWindow.setScene(newWindowScene);
            newWindow.initModality(Modality.NONE);
            newWindow.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void exitGame(){
        viewModel.exit_game();
    }

    private String openFileManager() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        Stage temporaryStage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(temporaryStage);

        if (selectedFile != null)
            return selectedFile.getAbsolutePath();

        return null;
    }

    private String ChooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Destination Folder");
        Stage temp_stage = new Stage();
        File selectedDirectory = directoryChooser.showDialog(temp_stage);

        if (selectedDirectory != null) {
            return selectedDirectory.getAbsolutePath() + File.separator;
        }
        return null;
    }


    private void show_error(String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(content);
        alert.show();
    }

    public void setOnScroll(ScrollEvent scrollEvent) {
        if (scrollEvent.isControlDown()) {
            double zoom_fac = 1.05;
            if (scrollEvent.getDeltaY() < 0) {
                zoom_fac = -0.05;
            }
            double mouseX = scrollEvent.getX();
            double mouseY = scrollEvent.getY();
            Point2D mouseLocal = mazeDisplayer.sceneToLocal(mouseX, mouseY);
            Scale newScale = new Scale();
            newScale.setX(mazeDisplayer.getScaleX() + zoom_fac);
            newScale.setY(mazeDisplayer.getScaleY() + zoom_fac);
            newScale.setPivotX(mouseLocal.getX());
            newScale.setPivotY(mouseLocal.getY());
            double translateY=0.0, translateX=0.0;
            if(zoom_fac > 0){
                translateX = mazeDisplayer.getTranslateX() - (mouseLocal.getX() * (zoom_fac - 1));
                translateY = mazeDisplayer.getTranslateY() - (mouseLocal.getY() * (zoom_fac - 1));}
            if(zoom_fac < 0){
                translateX = mazeDisplayer.getTranslateX() - (mouseLocal.getX() * (zoom_fac));
                translateY = mazeDisplayer.getTranslateY() - (mouseLocal.getY() * (zoom_fac));}
            mazeDisplayer.getTransforms().addAll(newScale);
            mazeDisplayer.setTranslateX(translateX);
            mazeDisplayer.setTranslateY(translateY);
       }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if(viewModel.getMaze() != null) {
            int maximumSize = Math.max(viewModel.getMaze()[0].length, viewModel.getMaze().length);
            double mousePosX=helperMouseDragged(maximumSize,mazeDisplayer.getHeight(),
                    viewModel.getMaze().length,mouseEvent.getX(),mazeDisplayer.getWidth() / maximumSize);
            double mousePosY=helperMouseDragged(maximumSize,mazeDisplayer.getWidth(),
                    viewModel.getMaze()[0].length,mouseEvent.getY(),mazeDisplayer.getHeight() / maximumSize);
            KeyCode keyCode;
            KeyEvent keyEvent;
            if ( mousePosX == viewModel.getColChar() && mousePosY < viewModel.getRowChar() ){
                keyCode = KeyCode.NUMPAD8;
                keyEvent = new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "", keyCode, false, false, false, false);
                viewModel.moveCharacter(keyEvent);
            }
            else if (mousePosY == viewModel.getRowChar() && mousePosX > viewModel.getColChar() ) {
                keyCode = KeyCode.NUMPAD6;
                keyEvent = new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "", keyCode, false, false, false, false);
                viewModel.moveCharacter(keyEvent);
            }
            else if ( mousePosY == viewModel.getRowChar() && mousePosX < viewModel.getColChar() ) {
                keyCode = KeyCode.NUMPAD4;
                keyEvent = new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "", keyCode, false, false, false, false);
                viewModel.moveCharacter(keyEvent);
            }
            else if (mousePosX == viewModel.getColChar() && mousePosY > viewModel.getRowChar()  ) {
                keyCode = KeyCode.NUMPAD2;
                keyEvent = new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "", keyCode, false, false, false, false);
                viewModel.moveCharacter(keyEvent);
            }

        }
    }

    private  double helperMouseDragged(int maxsize, double canvasSize, int mazeSize,double mouseEvent,double temp){
        double cellSize=canvasSize/maxsize;
        double start = (canvasSize / 2 - (cellSize * mazeSize / 2)) / cellSize;
        double mouse = (int) ((mouseEvent) / (temp) - start);
        return mouse;
    }
}