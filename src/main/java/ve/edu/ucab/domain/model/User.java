package ve.edu.ucab.domain.model;

// Domain exception imports
import ve.edu.ucab.domain.exceptions.InvalidAmountException;
import ve.edu.ucab.domain.exceptions.InvalidPasswordException;

// Java utility imports
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import ve.edu.ucab.domain.model.Asset;
import ve.edu.ucab.domain.model.Transaction;
import ve.edu.ucab.domain.model.CatalogAsset;
import ve.edu.ucab.domain.model.TransactionType;
import ve.edu.ucab.domain.model.AssetsType;

/**
 * Represents a user entity in the system, encapsulating user-related data such
 * as
 * ID, username, password, and balance. Provides methods for managing user
 * credentials
 * and balance operations.
 *
 * @author badjavii
 * @since 2025-07-01
 */
public class User {

  /**
   * The unique identifier for the user.
   */
  private int id;

  /**
   * The username of the user.
   */
  private String username;

  /**
   * The password of the user.
   */
  private String password;

  /**
   * The balance associated with the user.
   */
  private double balance;

  // NUEVOS CAMPOS
  private List<Asset> portfolio;
  private List<Transaction> transactionHistory;

  /**
   * Private constructor to initialize a user with the specified attributes.
   *
   * @param id       the unique identifier for the user
   * @param username the username of the user
   * @param password the password of the user
   * @param balance  the initial balance of the user
   */
  private User(int id, String username, String password, double balance, List<Asset> portfolio,
      List<Transaction> transactionHistory) {
    setId(id);
    setUsername(username);
    setPassword(password);
    setBalance(balance);
    this.portfolio = (portfolio != null) ? portfolio : new ArrayList<>();
    this.transactionHistory = (transactionHistory != null) ? transactionHistory : new ArrayList<>();
  }

  // Constructor anterior para compatibilidad
  private User(int id, String username, String password, double balance) {
    this(id, username, password, balance, new ArrayList<>(), new ArrayList<>());
  }

  /**
   * Gets the unique identifier of the user.
   *
   * @return the user's ID
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the unique identifier of the user.
   *
   * @param id the ID to set
   */
  private void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the username of the user.
   *
   * @return the user's username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username of the user.
   *
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Sets the password of the user.
   *
   * @param password the password to set
   */
  private void setPassword(String password) {
    this.password = password;
  }

  /**
   * Gets the password of the user.
   *
   * @return the user's password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Checks if the provided username matches the user's username.
   *
   * @param someUsername the username to compare
   * @return {@code true} if the usernames match, {@code false} otherwise
   */
  public boolean isSameUsername(String someUsername) {
    return Objects.equals(someUsername, this.username);
  }

  /**
   * Checks if the provided password matches the user's password.
   *
   * @param somePassword the password to compare
   * @return {@code true} if the passwords match, {@code false} otherwise
   */
  public boolean isSamePassword(String somePassword) {
    return Objects.equals(somePassword, this.password);
  }

  /**
   * Verifies if the provided credentials match the user's username and password.
   *
   * @param someUsername the username to verify
   * @param somePassword the password to verify
   * @return {@code true} if both username and password match, {@code false}
   *         otherwise
   */
  public boolean areSameCredentials(String someUsername, String somePassword) {
    return isSameUsername(someUsername) && isSamePassword(somePassword);
  }

  /**
   * Changes the user's password if the provided old password is correct.
   *
   * @param oldPassword the current password to verify
   * @param newPassword the new password to set
   * @throws InvalidPasswordException if the old password is incorrect
   */
  public void changePassword(String oldPassword, String newPassword) throws InvalidPasswordException {
    if (isSamePassword(oldPassword)) {
      setPassword(newPassword);
    } else {
      throw new InvalidPasswordException("Access denied for password change.");
    }
  }

  /**
   * Validates that the provided amount is not less than the specified minimum.
   *
   * @param amount  the amount to validate
   * @param minimum the minimum allowed value
   * @throws InvalidAmountException if the amount is less than the minimum
   */
  private void validateAmount(double amount, double minimum) {
    if (amount < minimum) {
      throw new InvalidAmountException("The value cannot be less than " + minimum);
    }
  }

