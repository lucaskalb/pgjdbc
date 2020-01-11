package org.postgresql.vault;

import java.util.Optional;
import java.util.Properties;

public class VaultAuthentication {

  private VaultAuthentication() {
  }

  public static VaultUser from(Properties info) {
    return Optional.ofNullable(info.getProperty("authVaultReadAlias"))
      .map(VaultReadCommand::create)
      .map(VaultReadCommand::execute)
      .map(VaultUser::of)
      .orElseThrow(() -> new IllegalArgumentException("Unable to recover vault username and password "));
  }
}
