package ve.edu.ucab.infrastructure.exceptions;

public class UserFileNotFoundException extends Exception {
  public UserFileNotFoundException(String message) {
    super(message);
  }
}