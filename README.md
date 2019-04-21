# Maze-Generator-and-Solver

This project is a visualization of several simple graph-theory algorithms through the use of a GUI.

![](https://github.com/magnesiumm/Maze-Generator-and-Solver/blob/master/images/maze%20solver%20and%20generator.png)

## How do I start the program?

Simply compile the files in the src folder. The main method is in com.magnesiumm.logic.Run

## What do the menus do?

### Options menu

**Speed Slider** - Changes the speed at which the visualization occurs.

**Real Time (Seizure Warning) Button** - Pressing this button will either enable or disable an option that allows the visualization to occur in real time. The first time you press this button, there's a surprise.

**Reset Button** - Pressing this button will reset the visualization, but not the settings. It will allow you to change the settings.

### Mode menu

**Demo mode** - This is the default, it runs the program in a mode where the generation and solve algorithms are randomly selected. Custom mazes may still be used.

**Custom mode** - This unlocks the generation and solve menus and allows the user to choose their own generation and solve algorithms.

### Maze menu
**Generated maze size slider** - This sets the scaling on the generated maze. It will always try to make the nodes of the maze as square as possible.

**Current maze file path** - Displays the file path of the currently selected maze. "resources/generatedMaze.txt" by default.

**Change maze file path** - Allows you to import a custom maze to be solved - file must be formatted to specifications listed in static variables of the Node class. "resources/maze3.txt" is an example of an importable maze file.

### Generation menu
This menu is only available when custom mode is enabled.

**DFS (Depth-First-Search) random generation** - Generates a maze using a randomized recursive DFS (Depth-First-Search) algorithm.

### Solve menu
This menu is only available when custom mode is enabled.

**DFS (Depth-First-Search)** - Solves the maze using a non-random recursive DFS (Depth-First-Search) algorithm.

**BFS (Breadth-First-Search)** - Solves the maze using a non-random iterative BFS (Breadth-First-Search) algorithm.

### Go button
This button can be used to stop and start the visualization, like a pause button. It can also be activated via the space bar.
