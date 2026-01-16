package ve.edu.ucab.presentation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.chart.PieChart;
import ve.edu.ucab.domain.model.*;
import ve.edu.ucab.presentation.util.SessionManager;
import java.util.List;
import javafx.application.Platform;

public class PortafolioController {
  @FXML
  private TableView<Asset> tablaPortafolio;
  @FXML
  private TableColumn<Asset, String> colNombre;
  @FXML
  private TableColumn<Asset, String> colTicker;
  @FXML
  private TableColumn<Asset, Double> colCantidad;
  @FXML
  private TableColumn<Asset, Double> colValorActual;
  @FXML
  private TableColumn<Asset, Double> colValorInvertido;
  @FXML
  private TableColumn<Asset, Double> colGanancia;
  @FXML
  private Button volverButton;
  @FXML
  private PieChart pieChart;

  private User usuario;

  @FXML
  public void initialize() {
    usuario = SessionManager.getCurrentUser();
    if (usuario == null)
      return;
    List<Asset> lotes = usuario.getPortfolio();
    ObservableList<Asset> data = FXCollections.observableArrayList(lotes);
    tablaPortafolio.setItems(data);
    colNombre.setCellValueFactory(cellData -> {
      String ticker = cellData.getValue().getTicker();
      CatalogAsset cat = AssetManager.getInstance().getCatalogAssets().stream()
          .filter(a -> a.getTicker().equals(ticker)).findFirst().orElse(null);
      return new javafx.beans.property.SimpleStringProperty(cat != null ? cat.getAssetName() : ticker);
    });
    colTicker.setCellValueFactory(
        cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTicker()));
    colCantidad.setCellValueFactory(
        cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());
    colValorActual.setCellValueFactory(cellData -> {
      String ticker = cellData.getValue().getTicker();
      CatalogAsset cat = AssetManager.getInstance().getCatalogAssets().stream()
          .filter(a -> a.getTicker().equals(ticker)).findFirst().orElse(null);
      double precioActual = (cat != null) ? cat.getCurrentPrice() : 0.0;
      double valorActual = cellData.getValue().getAmount() * precioActual;
      return new javafx.beans.property.SimpleDoubleProperty(valorActual).asObject();
    });
    colValorInvertido.setCellValueFactory(cellData -> {
      double valorInvertido = cellData.getValue().getAmount() * cellData.getValue().getPurchasePrice();
      return new javafx.beans.property.SimpleDoubleProperty(valorInvertido).asObject();
    });
    colGanancia.setCellValueFactory(cellData -> {
      String ticker = cellData.getValue().getTicker();
      CatalogAsset cat = AssetManager.getInstance().getCatalogAssets().stream()
          .filter(a -> a.getTicker().equals(ticker)).findFirst().orElse(null);
      double precioActual = (cat != null) ? cat.getCurrentPrice() : 0.0;
      double valorActual = cellData.getValue().getAmount() * precioActual;
      double valorInvertido = cellData.getValue().getAmount() * cellData.getValue().getPurchasePrice();
      double ganancia = valorActual - valorInvertido;
      return new javafx.beans.property.SimpleDoubleProperty(Math.round(ganancia * 100.0) / 100.0).asObject();
    });
    // PieChart: distribución por valor actual
    ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
    for (Asset a : lotes) {
      String ticker = a.getTicker();
      CatalogAsset cat = AssetManager.getInstance().getCatalogAssets().stream()
          .filter(c -> c.getTicker().equals(ticker)).findFirst().orElse(null);
      double precioActual = (cat != null) ? cat.getCurrentPrice() : 0.0;
      double valorActual = a.getAmount() * precioActual;
      pieData.add(new PieChart.Data(ticker, valorActual));
    }
    pieChart.setData(pieData);
    // Forzar color blanco en las etiquetas después de renderizar
    Platform
        .runLater(() -> pieChart.lookupAll(".chart-pie-label").forEach(node -> node.setStyle("-fx-text-fill: white;")));
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