  /**
   * Sets the user's balance, ensuring it is not negative. If invalid, sets
   * balance to 0.
   *
   * @param balance the balance to set
   */
  private void setBalance(double balance) {
    try {
      validateAmount(balance, 0);
      this.balance = balance;
    } catch (InvalidAmountException e) {
      this.balance = 0;
      System.err.println("Invalid balance. Automatically set to 0 to avoid interruptions.");
      e.printStackTrace();
    }
  }

  /**
   * Gets the user's current balance.
   *
   * @return the user's balance
   */
  public double getBalance() {
    return balance;
  }

  /**
   * Increases the user's balance by the specified amount.
   *
   * @param amount the amount to add to the balance
   */
  public void increaseBalance(double amount) {
    try {
      validateAmount(amount, 0);
      setBalance(this.balance + amount);
      // Registrar transacción de depósito
      transactionHistory.add(Transaction.createNewTransaction(TransactionType.DEPOSIT, amount, "USD",
          AssetsType.CURRENCY, 1.0, java.time.LocalDate.now()));
    } catch (InvalidAmountException e) {
      System.err.println("Invalid amount for increasing balance. Operation ignored.");
      e.printStackTrace();
    }
  }

  /**
   * Decreases the user's balance by the specified amount, ensuring the result is
   * not negative.
   *
   * @param amount the amount to subtract from the balance
   */
  public void decreaseBalance(double amount) {
    try {
      validateAmount(amount, 0);
      double newBalance = this.balance - amount;
      validateAmount(newBalance, 0);
      setBalance(newBalance);
    } catch (InvalidAmountException e) {
      System.err.println("Unable to decrease balance. Invalid operation.");
      e.printStackTrace();
    }
  }

  public List<Asset> getPortfolio() {
    return portfolio;
  }

  public List<Transaction> getTransactionHistory() {
    return transactionHistory;
  }

  // Cambiar buyAsset para que cada compra cree un nuevo Asset (lote)
  public void buyAsset(CatalogAsset asset, double amount) {
    double totalCost = asset.getCurrentPrice() * amount;
    if (amount <= 0)
      throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
    if (this.balance < totalCost)
      throw new IllegalArgumentException("Saldo insuficiente");
    // Siempre crear un nuevo lote
    portfolio.add(Asset.createNewAsset(asset.getTicker(), amount, asset.getCurrentPrice()));
    this.decreaseBalance(totalCost);
    transactionHistory.add(Transaction.createNewTransaction(TransactionType.BUY, amount, asset.getTicker(),
        asset.getAssetType(), asset.getCurrentPrice(), java.time.LocalDate.now()));
  }

  // Nuevo método: vender por lote específico
  public void sellAssetByLot(Asset lote, double amount) {
    if (amount <= 0)
      throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
    if (lote == null || !portfolio.contains(lote) || lote.getAmount() < amount)
      throw new IllegalArgumentException("No tienes suficiente cantidad en este lote");
    // Obtener precio actual del activo
    CatalogAsset cat = ve.edu.ucab.domain.model.AssetManager.getInstance().getCatalogAssets().stream()
        .filter(a -> a.getTicker().equals(lote.getTicker())).findFirst().orElse(null);
    double precioActual = (cat != null) ? cat.getCurrentPrice() : lote.getPurchasePrice();
    double totalValue = precioActual * amount;
    lote.subtractAmount(amount);
    if (lote.getAmount() == 0)
      portfolio.remove(lote);
    this.increaseBalance(totalValue);
    transactionHistory.add(Transaction.createNewTransaction(TransactionType.SELL, amount, lote.getTicker(),
        (cat != null ? cat.getAssetType() : null), precioActual, java.time.LocalDate.now()));
  }

  /**
   * Creates a new user with the specified attributes.
   *
   * @param id       the unique identifier for the user
   * @param username the username of the user
   * @param password the password of the user
   * @param balance  the initial balance of the user
   * @return a new {@link User} instance
   */
  public static User createNewUser(int id, String username, String password, double balance) {
    return new User(id, username, password, balance, new ArrayList<>(), new ArrayList<>());
  }

  // Nuevo método para crear usuario con portafolio e historial (para
  // deserialización)
  public static User createNewUser(int id, String username, String password, double balance, List<Asset> portfolio,
      List<Transaction> transactionHistory) {
    return new User(id, username, password, balance, portfolio, transactionHistory);
  }
}