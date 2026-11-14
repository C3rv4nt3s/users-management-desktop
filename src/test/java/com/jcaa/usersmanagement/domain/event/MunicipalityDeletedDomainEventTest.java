package com.jcaa.usersmanagement.domain.event;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para MunicipalityDeletedDomainEvent.
 *
 * <p>Este evento es el más simple: recibe un MunicipalityId y genera un payload con una sola
 * entrada. Se verifica el nombre del evento, que occurredOn quede registrado, que el accessor
 * devuelva la misma referencia y que el payload contenga exactamente un campo.
 */
@DisplayName("MunicipalityDeletedDomainEvent")
class MunicipalityDeletedDomainEventTest {

  private static final String ID = "13034";

  // ── eventName

  @Test
  @DisplayName("eventName debe retornar la constante 'municipality.deleted'")
  void shouldHaveEventNameMunicipalityDeleted() {
    // Arrange
    final MunicipalityDeletedDomainEvent event =
        new MunicipalityDeletedDomainEvent(new MunicipalityId(ID));

    // Act
    final String result = event.getEventName();

    // Assert
    assertEquals("municipality.deleted", result);
  }

  // ── occurredOn

  @Test
  @DisplayName("occurredOn no debe ser nulo y debe quedar acotado al instante de construcción")
  void shouldRecordOccurredOnAtCreationTime() {
    // Arrange
    final LocalDateTime before = LocalDateTime.now();
    final MunicipalityDeletedDomainEvent event =
        new MunicipalityDeletedDomainEvent(new MunicipalityId(ID));
    final LocalDateTime after = LocalDateTime.now();

    // Act
    final LocalDateTime occurredOn = event.getOccurredOn();

    // Assert
    assertNotNull(occurredOn, "occurredOn no debe ser null");
    assertFalse(occurredOn.isBefore(before), "occurredOn debe ser >= al instante anterior");
    assertFalse(occurredOn.isAfter(after), "occurredOn debe ser <= al instante posterior");
  }

  // ── municipalityId

  @Test
  @DisplayName("getMunicipalityId() debe devolver la misma instancia recibida en el constructor")
  void shouldReturnSameMunicipalityIdInstance() {
    // Arrange
    final MunicipalityId municipalityId = new MunicipalityId(ID);
    final MunicipalityDeletedDomainEvent event = new MunicipalityDeletedDomainEvent(municipalityId);

    // Act
    final MunicipalityId result = event.getMunicipalityId();

    // Assert
    assertSame(municipalityId, result);
  }

  // ── payload()

  @Test
  @DisplayName("payload() debe contener únicamente la entrada 'id'")
  void shouldReturnPayloadWithOnlyId() {
    // Arrange
    final MunicipalityDeletedDomainEvent event =
        new MunicipalityDeletedDomainEvent(new MunicipalityId(ID));

    // Act
    final Map<String, String> payload = event.payload();

    // Assert
    assertAll(
        "payload de MunicipalityDeletedDomainEvent",
        () -> assertEquals(1, payload.size(), "el mapa debe tener exactamente 1 entrada"),
        () -> assertEquals(ID, payload.get("id"), "id"));
  }
}
