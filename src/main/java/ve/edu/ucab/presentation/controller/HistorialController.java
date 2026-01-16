package ve.edu.ucab.presentation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ve.edu.ucab.domain.model.*;
import ve.edu.ucab.presentation.util.SessionManager;
import java.util.List;

public class HistorialController {
  @FXML
  private TableView<Transaction> tablaHistorial;
  @FXML
  private TableColumn<Transaction, String> colFecha;
  @FXML
  private TableColumn<Transaction, String> colTipo;
  @FXML
  private TableColumn<Transaction, String> colActivo;
  @FXML
  private TableColumn<Transaction, Double> colCantidad;
  @FXML
  private TableColumn<Transaction, Double> colMonto;
  @FXML
  private Button volverButton;

  private User usuario;

  @FXML
  public void initialize() {
    usuario = SessionManager.getCurrentUser();
    if (usuario == null)
      return;
    List<Transaction> historial = usuario.getTransactionHistory();
    ObservableList<Transaction> data = FXCollections.observableArrayList(historial);
    tablaHistorial.setItems(data);
    colFecha.setCellValueFactory(
        cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDateRelease().toString()));
    colTipo.setCellValueFactory(cellData -> {
      String tipo = cellData.getValue().getTransactionType();
      if ("BUY".equals(tipo))
        return new javafx.beans.property.SimpleStringProperty("Compra");
      if ("SELL".equals(tipo))
        return new javafx.beans.property.SimpleStringProperty("Venta");
      if ("DEPOSIT".equals(tipo))
        return new javafx.beans.property.SimpleStringProperty("Depósito");
      return new javafx.beans.property.SimpleStringProperty(tipo);
    });
    colActivo.setCellValueFactory(
        cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAssetTicker()));
    colCantidad.setCellValueFactory(
        cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());
    colMonto.setCellValueFactory(cellData -> {
      // Para depósitos, el monto es la cantidad
      if (cellData.getValue().getTransactionType().equals("DEPOSIT")) {
        return new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getAmount()).asObject();
      } else {
        // Para compra/venta, monto = cantidad * precio unitario de la transacción
        return new javafx.beans.property.SimpleDoubleProperty(
            cellData.getValue().getAmount() * cellData.getValue().getPurchasePrice()).asObject();
      }
    });
    volverButton.setOnAction(e -> handleVolver());
  }

  private void handleVolver() {
    try {
      javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
          getClass().getResource("/presentation/view/main.fxml"));
      javafx.scene.Parent mainRoot = loader.load();
      javafx.stage.Stage stage = (javafx.stage.Stage) volverButton.getScene().getWindow();
      javafx.scene.Scene mainScene = new javafx.scene.Scene(mainRoot);
      stage.setScene(mainScene);
      stage.setResizable(false);
      stage.setTitle("Menú Principal");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}