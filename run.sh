#!/bin/bash

# Function to compile the Java project
compile_project() {
    echo "Compiling the project..."
    javac -d bin -cp "src/main/resources:lib/*" src/main/java/**/*.java
}

# Function to run a Java instance
run_instance() {
    local instance_name=$1
    echo "Running $instance_name..."
    java --module-path lib/javafx --add-modules javafx.controls,javafx.fxml -cp "bin:src/main/resources:lib/*" edu.bhcc.bim.App
}

# Compile the project
compile_project

# Run the instances
run_instance "Instance 1" &
run_instance "Instance 2" &

# Wait for all background processes to finish
wait

echo "All instances have exited."
