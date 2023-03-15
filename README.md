# Battleship

This is a text-based Battleship game created as a project for ECE 651 - Software Engineering at Duke University in the Spring semester of 2023.

I started learning Java three weeks prior to starting this project. My primary goals were: 1) Learn the language features of Java deeply,
2) Practice incremental test-driven development, and 3) Follow SOLID design principles and use object-oriented design patterns to build
software that is amenable to change.

The game was released in two versions:

   ### Version 1
   
   * Players had only one type of action: fire at the enemy board.
   * There were four types of ships (submarine, destroyer, battleship, carrier), but all of them were $n$ x 1 rectangles and could only be placed
     in two orientations (Horizontal or Vertical).
   * The professor gave us a detailed task breakdown and walkthrough for how to implement and test Version 1.
   
   ### Version 2: Three equirements changes
   
   * Players gained two new action types:
        - Do a sonar scan of the enemy board by specifying a central coordinate for the scan. For each ship type, the player receives the number of
          squares within the scope of the scan (a diamond centered at the specified coordinate) occupied by that ship type. Limited to three uses per
          player per game.
        - Move one of your own ships to a new square. Limited to three uses per player per game.
   * Two new ship types
        - Battleships are now shaped like a T and can be placed in four orientations (Left, Right, Up, Down).
        - Carriers are now shaped like a Z and can be placed in four orientations (same as above).
   * Choose among three options at the start of the game
        - Player vs. Player
        - Player vs. Computer
        - Computer vs. Computer
   * It was left entirely up to us how to implement these new features. No walkthrough was given to us, only requirements specifications.
   
 ### Directions for playing the game from the command line
 * From the highest level of the repository, enter the following two commands in sequence:
 
        ./gradlew installDist
        app/build/install/app/bin/app
