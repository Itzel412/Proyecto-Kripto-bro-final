package ve.edu.ucab.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ve.edu.ucab.domain.model.User;
import ve.edu.ucab.presentation.util.SessionManager;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import ve.edu.ucab.infrastructure.repository.UserRepository;

public class FondearController {
  @FXML
  private TextField montoTextField;
  @FXML
  private Button confirmarButton;
  @FXML
  private Label mensajeLabel;
  @FXML
  private Button volverButton;

  @FXML
  private void handleConfirmarFondeo() {
    String montoStr = montoTextField.getText();
    try {
      double monto = Double.parseDouble(montoStr);
      if (monto <= 0) {
        mensajeLabel.setText("El monto debe ser mayor a 0.");
        return;
      }
      // Obtener usuario actual
      User usuario = SessionManager.getCurrentUser();
      if (usuario == null) {
        mensajeLabel.setText("No hay usuario en sesión.");
        return;
      }
      usuario.increaseBalance(monto);
      try {
        UserRepository.getInstance().updateUser(usuario);
      } catch (Exception e) {
        mensajeLabel.setText("Error al guardar el saldo en el archivo.");
        e.printStackTrace();
        return;
      }
      mensajeLabel.setText("Se han agregado " + monto + " USD a su cuenta.");
      montoTextField.clear();
      // Intentar actualizar el saldo en la pantalla principal si el label es
      // accesible
      javafx.scene.Scene scene = mensajeLabel.getScene();
      if (scene != null) {
        javafx.scene.control.Label saldoLabel = (javafx.scene.control.Label) scene.lookup("#cardBalanceLabel");
        if (saldoLabel != null) {
          saldoLabel.setText(String.format("%.2f", usuario.getBalance()));
        }
      }
    } catch (NumberFormatException e) {
      mensajeLabel.setText("Ingrese un monto válido.");
    }
  }

  @FXML
  private void handleVolver(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentation/view/main.fxml"));
      Parent mainRoot = loader.load();
      // Obtener el stage actual
      Node source = (Node) event.getSource();
      javafx.stage.Stage stage = (javafx.stage.Stage) source.getScene().getWindow();
      javafx.scene.Scene mainScene = new javafx.scene.Scene(mainRoot);
      stage.setScene(mainScene);
      stage.setResizable(false);
      stage.setTitle("Menú Principal");
    } catch (Exception e) {
      System.err.println("No se pudo volver al menú principal.");
      e.printStackTrace();
    }
  }
}