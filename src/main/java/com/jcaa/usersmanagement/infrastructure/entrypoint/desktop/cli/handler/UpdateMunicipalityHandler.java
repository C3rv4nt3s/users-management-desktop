package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.MunicipalityNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.MunicipalityResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.MunicipalityController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.MunicipalityResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateMunicipalityRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateMunicipalityHandler implements OperationHandler {

  private final MunicipalityController municipalityController;
  private final ConsoleIO console;
  private final MunicipalityResponsePrinter printer;

  @Override
  public void handle() {
    final String id           = console.readRequired("Municipality ID : ");
    final String name         = console.readRequired("New name        : ");
    final String provinceName = console.readRequired("New province    : ");

    try {
      final MunicipalityResponse updated =
          municipalityController.updateMunicipality(
              new UpdateMunicipalityRequest(id, name, provinceName));
      console.println("\n  Municipality updated successfully.");
      printer.print(updated);
    } catch (final MunicipalityNotFoundException exception) {
      console.println("  Not found: " + exception.getMessage());
    }
  }
}
