package com.jcaa.usersmanagement.domain.exception;

public final class InvalidMunicipalityIdException extends DomainException {

  private static final String MESSAGE_EMPTY = "The municipality id must not be empty.";
  private static final String MESSAGE_INVALID_FORMAT =
      "The municipality id must be a 5-digit code (2 digits for the province and 3 for the municipality).";

  private InvalidMunicipalityIdException(final String message) {
    super(message);
  }

  public static InvalidMunicipalityIdException becauseValueIsEmpty() {
    return new InvalidMunicipalityIdException(MESSAGE_EMPTY);
  }

  public static InvalidMunicipalityIdException becauseFormatIsInvalid() {
    return new InvalidMunicipalityIdException(MESSAGE_INVALID_FORMAT);
  }
}
