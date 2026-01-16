package ve.edu.ucab.presentation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ve.edu.ucab.domain.model.*;
import ve.edu.ucab.presentation.util.SessionManager;
import ve.edu.ucab.infrastructure.repository.UserRepository;

import java.util.List;

public class TransaccionesController {
  @FXML
  private Label saldoLabel;
  @FXML
  private ComboBox<Object> activoComboBox;
  @FXML
  private ComboBox<String> tipoOperacionComboBox;
  @FXML
  private TextField cantidadTextField;
  @FXML
  private Button confirmarButton;
  @FXML
  private Label mensajeLabel;
  @FXML
  private TableView<Asset> portafolioTable;
  @FXML
  private TableColumn<Asset, String> colTicker;
  @FXML
  private TableColumn<Asset, Double> colCantidad;
  @FXML
  private TableColumn<Asset, Double> colPrecio;
  @FXML
  private TableColumn<Asset, Double> colPrecioActual;
  @FXML
  private Button volverButton;

  private User usuario;

  @FXML
  public void initialize() {
    usuario = SessionManager.getCurrentUser();
    if (usuario == null) {
      mensajeLabel.setText("No hay usuario en sesión.");
      return;
    }
    saldoLabel.setText(String.format("%.2f", usuario.getBalance()));
    // Combo tipo operación
    tipoOperacionComboBox.setItems(FXCollections.observableArrayList("Comprar", "Vender"));
    tipoOperacionComboBox.getSelectionModel().selectFirst();
    tipoOperacionComboBox.setOnAction(e -> actualizarComboActivos());
    // Inicializar ComboBox de activos
    activoComboBox.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else if (item instanceof CatalogAsset) {
          CatalogAsset ca = (CatalogAsset) item;
          setText(ca.getAssetName() + " (" + ca.getTicker() + ") - $" + String.format("%.2f", ca.getCurrentPrice()));
        } else if (item instanceof Asset) {
          Asset a = (Asset) item;
          setText(a.getTicker() + " | Cant: " + a.getAmount() + " | Comprado a: $"
              + String.format("%.2f", a.getPurchasePrice()));
        }
      }
    });
    activoComboBox.setButtonCell(new ListCell<>() {
      @Override
      protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else if (item instanceof CatalogAsset) {
          CatalogAsset ca = (CatalogAsset) item;
          setText(ca.getAssetName() + " (" + ca.getTicker() + ") - $" + String.format("%.2f", ca.getCurrentPrice()));
        } else if (item instanceof Asset) {
          Asset a = (Asset) item;
          setText(a.getTicker() + " | Cant: " + a.getAmount() + " | Comprado a: $"
              + String.format("%.2f", a.getPurchasePrice()));
        }
      }
    });
    actualizarComboActivos();
    // Tabla portafolio
    colTicker.setCellValueFactory(new PropertyValueFactory<>("ticker"));
    colCantidad.setCellValueFactory(new PropertyValueFactory<>("amount"));
    colPrecioActual.setCellValueFactory(cellData -> {
      String ticker = cellData.getValue().getTicker();
      CatalogAsset cat = AssetManager.getInstance().getCatalogAssets().stream()
          .filter(a -> a.getTicker().equals(ticker)).findFirst().orElse(null);
      return new javafx.beans.property.SimpleDoubleProperty(cat != null ? cat.getCurrentPrice() : 0.0).asObject();
    });
    actualizarPortafolio();
    // Botón confirmar
    confirmarButton.setOnAction(e -> handleConfirmar());
    volverButton.setOnAction(e -> handleVolver());
  }

  private void actualizarPortafolio() {
    ObservableList<Asset> data = FXCollections.observableArrayList(usuario.getPortfolio());
    portafolioTable.setItems(data);
    portafolioTable.refresh();
  }

  private void actualizarComboActivos() {
    String tipo = tipoOperacionComboBox.getValue();
    if (tipo.equals("Comprar")) {
      List<CatalogAsset> catalogo = AssetManager.getInstance().getCatalogAssets();
      activoComboBox.setItems(FXCollections.observableArrayList(catalogo));
    } else {
      List<Asset> lotes = usuario.getPortfolio();
      activoComboBox.setItems(FXCollections.observableArrayList(lotes));
    }
    activoComboBox.getSelectionModel().clearSelection();
  }

  private void handleConfirmar() {
    mensajeLabel.setText("");
    String tipo = tipoOperacionComboBox.getValue();
    String cantidadStr = cantidadTextField.getText();
    if (tipo == null || cantidadStr.isEmpty()) {
      mensajeLabel.setText("Complete todos los campos.");
      return;
    }
    double cantidad;
    try {
      cantidad = Double.parseDouble(cantidadStr);
    } catch (NumberFormatException ex) {
      mensajeLabel.setText("Cantidad inválida.");
      return;
    }
    try {
      if (tipo.equals("Comprar")) {
        CatalogAsset activo = (CatalogAsset) activoComboBox.getValue();
        if (activo == null) {
          mensajeLabel.setText("Seleccione un activo del catálogo.");
          return;
        }
        usuario.buyAsset(activo, cantidad);
        mensajeLabel.setText("¡Compra exitosa! Precio actual: $" + String.format("%.2f", activo.getCurrentPrice()));
      } else {
        Asset lote = (Asset) activoComboBox.getValue();
        if (lote == null) {
          mensajeLabel.setText("Seleccione un lote de su portafolio.");
          return;
        }
        usuario.sellAssetByLot(lote, cantidad);
        CatalogAsset cat = AssetManager.getInstance().getCatalogAssets().stream()
            .filter(a -> a.getTicker().equals(lote.getTicker())).findFirst().orElse(null);
        double precioActual = (cat != null) ? cat.getCurrentPrice() : lote.getPurchasePrice();
        mensajeLabel.setText("¡Venta exitosa! Precio actual: $" + String.format("%.2f", precioActual));
      }
      UserRepository.getInstance().updateUser(usuario);
      saldoLabel.setText(String.format("%.2f", usuario.getBalance()));
      actualizarPortafolio();
      actualizarComboActivos();
    } catch (Exception ex) {
      mensajeLabel.setText(ex.getMessage());
    }
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
      mensajeLabel.setText("No se pudo volver al menú principal.");
      e.printStackTrace();
    }
  }
}