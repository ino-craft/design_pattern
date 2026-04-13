package grader.behavior.factory;

import maze.Direction;
import maze.MapSite;
import maze.Maze;
import maze.MazeGameAbstractFactory;
import maze.MazeGameCreator;
import maze.MazeFactory;
import maze.MazePrototypeFactory;
import maze.Room;

public class FactoryBehaviorTest {
    public static void main(String[] args) {
        Maze defaultMaze = new MazeGameCreator().createMaze();
        assertClassName(defaultMaze.findRoom(1), "maze.Room", "default factory method room");

        Maze harryMaze = new maze.harry.HarryPotterMazeGameCreator().createMaze();
        assertTheme(harryMaze, "maze.harry.HarryPotter", "Harry factory method");

        Maze harryAbstractFactoryMaze = MazeGameAbstractFactory.createMaze(new maze.harry.HarryPotterMazeFactory());
        assertTheme(harryAbstractFactoryMaze, "maze.harry.HarryPotter", "Harry abstract factory");

        Maze snowCreatorMaze = new maze.snow.SnowWhiteMazeGameCreator().createMaze();
        assertTheme(snowCreatorMaze, "maze.snow.SnowWhite", "Snow factory method");

        MazeFactory snowFactory = new maze.snow.SnowWhiteMazeFactory();
        Maze snowAbstractFactoryMaze = MazeGameAbstractFactory.createMaze(snowFactory);
        assertTheme(snowAbstractFactoryMaze, "maze.snow.SnowWhite", "Snow abstract factory");

        MazePrototypeFactory prototypeFactory = new MazePrototypeFactory(
            snowFactory.makeMaze(),
            snowFactory.makeWall(),
            snowFactory.makeRoom(0),
            snowFactory.makeDoor(null, null)
        );
        Maze snowPrototypeMaze = MazeGameAbstractFactory.createMaze(prototypeFactory);
        assertTheme(snowPrototypeMaze, "maze.snow.SnowWhite", "Snow prototype factory");

        System.out.println("PASS factory snow families and prototype");
    }

    private static void assertTheme(Maze maze, String classPrefix, String label) {
        Room room = maze.findRoom(1);
        assertClassName(room, classPrefix + "Room", label + " room");
        assertSiteClass(room.getSide(Direction.EAST), classPrefix + "Wall", label + " wall");
        assertSiteClass(room.getSide(Direction.WEST), classPrefix + "Door", label + " door");
    }

    private static void assertSiteClass(MapSite site, String expectedName, String label) {
        assertClassName(site, expectedName, label);
    }

    private static void assertClassName(Object value, String expectedName, String label) {
        if (value == null || !value.getClass().getName().equals(expectedName)) {
            String actual = value == null ? "null" : value.getClass().getName();
            throw new AssertionError(label + " should be " + expectedName + ", got " + actual);
        }
    }
}
