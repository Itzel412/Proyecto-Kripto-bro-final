package ve.edu.ucab.infrastructure.repository;

// Gson-related imports for JSON processing
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

// Domain model and exception imports
import ve.edu.ucab.domain.exceptions.ExistingUserException;
import ve.edu.ucab.domain.model.User;
import ve.edu.ucab.domain.repository.Repository;
import ve.edu.ucab.domain.security.Encrypter;
import ve.edu.ucab.infrastructure.exceptions.UserFileNotFoundException;
import ve.edu.ucab.domain.model.Asset;
import ve.edu.ucab.domain.model.Transaction;

// Java I/O and utility imports
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * A repository class for managing user data, implementing the Singleton pattern
 * to ensure a single instance and handling user data persistence using a JSON
 * file.
 * Extends the abstract {@link Repository} class for user-specific operations.
 *
 * @author badjavii
 * @since 2025-07-01
 */
public class UserRepository extends Repository<User, String> {

  /**
   * The list storing all user objects.
   */
  protected ArrayList<User> users;

  /**
   * The encrypter used for password encryption and decryption.
   */
  private final Encrypter encrypter = new Encrypter();

  /**
   * The single instance of the UserRepository (Singleton pattern).
   */
  private static UserRepository instance;

  /**
   * The file path for the JSON file containing user data.
   */
  private static final String FILE = "data/users.json";

  /**
   * Private constructor to enforce Singleton pattern and initialize the user
   * list.
   * Loads user data from the JSON file upon instantiation.
   *
   * @throws Exception if an error occurs during initialization or file loading
   */
  private UserRepository() throws Exception {
    super();
    this.users = this.data;
    loadData();
  }

  /**
   * Retrieves the singleton instance of the UserRepository.
   *
   * @return the single instance of {@link UserRepository}
   * @throws Exception if an error occurs during instance creation
   */
  public static UserRepository getInstance() throws Exception {
    if (instance == null) {
      instance = new UserRepository();
    }
    return instance;
  }

  /**
   * Validates that the user data file exists.
   *
   * @throws UserFileNotFoundException if the file does not exist or is not a
   *                                   valid file
   */
  public static void validateUserFileExists() throws UserFileNotFoundException {
    File file = new File(FILE);
    if (!file.exists() || !file.isFile()) {
      throw new UserFileNotFoundException("The user file was not found.");
    }
  }

  /**
   * Adds a new user to the repository.
   *
   * @param newUser the {@link User} object to be added
   * @throws ExistingUserException if a user with the same username already exists
   */
  @Override
  public void addData(User newUser) throws ExistingUserException {
    if (!existsData(newUser.getUsername())) {
      users.add(newUser);
      saveToFile(); // Save changes to file
    } else {
      throw new ExistingUserException("The user already exists in the database.");
    }
  }

  /**
   * Adds a new user to the repository by creating a {@link User} object with the
   * provided details.
   *
   * @param id       the unique identifier for the user
   * @param username the username of the user
   * @param password the password of the user
   * @param balance  the balance associated with the user
   * @throws ExistingUserException if a user with the same username already exists
   */

  public void addData(int id, String username, String password, double balance, List<Asset> portfolio,
      List<Transaction> transactionHistory) throws ExistingUserException {
    if (!existsData(username)) {
      User newUser = User.createNewUser(id, username, password, balance, portfolio, transactionHistory);
      addData(newUser);
    } else {
      throw new ExistingUserException("The user already exists in the database.");
    }
  }

