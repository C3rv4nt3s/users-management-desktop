package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.MunicipalityNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.MunicipalityResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.MunicipalityController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.MunicipalityResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FindMunicipalityByIdHandler implements OperationHandler {

  private final MunicipalityController municipalityController;
  private final ConsoleIO console;
  private final MunicipalityResponsePrinter printer;

  @Override
  public void handle() {
    final String id = console.readRequired("Municipality ID: ");
    try {
      final MunicipalityResponse municipality = municipalityController.findMunicipalityById(id);
      printer.print(municipality);
    } catch (final MunicipalityNotFoundException exception) {
      console.println("  Not found: " + exception.getMessage());
    }
  }
}
