# meme-programming-language (Scala 3 Engine Runner)

An advanced compiler-accurate processing pipeline powered by a **Scala 3 compilation engine (CapEngine)** that parses and executes `.cap` script files written using internet meme and slang dialects.

## Repository Layout

* `_data/keywords.json`: Central Scala 3 token map configuration dictionary.
* `content/`: Contains basic-meme-scripts and root test scripts (`test.cap`) for the automated runtime pipeline.
* `.scalafmt.conf` / `.scalafix.conf`: Prettier-equivalent Scala 3 indentation rules and ESLint-equivalent structural linter constraints.
* `.sdkmanrc`: NVM-equivalent automated Java and Scala environment locker.
* `CapEngine.scala` & `Makefile`: Main Scala processing pipeline compiler and cross-platform automated toolchain interface engine.

## Getting Started

1. **Clone the Workspace**:
   ```bash
   git clone https://github.com/GoogleHub67/meme-programming-language
   cd meme-programming-language
   ```
2. **Install Engine Dependencies**:
   ```bash
   make install
   ```
3. **Compile and Run**:
   ```bash
   make compile
   make run
   ```

## Technical Syntax Specifications

The compiler translates slang operations into Scala 3 types. For complete syntax examples including control gates (`vibe_check.cap`) and monadic option/collection pipelines (`options_and_safety.cap`), refer to the source files in the repository or inspect `_data/keywords.json` to modify mapping tokens.

## Auxiliary Code Quality Dotfiles

* **Code Formatter (`.scalafmt.conf`)**: Enforces rigid indentation constraints matching Scala 3 compiler dialects.
* **Static Linter (`.scalafix.conf`)**: Audits code patterns to strip unused imports and catch issues before compilation.
* **Version Locker (`.sdkmanrc`)**: Locks runtime environments to Java JDK 21 and Scala 3.3.3.

## License
Distributed under the open-source **MIT License**.
