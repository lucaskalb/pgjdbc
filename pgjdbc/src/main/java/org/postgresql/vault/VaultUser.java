package org.postgresql.vault;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

public class VaultUser {

  private final String username;
  private final String password;

  static VaultUser of(String text) {
    return new VaultUser(text);
  }

  private VaultUser(String value) {
    if (value == null || value.trim().isEmpty())
      throw new IllegalArgumentException("Value of vault execute command cannot be null");

    this.username = extract(Pattern.compile("username\\s*(.*)", CASE_INSENSITIVE), value);
    this.password = extract(Pattern.compile("password\\s*(.*)", CASE_INSENSITIVE), value);
  }

  private String extract(Pattern pattern, String value){
    Matcher matcher = pattern.matcher(value);
    if (matcher.find())
      return matcher.group( 1 );

    throw new RuntimeException( "Unable to identify username or password" );
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
