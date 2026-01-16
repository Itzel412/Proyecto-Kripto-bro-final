package ve.edu.ucab.domain.usecase;

/**
 * Represents the input data required for user login, encapsulating the username
 * and password provided by the user.
 *
 * @author badjavii
 * @since 2025-07-01
 */
public class LoginInput {

  /**
   * The username provided for login.
   */
  private final String username;

  /**
   * The password provided for login.
   */
  private final String password;

  /**
   * Constructs a new LoginInput with the specified username and password.
   *
   * @param username the username for login
   * @param password the password for login
   */
  public LoginInput(String username, String password) {
    this.username = username;
    this.password = password;
  }

  /**
   * Gets the username provided for login.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Gets the password provided for login.
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }
}
