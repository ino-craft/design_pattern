package maze.harry;

import maze.Direction;
import maze.MapSite;
import maze.Maze;
import maze.MazeGameAbstractFactory;
import maze.MazeGameCreator;
import maze.Room;

public class FactoryBehaviorTest {
    public static void main(String[] args) {
        Maze defaultMaze = new MazeGameCreator().createMaze();
        assertRoom(defaultMaze.findRoom(1), Room.class, "default factory method room");

        Maze harryMaze = new HarryPotterMazeGameCreator().createMaze();
        Room harryRoom = harryMaze.findRoom(1);
        assertRoom(harryRoom, HarryPotterRoom.class, "Harry factory method room");
        assertSite(harryRoom.getSide(Direction.EAST), HarryPotterWall.class, "Harry wall");
        assertSite(harryRoom.getSide(Direction.WEST), HarryPotterDoor.class, "Harry door");

        Maze abstractFactoryMaze = MazeGameAbstractFactory.createMaze(new HarryPotterMazeFactory());
        Room abstractFactoryRoom = abstractFactoryMaze.findRoom(1);
        assertRoom(abstractFactoryRoom, HarryPotterRoom.class, "Harry abstract factory room");

        System.out.println("PASS factory product families");
    }

    private static void assertRoom(Room room, Class<? extends Room> expected, String label) {
        if (room == null || !expected.isInstance(room)) {
            throw new AssertionError(label + " should be " + expected.getSimpleName());
        }
    }

    private static void assertSite(MapSite site, Class<?> expected, String label) {
        if (site == null || !expected.isInstance(site)) {
            throw new AssertionError(label + " should be " + expected.getSimpleName());
        }
    }
}
