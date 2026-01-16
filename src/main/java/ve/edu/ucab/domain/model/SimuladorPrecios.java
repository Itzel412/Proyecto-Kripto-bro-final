package ve.edu.ucab.domain.model;

import java.util.List;
import java.util.Random;

public class SimuladorPrecios {
  private final List<CatalogAsset> activos;
  private final Random random = new Random();

  public SimuladorPrecios(List<CatalogAsset> activos) {
    this.activos = activos;
  }

  public void simularCambioPrecios() {
    for (CatalogAsset activo : activos) {
      double variacion = (random.nextDouble() - 0.5) * 2 * activo.getVolatility(); // Variación ±volatilidad
      double nuevoPrecio = activo.getCurrentPrice() * (1 + variacion);
      activo.setCurrentPrice(Math.max(Math.round(nuevoPrecio * 100.0) / 100.0, 0.01));
    }
  }
}