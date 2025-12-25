# NexusGraph - XML Editor and Social Network Analyzer

A comprehensive Java-based XML Editor with Social Network Analysis capabilities, featuring both a Command Line Interface (CLI) and a modern JavaFX Graphical User Interface (GUI).

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
  - [GUI Mode](#gui-mode)
  - [CLI Mode](#cli-mode)
  - [Interactive Shell](#interactive-shell)
- [Command Reference](#command-reference)
- [Project Structure](#project-structure)
- [Test Files](#test-files)
- [Building the Project](#building-the-project)
- [Contributors](#contributors)
- [License](#license)

---

## Overview

NexusGraph is a versatile tool designed to handle XML file processing and social network data analysis. The project is divided into two main functional levels:

- **Level 1**: Core XML processing operations including validation, formatting, minification, JSON conversion, and compression/decompression.
- **Level 2**: Social network analysis features including graph visualization, user influence analysis, mutual followers detection, and post searching.

---

## Features

### Level 1 - XML Processing

| Feature               | Description                                                      |
| --------------------- | ---------------------------------------------------------------- |
| **XML Validation**    | Validate XML syntax and structure with automatic error detection |
| **XML Fixing**        | Automatically repair malformed XML files                         |
| **Pretty Formatting** | Format XML with proper indentation for readability               |
| **Minification**      | Remove unnecessary whitespace to reduce file size                |
| **JSON Conversion**   | Convert XML files to JSON format                                 |
| **Compression**       | Compress XML files using Huffman encoding                        |
| **Decompression**     | Decompress previously compressed files                           |

### Level 2 - Social Network Analysis

| Feature                    | Description                                                  |
| -------------------------- | ------------------------------------------------------------ |
| **Graph Visualization**    | Generate visual representation of social network connections |
| **Most Active Users**      | Identify users with the highest activity levels              |
| **Most Influential Users** | Find users with the most followers/influence                 |
| **Mutual Followers**       | Discover common followers between multiple users             |
| **User Suggestions**       | Get follow suggestions for a specific user                   |
| **Post Search**            | Search posts by keyword or topic                             |

---

## Technologies

- **Java 17** - Core programming language
- **JavaFX 21** - Modern GUI framework
- **Maven** - Build and dependency management
- **Graphviz-Java 0.18.1** - Graph visualization (pure Java, no external dependencies)
- **SLF4J** - Logging framework

---

## Prerequisites

Before running the project, ensure you have the following installed:

- Java Development Kit (JDK) 17 or higher
- Apache Maven 3.6 or higher

---

## Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/yourusername/NexusGraph.git
   cd NexusGraph
   ```

2. **Navigate to the project directory**

   ```bash
   cd NexusGraph
   ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

---

## Usage

### GUI Mode

Launch the graphical user interface:

```bash
mvn javafx:run
```

Or run without arguments to enter interactive mode, then type:

```
xml_editor
```

### CLI Mode

Run commands directly from the command line:

```bash
java -jar target/xml-editor-1.0-SNAPSHOT.jar <command> [options]
```

### Interactive Shell

Run the application without arguments to enter the interactive shell:

```bash
java -jar target/xml-editor-1.0-SNAPSHOT.jar
```

You will see a prompt where you can enter commands:

```
xml_editor>
```

---

## Command Reference

### Level 1 - XML Processing Commands

| Command        | Syntax                                        | Description                                    |
| -------------- | --------------------------------------------- | ---------------------------------------------- |
| **verify**     | `verify -i <file.xml> [-f] [-o <output.xml>]` | Validate XML file, use `-f` to auto-fix errors |
| **format**     | `format -i <file.xml> -o <output.xml>`        | Pretty-print XML with proper indentation       |
| **mini**       | `mini -i <file.xml> -o <output.xml>`          | Minify XML by removing whitespace              |
| **json**       | `json -i <file.xml> -o <output.json>`         | Convert XML to JSON format                     |
| **compress**   | `compress -i <file.xml> -o <output.comp>`     | Compress XML using Huffman encoding            |
| **decompress** | `decompress -i <file.comp> -o <output.xml>`   | Decompress a compressed file                   |

### Level 2 - Social Network Analysis Commands

| Command             | Syntax                                 | Description                             |
| ------------------- | -------------------------------------- | --------------------------------------- |
| **draw**            | `draw -i <file.xml> [-o <output.png>]` | Generate network graph visualization    |
| **most_active**     | `most_active -i <file.xml>`            | Find most active users in the network   |
| **most_influencer** | `most_influencer -i <file.xml>`        | Find most influential users             |
| **mutual**          | `mutual -i <file.xml> -ids <1,2,3>`    | Find mutual followers between users     |
| **suggest**         | `suggest -i <file.xml> -id <user_id>`  | Get follow suggestions for a user       |
| **search**          | `search -i <file.xml> -w <word>`       | Search posts containing a specific word |
| **search**          | `search -i <file.xml> -t <topic>`      | Search posts by topic                   |

### Other Commands

| Command               | Description                    |
| --------------------- | ------------------------------ |
| `xml_editor` or `gui` | Launch the GUI                 |
| `help`                | Display all available commands |
| `exit` or `quit`      | Exit the program               |

---

## Project Structure

```
NexusGraph/
├── NexusGraph/
│   ├── pom.xml                          # Maven configuration
│   └── src/
│       └── main/
│           └── java/
│               └── org/
│                   └── example/
│                       ├── Main.java                    # Application entry point
│                       ├── GUI/                         # JavaFX GUI components
│                       │   ├── GuiApplication.java      # GUI launcher
│                       │   ├── EditorWindow.java        # Main editor window
│                       │   ├── ButtonPanel.java         # Action buttons
│                       │   ├── FileBrowser.java         # File selection
│                       │   ├── InputArea.java           # Text input area
│                       │   ├── OutputArea.java          # Text output area
│                       │   └── Level2Panel.java         # Network analysis panel
│                       ├── Level_1/                     # XML Processing
│                       │   ├── XmlEditorCLI.java        # CLI handler
│                       │   ├── XMLValidator.java        # XML validation
│                       │   ├── FormatingFile.java       # XML formatting
│                       │   ├── XmlMinifier.java         # XML minification
│                       │   ├── XmlToJsonConverter.java  # XML to JSON
│                       │   ├── Compression.java         # File compression
│                       │   ├── Decompression.java       # File decompression
│                       │   ├── HuffmanEncoder.java      # Huffman encoding
│                       │   ├── HuffmanNode.java         # Huffman tree node
│                       │   ├── XMLDecompressor.java     # XML decompression
│                       │   └── Xml_Rearder.java         # XML reader utility
│                       └── Level_2/                     # Social Network Analysis
│                           ├── Graph.java               # Graph data structure
│                           ├── GraphBuilder.java        # Graph construction
│                           ├── Graph2Photo.java         # Graph visualization
│                           ├── NetworkAnalyzer_2.java   # Network analysis
│                           ├── SocialNetworkLoader.java # Network data loader
│                           ├── User.java                # User model
│                           ├── Post.java                # Post model
│                           ├── Vertex.java              # Graph vertex
│                           └── Pair.java                # Utility class
├── Test_Files/                          # Sample test files
│   ├── *.xml                            # Sample XML files
│   ├── *.json                           # Sample JSON files
│   └── *.comp                           # Compressed files
└── README.md                            # This file
```

---

## Test Files

The `Test_Files` directory contains sample files for testing:

- **XML Files**: `1.xml`, `2.xml`, `Network.xml` - Sample social network data
- **JSON Files**: `3.json`, `js1.json` - JSON format examples
- **Compressed Files**: `*.comp` - Pre-compressed files for decompression testing
- **Fixed Files**: `Fix_*.xml` - Examples of fixed/formatted XML

---

## Building the Project

### Compile the Project

```bash
mvn clean compile
```

### Run Tests

```bash
mvn test
```

### Create Executable JAR

```bash
mvn package
```

This creates a fat JAR with all dependencies at:

```
target/xml-editor-1.0-SNAPSHOT.jar
```

### Run the Application

```bash
java -jar target/xml-editor-1.0-SNAPSHOT.jar
```

---

## Contributors

This project was developed as part of a Data Structures and Algorithms course project.

---

## License

This project is for educational purposes.

---

**NexusGraph** - Simplifying XML Processing and Social Network Analysis
