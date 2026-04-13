package grader.behavior.composite;

import hiroshi.directoryCompositeProblem.Directory;
import hiroshi.directoryCompositeProblem.Entry;
import hiroshi.directoryCompositeProblem.File;
import hiroshi.directoryCompositeProblem.FileTreatmentException;

public class CompositeBehaviorTest {
    public static void main(String[] args) throws FileTreatmentException {
        Directory root = new Directory("root");
        Directory bin = new Directory("bin");
        Directory tmp = new Directory("tmp");
        File vi = new File("vi", 10000);
        File latex = new File("latex", 20000);

        root.add(bin);
        root.add(tmp);
        bin.add(vi);
        bin.add(latex);

        assertEquals(30000, root.getSize(), "root size should aggregate children");
        assertEquals("root", root.getName(), "directory name");
        assertEquals("vi", vi.getName(), "file name");
        assertEquals("/root/bin", bin.getFullName(), "directory full path");
        assertEquals("/root/bin/vi", vi.getFullName(), "file full path");
        assertLeafRejectsChildren(vi);

        System.out.println("PASS composite full path behavior");
    }

    private static void assertLeafRejectsChildren(Entry file) {
        try {
            file.add(new File("illegal", 1));
            throw new AssertionError("File leaf should reject add()");
        } catch (FileTreatmentException expected) {
            return;
        }
    }

    private static void assertEquals(int expected, int actual, String label) {
        if (expected != actual) {
            throw new AssertionError(label + ": expected " + expected + ", got " + actual);
        }
    }

    private static void assertEquals(String expected, String actual, String label) {
        if (!expected.equals(actual)) {
            throw new AssertionError(label + ": expected " + expected + ", got " + actual);
        }
    }
}
