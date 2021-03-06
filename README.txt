Implements Dots Game - classic pen-and-paper game. Two players put dots of different color one by one on a grid. If manhattan distance between two dots of the same color equals 1 or 2, one can connect such dots. The goal is to build polygons around surround enemy's dots. One who captures more dots than other wins. The dots already captured may not be used no more - they become "not available".

Players only put dots on a field by mouse click. A polygon is built automatically after player adds new dot if at least one can be built. If more than one, those that surrounds more enemy's dots will be built. RedCount and BlueCount fields show count of captured enemy's dots for each player. Color label indicates color of Player who is about to make move.

Each dot is a vertex of an undirected graph. Auto-building polygons is implemented by finding cycles in that graph. While detecting cycles during DFS, adjacent vertexes for each vertex is sorted by manhattan distance from this vertex: this need to be done to avoid edges self-crossing.

Grid side and opposite colors can be set in com.DotGame.DotGameConstant class.

What I've learnt:
- some experience in building my own graph, implementing cycle detecting during DFS;
- implementing custom hash function for my own class (com.DotGame.Path class in this case) to use it in HashSet;
- create Comparator, overriding compareTo method, equals method;
- first steps in using Junit Tests;
- how to create .jar file;
- you can not make custom Comparator for primitives without Wrapping :(
- not handle exception is a bad practice;
- to delete from collection while iterating: use {new Iterator(); if condition Iterator.remove(//without argument)
  or Collection.removeIf(var - > condition)

An interesting way to find whether a polygon strongly contains point https://stackoverflow.com/questions/42968171/how-to-determine-if-a-grid-cell-is-within-an-enclosed-perimeter-of-cells

