package ve.edu.ucab.domain.repository;

// Java utility imports
import java.util.ArrayList;

/**
 * An abstract base class for repositories that manage a collection of data objects.
 * Provides common functionality for loading and storing data, with abstract methods
 * for specific data operations. Uses generics to support different data types and keys.
 *
 * @param <T> the type of data objects stored in the repository
 * @param <K> the type of key used to identify data objects
 * @author badjavii
 * @since 2025-07-01
 */
public abstract class Repository<T, K> {

  /**
   * The list storing all data objects managed by the repository.
   */
  protected ArrayList<T> data;

  /**
   * Constructs a new Repository, initializing the data list.
   */
  public Repository() {
    this.data = new ArrayList<>();
  }

  /**
   * Loads data into the repository by initializing the data list if null
   * and invoking the abstract {@link #loadFromSource()} method.
   */
  public final void loadData() {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    loadFromSource();
  }

  /**
   * Loads data from the underlying data source.
   * Must be implemented by subclasses to define how data is retrieved.
   */
  protected abstract void loadFromSource();

  /**
   * Adds a new item to the repository.
   *
   * @param item the data object of type {@code T} to be added
   * @throws Exception if an error occurs during the addition process
   */
  public abstract void addData(T item) throws Exception;

  /**
   * Checks if a data object with the specified key exists in the repository.
   *
   * @param key the key of type {@code K} to check for existence
   * @return {@code true} if a data object with the key exists, {@code false} otherwise
   */
  public abstract boolean existsData(K key);
}