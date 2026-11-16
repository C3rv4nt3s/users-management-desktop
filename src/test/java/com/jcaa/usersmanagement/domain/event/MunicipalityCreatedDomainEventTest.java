package com.jcaa.usersmanagement.domain.event;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityName;
import com.jcaa.usersmanagement.domain.valueobject.ProvinceName;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para MunicipalityCreatedDomainEvent.
 *
 * <p>Se verifica que el nombre del evento sea correcto, que occurredOn quede registrado, que el
 * accessor devuelva la misma referencia del modelo y que el payload contenga los cuatro campos.
 */
@DisplayName("MunicipalityCreatedDomainEvent")
class MunicipalityCreatedDomainEventTest {

  private static MunicipalityModel buildMunicipality() {
    return MunicipalityModel.create(
        new MunicipalityId("13034"),
        new MunicipalityName("Ciudad Real"),
        new ProvinceName("Ciudad Real"));
  }

  // ── eventName

  @Test
  @DisplayName("eventName debe retornar la constante 'municipality.created'")
  void shouldHaveCorrectEventName() {
    // Arrange
    final MunicipalityCreatedDomainEvent event = new MunicipalityCreatedDomainEvent(buildMunicipality());

    // Act
    final String result = event.getEventName();

    // Assert
    assertEquals("municipality.created", result);
  }

  // ── occurredOn

  @Test
  @DisplayName("occurredOn no debe ser nulo y debe quedar acotado al instante de construcción")
  void shouldRecordOccurredOnAtCreationTime() {
    // Arrange
    final LocalDateTime before = LocalDateTime.now();
    final MunicipalityCreatedDomainEvent event = new MunicipalityCreatedDomainEvent(buildMunicipality());
    final LocalDateTime after = LocalDateTime.now();

    // Act
    final LocalDateTime occurredOn = event.getOccurredOn();

    // Assert
    assertNotNull(occurredOn, "occurredOn no debe ser null");
    assertFalse(occurredOn.isBefore(before), "occurredOn debe ser >= al instante anterior");
    assertFalse(occurredOn.isAfter(after), "occurredOn debe ser <= al instante posterior");
  }

  // ── municipality

  @Test
  @DisplayName("getMunicipality() debe devolver la misma instancia recibida en el constructor")
  void shouldReturnSameMunicipalityInstance() {
    // Arrange
    final MunicipalityModel municipality = buildMunicipality();
    final MunicipalityCreatedDomainEvent event = new MunicipalityCreatedDomainEvent(municipality);

    // Act
    final MunicipalityModel result = event.getMunicipality();

    // Assert
    assertSame(municipality, result);
  }

  // ── payload()

  @Test
  @DisplayName("payload() debe contener id, name, provinceName y provinceCode")
  void shouldReturnPayloadWithAllFields() {
    // Arrange
    final MunicipalityCreatedDomainEvent event = new MunicipalityCreatedDomainEvent(buildMunicipality());

    // Act
    final Map<String, String> payload = event.payload();

    // Assert
    assertAll(
        "payload de MunicipalityCreatedDomainEvent",
        () -> assertEquals(4, payload.size(), "el mapa debe tener exactamente 4 entradas"),
        () -> assertEquals("13034", payload.get("id"), "id"),
        () -> assertEquals("Ciudad Real", payload.get("name"), "name"),
        () -> assertEquals("Ciudad Real", payload.get("provinceName"), "provinceName"),
        () -> assertEquals("13", payload.get("provinceCode"), "provinceCode"));
  }
}
