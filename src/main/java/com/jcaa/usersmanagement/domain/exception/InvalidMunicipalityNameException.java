package com.jcaa.usersmanagement.domain.exception;

public final class InvalidMunicipalityNameException extends DomainException {

  private static final String MESSAGE_EMPTY = "The municipality name must not be empty.";
  private static final String MESSAGE_TOO_SHORT =
      "The municipality name must have at least %d characters.";

  private InvalidMunicipalityNameException(final String message) {
    super(message);
  }

  public static InvalidMunicipalityNameException becauseValueIsEmpty() {
    return new InvalidMunicipalityNameException(MESSAGE_EMPTY);
  }

  public static InvalidMunicipalityNameException becauseLengthIsTooShort(final int minimumLength) {
    return new InvalidMunicipalityNameException(String.format(MESSAGE_TOO_SHORT, minimumLength));
  }
}
