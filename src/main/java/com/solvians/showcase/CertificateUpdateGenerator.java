package com.solvians.showcase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class CertificateUpdateGenerator {

    private final int threads;
    private final int quotes;

    public CertificateUpdateGenerator(int threads, int quotes) {
        if (threads < 1) {
            throw new IllegalArgumentException("threads must be >= 1");
        }
        if (quotes < 0) {
            throw new IllegalArgumentException("quotes must be >= 0");
        }
        this.threads = threads;
        this.quotes = quotes;
    }

    /**
     * generates updates using multiple threads and returns the results as a stream.
     *
     * total generated updates = threads * quotes.
     * updates are returned in the order the Future results are submitted.
     */
    public Stream<CertificateUpdate> generateQuotes() {
        int total = threads * quotes;
        if (total == 0) {
            return Stream.empty();
        }
        ThreadLocalRandom random = ThreadLocalRandom.current();
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        try {
            IsinGenerator isinGenerator = new IsinGenerator();
            List<CertificateUpdate> updateList = new ArrayList<>(total);
            List<Future<CertificateUpdate>> futures = new ArrayList<>(total);
            for (int i = 0; i < total; i++) {
                futures.add(executor.submit(
                        () -> CertificateUpdate.random(isinGenerator, ThreadLocalRandom.current())));
            }
            for (Future<CertificateUpdate> future : futures) {
                updateList.add(future.get());
            }

            random.nextInt();
            return updateList.stream();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            Throwable c = e.getCause() != null ? e.getCause() : e;
            throw new RuntimeException(c);
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
