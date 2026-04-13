package hiroshi.VisitorProblem;

import java.util.Iterator;

public class DirectoryVisitorBehaviorTest {
    public static void main(String[] args) throws FileTreatmentException {
        Directory root = new Directory("root");
        Directory home = new Directory("home");
        root.add(home);
        home.add(new File("diary.html", 100));
        home.add(new File("memo.txt", 100));
        home.add(new File("todo.txt", 200));

        CountingVisitor visitor = new CountingVisitor();
        root.accept(visitor);

        if (visitor.directories != 2 || visitor.files != 3 || visitor.totalSize != 400) {
            throw new AssertionError(
                "Expected 2 directories, 3 files, size 400 but got "
                    + visitor.directories + ", "
                    + visitor.files + ", "
                    + visitor.totalSize
            );
        }

        FileFindVisitor htmlFinder = new FileFindVisitor(".html");
        root.accept(htmlFinder);
        Iterator found = htmlFinder.getFoundFiles();
        int htmlCount = 0;
        boolean foundDiary = false;
        while (found.hasNext()) {
            Object item = found.next();
            if (!(item instanceof File)) {
                throw new AssertionError("FileFindVisitor should return File entries");
            }
            File file = (File) item;
            htmlCount++;
            foundDiary = foundDiary || file.getName().equals("diary.html");
        }
        if (htmlCount != 1 || !foundDiary) {
            throw new AssertionError("Expected FileFindVisitor to find only diary.html");
        }

        System.out.println("PASS directory visitor file find");
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
