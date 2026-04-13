package grader.behavior.decorator;

import headfirst.decorator.io.skeleton.LowerCaseInputStream;
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

        System.out.println("PASS lowercase decorator stream");
    }
}
