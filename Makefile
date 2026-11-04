.PHONY: install compile run

# Step 1: Install Scala 3 and make the binaries executable
install:
	@echo "Installing Scala 3..."
	@wget -q https://github.com/lampepfl/dotty/releases/download/3.3.3/scala3-3.3.3.tar.gz
	@tar -xzf scala3-3.3.3.tar.gz
	@mv scala3-3.3.3 /opt/scala3
	@ln -sf /opt/scala3/bin/scala3 /usr/local/bin/scala3
	@ln -sf /opt/scala3/bin/scalac /usr/local/bin/scalac
	@chmod +x /opt/scala3/bin/scala3 /opt/scala3/bin/scalac /usr/local/bin/scala3 /usr/local/bin/scalac
	@echo "Scala 3 installation and permissions set up successfully!"

# Step 2: Compile / Run the Engine and check execution status
compile:
	@chmod +x /usr/local/bin/scala3
	scala3 CapEngine.scala 2>&1; echo "EXIT: $$?"

# Step 3: Run the Script Engine targeting content/test.cap
run:
	java -cp "$$(ls /opt/scala3/*.jar | tr '\n' ':').":. CapScriptEngine content/test.cap
