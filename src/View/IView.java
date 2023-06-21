package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public interface IView {
    void setViewModel(MyViewModel viewModel);
    String getUpdatePlayerRow();
    String getUpdatePlayerCol();
    void setUpdatePlayerRow(String updatePlayerRow);
    void setUpdatePlayerCol(String updatePlayerCol);
    void generateMaze(ActionEvent actionEvent);
    void solveMaze(ActionEvent actionEvent);
    void keyPressed(KeyEvent keyEvent);
    void mouseClicked(MouseEvent mouseEvent);
    void setOnScroll(ScrollEvent scrollEvent);
    void mouseDragged(MouseEvent mouseEvent);
    void openWinWindow();
    void newGame(ActionEvent actionEvent);
    void saveGame(ActionEvent actionEvent);
    void loadGame(ActionEvent actionEvent);
    void propertiesGame(ActionEvent actionEvent);
    void aboutGame(ActionEvent actionEvent);
    void helpGame(ActionEvent actionEvent);
    void exitGame();
}
