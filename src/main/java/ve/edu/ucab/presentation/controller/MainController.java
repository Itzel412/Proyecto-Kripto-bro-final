package ve.edu.ucab.presentation.controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import ve.edu.ucab.domain.model.User;
import ve.edu.ucab.presentation.util.SessionManager;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import ve.edu.ucab.domain.model.AssetManager;
import ve.edu.ucab.domain.model.SimuladorPrecios;

public class MainController {
    @FXML
    private Label cardBalanceLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private AnchorPane mainContent;

    private User user;
    private boolean initialized = false;
    private Timeline timeline;
    private SimuladorPrecios simulador;

    public void setUser(User user) {
        this.user = user;
        updateUserInfo();
    }

    private void updateUserInfo() {
        if (user != null && cardBalanceLabel != null && usernameLabel != null) {
            System.out.println("[DEBUG] Mostrando saldo: " + user.getBalance());
            cardBalanceLabel.setText(String.format("%.2f", user.getBalance()));
            usernameLabel.setText(user.getUsername());
        }
    }

    @FXML
    public void initialize() {
        User user = SessionManager.getCurrentUser();
        System.out
                .println("[DEBUG] MainController.initialize() - user: " + (user != null ? user.getUsername() : "null"));
        System.out.println("[DEBUG] MainController.initialize() - cardBalanceLabel: " + (cardBalanceLabel != null));
        if (user != null && cardBalanceLabel != null && usernameLabel != null) {
            System.out.println("[DEBUG] Mostrando saldo en initialize: " + user.getBalance());
            cardBalanceLabel.setText(String.format("%.2f", user.getBalance()));
            usernameLabel.setText(user.getUsername());
        }
        // Refresco automático de precios
        simulador = new SimuladorPrecios(AssetManager.getInstance().getCatalogAssets());
        timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> refrescarPrecios()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void refrescarPrecios() {
        simulador.simularCambioPrecios();
    }

    @FXML
    private void handlePerfil(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/presentation/view/perfil.fxml"));
            javafx.scene.Parent perfilRoot = loader.load();
            javafx.scene.control.Button sourceButton = (javafx.scene.control.Button) event.getSource();
            javafx.stage.Stage stage = (javafx.stage.Stage) sourceButton.getScene().getWindow();
            javafx.scene.Scene perfilScene = new javafx.scene.Scene(perfilRoot);
            stage.setScene(perfilScene);
            stage.setResizable(false);
            stage.setTitle("Perfil");
        } catch (Exception e) {
            System.err.println("No se pudo cargar la pantalla de perfil.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/presentation/view/login.fxml"));
            javafx.scene.Parent loginRoot = loader.load();
            javafx.scene.control.Button sourceButton = (javafx.scene.control.Button) event.getSource();
            javafx.stage.Stage stage = (javafx.stage.Stage) sourceButton.getScene().getWindow();
            javafx.scene.Scene loginScene = new javafx.scene.Scene(loginRoot);
            stage.setScene(loginScene);
            stage.setResizable(false);
            stage.setTitle("Iniciar Sesión");
        } catch (Exception e) {
            System.err.println("No se pudo cargar la pantalla de login.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCatalogo(javafx.event.ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/presentation/view/catalogo.fxml"));
            javafx.scene.Parent catalogoRoot = loader.load();
            javafx.scene.control.Button sourceButton = (javafx.scene.control.Button) event.getSource();
            javafx.stage.Stage stage = (javafx.stage.Stage) sourceButton.getScene().getWindow();
            javafx.scene.Scene catalogoScene = new javafx.scene.Scene(catalogoRoot);
            stage.setScene(catalogoScene);
            stage.setResizable(false);
            stage.setTitle("Catálogo de Activos");
        } catch (Exception e) {
            System.err.println("No se pudo cargar la pantalla de catálogo.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFondear(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentation/view/fondear.fxml"));
            Parent fondearRoot = loader.load();
            mainContent.getChildren().setAll(fondearRoot);
        } catch (Exception e) {
            System.err.println("No se pudo cargar la pantalla de fondeo.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTransacciones(javafx.event.ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/presentation/view/transacciones.fxml"));
            javafx.scene.Parent transaccionesRoot = loader.load();
            javafx.scene.control.Button sourceButton = (javafx.scene.control.Button) event.getSource();
            javafx.stage.Stage stage = (javafx.stage.Stage) sourceButton.getScene().getWindow();
            javafx.scene.Scene transaccionesScene = new javafx.scene.Scene(transaccionesRoot);
            stage.setScene(transaccionesScene);
            stage.setResizable(false);
            stage.setTitle("Operaciones de Compra y Venta");
        } catch (Exception e) {
            System.err.println("No se pudo cargar la pantalla de transacciones.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePortafolio(javafx.event.ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/presentation/view/portafolio.fxml"));
            javafx.scene.Parent portafolioRoot = loader.load();
            javafx.scene.control.Button sourceButton = (javafx.scene.control.Button) event.getSource();
            javafx.stage.Stage stage = (javafx.stage.Stage) sourceButton.getScene().getWindow();
            javafx.scene.Scene portafolioScene = new javafx.scene.Scene(portafolioRoot);
            stage.setScene(portafolioScene);
            stage.setResizable(false);
            stage.setTitle("Portafolio");
        } catch (Exception e) {
            System.err.println("No se pudo cargar la pantalla de portafolio.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHistorial(javafx.event.ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/presentation/view/historial.fxml"));
            javafx.scene.Parent historialRoot = loader.load();
            javafx.scene.control.Button sourceButton = (javafx.scene.control.Button) event.getSource();
            javafx.stage.Stage stage = (javafx.stage.Stage) sourceButton.getScene().getWindow();
            javafx.scene.Scene historialScene = new javafx.scene.Scene(historialRoot);
            stage.setScene(historialScene);
            stage.setResizable(false);
            stage.setTitle("Historial de Transacciones");
        } catch (Exception e) {
            System.err.println("No se pudo cargar la pantalla de historial.");
            e.printStackTrace();
        }
    }
}
