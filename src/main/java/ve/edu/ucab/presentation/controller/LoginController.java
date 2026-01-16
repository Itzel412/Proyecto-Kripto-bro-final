package ve.edu.ucab.presentation.controller;

// JavaFX imports for UI components
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

// Domain model, usecase, and exception imports
import javafx.stage.Stage;
import ve.edu.ucab.domain.exceptions.InvalidPasswordException;
import ve.edu.ucab.domain.exceptions.UserNotFoundException;
import ve.edu.ucab.domain.model.User;
import ve.edu.ucab.domain.usecase.LoginInput;
import ve.edu.ucab.domain.usecase.LoginUseCase;
import ve.edu.ucab.infrastructure.repository.UserRepository;

import java.io.IOException;

/**
 * A controller class for handling user login functionality in a JavaFX application.
 * Manages the login form, processes user input, and interacts with the {@link LoginUseCase}
 * to authenticate users.
 *
 * @author badjavii
 * @since 2025-07-01
 */
public class LoginController {

  /**
   * The text field for entering the username.
   */
  @FXML
  private TextField usernameField;

  /**
   * The password field for entering the password.
   */
  @FXML
  private PasswordField passwordField;

  /**
   * The label for displaying error messages.
   */
  @FXML
  private Label errorLabel;

  /**
   * The button for initiating the login action.
   */
  @FXML
  private Button loginButton;

  @FXML
  private Hyperlink register;

  /**
   * The use case responsible for handling login logic.
   */
  private final LoginUseCase loginUseCase;

  /**
   * Constructs a new LoginController, initializing the {@link LoginUseCase}
   * with a {@link UserRepository} instance.
   *
   * @throws RuntimeException if the user repository cannot be initialized
   */
  public LoginController() {
    try {
      this.loginUseCase = new LoginUseCase(UserRepository.getInstance());
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialize user repository.", e);
    }
  }

  /**
   * Handles the login action triggered by the login button.
   * Retrieves the username and password from the input fields, creates a {@link LoginInput},
   * and uses the {@link LoginUseCase} to authenticate the user. Displays error messages
   * if authentication fails, or logs a successful login.
   */
  @FXML
  private void handleLogin() {
    String username = usernameField.getText();
    String password = passwordField.getText();

    LoginInput input = new LoginInput(username, password);

    try {
      User user = loginUseCase.execute(input);
      System.out.println("[DEBUG] Usuario autenticado: " + user.getUsername() + ", saldo: " + user.getBalance());
      ve.edu.ucab.presentation.util.SessionManager.setCurrentUser(user);
      errorLabel.setText(""); // Clear any previous error messages
      System.out.println("Successful login: " + user.getUsername());
      // Redirigir a la vista principal (main.fxml)
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentation/view/main.fxml"));
      Parent mainRoot = loader.load();
      Stage stage = (Stage) loginButton.getScene().getWindow();
      Scene mainScene = new Scene(mainRoot);
      stage.setScene(mainScene);
      stage.setResizable(false);
      stage.setTitle("Panel Principal");
    } catch (UserNotFoundException | InvalidPasswordException e) {
      errorLabel.setText(e.getMessage());
    } catch (Exception e) {
      errorLabel.setText("Unexpected error.");
      e.printStackTrace();
    }
  }

  @FXML
  private void handleRegister() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentation/view/signup.fxml"));
      Parent signUpRoot = loader.load();
      Stage stage = (Stage) register.getScene().getWindow();
      Scene signUpScene = new Scene(signUpRoot);
      stage.setScene(signUpScene);
      stage.setResizable(false);
      stage.setTitle("Registro");
    } catch (Exception e) {
      errorLabel.setText("No se pudo cargar la pantalla de registro.");
      e.printStackTrace();
    }
  }


}