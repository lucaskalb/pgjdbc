package org.postgresql.vault;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

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
      StringBuilder result = new StringBuilder();
      Process p = Runtime.getRuntime().exec(command);

      new Thread(() -> {
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;

        try {
          while ((line = input.readLine()) != null)
            result.append(line).append("\n");
        } catch (IOException e) {

          e.printStackTrace();
        }
      }).start();
      p.waitFor();
      return result.toString();
    } catch (InterruptedException | IOException e) {
      throw new RuntimeException("Error on vault command execute", e);
    }
  }
}
