package ve.edu.ucab.domain.security;

public class Encrypter {

  /**
   * Cifra una letra usando un cifrado César con desplazamiento de 3 posiciones.
   */
  private char encryptLetter(char p) {
    if (Character.isUpperCase(p)) {
      return (char) ('A' + ((p - 'A' + 3) % 26));
    }
    if (Character.isLowerCase(p)) {
      return (char) ('a' + ((p - 'a' + 3) % 26));
    }
    return p;
  }

  /**
   * Cifra un dígito numérico reflejándolo respecto a '9'.
   * Ejemplo: '0' → '9', '1' → '8', ..., '9' → '0'
   */
  private char encryptDigit(char p) {
    if (Character.isDigit(p)) {
      return (char) ('9' - (p - '0'));
    }
    return p;
  }

  /**
   * Cifra un carácter, ya sea letra o dígito.
   */
  private char encryptChar(char p) {
    if (Character.isDigit(p)) {
      return encryptDigit(p);
    } else {
      return encryptLetter(p);
    }
  }

  /**
   * Cifra una línea completa de texto, carácter por carácter.
   */
  public String encryptLine(String line) {
    StringBuilder encryptedLine = new StringBuilder();
    for (char letter : line.toCharArray()) {
      encryptedLine.append(encryptChar(letter));
    }
    return encryptedLine.toString();
  }

  //

  private char decryptLetter(char p) {
    if (Character.isUpperCase(p)) {
      return (char) ('A' + ((p - 'A' - 3 + 26) % 26));
    }
    if (Character.isLowerCase(p)) {
      return (char) ('a' + ((p - 'a' - 3 + 26) % 26));
    }
    return p;
  }

  private char decryptDigit(char p) {
    if (Character.isDigit(p)) {
      return (char) ('9' - (p - '0'));
    }
    return p;
  }

  private char decryptCaracter(char p) {
    if (Character.isDigit(p)) {
      return decryptDigit(p);
    } else {
      return decryptLetter(p);
    }
  }

  public String decryptLine(String line) {
    StringBuilder lineDecrypt = new StringBuilder();
    for (char letter : line.toCharArray()) {
      lineDecrypt.append(decryptCaracter(letter));
    }
    return lineDecrypt.toString();
  }
}

