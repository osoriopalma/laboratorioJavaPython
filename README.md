Dinosaur Park Simulation

Simulación de un parque de dinosaurios desarrollada en Java utilizando Maven, PostgreSQL, Docker y Liquibase.

Características
Simulación por pasos (steps)
Gestión de turistas
Dinosaurios carnívoros y herbívoros
Sistema de ataques y escapes
Zonas del parque:
Arrival Zone
Central Hub
Bathroom Zone
Power Plant Zone
Persistencia de eventos en PostgreSQL
Migraciones automáticas con Liquibase
Configuración externa mediante park.properties
Arquitectura basada en estado centralizado (ParkState)
Pruebas unitarias con JUnit 5
Tecnologías utilizadas
Java 17
Maven
PostgreSQL
Docker
Liquibase
JUnit 5
Estructura del proyecto
src
├── main
│   ├── java
│   │   └── com.axity.dinosaurpark
│   └── resources
│       ├── db/changelog
│       └── park.properties
└── test
    └── java
Requisitos
Java 17+
Maven
Docker Desktop
Configuración de PostgreSQL con Docker
Levantar contenedor
docker compose up -d
Verificar contenedor
docker ps
Configuración del proyecto

Archivo:

src/main/resources/park.properties

Ejemplo:

simulation.totalSteps=10
simulation.seed=42

powerplant.initialEnergy=100
powerplant.consumptionPerStep=2
powerplant.failureProbability=0.05
Ejecutar migraciones Liquibase

Las migraciones se ejecutan automáticamente al iniciar la aplicación.

Tablas creadas:

simulation_events
simulation_reports
databasechangelog
databasechangeloglock
Compilar proyecto
mvn compile
Ejecutar simulación
mvn exec:java
Ejecutar pruebas
mvn test
Base de datos
Entrar al contenedor PostgreSQL
docker exec -it dinosaur-park-db psql -U dinosaur_user -d dinosaur_park
Consultar eventos
select * from simulation_events;
Consultar reportes
select * from simulation_reports;
Funcionalidades implementadas
Turistas
Entrada al parque
Movimiento entre zonas
Compras
Uso de SPA
Salida del parque
Dinosaurios
Escape de recintos
Ataques a turistas
Recaptura por guardias
Planta eléctrica
Consumo de energía
Fallas aleatorias
Reparación por técnicos
Arquitectura
ParkState

Centraliza:

turistas
dinosaurios
guardias
técnicos
zonas
métricas
persistencia
SimulationEngine

Responsable de:

orquestar la simulación
ejecutar pasos
coordinar eventos
Pruebas unitarias

Incluye pruebas para:

PowerPlantZone
ArrivalZone
CentralHub
Tourist
Dinosaur
ParkState
Autor

Salvador Osorio Palma

GitHub:

@salvador.osorio.palma
