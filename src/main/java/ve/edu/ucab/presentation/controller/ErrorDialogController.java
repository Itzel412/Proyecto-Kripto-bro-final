package ve.edu.ucab.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorDialogController {

  @FXML
  private Label messageLabel;

  private Stage dialogStage;

  public void setDialogStage(Stage stage) {
    this.dialogStage = stage;
  }

  public void setMessage(String message) {
    messageLabel.setText(message);
  }

  @FXML
  private void handleClose() {
    dialogStage.close();
  }
}

