package ve.edu.ucab.presentation.controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import ve.edu.ucab.domain.model.User;
import ve.edu.ucab.presentation.util.SessionManager;
import ve.edu.ucab.domain.exceptions.InvalidPasswordException;
import ve.edu.ucab.infrastructure.repository.UserRepository;

import java.util.Optional;

public class PerfilController {
    @FXML private Label nombreLabel;
    @FXML private Label passwordLabel;
    @FXML private Label saldoLabel;

    @FXML
    public void initialize() {
        User user = SessionManager.getCurrentUser();
        if (user != null) {
            nombreLabel.setText(user.getUsername());
            passwordLabel.setText("********"); // No mostrar la contraseña real
            saldoLabel.setText(String.valueOf(user.getBalance()));
        }
    }

    @FXML
    private void handleMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentation/view/main.fxml"));
            Parent mainRoot = loader.load();
            Button sourceButton = (Button) event.getSource();
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            Scene mainScene = new Scene(mainRoot);
            stage.setScene(mainScene);
            stage.setResizable(false);
            stage.setTitle("Menú Principal");
        } catch (Exception e) {
            System.err.println("No se pudo cargar la pantalla principal.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChangeName(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null) {
            TextInputDialog dialog = new TextInputDialog(user.getUsername());
            dialog.setTitle("Cambiar nombre");
            dialog.setHeaderText("Cambiar nombre de usuario");
            dialog.setContentText("Nuevo nombre:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newName -> {
                if (!newName.trim().isEmpty()) {
                    user.setUsername(newName.trim());
                    nombreLabel.setText(newName.trim());
                    try {
                        UserRepository.getInstance().updateUser(user);
                    } catch (Exception e) {
                        System.err.println("Error updating the user: " + e.getMessage());
                    }
                }
            });
        }
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null) {
            TextInputDialog oldDialog = new TextInputDialog();
            oldDialog.setTitle("Cambiar contraseña");
            oldDialog.setHeaderText("Cambiar contraseña");
            oldDialog.setContentText("Contraseña actual:");
            Optional<String> oldResult = oldDialog.showAndWait();
            if (oldResult.isPresent()) {
                String oldPassword = oldResult.get();
                TextInputDialog newDialog = new TextInputDialog();
                newDialog.setTitle("Cambiar contraseña");
                newDialog.setHeaderText("Cambiar contraseña");
                newDialog.setContentText("Nueva contraseña:");
                Optional<String> newResult = newDialog.showAndWait();
                if (newResult.isPresent()) {
                    String newPassword = newResult.get();
                    try {
                        user.changePassword(oldPassword, newPassword);
                        UserRepository.getInstance().saveToFile();
                        // Opcional: mostrar mensaje de éxito
                    } catch (InvalidPasswordException e) {
                        // Opcional: mostrar mensaje de error
                    } catch (Exception e) {
                        System.err.println("Error guardando el usuario: " + e.getMessage());
                    }
                }
            }
        }
    }
}
