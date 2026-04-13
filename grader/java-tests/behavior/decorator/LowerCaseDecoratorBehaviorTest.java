package grader.behavior.decorator;

import headfirst.decorator.io.skeleton.LowerCaseInputStream;
import headfirst.decorator.io.skeleton.ShiftInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LowerCaseDecoratorBehaviorTest {
    public static void main(String[] args) throws IOException {
        byte[] buffer = new byte[32];
        LowerCaseInputStream stream = new LowerCaseInputStream(
            new ByteArrayInputStream("HeLLo PATTERN".getBytes(StandardCharsets.UTF_8))
        );

        int count = stream.read(buffer, 0, buffer.length);
        String text = new String(buffer, 0, count, StandardCharsets.UTF_8);
        if (!"hello pattern".equals(text)) {
            throw new AssertionError("Expected lower-case bulk read, got " + text);
        }
        if (stream.read() != -1) {
            throw new AssertionError("Expected EOF to remain -1");
        }

        LowerCaseInputStream single = new LowerCaseInputStream(
            new ByteArrayInputStream("A".getBytes(StandardCharsets.UTF_8))
        );
        if (single.read() != 'a') {
            throw new AssertionError("Expected single-byte read to lower-case A");
        }

        assertReadAll("Tpguxbsf", new ShiftInputStream(bytes("Software"), 1));
        assertReadAll("Z12YWz#", new ShiftInputStream(bytes("A12ZXa#"), -1));
        assertReadAll("Pattern", new ShiftInputStream(bytes("Pattern")));
        assertReadAll(
            "qbuufso",
            new ShiftInputStream(new LowerCaseInputStream(bytes("PATTERN")), 1)
        );

        System.out.println("PASS decorator stream filters");
    }

    private static ByteArrayInputStream bytes(String text) {
        return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }

    private static void assertReadAll(String expected, ShiftInputStream stream) throws IOException {
        byte[] buffer = new byte[64];
        int count = stream.read(buffer, 0, buffer.length);
        String actual = new String(buffer, 0, count, StandardCharsets.UTF_8);
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected shifted text " + expected + ", got " + actual);
        }
        if (stream.read() != -1) {
            throw new AssertionError("Expected EOF to remain -1");
        }
    }
}
