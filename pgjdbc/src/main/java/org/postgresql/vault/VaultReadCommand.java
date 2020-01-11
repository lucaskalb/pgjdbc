package org.postgresql.vault;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

class VaultReadCommand {

  private final String command;

  private VaultReadCommand(String alias) {
    guardIfAliasIsNull(alias);

    this.command = "vault read ".concat(alias);
  }

  private void guardIfAliasIsNull(String alias) {
    if (Objects.isNull(alias) || alias.trim().isEmpty())
      throw new IllegalArgumentException("Vault read alias property cannot be null or empty");
  }

  static VaultReadCommand create(String alias) {
    return new VaultReadCommand(alias);
  }

  String execute() {
    try {
      StringBuffer buffer = new StringBuffer();
      Process process = Runtime.getRuntime().exec(command);

      new Thread(readOutputOf(process, buffer)).start();

      process.waitFor();
      return buffer.toString();
    } catch (InterruptedException | IOException e) {
      throw new RuntimeException("Error on vault command execute", e);
    }
  }

  private Runnable readOutputOf(Process process, StringBuffer buffer) {
    return () -> {
      Scanner sc = new Scanner(process.getInputStream());

      while (sc.hasNext())
        buffer.append(sc.nextLine()).append("\n");
    };
  }
}
