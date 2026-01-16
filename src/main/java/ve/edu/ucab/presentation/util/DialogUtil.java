package ve.edu.ucab.presentation.util;

// JavaFX imports for UI components and scene management
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

// Controller import for error dialog
import ve.edu.ucab.presentation.controller.ErrorDialogController;

/**
 * A utility class for displaying error dialogs in a JavaFX application.
 * Provides a method to show a modal error dialog with a custom message.
 *
 * @author badjavii
 * @since 2025-07-01
 */
public class DialogUtil {

  /**
   * Displays a modal error dialog with the specified message.
   * Loads the error dialog view from an FXML file, configures the dialog controller,
   * and shows the dialog in a new stage.
   *
   * @param message the error message to display in the dialog
   */
  public static void showErrorDialog(String message) {
    try {
      FXMLLoader loader = new FXMLLoader(DialogUtil.class.getResource("/presentation/view/errorDialog.fxml"));
      Parent root = loader.load();

      ErrorDialogController controller = loader.getController();

      Stage dialogStage = new Stage();
      controller.setDialogStage(dialogStage);
      controller.setMessage(message);

      dialogStage.initModality(Modality.APPLICATION_MODAL);
      dialogStage.setTitle("Error");
      dialogStage.setScene(new Scene(root));
      dialogStage.setResizable(false);
      dialogStage.showAndWait();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}