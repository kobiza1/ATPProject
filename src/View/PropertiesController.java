package View;

import Server.Configurations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {
    Configurations con;
    @FXML
    public TextField thread;
    @FXML
    public ComboBox<String> searching;
    @FXML
    public ComboBox<String> generator;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        generator.getItems().addAll("MyMazeGenerator","SimpleMazeGenerator","EmptyMazeGenerator");
        searching.getItems().addAll("BestFirstSearch","DepthFirstSearch" ,"BreadthFirstSearch");

        try{
            Properties properties = new Properties();
            properties.load(new FileInputStream("resources/config.properties"));
            con = Configurations.getInstance();

            int threadPoolSize = Integer.parseInt(properties.getProperty("threadPoolSize"));
            con.setThreadPoolSize(threadPoolSize);

            String generatorStr = properties.getProperty("MyMazeGenerator");
            con.setMazeAlgorithm(generatorStr);

            if(generatorStr.equals("MyMazeGenerator")){
                generator.setValue("MyMazeGenerator");
            }
            else if(generatorStr.equals("SimpleMazeGenerator")) {
                generator.setValue("SimpleMazeGenerator");
            }
            else if(generatorStr.equals("EmptyMazeGenerator")){
                generator.setValue("EmptyMazeGenerator");
            }

            String searchingStr = properties.getProperty("mazeSearchingAlgorithm");
            con.setSearchingAlgorithm(searching.getValue());

            if(searchingStr.equals("BreadthFirstSearch"))
                searching.setValue("BreadthFirstSearch");
            else if(searchingStr.equals("DepthFirstSearch"))
                searching.setValue("DepthFirstSearch");
            else if(searchingStr.equals("BestFirstSearch"))
                searching.setValue("BestFirstSearch");
        }
        catch (Exception e){}
    }

    public void Submit(ActionEvent actionEvent){

        try {
            con.setThreadPoolSize(Integer.parseInt(thread.getText()));
            con.setMazeAlgorithm(generator.getValue());
            con.setSearchingAlgorithm(searching.getValue());
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Don't mess with me, Insert Again Number of Threads.");
            alert.show();
        }

        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
