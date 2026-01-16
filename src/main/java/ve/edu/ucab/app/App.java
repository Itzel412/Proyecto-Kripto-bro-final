package ve.edu.ucab.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import ve.edu.ucab.infrastructure.repository.UserRepository;
import ve.edu.ucab.infrastructure.exceptions.UserFileNotFoundException;
import ve.edu.ucab.presentation.util.DialogUtil;

public class App extends Application {

  private static UserRepository userRepository;

  public static UserRepository getUserRepository() {
    return userRepository;
  }

  @Override
  public void start(Stage stage) throws Exception {
    System.out.println("Iniciando aplicación...");

    try {
      UserRepository.validateUserFileExists(); // validación explícita
      userRepository = UserRepository.getInstance();
    } catch (UserFileNotFoundException e) {
      DialogUtil.showErrorDialog("Error: " + e.getMessage());
      System.exit(1);
      return;
    } catch (Exception e) {
      DialogUtil.showErrorDialog("Error inesperado: " + e.getMessage());
      System.exit(1);
      return;
    }

    System.out.println("Contenido de los usuarios: " + userRepository.getAllData());

    try {
      System.out.println("Cargando FXML...");
      Parent root = FXMLLoader.load(getClass().getResource("/presentation/view/login.fxml"));
      Scene newScene = new Scene(root);
      newScene.getStylesheets().add(getClass().getResource("/presentation/style/stylesLogin.css").toExternalForm());
      stage.setTitle("Logearse como un pro");
      stage.setScene(newScene);
      stage.show();
      System.out.println("Interfaz mostrada.");
    } catch (Exception e) {
      DialogUtil.showErrorDialog("No se pudo cargar la interfaz gráfica.");
      System.exit(1);
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
