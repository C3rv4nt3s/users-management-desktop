package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidMunicipalityIdException;
import java.util.Objects;
import java.util.regex.Pattern;

public record MunicipalityId(String value) {

  private static final Pattern FORMAT = Pattern.compile("\\d{5}");
  private static final int PROVINCE_CODE_LENGTH = 2;

  public MunicipalityId {
    final String normalizedValue =
        Objects.requireNonNull(value, "MunicipalityId cannot be null").trim();
    validateNotEmpty(normalizedValue);
    validateFormat(normalizedValue);
    value = normalizedValue;
  }

  /** The first two digits of the id identify the province the municipality belongs to. */
  public String provinceCode() {
    return value.substring(0, PROVINCE_CODE_LENGTH);
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidMunicipalityIdException.becauseValueIsEmpty();
    }
  }

  private static void validateFormat(final String normalizedValue) {
    if (!FORMAT.matcher(normalizedValue).matches()) {
      throw InvalidMunicipalityIdException.becauseFormatIsInvalid();
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
