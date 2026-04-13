package headfirst.decorator.io.skeleton;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ShiftInputStream extends FilterInputStream {
    private static final int A = 65;
    private static final int Z = 90;
    private static final int a = 97;
    private static final int z = 122;

    private final int offset;

    public ShiftInputStream(InputStream in, int offset) {
        super(in);
        this.offset = offset;
    }

    public ShiftInputStream(InputStream in) {
        this(in, 0);
    }

    public int read() throws IOException {
        int c = super.read();
        return c == -1 ? -1 : shift(c);
    }

    public int read(byte[] b, int offset, int len) throws IOException {
        int result = super.read(b, offset, len);
        if (result == -1) {
            return -1;
        }
        for (int i = offset; i < offset + result; i++) {
            b[i] = (byte) shift(b[i] & 0xff);
        }
        return result;
    }

    private int shift(int c) {
        if (A <= c && c <= Z) {
            return rotate(c, A, Z);
        }
        if (a <= c && c <= z) {
            return rotate(c, a, z);
        }
        return c;
    }

    private int rotate(int c, int start, int end) {
        int width = end - start + 1;
        int normalized = (c - start + offset) % width;
        if (normalized < 0) {
            normalized += width;
        }
        return start + normalized;
    }
}
