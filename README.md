Nick Schrandt
CS351
3D Game of Life

This program is a 3D implementation of Conway's Game of Life, wherein cells inhabit a 3-Dimenstional grid and live
or die according to parameters specified by the user. There are also 5 preset simulations that can be run that produce
interesting patterns.

Parameters:
Low Population Death: If a living cell has fewer than this many neighbors, it will die.
Over Population Death: If a cell has more than this many neighbors, it will die.
Low Population Birth: If an empty space has at least this many neighbors, it will come to life.
High population Birth: If an empty space has at most this many neighbors, it will come to life.

Note: Both the low birth and high birth parameters must be met for a space to come to life.

Controls:
During the simulation, the user can press the Up and Down arros to zoom in and out. They can also press the R key to
restart the simulation and return to the start screen.

Citations:
The Main.java class was originally the Molecule.java class from the previous project, but heavily altered. Most of what
remains is the camera functionality and creation.

Future Plans:
Make main menu AI prettier
Continue to increase user controls such as:
    *Button to take a single step in the simulation.
    *Rotation controls
