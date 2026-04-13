package grader.behavior.visitor;

import hiroshi.VisitorProblem.Directory;
import hiroshi.VisitorProblem.Entry;
import hiroshi.VisitorProblem.File;
import hiroshi.VisitorProblem.FileTreatmentException;
import hiroshi.VisitorProblem.Visitor;
import java.util.Iterator;

public class DirectoryVisitorBehaviorTest {
    public static void main(String[] args) throws FileTreatmentException {
        Directory root = new Directory("root");
        Directory home = new Directory("home");
        root.add(home);
        home.add(new File("memo.txt", 100));
        home.add(new File("todo.txt", 200));

        CountingVisitor visitor = new CountingVisitor();
        root.accept(visitor);

        if (visitor.directories != 2 || visitor.files != 2 || visitor.totalSize != 300) {
            throw new AssertionError(
                "Expected 2 directories, 2 files, size 300 but got "
                    + visitor.directories + ", "
                    + visitor.files + ", "
                    + visitor.totalSize
            );
        }

        System.out.println("PASS directory visitor traversal");
    }

    private static class CountingVisitor extends Visitor {
        private int directories;
        private int files;
        private int totalSize;

        public void visit(File file) {
            files++;
            totalSize += file.getSize();
        }

        public void visit(Directory directory) {
            directories++;
            Iterator iterator = directory.iterator();
            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                entry.accept(this);
            }
        }
    }
}
