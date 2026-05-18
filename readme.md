Nedaj: Gas Station Management System (MVP CLI)
An object-oriented, production-grade Command Line Interface (CLI) application built in Java to address critical fuel logistics, shortages, and distribution transparency challenges in Ethiopia. This software serves as a Minimum Viable Product (MVP) to bridge the real-time information gap between everyday commuters/drivers and service station administrators.

📋 Table of Contents
Project Overview & Context

Key Features

System Architecture

Project Workflow & Lifecycle

Domain Data Model Mapping

Design Patterns & OOP Principles Applied

Installation & Setup

Usage Guide

Future Development Roadmap

🌍 Project Overview & Context
In urban and suburban centers across Ethiopia (such as Addis Ababa, Hawassa, Adama, and Bahir Dar), fuel distribution gridlocks regularly cause long commuter lines, unproductive travel times, and severe congestion at retail stations. Drivers frequently navigate blindly from one neighborhood (Zone) to another searching for specific fuel variants—Benzene, Regular (Diesel), or Kerosene—only to find depleted station inventories.

Nedaj solves this systemic operational vulnerability by providing an intuitive, rapid-lookup console framework. It functions via a dual-actor paradigm:

Drivers: Query instantaneous real-time fuel availability sorted strictly by geographical proximity/zone criteria to avoid blind transit.

Station Administrators: Access a protected administration terminal to instantly toggle resource availability configurations and update volume metrics as trucks replenish tanks or pumps run dry.

✨ Key Features
Layered Architectural Decoupling: Implements a strict separation between Presentation (CLI), Business Logic (Services), and Data/State Layers (Models).

State-Driven Inventory System: Dynamically handles boolean availability logic synchronized with continuous real-time volume capacities (Liters).

Geographical Clustering: Segregates station routing criteria into operational sub-districts (e.g., Bole, Megenagna, Piazza, Kazanchis) for efficient localization lookup.

Robust Input Validation: Standardizes text and input normalization across the application to eliminate manual user typos or buffer crashes via programmatic Enum constraints.


🗺️ System Architecture
The software is engineered around a 3-Tier Layered Architecture Style to isolate technical responsibilities and ensure high-testability codebases.

       +-------------------------------------------------------+
       |                  PRESENTATION LAYER                   |
       |  - ConsoleMenu (Renders interface menus, handles IO)   |
       |  - Main (Bootstrap configuration & runtime loop)     |
       +----------------------------+--------------------------+
                                    |
                                    v [Requests DTOs / Method Invocation]
       +-------------------------------------------------------+
       |                     SERVICE LAYER                     |
       |  - GasStationService (Implements lookup queries,     |
       |                       admin mutations, data seed)    |
       +----------------------------+--------------------------+
                                    |
                                    v [Manipulates / Reads Engine Domain Entities]
       +-------------------------------------------------------+
       |                     MODEL LAYER                       |
       |  - GasStation  - FuelInventory  - FuelType (Enum)     |
       +-------------------------------------------------------+
Class Blueprint & Responsibility Grid
FuelType (Enum): Strong domain types enforcing standard invariants (BENZENE, REGULAR, KEROSENE).

FuelInventory (Class): Represents discrete tracking units. Encapsulates pricePerLiter, quantityLiters, and isAvailable status metrics.

GasStation (Class): Aggregates inventory state records mapped via a polymorphic structure (Map<FuelType, FuelInventory>) along with unique id, corporate operating name, and localized metropolitan zone.

GasStationService (Class): The centralized state manager orchestrating filtering routines and mutations. Hosts in-memory structural collections acting as mock persistence providers.

🔄 Project Workflow & Lifecycle
The execution lifecycle transitions sequentially from system boot up to active interaction looping.

  [ SYSTEM START ]
         │
         ▼
  ┌─────────────────────────────────────────┐
  │ Initialize Application Environment      │
  │ Instantiate Centralized Services        │
  └────────────────────────┬────────────────┘
                           │
                           ▼
  ┌─────────────────────────────────────────┐
  │ Execute Mock Seed Processor             │
  │ (Pre-populates sample stations in RAM)  │
  └────────────────────────┬────────────────┘
                           │
                           ▼
             //═════════════════════════\\
            //    Is Application Exit    \\
           <      Signal Requested?       >
            \\═════════════════════════//
                        │       ▲
               [No]     │       │ [Returns To System Menu]
                        ▼       │
             //═════════════════════════\\
            //      Main System Menu     \\
           <   Choose Role Context Screen >
            \\═════════════════════════//
                   │               │
      [1] Driver Terminal   [2] Administrator Terminal
                   │               │
                   ▼               ▼
  ┌────────────────────────┐     ┌────────────────────────┐
  │ Input Selection:       │     │ Authenticate Credentials│
  │ Target Zone & FuelType │     │ & Input Unique StationID│
  └────────────┬───────────┘     └───────────┬────────────┘
               │                             │
               ▼                             ▼
  ┌────────────────────────┐     ┌────────────────────────┐
  │ Scan Active Inventory; │     │ Render Inventory Matrix│
  │ Extract Matches        │     │ Request Modification   │
  └────────────┬───────────┘     └───────────┬────────────┘
               │                             │
               ▼                             ▼
  ┌────────────────────────┐     ┌────────────────────────┐
  │ Display Matching Output│     │ Mutate In-Memory States│
  │ Ranked Matrix          │     │ (Apply Changes Instantly)
  └────────────┬───────────┘     └───────────┬────────────┘
               │                             │
               └─────────────────────────────┘
