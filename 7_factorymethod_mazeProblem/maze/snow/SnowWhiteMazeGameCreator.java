
package maze.snow; 

import maze.*;

//don't forget to inherit
public class SnowWhiteMazeGameCreator  { 

	public Wall makeWall() { 
		  return null;
		  	  
	  }

	  public Room makeRoom(int roomNumber) { 
		  return null;
		  	  
	  }

	  public Door makeDoor(Room room1, Room room2) { 
		  return null;
	  }
}

/* Solution
public class SnowWhiteMazeGameCreator extends MazeGameCreator  { 

	public Wall makeWall() { 
		  return new SnowWhiteWall();
		  	  
	  }

	  public Room makeRoom(int roomNumber) { 
		  return new SnowWhiteRoom(roomNumber);
		  	  
	  }

	  public Door makeDoor(Room room1, Room room2) { 
		  return new SnowWhiteDoor(room1, room2);
	  }
} */
