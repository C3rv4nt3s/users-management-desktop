package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

public record UpdateMunicipalityRequest(
    String id,
    String name,
    String provinceName) {}
