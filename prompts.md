Prompt 1: 
USED: I'm building a Snake game in Java using Swing. Create a single file called SnakeGame.java. It should have a main method that opens a JFrame window that is 600 by 600 pixels and titled Snake. Inside the frame, add a JPanel subclass called GamePanel. Do not add any game logic yet. Just get the window to open correctly.
RESULTS: just a black window
FIXES: none it worked fine
OBSERVATIONS: AI is very efficent


Prompt 2:
USED: Now extend SnakeGame.java. Keep it as one file. Add a dark background grid and draw a starting snake that is three segments long near the center of the board, facing right. Each cell should be a 30x30 pixel square. Draw the snake in green and the background in dark gray. Do not add movement yet.
RESULTS: 3 green cubes in a line one a lined black/gray background, the green cubes are not moving
FIXES: it said "the problem was the wildcard imports causing ambiguity with the Point class  I've changed the imports to be explicit - now it specifically imports java.awt.Point, ArrayList, and List rather than using wildcards"
OBSERVATIONS: although effiecant and easy it still is error prone


Prompt 3: 
USED: Make the snake move automatically using a Swing timer that ticks every 150 milliseconds. Add arrow key controls so the player can steer, but don't allow the snake to reverse direction. For now, have the snake wrap around the edges instead of dying. Make sure the panel can receive keyboard input.
RESULTS: the green cubes now move in a direction based on keyboard input but they dont stop when they get to the edge, they just loop around
FIXES: none it worked
OBSERVATIONS: i can see the game being closer to actual one


Prompt 4:
USED: Add a food pellet that spawns at a random empty cell. When the snake eats it, grow by one segment and spawn new food. Add collision detection: hitting a wall or the snake's own body should end the game, stop movement, and show a "Game Over" message with the final score. Display the current score in the top-left corner during play. When the game is over, let the player press R to reset everything and play again.
RESULTS: a red circle was added and when the green cubes (snake) hits one another appears and now once the snake hits the endge of the screen it ends the game and produces a final score.
FIXES: none
OBSERVATIONS: im kind of bad at these snake games

// additional prompts

Prompt 5: 
USED: change/alter the theme of the game so its a ladybug (instead of the snake) and there is a trail of 1 block behind it and change the food pellet it is chasing to a simple flower shape
RESULTS: the snake turned into a ladybug(1by1 red cube) and the food pellet turned into a simple flower, it also had a trail of black behind it
FIXES: none
OBSERVATIONS: in the next prompt im going to make the ladybug a circle and have the trail behind it be like dotted white lines

