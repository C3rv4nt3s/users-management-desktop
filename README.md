# users-menagement-desktop-hezagonal-ddd
Ejemplo de una aplicación Desktop que hace CRUDL y control de acceso de Usuarios, escrita en Java,  arquitectura Hexagonal y enfoque DDD, usando buenas practicas y Clean code
Este es un ejemplo de Proyecto Java con hexagonal para los alumnos de UdeC

---

## Extensión del proyecto: entidad `Municipality` (CEA – Censo de población)

**Estudiante:** Alexander Junior Cervantes De La Ossa · **Código:** 7502520044  
**Asignatura:** Desarrollo de Software (Universidad de Cartagena) · **Tutor:** John Carlos Arrieta

Se agregó el CRUDL de la entidad **Municipality** (municipio) del caso de estudio asignado (Censo de población),
siguiendo la misma estructura de capas, convenciones de nombres y patrones del módulo `User`.
La entidad `Municipality` es el catálogo que necesita la historia de usuario HU-001 de la Unidad 1
("Requiere el catálogo de municipios cargado").

### Modelo de dominio

| Campo | Tipo (Value Object) | Regla |
|---|---|---|
| `id` | `MunicipalityId` | Código de 5 dígitos (2 de provincia + 3 de municipio, ej. `13034`) |
| `name` | `MunicipalityName` | Obligatorio, mínimo 3 caracteres |
| `provinceName` | `ProvinceName` | Obligatorio, mínimo 3 caracteres |

El código de provincia (`provinceCode()`) se deriva de los dos primeros dígitos del id.

### Arquitectura hexagonal aplicada

| Concepto | Dónde está |
|---|---|
| Entidad y Value Objects | `domain/model/MunicipalityModel`, `domain/valueobject/Municipality*`, `ProvinceName` |
| Excepciones de dominio | `domain/exception/*Municipality*Exception`, `InvalidProvinceNameException` |
| Eventos de dominio | `domain/event/Municipality{Created,Updated,Deleted}DomainEvent` |
| **Puertos de entrada** (casos de uso) | `application/port/in/{Create,Update,Delete,GetById,GetAll}Municipality*UseCase` |
| **Puertos de salida** | `application/port/out/{Save,Update,Delete,GetById,GetAll}Municipality*Port` |
| Servicios (implementan los casos de uso) | `application/service/*MunicipalityService` |
| **Adaptador de salida (memoria)** | `infrastructure/adapter/persistence/repository/MunicipalityRepositoryInMemory` |
| **Adaptador de entrada (CLI)** | `infrastructure/entrypoint/desktop/{controller,dto,mapper,cli}` |
| Composición de dependencias | `infrastructure/config/DependencyContainer` |

### Flujo de ejecución (crear un municipio)

`CLI (CreateMunicipalityHandler)` → `MunicipalityController` → `MunicipalityDesktopMapper` →
`CreateMunicipalityUseCase` (puerto de entrada) → `CreateMunicipalityService` →
`SaveMunicipalityPort` (puerto de salida) → `MunicipalityRepositoryInMemory` (adaptador de memoria).

El dominio y la aplicación **no conocen** la CLI ni el almacenamiento: para cambiar la persistencia (por ejemplo,
a MySQL o Firebase) solo se escribe otro adaptador que implemente los mismos puertos de salida.

### Uso (CLI)

Al ejecutar la aplicación, el menú incluye las nuevas opciones:

```
[7]  List all municipalities
[8]  Find municipality by ID
[9]  Create municipality
[10] Update municipality
[11] Delete municipality
```

Los casos de uso originales de `User` (opciones 1 a 6) siguen funcionando sin cambios.
Los municipios se guardan en memoria: se pierden al cerrar la aplicación.

### Pruebas

```bash
mvn test
```
