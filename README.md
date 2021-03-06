# 5_in_a_row

## Project Features

This project is based off the well-known 2 player Connect-4 game and the task is to implement a variation of the game.

Instructions include:
* The winner of the game connects 5 counters.
* The width of the game board is 9.
* The heigth of the game board is 6.

The ```src/Game/Game.java``` class has been extended to allow heigth, width and counter variations of all values with the only restriction being that the counter value cannot be larger than the width or heigth. The Game.java program checks for this (server side) and will throw an exception if the user does not operate within this constraint throwing a custom InvalidServerGameInputException. To change these default values a user can amended the main method of the ```src/Server/Server.java``` class.

The Game class is thoroughy tested in the ```/src/test/Game/GameTest.java``` class. The easiest way to run the JUNIT tests is by importing the project to Eclipse or another Java IDE and running the test class from there.

Additionally, because it is more difficult to unit test socket connections, a series of program tests have been run to test the robustness of the program: 
* When client-server disconnections occur abruptly on either side.
* Invalid inputs are sent to the server from the client.
* The client wishes to forefeit/end the game manually.

All socket connection failures are taken care of through catching of IOExceptions where if either a client or server encounters a connection failure, a meaningful message is printed to the screen and and program ends safely.

If a invalid input occurs from the client (e.g String input for the game or index out of range), the client side code should always catch the error and print a corresponding message. The server side also checks for such errors to ensure no modification of the client side code has occured to penetrate the server.

If the client wishes forefit the game, the client can enter 'end' when their turn becomes available.The server deals with the message, informing the other client that they are the nominated winner and all connections close safely.

## Running The Program

Bash scripts are provided under the ```src/``` directory. The user should run all 3 bash scripts on 3 different command prompts. The scripts should be run in the following order:

1. ```bash Server_1.sh```
2. ```bash Client_2.sh```
3. ```bash Client_3.sh```

## JUnit Test Output

As stated above, unit testing of the ```Game.java``` class tests all methods of the class. Tests for boundary conditions and exceptions thrown are carfully implemented. 

The Junit tests implemented are independent of counter, heigth and width variables chosen. The default for the below test are values 5, 6 and 9 as specified in the instructions. 

Note: methods in the ```Game.java``` class that count the number of counters in a row are declared public for testing purposes. Ideally, these methods should be declared private as the method ```checkWin()``` calls these methods locally.

![JUnit Tests Output](Images/Testing_Output/img1.PNG)

## Example Game Senarios

### Senario 1

The below images show initiating the Client-Server connection, client valid and invalid inputs and a client forefitting the game by 'end' input.

Client Output:
![Game Clients](Images/Game_1_Senario/img1.png)

Server Output:
![Game Server](Images/Game_1_Senario/img2.png)

### Senario 2

The below images show a player inputting to a column that is already full (player 2) and also player 1 initiating an abrupt disconnection.

Client Output:
![Game Clients](Images/Game_2_Senario/img1.png)

Server Output:
![Game Server](Images/Game_2_Senario/img2.png)

### Senario 3

The below images show a player winning a game.

Client Output:
![Game Clients](Images/Game_2_Senario/img1.png)

Server Output:
![Game Server](Images/Game_2_Senario/img2.png)
