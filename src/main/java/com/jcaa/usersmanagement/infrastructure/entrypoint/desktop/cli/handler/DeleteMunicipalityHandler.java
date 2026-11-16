package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.MunicipalityNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.MunicipalityController;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DeleteMunicipalityHandler implements OperationHandler {

  private final MunicipalityController municipalityController;
  private final ConsoleIO console;

  @Override
  public void handle() {
    final String id = console.readRequired("Municipality ID to delete: ");
    try {
      municipalityController.deleteMunicipality(id);
      console.println("  Municipality deleted successfully.");
    } catch (final MunicipalityNotFoundException exception) {
      console.println("  Not found: " + exception.getMessage());
    }
  }
}
