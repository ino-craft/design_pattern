package maze.snow;

import maze.Door;
import maze.MazeGameCreator;
import maze.Room;
import maze.Wall;

public class SnowWhiteMazeGameCreator extends MazeGameCreator {
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
