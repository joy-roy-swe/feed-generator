package com.solvians.showcase;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IsinGeneratorTest {

    @Test
    void computeCheckDigit_readmeExample() {
        assertEquals(6, IsinGenerator.computeCheckDigit("DE123456789"));
    }

    @Test
    void expandToDigits_readmeExample() {
        List<Integer> digits = IsinGenerator.expandToDigits("DE123456789");
        assertEquals(List.of(1, 3, 1, 4, 1, 2, 3, 4, 5, 6, 7, 8, 9), digits);
    }

    @Test
    void generate_producesTwelveCharacterIsinWithValidCheckDigit() {
        String isin = new IsinGenerator().generate();
        assertEquals(12, isin.length());
        assertTrue(isin.substring(0, 2).chars().allMatch(ch -> ch >= 'A' && ch <= 'Z'));
        assertTrue(IsinGenerator.isValid(isin));
    }

    @RepeatedTest(50)
    void generate_randomSamplesStayValid() {
        assertTrue(IsinGenerator.isValid(new IsinGenerator().generate()));
    }

    @Test
    void computeCheckDigit_rejectsWrongLength() {
        assertThrows(IllegalArgumentException.class, () -> IsinGenerator.computeCheckDigit("DE12345678"));
        assertThrows(IllegalArgumentException.class, () -> IsinGenerator.computeCheckDigit("DE1234567890"));
    }
}
