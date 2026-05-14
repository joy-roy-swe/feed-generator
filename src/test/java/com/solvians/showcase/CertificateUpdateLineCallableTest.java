package com.solvians.showcase;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CertificateUpdateLineCallableTest {

    @Test
    void call_returnsSixCommaSeparatedFieldsWithReadmeRanges() throws Exception {
        String line = new CertificateUpdateLineCallable().call();
        String[] parts = line.split(",", -1);
        assertEquals(6, parts.length);

        Long.parseLong(parts[0]);

        assertEquals(12, parts[1].length());
        assertTrue(parts[1].matches("[A-Z]{2}[A-Z0-9]{9}[0-9]"));
        assertTrue(IsinGenerator.isValid(parts[1]));

        BigDecimal bid = new BigDecimal(parts[2]);
        assertTrue(bid.compareTo(new BigDecimal("100.00")) >= 0);
        assertTrue(bid.compareTo(new BigDecimal("200.00")) <= 0);

        int bidSize = Integer.parseInt(parts[3]);
        assertTrue(bidSize >= 1_000 && bidSize <= 5_000);

        BigDecimal ask = new BigDecimal(parts[4]);
        assertTrue(ask.compareTo(new BigDecimal("100.00")) >= 0);
        assertTrue(ask.compareTo(new BigDecimal("200.00")) <= 0);

        int askSize = Integer.parseInt(parts[5]);
        assertTrue(askSize >= 1_000 && askSize <= 10_000);
    }
}
