# Checkers
 A Checkers Game based on JavaFX

Checkers is a very popular game all over the world. The first attempts to build the first English draughts computer program were in the early 1950s. In this project, I have implemented an automated player for the game of checkers.

The game of checkers is considered a
complicated game with 10^20 possible legal
positions in the English draughts version (8 х 8
board) alone (much more on higher
dimensions). My strategy is to build a Minimax
algorithm based agent, along with potential
changes, which is the state of the art in
one-on-one games. We start with the
implementation of a simple minimax player
with a simple heuristic evaluation, and By
introducing alpha-beta pruning and also, we
enhance the pruning. The agent has been
created which is capable of playing the game of
draughts or checkers with a remarkable win
rate against average players.

I choose Java Programming language for the
implementation of this application because it is
an Object-Oriented Language and it provides
all necessary data structures required to
implement a Checkers game.
The MVC (Model-View-Controller)
architectural pattern was used in the
development of the software to separate the
game logic from the Graphical Interface, thus
making code easier to understand and maintain
