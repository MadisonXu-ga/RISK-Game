# ECE 651: Team5-RISC Game

![pipeline](https://gitlab.oit.duke.edu/mz213/team5-risc-game/badges/main/pipeline.svg)
![coverage](https://gitlab.oit.duke.edu/mz213/team5-risc-game/badges/main/coverage.svg?job=test)

## Coverage

[Detailed coverage](https://mz213.pages.oit.duke.edu/team5-risc-game/dashboard.html)

## Game Mechanics

Welcome to our game! Here you will find the latest version of the RISC game from us, team 5.

Here we explain the game mechanics of how it works:

- please open at least 2 terminals
- in the first one run the following commands
  - `.\gradlew installDist` to make sure the file is up to date
  - `.\gradlew run-server`
- open the second terminal
  - `./client/build/install/client/bin/client`
  - choose how many players in total you want in your game [2-4]
- open the other terminals and run the below command in each one
  - `./client/build/install/client/bin/client`

### Territory unit placement

#### Our Rules

- each player will have 50 units at the start which they could place in each of their pre assigned territories at will
- our RISK map has 24 existing territories which are pre assigned to the current players
- each territory already has a starting unit which they can't remove at least until the turn starts
- PS. BE SMART ABOUT YOUR CHOICES! it can mean the difference between winning and losing
- continue placing the units until you run out

### Move and Attack!

- now that you made some good decisions for the territories is time to have some fun!
- Start making moves across your map any time a connecting territory is present
  - make sure you have enough units to do so
- if you feel confident you can even attack your opponent! But how?
  - the attack movement can only be performed from adjacent territories, and will always be performed after the movements have been made, even if they were 'ordered' before them.
  - the result of the attack will be decided by a dice which determines by random chance who wins in a fight 1v1 for each unit that fights
- the game will continue until a winner (the person who gets all the territories) is reached

Have some fun and see you for our V2 of the game!
