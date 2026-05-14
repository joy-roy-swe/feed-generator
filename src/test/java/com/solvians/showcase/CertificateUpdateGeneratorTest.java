package com.solvians.showcase;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * use ThreadLocalRandom for generating certificate updates
 */
class CertificateUpdateGeneratorTest {

    @Test
    public void generateQuotes() {
        CertificateUpdateGenerator certificateUpdateGenerator = new CertificateUpdateGenerator(10, 100);
        Stream<CertificateUpdate> quotes = certificateUpdateGenerator.generateQuotes();
        assertNotNull(quotes);
        assertEquals(10 * 100, quotes.count());
    }

    @Test
    public void generateQuotes_zeroQuotes_returnsEmptyStream() {
        assertEquals(0, new CertificateUpdateGenerator(4, 0).generateQuotes().count());
    }

    @Test
    public void constructor_rejectsInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> new CertificateUpdateGenerator(0, 10));
        assertThrows(IllegalArgumentException.class, () -> new CertificateUpdateGenerator(1, -1));
    }
}
