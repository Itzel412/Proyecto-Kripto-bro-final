package ve.edu.ucab.domain.usecase;

/**
 * Defines a generic use case interface for executing business logic with a specific input and output.
 * Implementations of this interface encapsulate a single unit of business functionality.
 *
 * @param <I> the type of the input data
 * @param <O> the type of the output data
 * @author badjavii
 * @since 2025-07-01
 */
public interface UseCase<I, O> {

  /**
   * Executes the use case with the provided input and produces an output.
   *
   * @param input the input data of type {@code I} required to perform the use case
   * @return the output data of type {@code O} resulting from the use case execution
   * @throws Exception if an error occurs during the execution of the use case
   */
  O execute(I input) throws Exception;
}