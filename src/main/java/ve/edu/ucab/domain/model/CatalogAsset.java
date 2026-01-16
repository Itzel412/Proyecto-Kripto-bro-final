package ve.edu.ucab.domain.model;

public class CatalogAsset {
  private String assetName;
  private String ticker;
  private AssetsType assetType;
  private double basePrice;
  private double volatility; // porcentaje máximo de variación por ciclo (ej: 0.05 para 5%)
  private double currentPrice;

  public CatalogAsset(String assetName, String ticker, AssetsType assetType, double basePrice, double volatility) {
    this.assetName = assetName;
    this.ticker = ticker;
    this.assetType = assetType;
    this.basePrice = basePrice;
    this.volatility = volatility;
    this.currentPrice = basePrice;
  }

  public String getAssetName() {
    return assetName;
  }

  public String getTicker() {
    return ticker;
  }

  public AssetsType getAssetType() {
    return assetType;
  }

  public double getBasePrice() {
    return basePrice;
  }

  public double getVolatility() {
    return volatility;
  }

  public double getCurrentPrice() {
    return currentPrice;
  }

  public void setCurrentPrice(double currentPrice) {
    this.currentPrice = currentPrice;
  }

  public void resetPrice() {
    this.currentPrice = this.basePrice;
  }
}