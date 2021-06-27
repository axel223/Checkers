import Controller.Control;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Control ctrl = new Control();
        Scene scene = new Scene(ctrl.createContent());
        stage.setTitle("Checkers");
        stage.setScene(scene);
        stage.show();
    }
}

