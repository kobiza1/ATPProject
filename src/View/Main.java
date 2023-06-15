package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MyView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Icy Tower Maze");
        stage.setScene(scene);
        stage.show();


        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController controller = fxmlLoader.getController();
        controller.setViewModel(viewModel);
        viewModel.assignObserver(controller);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                controller.exitGame();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}