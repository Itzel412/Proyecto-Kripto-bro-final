package ve.edu.ucab.domain.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AssetManager {
  private static AssetManager instance;
  private static final String FILE = "data/assets.json";
  private List<CatalogAsset> catalogAssets;
  private final Random random = new Random();

  private AssetManager() {
    loadCatalog();
  }

  public static AssetManager getInstance() {
    if (instance == null) {
      instance = new AssetManager();
    }
    return instance;
  }

  private void loadCatalog() {
    Gson gson = new Gson();
    try (FileReader reader = new FileReader(FILE)) {
      Type listType = new TypeToken<ArrayList<CatalogAsset>>() {
      }.getType();
      catalogAssets = gson.fromJson(reader, listType);
      if (catalogAssets == null || catalogAssets.isEmpty()) {
        catalogAssets = getDefaultAssets();
        saveCatalog();
      } else {
        // Inicializar precios actuales al precio base si están en 0
        for (CatalogAsset asset : catalogAssets) {
          if (asset.getCurrentPrice() == 0) {
            asset.setCurrentPrice(asset.getBasePrice() > 0 ? asset.getBasePrice() : getJsonPrice(asset));
          }
        }
      }
    } catch (IOException e) {
      catalogAssets = getDefaultAssets();
      saveCatalog();
    }
  }

  // Método auxiliar para obtener el campo 'price' del JSON si existe
  private double getJsonPrice(CatalogAsset asset) {
    try (FileReader reader = new FileReader(FILE)) {
      com.google.gson.JsonArray arr = com.google.gson.JsonParser.parseReader(reader).getAsJsonArray();
      for (com.google.gson.JsonElement el : arr) {
        com.google.gson.JsonObject obj = el.getAsJsonObject();
        if (obj.get("ticker").getAsString().equals(asset.getTicker()) && obj.has("price")) {
          return obj.get("price").getAsDouble();
        }
      }
    } catch (Exception ignored) {
    }
    return 0;
  }

  private void saveCatalog() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // Redondear todos los precios antes de guardar
    for (CatalogAsset asset : catalogAssets) {
      asset.setCurrentPrice(round2(asset.getCurrentPrice()));
    }
    try (FileWriter writer = new FileWriter(FILE)) {
      gson.toJson(catalogAssets, writer);
    } catch (IOException e) {
      System.err.println("Error guardando el catálogo de activos: " + e.getMessage());
    }
  }

  private List<CatalogAsset> getDefaultAssets() {
    List<CatalogAsset> defaults = new ArrayList<>();
    defaults.add(new CatalogAsset("Apple Inc.", "AAPL", AssetsType.STOCK, 180.00, 0.03));
    defaults.add(new CatalogAsset("Microsoft Corp.", "MSFT", AssetsType.STOCK, 320.00, 0.025));
    defaults.add(new CatalogAsset("Tesla Inc.", "TSLA", AssetsType.STOCK, 250.00, 0.05));
    defaults.add(new CatalogAsset("Amazon.com Inc.", "AMZN", AssetsType.STOCK, 135.00, 0.035));
    defaults.add(new CatalogAsset("Alphabet Inc.", "GOOGL", AssetsType.STOCK, 140.00, 0.028));
    defaults.add(new CatalogAsset("Bitcoin", "BTC", AssetsType.CRYPTO, 40000.00, 0.07));
    defaults.add(new CatalogAsset("Ethereum", "ETH", AssetsType.CRYPTO, 2200.00, 0.06));
    defaults.add(new CatalogAsset("Solana", "SOL", AssetsType.CRYPTO, 90.00, 0.09));
    defaults.add(new CatalogAsset("Cardano", "ADA", AssetsType.CRYPTO, 0.50, 0.08));
    defaults.add(new CatalogAsset("Dogecoin", "DOGE", AssetsType.CRYPTO, 0.08, 0.12));
    return defaults;
  }

  public List<CatalogAsset> getCatalogAssets() {
    return catalogAssets;
  }

  // Simula la variación de precios según la volatilidad de cada activo
  public void simulatePriceChanges() {
    for (CatalogAsset asset : catalogAssets) {
      double maxVar = asset.getVolatility();
      double variation = (random.nextDouble() - 0.5) * 2 * maxVar; // Ej: ±0.05
      if (asset.getCurrentPrice() <= 0.011) {
        // Si está en el mínimo, solo permitir variación positiva
        variation = Math.abs(variation);
      }
      double newPrice = asset.getCurrentPrice() * (1 + variation);
      asset.setCurrentPrice(Math.max(round2(newPrice), 0.01));
    }
  }

  // Redondea a 2 decimales
  private double round2(double value) {
    return Math.round(value * 100.0) / 100.0;
  }

  // Refresca los precios y guarda el catálogo actualizado
  public void refreshAndSavePrices() {
    simulatePriceChanges();
    saveCatalog();
  }
}
