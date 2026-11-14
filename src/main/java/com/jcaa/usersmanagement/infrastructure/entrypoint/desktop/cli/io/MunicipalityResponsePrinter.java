package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.MunicipalityResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class MunicipalityResponsePrinter {

  private static final String SEPARATOR = "-".repeat(52);
  private static final String ROW_FORMAT = "  %-14s : %s%n";

  private final ConsoleIO console;

  public void print(final MunicipalityResponse response) {
    console.println(SEPARATOR);
    console.printf(ROW_FORMAT, "ID", response.id());
    console.printf(ROW_FORMAT, "Name", response.name());
    console.printf(ROW_FORMAT, "Province", response.provinceName());
    console.printf(ROW_FORMAT, "Province code", response.provinceCode());
    console.println(SEPARATOR);
  }

  public void printList(final List<MunicipalityResponse> municipalities) {
    if (municipalities.isEmpty()) {
      console.println("  No municipalities found.");
      return;
    }
    console.printf("%n  Total: %d municipality(ies)%n", municipalities.size());
    municipalities.forEach(this::print);
  }
}
