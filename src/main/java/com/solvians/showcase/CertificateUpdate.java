package com.solvians.showcase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * stores a single certificate market update.
 * includes:
 * - update time (timestamp)
 * - certificate ID (ISIN)
 * - bid/ask prices
 * - bid/ask sizes
 * value ranges are described in the README.
 */

public final class CertificateUpdate {

    private final long timestamp;
    private final String isin;
    private final BigDecimal bidPrice;
    private final int bidSize;
    private final BigDecimal askPrice;
    private final int askSize;

    public CertificateUpdate(long timestamp, String isin, BigDecimal bidPrice, int bidSize, BigDecimal askPrice,
            int askSize) {
        this.timestamp = timestamp;
        this.isin = isin;
        this.bidPrice = bidPrice;
        this.bidSize = bidSize;
        this.askPrice = askPrice;
        this.askSize = askSize;
    }

    public static CertificateUpdate random(IsinGenerator isinGenerator) {
        return random(isinGenerator, ThreadLocalRandom.current());
    }

    /**
     * @param random Random generator used to create certificate data.
     *               Use ThreadLocalRandom current() inside the same thread
     *               where the data generation happens.
     */
    public static CertificateUpdate random(IsinGenerator isinGenerator, ThreadLocalRandom random) {
        long timestamp = System.currentTimeMillis();
        String isin = isinGenerator.generate();
        BigDecimal bidPrice = money(random, 10_000, 20_000);
        int bidSize = random.nextInt(1_000, 5_000 + 1);
        BigDecimal askPrice = money(random, 10_000, 20_000);
        int askSize = random.nextInt(1_000, 10_000 + 1);
        return new CertificateUpdate(timestamp, isin, bidPrice, bidSize, askPrice, askSize);
    }

    private static BigDecimal money(ThreadLocalRandom r, int minCentsInclusive, int maxCentsInclusive) {
        int cents = r.nextInt(minCentsInclusive, maxCentsInclusive + 1);
        return BigDecimal.valueOf(cents, 2);
    }

    //comma-separated line of values without thousands separators
    public String toCsvLine() {
        return String.format(Locale.US, "%d,%s,%s,%d,%s,%d",
                timestamp,
                isin,
                bidPrice.setScale(2, RoundingMode.UNNECESSARY).toPlainString(),
                bidSize,
                askPrice.setScale(2, RoundingMode.UNNECESSARY).toPlainString(),
                askSize);
    }
}
