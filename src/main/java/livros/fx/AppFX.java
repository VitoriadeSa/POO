package livros.fx;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import livros.modelo.Biblioteca;
import livros.persistencia.Persistencia;

/**
 * Aplicação JavaFX: carrega os dados, abre a janela principal (Principal.fxml)
 * e grava os dados quando a janela é fechada.
 */
public class AppFX extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Biblioteca biblioteca;
        try {
            biblioteca = Persistencia.carregarOuCriar(Persistencia.FICHEIRO);
        } catch (IOException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Não foi possível ler " + Persistencia.FICHEIRO + ":\n" + e.getMessage()).showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Principal.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("estilo.css").toExternalForm());

        PrincipalController controller = loader.getController();
        controller.setBiblioteca(biblioteca);

        stage.setTitle("G99 - Os meus livros");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setOnCloseRequest(e -> controller.guardar());
        stage.show();
        controller.mostrarAvisoAtrasos();
    }
}
