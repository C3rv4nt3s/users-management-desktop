package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.MunicipalityResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.MunicipalityController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.MunicipalityResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ListMunicipalitiesHandler implements OperationHandler {

  private final MunicipalityController municipalityController;
  private final MunicipalityResponsePrinter printer;

  @Override
  public void handle() {
    final List<MunicipalityResponse> municipalities = municipalityController.listAllMunicipalities();
    printer.printList(municipalities);
  }
}
