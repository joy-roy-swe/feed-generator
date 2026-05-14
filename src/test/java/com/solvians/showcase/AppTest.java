package com.solvians.showcase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {

    @Test
    public void expectTwoIntArgs() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            App.main(new String[]{"xxx"});
        });
        NumberFormatException numbers = Assertions.assertThrows(NumberFormatException.class, () -> {
            App.main(new String[]{"xxx", "zzz"});
        });
        numbers = Assertions.assertThrows(NumberFormatException.class, () -> {
            App.main(new String[]{"10", "zzz"});
        });
        assertEquals("For input string: \"zzz\"", numbers.getMessage());
    }

    @Test
    public void validArgs_printsOneLinePerUpdate() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        try {
            App.main(new String[]{"2", "3"});
        } finally {
            System.out.flush();
            System.setOut(original);
        }
        String[] lines = out.toString().trim().split("\\R");
        assertEquals(6, lines.length);
    }
}
