package ve.edu.ucab.presentation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ve.edu.ucab.domain.exceptions.ExistingUserException;
import ve.edu.ucab.domain.model.User;
import ve.edu.ucab.domain.usecase.RegisterInput;
import ve.edu.ucab.domain.usecase.RegisterUserUseCase;
import ve.edu.ucab.infrastructure.repository.UserRepository;

public class SignupController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final RegisterUserUseCase registerUseCase;

    public SignupController() {
        try {
            this.registerUseCase = new RegisterUserUseCase(UserRepository.getInstance());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo inicializar el repositorio.", e);
        }
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        RegisterInput input = new RegisterInput(username, password);

        try {
            User user = registerUseCase.execute(input);
            errorLabel.setText("Usuario registrado: " + user.getUsername());
            handleShowLogin();
        } catch (ExistingUserException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Error inesperado.");
            e.printStackTrace();
        }
    }

  @FXML
  private void handleShowLogin() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentation/view/login.fxml"));
      Parent loginRoot = loader.load();
      Stage stage = (Stage) usernameField.getScene().getWindow();
      Scene loginScene = new Scene(loginRoot);
      stage.setScene(loginScene);
      stage.setResizable(false);
      stage.setTitle("Iniciar Sesión");
    } catch (Exception e) {
      errorLabel.setText("No se pudo cargar la pantalla de login.");
    }
  }
}
