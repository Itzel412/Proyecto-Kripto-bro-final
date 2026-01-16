package ve.edu.ucab.domain.usecase;

// Domain exception imports
import ve.edu.ucab.domain.exceptions.InvalidPasswordException;
import ve.edu.ucab.domain.exceptions.UserNotFoundException;

// Domain model and repository imports
import ve.edu.ucab.domain.model.User;
import ve.edu.ucab.infrastructure.repository.UserRepository;

/**
 * A use case class responsible for handling user login functionality.
 * Validates user credentials against the data stored in the user repository.
 *
 * @author badjavii
 * @since 2025-07-01
 */
public class LoginUseCase implements UseCase<LoginInput, User> {

  /**
   * The repository used to access user data.
   */
  private final UserRepository userRepository;

  /**
   * Constructs a new LoginUseCase with the specified user repository.
   *
   * @param userRepository the {@link UserRepository} used to access user data
   */
  public LoginUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User execute(LoginInput credentials) throws UserNotFoundException, InvalidPasswordException {
    return login(credentials.getUsername(), credentials.getPassword());
  }

  /**
   * Authenticates a user based on the provided username and password.
   * Returns the user if credentials are valid; otherwise, throws an exception.
   *
   * @param username the username to authenticate
   * @param password the password to authenticate
   * @return the authenticated {@link User} object
   * @throws UserNotFoundException  if no user with the specified username is found
   * @throws InvalidPasswordException if the password is incorrect
   */
  public User login(String username, String password) throws UserNotFoundException, InvalidPasswordException {
    for (User user : userRepository.getAllData()) {
      if (user.isSameUsername(username)) {
        if (user.isSamePassword(password)) {
          return user;
        } else {
          throw new InvalidPasswordException("Incorrect password.");
        }
      }
    }
    throw new UserNotFoundException("User not found.");
  }
}