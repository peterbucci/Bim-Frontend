@echo off
REM Batch script to compile and run the Buddy List App

REM Function to compile the Java project
:compile_project
echo Compiling the project...
javac -d bin -cp "src\main\resources;lib\*" src\main\java\**\*.java
goto :eof

REM Function to run a Java instance
:run_instance
set instance_name=%1
echo Running %instance_name%...
java --module-path lib\javafx --add-modules javafx.controls,javafx.fxml -cp "bin;src\main\resources;lib\*" edu.bhcc.bim.App
goto :eof

REM Compile the project
call :compile_project

REM Run the instances
start cmd /c call :run_instance "Instance 1"
start cmd /c call :run_instance "Instance 2"

REM Wait for all background processes to finish
echo All instances have been started.
pause
