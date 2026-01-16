package ve.edu.ucab.domain.model;

import java.time.LocalDate;

public class Transaction {
  private final TransactionType transactionType;
  private final double amount;
  private final String assetTicker;
  private final AssetsType assetType;
  private final LocalDate dateRelease;
  private final double purchasePrice; // Nuevo campo

  private Transaction(TransactionType transactionType, double amount, String assetTicker, AssetsType assetType,
      double purchasePrice) {
    this.transactionType = transactionType;
    this.amount = amount;
    this.assetTicker = assetTicker;
    this.assetType = assetType;
    this.dateRelease = LocalDate.now();
    this.purchasePrice = purchasePrice;
  }

  private Transaction(TransactionType transactionType, double amount, String assetTicker, AssetsType assetType,
      LocalDate dateRelease, double purchasePrice) {
    this.transactionType = transactionType;
    this.amount = amount;
    this.assetTicker = assetTicker;
    this.assetType = assetType;
    this.dateRelease = dateRelease;
    this.purchasePrice = purchasePrice;
  }

  public static Transaction createNewTransaction(TransactionType transactionType, double amount, String assetTicker,
      AssetsType assetType, double purchasePrice, LocalDate dateRelease) {
    if (dateRelease == null) {
      return new Transaction(transactionType, amount, assetTicker, assetType, purchasePrice);
    }
    return new Transaction(transactionType, amount, assetTicker, assetType, dateRelease, purchasePrice);
  }

  public String getTransactionType() {
    return transactionType.name();
  }

  public double getAmount() {
    return amount;
  }

  public String getAssetTicker() {
    return assetTicker;
  }

  public String getAssetType() {
    return assetType.name(); // o assetType.toString()
  }

  public LocalDate getDateRelease() {
    return dateRelease;
  }

  public double getPurchasePrice() {
    return purchasePrice;
  }
}