📊 Domain Data Model Mapping
The object relationship diagram maps individual composite items directly into structural properties:

+-----------------------------------------------------------------+
|                           GasStation                            |
+-----------------------------------------------------------------+
| - id : String                                                   |
| - name : String                                                 |
| - zone : String                                                 |
| - inventory : Map<FuelType, FuelInventory>                      |
+-----------------------------------------------------------------+
| + getId() : String                                              |
| + getName() : String                                            |
| + getZone() : String                                            |
| + getInventory() : Map<FuelType, FuelInventory>                 |
| + addFuelInventory(type : FuelType, inv : FuelInventory) : void |
+-----------------------------------------------------------------+
                                  |
                                  | 1 handles multi-level mapping
                                  v
+-----------------------------------------------------------------+
|                          FuelInventory                          |
+-----------------------------------------------------------------+
| - pricePerLiter : double                                        |
| - quantityLiters : double                                       |
| - isAvailable : boolean                                         |
+-----------------------------------------------------------------+
| + getPricePerLiter() : double                                   |
| + setPricePerLiter(price : double) : void                       |
| + getQuantityLiters() : double                                  |
| + setQuantityLiters(qty : double) : void                        |
| + isAvailable() : boolean                                       |
| + setAvailable(status : boolean) : void                         |
+-----------------------------------------------------------------+
🛠️ Design Patterns & OOP Principles Applied
To satisfy core standard specifications for industrial evaluations, the system leverages foundational paradigms:

Encapsulation: State attributes across GasStation and FuelInventory are securely initialized as private scopes. Visibility and mutability properties are exposed exclusively via defensive, verified programmatic Accessors/Mutators (getters/setters).

Single Responsibility Principle (SRP): ConsoleMenu retains processing workflows explicitly constrained to input extraction and textual rendering. All sorting matrices and mutation filtering actions are offloaded cleanly onto GasStationService.

Abstraction via Domain Modeling: Technical implementations shield calling blocks from algorithmic complexities. For instance, testing for availability hides underlying map tracking mechanisms behind transparent business calls like searchAvailableFuel(...).

⚙️ Installation & Setup
Prerequisites
Java Development Kit (JDK): Version 8, 11, or 17+ installed on your host system environment.

Environment Variables: Verify path tools using terminal inputs:

Bash
java -version
javac -version
Execution Steps
Clone or extract the repository archive to your preferred local system directory:

Bash
git clone https://github.com/ABK1-Kuul/nedaj.git
cd Nedaj
Compile all structural component packages via command console lines:

Bash
javac src/models/*.java src/services/*.java src/presentation/*.java -d bin
Boot up the runtime executable target:

Bash
java -cp bin presentation.Main
📖 Usage Guide
When execution successfully initialises, operators interact smoothly using basic input workflows:

Driver Target Lookups
Select option [1] to access the driver search terminal interface.

Enter the zone keyword target when prompted (e.g., Bole).

Select preferred fuel option from the enumerated menu listing (1 for Benzene, 2 for Regular, 3 for Kerosene).

The system outputs a structured terminal table displaying station options that have active fuel quantities matching your search.

Station Administrator Inventories
Select option to access the secure administrative terminal pane.

Provide your station validation key ID tag (e.g., ST001).

Choose the target commodity inventory sector to update.

Input modern quantity variations (e.g., 4500 liters) or override Boolean flags (true/false) to broadcast current storage statuses instantly across the system.

🚀 Future Development Roadmap
As this implementation transitions past validation milestones, developmental sprints will scale components into enterprise environments:

Persistent Database Engines: Swap out volatile in-memory collections with structured SQL/NoSQL engines (PostgreSQL) coupled via JDBC repositories.

Geospatial Tracking APIs: Integrate Haversine algorithmic geometry and GPS matrix models to switch spatial calculations from textual zones to precise distance calculations.

Distributed Interfaces: Build RESTful controller sets with Spring Boot to feed state streams directly to React Native and Flutter client apps for end-user drivers.