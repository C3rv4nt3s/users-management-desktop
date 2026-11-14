package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

public record CreateMunicipalityRequest(
    String id,
    String name,
    String provinceName) {}
