
package maze.snow; 

import maze.*;

// don't forget to inherit
public class SnowWhiteMazeFactory  { 
  
  // create the Wall
  public Wall makeWall() { 
	  return null;	  	  
  }

  // create the Room
  public Room makeRoom(int roomNumber) { 
	  return null;	  	  
  }

  // create the Door
  public Door makeDoor(Room room1, Room room2) { 
	  return null;
	  	  
  }
}

/* Solution 
 * 
 public class SnowWhiteMazeFactory extends MazeFactory { 

  public Wall makeWall() { 
	  return new SnowWhiteWall();
	  	  
  }

  public Room makeRoom(int roomNumber) { 
	  return new SnowWhiteRoom(roomNumber);
	  	  
  }

  public Door makeDoor(Room room1, Room room2) { 
	  return new SnowWhiteDoor(room1, room2);
	  	  
  }
}
 */


