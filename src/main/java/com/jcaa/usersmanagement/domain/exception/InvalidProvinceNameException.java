package com.jcaa.usersmanagement.domain.exception;

public final class InvalidProvinceNameException extends DomainException {

  private static final String MESSAGE_EMPTY = "The province name must not be empty.";
  private static final String MESSAGE_TOO_SHORT =
      "The province name must have at least %d characters.";

  private InvalidProvinceNameException(final String message) {
    super(message);
  }

  public static InvalidProvinceNameException becauseValueIsEmpty() {
    return new InvalidProvinceNameException(MESSAGE_EMPTY);
  }

  public static InvalidProvinceNameException becauseLengthIsTooShort(final int minimumLength) {
    return new InvalidProvinceNameException(String.format(MESSAGE_TOO_SHORT, minimumLength));
  }
}
