# meme-programming-language (Scala 3 Engine Runner)

This repository contains the **CapEngine** pipeline, structured for quick deployment and execution across terminal environments using a unified Makefile interface.

## Repository Structure

```text
├── content/
│   ├── keywords.json      # Configuration data for all memes and brainrots
│   └── test.cap           # Script file for processing
├── CapEngine.scala        # Main Scala engine source
├── Makefile               # Automated execution steps
└── README.md              # Project documentation
```

## Getting Started

### Clone Repository
```bash
git clone https://github.com/GoogleHub67/meme-progamming-language
cd meme-programming-language
```

### Step 1: Install Dependencies
Download Scala 3, configure system symbolic links, and unlock tool execution permissions:
```bash
make install
```

### Configuration Notes
* Check `content/keywords.json` to view or modify all supported memes and brainrot terms used by the parser.

### Step 2: Compile & Test Engine
Run the primary Scala compilation engine step and verify its exit status code:
```bash
make compile
```

### Step 3: Run Script Engine
Execute the runner target using Java, parsing the target script located at `content/test.cap`:
```bash
make run
```
