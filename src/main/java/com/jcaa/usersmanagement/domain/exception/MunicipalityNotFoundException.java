package com.jcaa.usersmanagement.domain.exception;

public final class MunicipalityNotFoundException extends DomainException {

  private static final String MESSAGE_BY_ID = "The municipality with id '%s' was not found.";

  private MunicipalityNotFoundException(final String message) {
    super(message);
  }

  public static MunicipalityNotFoundException becauseIdWasNotFound(final String municipalityId) {
    return new MunicipalityNotFoundException(String.format(MESSAGE_BY_ID, municipalityId));
  }
}
