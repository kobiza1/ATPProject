package View;


import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.stage.Stage;

import static View.MyViewController.setMusic;

public class WinWindowController{
    public void playAgain(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        Media mazeSong = new Media(getClass().getResource("../music/mainWindow.mp3").toExternalForm());
        setMusic(mazeSong);
    }
}
