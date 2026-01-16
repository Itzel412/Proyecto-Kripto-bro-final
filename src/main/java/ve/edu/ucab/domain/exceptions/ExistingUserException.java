package ve.edu.ucab.domain.exceptions;

public class ExistingUserException extends RuntimeException {
  public ExistingUserException(String message) {
    super(message);
  }
}
