
05/02/21 18:23
Assignment03
Team 15 Russet

Tony Dattolo, Connor Mahern, Nolan Cauley

QuickPastes for running the game
cd Desktop/Russet/assignments/assignment03/ForagerCode/src
javac -Xlint:deprecation ForagerController.java ForagerInterface.java ForagerModel.java ForagerView.java
java -Djava.security.policy=permit ForagerModel
java ForagerController

		*Controls*
* WASD for control 'e' for eating resources *
*********************************************

Player Interaction with resources:
Simple solution to this was by adding an another action to listen for which is for the space bar
being hit. In the event of this happening we update the current count for this.resources += 
model.gatherResource(playerId); which is then updated on the view as well. In gatherResource() this
then does the appropriate handling of the resource spot 

implementing Admin player
This process was achieved by checking when a player is added to the HashMap of all players currently
 in the game if whether or not they are the first one to be added
If they are the only one (playerPositions().size == 0), then they are now set to the variable adminId
which will then receive special treatment to start the game as well as select resource distribution.
This is also stored in variables to signify what round we are in as well as the selected distribution.

This involved some adaptations to the model and interface:
Resetting the board after each round
Allowing different cases for resource generation
Constructor now handles multiple rounds and checks to see what game type is selected for resource generation 
Added methods to ModelInterface to use in Model: getRound(), getAdminID(), setGenType(), setStart(), 
First two are pretty self-explanatory in the sense of getters but setGenType() does as follows:
Takes in an int corresponding to the selected resource generation. This is then reflected in the
 genType boolean[] so the model knows how to properly generate our resources. If nothing is selected 
we opted to have all resources generate randomly.
setStart() sets the startConfig boolean to true for the initial game
Added isPlaying Boolean variable to allow functionality between stopping and starting the game.
Otherwise the game will go on forever (as it originally was implemented) we then can interact with
this variable when needed from the timer in order to set the value to 'false' and end the game.
startConfig controls the timer
isPlaying controls the state of the game

In View:
Added another two parameters in displayBoard() in order for it to reach the views and display the
appropriate round. Also for the appropriate genType that is currently updating the board. This is all
done though the Observer pattern being implemented through the relationship between the controller 
and view every time the controller updates its information -> so does the view 
Now there are some JButtons and label for the admin to decide the configuration to start as well as 
now a label for all players to see the current round being played.
View now checks for the adminID against the playerID in order to configure their view specifically 
with admin access. Instead of text-based input we opted all for JButton choice and response. Upon 
clicking the respective values for each generation level we then have the action listener interact 
with the controller in order to set the genType (1, 2, or 3)

A necessary change to compliment the functionality was by changing the collect button for resources 
to 'e' since ' ' spacebar can oftentimes be interpreted as pressing the enter key. This caused some 
issues where the resources were being updated over and over everytime the admin went to collect 
resources. 


In Controller:
We're now obtaining the adminID stored in the model through the constructor through the method 
getAdminID() mentioned before and some other variables to be able to keep track of round, gentype, 
admin, and some others. A key feature was by adding information to be sent to the view. This includes 
data like the round number to properly display what's needed.


Player interaction with other players: Cannot be on same space
We did this by obtaining all locations of players using getPlayerPositions(), if there weren't any 
players, then we could move to that location, otherwise you remain.

The only changes made to this was by an if statement before calling sendPlayerPosition()

Also starting point for player is now randomized rather than being 1,1 every time. This makes the 
game more interesting and makes it so players aren't always beginning on the same spot as another 
player.

Timer for the game:
This is where Added isPlaying Boolean variable to allow functionality between stopping and starting 
the game. Otherwise the game will go on forever (as it originally was implemented) we then can 
interact with this variable when needed from the timer in order to set the value to 'false' and end 
the game. By having it interact with our timer we have elected to have games last a couple minutes 
and when that happens the round resets and then we "start over" even though scores for each player do 
not reset. For the overall game, there is a timer made in the model and once it hits 5 minutes it 
passes a boolean value to the clients to notify them that the game is finished. From here it is then 
passed onto the view where it displays that the game is now over. The timer is called FinalTimer

Growth rate:
We elected for the previous g of .005. this is just slow enough to keep it difficult and actually 
show the progression of resource generation (especially in the selected clusters) but also fast 
enough so the game remains interesting and there isn't a wipeout of resources. Interesting to note, 
this was only tested with about 4 players so who knows the results of having more and more players 
added. In the future this would be best to update the growth param to adjust (faster for more players 
and vice versa for less) but we didn't exactly have time to do so.

Observer pattern is used as detailed above in relation between the View and the Controller

Decorator pattern is used in the player being used with concrete implementations for the corresponding color. 
- RedPlayer for others
- BluePlayer for the main player 
All are implemented from the PlayerInterface to then be decorated as explained above. Depending on 
how they are decorated is their color as well as their respective symbol.

