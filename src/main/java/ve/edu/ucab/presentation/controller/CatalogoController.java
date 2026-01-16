package ve.edu.ucab.presentation.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ve.edu.ucab.domain.model.AssetManager;
import ve.edu.ucab.domain.model.CatalogAsset;
import ve.edu.ucab.domain.model.SimuladorPrecios;

public class CatalogoController {
  @FXML
  private TableView<CatalogAsset> tablaActivos;
  @FXML
  private TableColumn<CatalogAsset, String> colNombre;
  @FXML
  private TableColumn<CatalogAsset, String> colTicker;
  @FXML
  private TableColumn<CatalogAsset, String> colTipo;
  @FXML
  private TableColumn<CatalogAsset, Double> colPrecio;
  @FXML
  private TableColumn<CatalogAsset, Double> colVolatilidad;
  @FXML
  private Button volverButton;

  private ObservableList<CatalogAsset> activosObservable;
  private SimuladorPrecios simulador;
  private Timeline timeline;

  @FXML
  public void initialize() {
    colNombre.setCellValueFactory(new PropertyValueFactory<>("assetName"));
    colTicker.setCellValueFactory(new PropertyValueFactory<>("ticker"));
    colTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAssetType().name()));
    colPrecio.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
    colVolatilidad.setCellValueFactory(new PropertyValueFactory<>("volatility"));
    cargarActivos();
    simulador = new SimuladorPrecios(AssetManager.getInstance().getCatalogAssets());
    timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> refrescarAutomatico()));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  private void cargarActivos() {
    activosObservable = FXCollections.observableArrayList(AssetManager.getInstance().getCatalogAssets());
    tablaActivos.setItems(activosObservable);
  }

  @FXML
  private void handleRefrescar() {
    simulador.simularCambioPrecios();
    cargarActivos();
    tablaActivos.refresh();
  }

  private void refrescarAutomatico() {
    simulador.simularCambioPrecios();
    cargarActivos();
    tablaActivos.refresh();
  }

  @FXML
  private void handleVolver() {
    timeline.stop();
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentation/view/main.fxml"));
      Parent mainRoot = loader.load();
      Stage stage = (Stage) volverButton.getScene().getWindow();
      Scene mainScene = new Scene(mainRoot);
      stage.setScene(mainScene);
      stage.setResizable(false);
      stage.setTitle("Menú Principal");
    } catch (Exception e) {
      System.err.println("No se pudo volver al menú principal.");
      e.printStackTrace();
    }
  }
}