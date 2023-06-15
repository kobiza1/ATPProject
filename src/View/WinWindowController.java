package View;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

//import static View.MyViewController.restart;

public class WinWindowController {
    public void playAgain(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
//        restart();
    }

}
