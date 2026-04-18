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


Prompt 6: 
USED: change the shape of the lady bug into a circle. change the trail behind into white dotted lines, like the ladybug is flying. AFter each time the flower has been collected, change the color
FIXES: i wasnt clear enough the first time so i corrected it and said that it should be the flower changing color not the ladybug. the lady bug should stay red, but then that caused an error (an error, it says that PURPLE cannot be resolved or is not  a field ava(33554503) {Ln 38, Col 114})
RESULTS: at first it was the ladybug that changed colors, then i corrected it and changed it so the flower changed colors. Its a red cirlce(ladybug) with a trail of white dotted lines and each time the ladybug hits the flower it changes color
OBSERVATIONS: I realized i need to be precise with my prompt


Prompt 7: when the lady bug hits the flower, change the color of the petals each time but keep the center of it white. Each time the ladybug hits a flower increase the speed of the ladybug by 20 milliseconds
FIXES: i told it to change it so that the lady bug increase speed only 5 milisceonds instead
RESULTS: each time the lady bug hits the flower, its speed increase a little bit and the color of the petals change
OBSERVATIONS: for the next prompts im going to have it create a button to save previous score, reset score and add most recent score to accumulate best total score


Prompt 8: 
USED: before the game starts make a start screen with the the words "LadyBug in Spring" and below that it should have a button that says start the game. Then below the button it should put the most recent score, if this is the first time playing it should say N/A. 
FIXES: no fixes, it worked how i wanted it to, and in next prompt im going to make and end/restart screen
OBSERVATIONS: it might just be mine but there is already a lot of code on there for such a simple game, i cant imagine what complex open world games are like
