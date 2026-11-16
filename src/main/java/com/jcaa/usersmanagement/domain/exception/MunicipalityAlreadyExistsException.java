package com.jcaa.usersmanagement.domain.exception;

public final class MunicipalityAlreadyExistsException extends DomainException {

  private static final String MESSAGE_ID_EXISTS = "A municipality with id '%s' already exists.";

  private MunicipalityAlreadyExistsException(final String message) {
    super(message);
  }

  public static MunicipalityAlreadyExistsException becauseIdAlreadyExists(
      final String municipalityId) {
    return new MunicipalityAlreadyExistsException(String.format(MESSAGE_ID_EXISTS, municipalityId));
  }
}
