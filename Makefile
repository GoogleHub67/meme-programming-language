# OS Architecture Detection Hook
ifeq ($(OS),Windows_NT)
    # Windows native execution paths and commands
    RM_DIR = if exist /opt/scala3 rmdir /S /Q /opt/scala3
    NULL_OUTPUT = >nul 2>&1
    
    # Conditional logic block for setup/permissions
    define INSTALL_ENV
    @echo "Windows environment detected. Skipping apt/wget Linux toolchains..."
    @echo "Please ensure Scala 3 binaries are manually accessible in your Windows %PATH%."
    endef
    
    COMPILE_CMD = scalac CapEngine.scala
    RUN_CMD = java -cp ".;/opt/scala3/*" CapScriptEngine content/basic-meme-scripts/test.cap
else
    # Linux / Debian native execution paths and commands
    RM_DIR = sudo rm -rf /opt/scala3 scala3-3.3.3.tar.gz
    NULL_OUTPUT = >/dev/null 2>&1
    
    # Restored your exact sequential Linux dependencies pipeline
    define INSTALL_ENV
    wget -q https://github.com/lampepfl/dotty/releases/download/3.3.3/scala3-3.3.3.tar.gz && \
    tar -xzf scala3-3.3.3.tar.gz && \
    sudo mv scala3-3.3.3 /opt/scala3 && \
    sudo ln -sf /opt/scala3/bin/scala3 /usr/local/bin/scala3 && \
    sudo ln -sf /opt/scala3/bin/scalac /usr/local/bin/scalac && \
    sudo chmod +x /opt/scala3/bin/scala3 /opt/scala3/bin/scalac /usr/local/bin/scala3 /usr/local/bin/scalac
    endef
    
    COMPILE_CMD = @chmod +x /usr/local/bin/scala3 && scala3 CapEngine.scala 2>&1; echo "EXIT: $$?"
    RUN_CMD = java -cp "$$(ls /opt/scala3/*.jar | tr '\n' ':').":. CapScriptEngine content/basic-meme-scripts/test.cap
endif

.PHONY: install compile run clean

# Step 1: Detect architecture and setup Scala 3 environment
install:
	@echo "Detected Operating System Focus: [$(if $(filter Windows_NT,$(OS)),Windows CMD,Debian Linux)]"
	@echo "Installing Scala 3 and setting up binary symbolic link configurations..."
	$(INSTALL_ENV)
	@echo "Scala 3 environment installation and permissions set up successfully!"

# Step 2: Compile / Run the Engine and check exit status codes
compile:
	@echo "Analyzing compilation diagnostics across the workspace engine core..."
	$(COMPILE_CMD)

# Step 3: Run the Script Engine targeting content/test.cap
run:
	@echo "Invoking the custom parsing pipeline on content/basic-meme-scripts/test.cap..."
	$(RUN_CMD)
	@echo "Don't forget to check out the other files too!"
	@echo "You can run them by passing their path to the engine."

# Bonus Step: Clean workspace development caches safely across cross-platforms
clean:
	@echo "Wiping module dependency locks and cleaning target binaries..."
	@$(RM_DIR) $(NULL_OUTPUT) || true
	@echo "Workspace directory cleaned successfully."
