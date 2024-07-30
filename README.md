# BHCC Instant Messenger (BIM) Frontend

## Getting Started

This guide will help you compile and run the messenger app on both Unix-like systems and Windows.

### Prerequisites

- Java Development Kit (JDK)
- Ensure that the backend server is running. You can find the backend repository [here](https://github.com/peterbucci/BIM-Backend).

## Configuration

The application uses a `.env` file for configuration. You need to create this file in the project root directory and define the backend server's host and port.

### Example `.env` File

Create a `.env` file in the project root directory and add the following content:

```dotenv
HOST={host}
PORT={port}
```

### Running on Unix-like Systems

To compile and run the app on Unix-like systems (Linux, macOS), use the provided `run.sh` script.

#### Steps:

1. Open your terminal.
2. Navigate to the project directory.
3. Ensure `run.sh` has execute permissions. If not, you can add them using:

   ```sh
   chmod +x run.sh
   ```

4. Run the script:

   ```sh
   ./run.sh
   ```

### Running on Windows

To compile and run the app on Windows, use the `run.bat` script.

#### Steps:

1. Open Command Prompt.
2. Navigate to the project directory.
3. Run the script:
   ```cmd
   run.bat
   ```

### What Happens When You Run the Scripts

When you run either script, it will start two instances of the Buddy List App. Here is what you can do with the two instances:

1. **Create Accounts**: Enter a username and password to create an account in each instance.
2. **Add a Friend**: Use one instance to add the other instance's user as a friend.
3. **Accept Friend Request**: Switch to the other instance to accept the friend request.
4. **Send Messages**: Message the other user you created.
5. **Test Online/Offline Status**: Log in and out in each instance to see how the user moves between the offline and online lists.

For any issues or contributions, please refer to the repository on [GitHub](https://github.com/peterbucci/Bim-Frontend).
