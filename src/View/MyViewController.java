package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController implements IView , Initializable, Observer {
    @FXML
    public TextField rowNumber;
    @FXML
    public TextField colNumber;
    @FXML
    public TextField playerRow;
    @FXML
    public TextField playerCol;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();
    public MyViewModel viewModel;
    public MazeDisplayer mazeDisplayer;

    public MyViewController(){
        MyModel model = new MyModel();
        viewModel = new MyViewModel(model);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
    }

    public void generateMaze(ActionEvent actionEvent) {
        int nRow=0;
        int nCol=0;
        try {
            nRow = Integer.parseInt(rowNumber.getText());
            nCol = Integer.parseInt(colNumber.getText());
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Don't mess with me, Insert only numbers.");
            alert.show();
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

    public void solveMaze(ActionEvent actionEvent) {
        viewModel.solveMaze();
    }

    private void mazeSolved() {
        mazeDisplayer.setSolution(viewModel.getSolution());
    }

    public void keyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

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
            int row = mazeDisplayer.getPlayerRow();
            int col = mazeDisplayer.getPlayerCol();
            int rowChar = viewModel.getRowChar();
            int colChar = viewModel.getColChar();

            setUpdatePlayerRow(""+rowChar);
            setUpdatePlayerCol(""+colChar);
            mazeDisplayer.setPlayerPosition(rowChar, colChar);
        }
        else if(action_num == 3) {
            //solution
        }

    }

    public void newGame(ActionEvent actionEvent){
        generateMaze(actionEvent);
    }
    public void saveGame(ActionEvent actionEvent){
       String path = ChooseDirectory();
       viewModel.saveMaze(path, "generic_name.ser");
    }
    public void loadGame(ActionEvent actionEvent){
        String path = openFileManager();
        viewModel.loadMaze(path);
        mazeDisplayer.drawMaze(viewModel.getMaze());
    }
    public void propertiesGame(ActionEvent actionEvent){

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

    public void exitGame(ActionEvent actionEvent){

    }
    private String openFileManager() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");

        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Serialized Objects (*.ser)", "*.ser");
        fileChooser.getExtensionFilters().add(filter);
        Stage temporaryStage = new Stage();
        File selectedFile = fileChooser.showSaveDialog(temporaryStage);
        if(selectedFile != null)
            return selectedFile.getPath();
        return null;
    }
    public String ChooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Destination Folder");
        Stage temporaryStage = new Stage();
        File selectedDirectory = directoryChooser.showDialog(temporaryStage);
        if (selectedDirectory != null)
            return selectedDirectory.getPath();
        return null;
    }



}
