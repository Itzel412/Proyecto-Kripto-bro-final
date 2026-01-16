package ve.edu.ucab.domain.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileTest {

  private static final String FILE = "data/users.json";

  public static void main(String[] args) {
    try {
      validateFileExists();
      try (FileReader reader = new FileReader(FILE)) {
        System.out.println("Archivo abierto correctamente.");
      }
    } catch (Exception e) {
      System.out.println("Excepción atrapada: " + e.getMessage());
    }
  }

  private static void validateFileExists() throws Exception {
    File file = new File(FILE);
    System.out.println("Buscando archivo en: " + file.getAbsolutePath());
    if (!file.exists() || !file.isFile()) {
      throw new Exception("Archivo no encontrado: " + FILE);
    }
  }
}
