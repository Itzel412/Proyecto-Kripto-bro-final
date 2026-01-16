package ve.edu.ucab.domain.model;

/**
 * Represents an asset in the system, encapsulating its ticker, amount, and purchase price.
 * This class does not hold market-related data such as the current price of the asset.
 */
public class Asset {

  /**
   * The unique ticker symbol identifying the asset.
   */
  private final String ticker;

  /**
   * The quantity of the asset held.
   */
  private double amount;

  /**
   * The average purchase price of the asset.
   */
  private double purchasePrice;

  /**
   * Private constructor to initialize an asset with the specified attributes.
   *
   * @param ticker        the ticker symbol of the asset
   * @param amount        the initial quantity of the asset
   * @param purchasePrice the initial purchase price of the asset
   */
  private Asset(String ticker, double amount, double purchasePrice) {
    this.ticker = ticker;
    this.amount = amount;
    this.purchasePrice = purchasePrice;
  }

  /**
   * Creates a new asset with the specified attributes.
   *
   * @param ticker        the ticker symbol of the asset
   * @param amount        the initial quantity of the asset
   * @param purchasePrice the initial purchase price of the asset
   * @return a new {@link Asset} instance
   */
  public static Asset createNewAsset(String ticker, double amount, double purchasePrice) {
    return new Asset(ticker, amount, purchasePrice);
  }

  /**
   * Gets the ticker symbol of the asset.
   *
   * @return the ticker symbol
   */
  public String getTicker() {
    return ticker;
  }

  /**
   * Gets the quantity of the asset held.
   *
   * @return the amount of the asset
   */
  public double getAmount() {
    return amount;
  }

  /**
   * Gets the average purchase price of the asset.
   *
   * @return the purchase price
   */
  public double getPurchasePrice() {
    return purchasePrice;
  }

  /**
   * Adds a specified amount to the asset's quantity and updates the average purchase price.
   * The new purchase price is calculated as a weighted average based on the existing and new amounts.
   *
   * @param newAmount       the amount to add to the asset's quantity
   * @param newPurchasePrice the purchase price of the added amount
   */
  public void addAmount(double newAmount, double newPurchasePrice) {
    if (newAmount >= 0) {
      double totalAmount = this.amount + newAmount;
      double weightedPrice = ((this.amount * this.purchasePrice) + (newAmount * newPurchasePrice)) / totalAmount;
      this.amount = totalAmount;
      this.purchasePrice = weightedPrice;
    }
  }

  /**
   * Subtracts a specified amount from the asset's quantity. If the amount sold is greater than
   * or equal to the current amount, the asset's quantity and purchase price are set to zero.
   * Otherwise, only the quantity is reduced.
   *
   * @param amountSold the amount to subtract from the asset's quantity
   */
  public void subtractAmount(double amountSold) {
    if (amountSold >= this.amount) {
      this.amount = 0;
      this.purchasePrice = 0;
    } else {
      this.amount -= amountSold;
    }
  }
}
