# 📨 Inquiry Manager

A multi-threaded **client–server inquiry management system** written in **Java**.

Customers use a console client to submit questions, requests and complaints, track their status, and cancel them. On the server side, service agents are registered, logged in and automatically assigned to open inquiries, with each inquiry handled concurrently in its own thread.

The project demonstrates object-oriented design, socket networking, multithreading, file-based persistence and Java Reflection — without any external libraries or databases.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Domain Model](#domain-model)
- [Communication Protocol](#communication-protocol)
- [Concurrency Model](#concurrency-model)
- [Persistence](#persistence)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Key Concepts and Technologies](#key-concepts-and-technologies)
- [Author](#author)

---

## Overview

The repository contains two standalone applications:

| Component | Directory | Responsibility |
|-----------|-----------|----------------|
| **Server** | `IntelliJTraining_inquirymanager_chanadavidov` | Listens on port `8888`, maintains the inquiry and agent queues, assigns agents to inquiries, and persists all data to disk. |
| **Client** | `ClientSide` | Console application that connects to the server, presents an action menu, and sends requests. |

Both applications share the same model classes (the `Shared` package). Objects are exchanged over the network using standard **Java Serialization**.

---

## Features

### Inquiry management
- Submit a new inquiry of one of three types: **Question**, **Request** or **Complaint**.
- List all open inquiries.
- Look up the status of an inquiry by its code.
- Cancel an open inquiry.
- Report the number of inquiries opened in a given month.

### Agent management
- Register a new agent (persisted to disk).
- Remove an agent from the system.
- Agent **login** — the agent becomes available to handle inquiries.
- Agent **logout** — the agent is removed from the pool of available agents.

### Automatic processing
- A background dispatcher continuously pairs the next open inquiry with an available agent.
- Each inquiry is processed in its own thread, allowing several inquiries to be handled in parallel.
- Treated or canceled inquiries are moved to a history store.

---

## Architecture

```
┌──────────────────────┐        RequestObj        ┌───────────────────────────────┐
│   Client (console)   │ ───────────────────────▶ │  InquiryManagerServer :8888   │
│ InquiryManagerClient │ ◀─────────────────────── │   └─ ClientHandler (thread)   │
└──────────────────────┘        ResponseObj       │        └─ ServerService       │
                                                   │             └─ InquiryManager │
                                                   │                  ├─ queues    │
                                                   │                  └─ HandleFiles│
                                                   └───────────────────────────────┘
                                                                   │
                                                                   ▼
                                                        File system (text / CSV)
```

The server is organised into four layers:

1. **Network layer** — `InquiryManagerServer` accepts incoming connections and spawns a `ClientHandler` thread per client.
2. **Service layer** — `ServerService` receives a `RequestObj`, dispatches it by action type and returns a `ResponseObj`.
3. **Business layer** — `InquiryManager` owns the queues, agent assignment and inquiry logic.
4. **Data layer** — `HandleFiles` and `HandleFilesReflection` read and write objects to the file system.

---

## Project Structure

```
project-inquirymanager/
├── ClientSide/                                  # Client application
│   └── src/
│       ├── Main.java                            # Client entry point
│       ├── clientData/
│       │   └── InquiryManagerClient.java        # Console menu and socket communication
│       ├── Shared/                              # Model classes shared with the server
│       ├── business/
│       └── HandleStoreFiles/
│
└── IntelliJTraining_inquirymanager_chanadavidov/    # Server application
    ├── nextVal.txt                              # Persisted counter for the next inquiry code
    └── src/
        ├── Main.java                            # Server entry point
        ├── Server/
        │   ├── InquiryManagerServer.java        # ServerSocket listening on port 8888
        │   ├── ClientHandler.java               # One thread per connected client
        │   └── ServerService.java               # Routes each request to its handler
        ├── business/
        │   ├── InquiryManager.java              # Core logic: queues, agents, statistics
        │   ├── InquiryHandling.java             # Thread that processes a single inquiry
        │   └── InquiryHandlingTask.java         # Runnable that pairs an agent with an inquiry
        ├── Shared/
        │   ├── Inquiry.java                     # Abstract base class
        │   ├── Question.java
        │   ├── Request.java
        │   ├── Complaint.java
        │   ├── Representative.java              # Service agent
        │   ├── RequestObj.java                  # Client → server message
        │   └── ResponseObj.java                 # Server → client message
        ├── HandleStoreFiles/
        │   ├── IForSaving.java                  # Contract for persistable objects
        │   ├── HandleFiles.java                 # Save, read and delete text files
        │   └── HandleFilesReflection.java       # Generic CSV persistence via Reflection
        └── testSaveFile/                        # Manual tests for the persistence layer
```

---

## Domain Model

### `Inquiry` (abstract)

| Field | Type | Description |
|-------|------|-------------|
| `code` | `Integer` | Unique identifier, generated from the `nextCodeVal` counter |
| `description` | `String` | Free-text description |
| `creationDate` | `LocalDateTime` | Time of creation |
| `status` | `Status` | Current lifecycle state |

### Inquiry types

| Class | Purpose | Additional fields | Simulated handling time | Thread priority |
|-------|---------|-------------------|-------------------------|-----------------|
| `Question` | A general question | — | 5 seconds | **High** (10) |
| `Request` | A service request | — | 3 seconds | Normal |
| `Complaint` | A customer complaint | `assignedBranch` | 5 seconds | Normal |

### Inquiry lifecycle

```
OPEN ──▶ IN_TREATMENT ──▶ TREATED    (moved to history)
  │
  └─────────────────────▶ CANCELED   (moved to history)
```

### `Representative`

| Field | Description |
|-------|-------------|
| `code` | Internal sequential number |
| `name` | Agent name |
| `id` | Agent identifier |
| `handledInquiriesIds` | Codes of the inquiries the agent has handled |

---

## Communication Protocol

Every exchange is a single round trip: the client sends a `RequestObj` (an **action** plus an optional **parameter**), and the server replies with a `ResponseObj` (a **status code**, a **message** and a **result**).

| Action | Parameter | Result |
|--------|-----------|--------|
| `ADD_INQUIRY` | `Inquiry` | Code of the new inquiry |
| `GET_ALL` | — | List of all open inquiries |
| `GET_INQUIRY_STATUS` | Inquiry code (`int`) | Current status |
| `GET_COUNT_BY_MONTH` | Month (`1–12`) | Number of inquiries opened that month |
| `CANCEL_INQUIRY` | Inquiry code (`int`) | Success, or *not found* |
| `AGENT_LOGIN` | `Representative` | Login confirmation |
| `AGENT_LOGOUT` | Agent ID (`int`) | Logout confirmation |
| `ADD_AGENT` | `Representative` | Registration confirmation |
| `REMOVE_AGENT` | Agent ID (`int`) | Removal confirmation |
| `TEST` | — | `SERVER_READY` (health check) |

### Response status codes

| Code | Meaning |
|------|---------|
| `200` | Success |
| `400` | Invalid request or unsupported action |
| `404` | Requested item not found |
| `500` | Internal server error |

---

## Concurrency Model

`InquiryManager` maintains the following queues:

| Queue | Contents |
|-------|----------|
| `QInquiry` | Open inquiries waiting to be handled |
| `QRepresentative` | All registered agents |
| `QActiveRepresentative` | Logged-in agents available for work |

### Processing flow

1. On startup, the server loads open inquiries, registered agents and the `nextVal` counter from disk.
2. A background dispatcher thread watches for the moment both an open inquiry and an available agent exist.
3. When they do, it creates an `InquiryHandlingTask` that pairs the two and runs it on a new thread.
4. `InquiryHandling` simulates the work according to the inquiry type; questions run with elevated priority.
5. When handling completes, the inquiry is marked `TREATED`, moved to history, and the agent is returned to the pool.

Independently of inquiry processing, the server serves multiple clients at once — each connection runs in its own `ClientHandler` thread.

---

## Persistence

The system does not use a database. All data is stored as text files in the server's working directory:

| Location | Contents |
|----------|----------|
| `Shared.Question/`, `Shared.Request/`, `Shared.Complaint/` | One file per open inquiry (`<code>.txt`) |
| `history/` | Treated and canceled inquiries |
| `Representative/` | One file per agent (`<id>.txt`) |
| `nextVal.txt` | Next inquiry code, so numbering continues across restarts |

Two persistence strategies are implemented:

- **`HandleFiles`** — works with any class implementing `IForSaving`. Each class defines its own folder name, file name, serialised form (`getData`) and how to rebuild itself from that form (`parseFromFile`).
- **`HandleFilesReflection`** — a generic CSV serializer built on **Java Reflection**. The fully qualified class name is written as the first value; on load, the class is instantiated by name and its fields are populated automatically, with no class-specific code.

---

## Getting Started

### Prerequisites

- **JDK 17 or later** (the code uses switch expressions and pattern matching for `instanceof`)
- **IntelliJ IDEA** (recommended)

### Clone the repository

```bash
git clone https://github.com/chanadavidov732-ship-it/project-inquirymanager.git
cd project-inquirymanager
```

### 1. Start the server

1. Open `IntelliJTraining_inquirymanager_chanadavidov` as a project in IntelliJ.
2. Run `Server/InquiryManagerServer.java`.
   Alternatively, run `Main.java`, which first prompts for two sample inquiries in the console and then starts the server.
3. When the server is ready, it prints:

   ```
   Inquiry Manager Server started on port 8888
   ```

### 2. Start the client

1. Open `ClientSide` as a separate project.
2. In `ClientSide/src/Main.java`, set the server address. To run both on the same machine:

   ```java
   InquiryManagerClient i = new InquiryManagerClient("localhost", 8888);
   ```

3. Run `Main.java`. On a successful connection the client prints `client success connect to server`.

> **Note:** The server must be running before the client starts. Multiple clients can connect to the same server simultaneously. Data folders are created in the server's working directory.

---

## Usage

Once connected, the client displays the main menu:

```
press:
1 - get all inquiries
2 - create new inquiry
3 - get inquiry`s status
4 - get monthly count
5 - cancel inquiry
6 - Agent Login
7 - Agent Logout
8 - Add New Agent
9 - Remove Agent
10 - exit
```

### Example: submitting a complaint

```
> 2
press:
1 - create complaint inquiry
2 - create question inquiry
3 - create request inquiry
> 1
enter description
> The order arrived damaged
enter assignedBranch
> Jerusalem
your request over successfully
the server massage is :
SUCCESS
5
```

The returned value (`5` in this example) is the inquiry code. Use it with option **3** to check the inquiry's status, or option **5** to cancel it.

---

## Key Concepts and Technologies

| Concept | Where it appears |
|---------|------------------|
| **Inheritance and polymorphism** | `Question`, `Request` and `Complaint` extend `Inquiry`, each providing its own `handling()` implementation |
| **Interfaces** | `IForSaving` defines a uniform contract for every persistable object |
| **Socket networking** | `ServerSocket` / `Socket` with `ObjectInputStream` / `ObjectOutputStream` |
| **Serialization** | Model and message classes implement `Serializable` |
| **Multithreading** | A thread per client, a background dispatcher, a thread per inquiry, and thread priorities |
| **Concurrent collections** | `ConcurrentLinkedQueue` and queue-based task management |
| **Reflection** | Generic object persistence and instantiation by class name |
| **File I/O** | `FileWriter`, `Scanner`, `java.nio.file.Files` |
| **Streams API** | Monthly statistics using `Stream.concat` and `filter` |

---

## Author

**Chana Davidov**
GitHub: [chanadavidov732-ship-it](https://github.com/chanadavidov732-ship-it)
