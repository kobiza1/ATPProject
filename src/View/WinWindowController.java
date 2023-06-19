package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WinWindowController{
    StringProperty imageFileNameWin = new SimpleStringProperty();
    public void playAgain(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public String getImageFileNameWin() {
        return imageFileNameWin.get();
    }

    public void setImageFileNameWin(String imageFileNameWin) {
        this.imageFileNameWin.set(imageFileNameWin);
    }
}