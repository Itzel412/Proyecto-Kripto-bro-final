package ve.edu.ucab.domain.usecase;

// Domain exception imports
import ve.edu.ucab.domain.exceptions.ExistingUserException;

// Domain model and repository imports
import ve.edu.ucab.domain.model.User;
import ve.edu.ucab.infrastructure.repository.UserRepository;
import java.util.ArrayList;
import ve.edu.ucab.domain.model.Asset;
import ve.edu.ucab.domain.model.Transaction;

/**
 * A use case class for registering new users in the system.
 * Implements the {@link UseCase} interface to handle user registration
 * by adding a new user to the {@link UserRepository} and returning the created
 * user.
 *
 * @author badjavii
 * @since 2025-07-01
 */
public class RegisterUserUseCase implements UseCase<RegisterInput, User> {

  /**
   * The repository used to manage user data.
   */
  private final UserRepository userRepository;

  /**
   * Constructs a new RegisterUserUseCase with the specified user repository.
   *
   * @param userRepository the {@link UserRepository} used to store user data
   */
  public RegisterUserUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Executes the user registration process by creating a new user with the
   * provided input.
   * Generates a new user ID, adds the user to the repository, and returns the
   * created user.
   *
   * @param input the {@link RegisterInput} containing the username and password
   * @return the newly created {@link User} object
   * @throws ExistingUserException if a user with the same username already exists
   *                               in the repository
   */
  @Override
  public User execute(RegisterInput input) throws ExistingUserException {
    if (userRepository.existsData(input.getPassword())) {
      throw new ExistingUserException("Ya existe un usuario con esa contraseña.");
    }

    int newId = userRepository.getAllData().size() + 1;
    userRepository.addData(newId, input.getUsername(), input.getPassword(), 100.0, new ArrayList<Asset>(),
        new ArrayList<Transaction>());
    return User.createNewUser(newId, input.getUsername(), input.getPassword(), 100.0, new ArrayList<Asset>(),
        new ArrayList<Transaction>());
  }

}