  /**
   * Loads user data from the JSON file specified in {@link #FILE} and populates
   * the user list.
   * Decrypts passwords using the {@link Encrypter} before adding users to the
   * repository.
   *
   * @throws RuntimeException if an error occurs while loading the file
   */
  @Override
  protected void loadFromSource() {
    try (FileReader reader = new FileReader(FILE)) {
      Gson gson = new GsonBuilder()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();
      JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

      for (JsonElement element : jsonArray) {
        try {
          JsonObject obj = element.getAsJsonObject();

          int id = obj.get("id").getAsInt();
          String username = obj.get("username").getAsString();
          String password = obj.get("password").getAsString();
          double balance = obj.get("balance").getAsDouble();

          // Leer portafolio
          List<Asset> portfolio = new ArrayList<>();
          if (obj.has("portfolio") && obj.get("portfolio").isJsonArray()) {
            portfolio = gson.fromJson(obj.get("portfolio"), new TypeToken<List<Asset>>() {
            }.getType());
          }
          // Leer historial de transacciones
          List<Transaction> transactionHistory = new ArrayList<>();
          if (obj.has("transactionHistory") && obj.get("transactionHistory").isJsonArray()) {
            transactionHistory = gson.fromJson(obj.get("transactionHistory"), new TypeToken<List<Transaction>>() {
            }.getType());
          }

          // Usar el nuevo método de User
          users.add(ve.edu.ucab.domain.model.User.createNewUser(id, username, password, balance, portfolio,
              transactionHistory));
        } catch (Exception userEx) {
          System.err.println("Error al cargar un usuario: " + userEx.getMessage());
          userEx.printStackTrace();
        }
      }
      System.out.println("Total de usuarios cargados: " + users.size());
    } catch (Exception e) {
      System.err.println("Error general al leer el archivo de usuarios:");
      e.printStackTrace();
      throw new RuntimeException("Error loading user file: " + e.getMessage(), e);
    }
  }

  /**
   * Checks if a user with the specified password exists in the repository.
   *
   * @param targetPassword the password to check
   * @return {@code true} if a user with the password exists, {@code false}
   *         otherwise
   */
  @Override
  public boolean existsData(String targetPassword) {
    for (User u : users) {
      if (u.isSamePassword(targetPassword)) { // Removed encrypter usage
        return true;
      }
    }
    return false;
  }

  /**
   * Saves the user data to the JSON file specified in {@link #FILE}.
   * Encrypts passwords using the {@link Encrypter} before saving.
   *
   * @throws RuntimeException if an error occurs while saving the file
   */
  public void saveToFile() {
    JsonArray jsonArray = new JsonArray();
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .setPrettyPrinting().create();

    for (User user : users) {
      JsonObject obj = new JsonObject();
      obj.addProperty("id", user.getId());
      obj.addProperty("username", user.getUsername());
      obj.addProperty("password", user.getPassword());
      obj.addProperty("balance", user.getBalance());
      // Serializar portafolio
      obj.add("portfolio", gson.toJsonTree(user.getPortfolio()));
      // Serializar historial
      obj.add("transactionHistory", gson.toJsonTree(user.getTransactionHistory()));
      jsonArray.add(obj);
    }

    try (FileWriter writer = new FileWriter(FILE)) {
      gson.toJson(jsonArray, writer);
    } catch (IOException e) {
      throw new RuntimeException("Error saving users to file: " + e.getMessage(), e);
    }
  }

  /**
   * Retrieves all users stored in the repository.
   *
   * @return an {@link ArrayList} containing all {@link User} objects in the
   *         repository
   */
  public ArrayList<User> getAllData() {
    return users;
  }

  /**
   * Updates an existing user in the repository with the provided updated user
   * data.
   * Only the username is updated; password and balance remain unchanged.
   *
   * @param updatedUser the {@link User} object containing updated data
   */
  public void updateUser(User updatedUser) {
    for (User user : users) {
      if (user.getId() == updatedUser.getId()) {
        user.setUsername(updatedUser.getUsername());
        // Ensure password and balance remain unchanged unless explicitly updated
        break;
      }
    }
    saveToFile();
  }
}

// Adaptador para LocalDate
class LocalDateAdapter extends TypeAdapter<LocalDate> {
  public LocalDateAdapter() {
  }

  @Override
  public void write(JsonWriter out, LocalDate value) throws IOException {
    out.value(value != null ? value.toString() : null);
  }

  @Override
  public LocalDate read(JsonReader in) throws IOException {
    String str = in.nextString();
    return (str != null && !str.isEmpty()) ? LocalDate.parse(str) : null;
  }
}