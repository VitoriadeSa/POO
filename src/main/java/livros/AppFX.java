package livros;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Principal.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("G16 - Os meus livros");

        Controller controller = loader.getController();
        stage.setOnCloseRequest(e -> controller.guardar());
        stage.show();
    }
}
