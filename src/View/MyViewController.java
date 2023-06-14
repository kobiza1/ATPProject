package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

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
        viewModel.generateMaze(Integer.parseInt(rowNumber.getText()),Integer.parseInt(colNumber.getText()));
        mazeDisplayer.drawMaze(viewModel.getMaze());
        setUpdatePlayerRow("" + mazeDisplayer.getPlayerRow());
        setUpdatePlayerCol("" + mazeDisplayer.getPlayerCol());
    }

    public void solveMaze(ActionEvent actionEvent) {
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
}
