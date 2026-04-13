package hiroshi.VisitorProblem;

import java.util.ArrayList;
import java.util.Iterator;

public class FileFindVisitor extends Visitor {
    private final String extension;
    private final ArrayList found = new ArrayList();

    public FileFindVisitor(String extension) {
        this.extension = extension;
    }

    public Iterator getFoundFiles() {
        return found.iterator();
    }

    public void visit(File file) {
        if (file.getName().endsWith(extension)) {
            found.add(file);
        }
    }

    public void visit(Directory directory) {
        Iterator it = directory.iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            entry.accept(this);
        }
    }
}
