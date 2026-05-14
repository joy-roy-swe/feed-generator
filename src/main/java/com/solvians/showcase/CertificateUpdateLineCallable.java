package com.solvians.showcase;

import java.util.concurrent.Callable;

//generates a single certificate update formatted as one CSV line.
public final class CertificateUpdateLineCallable implements Callable<String> {

    private final IsinGenerator isinGenerator;

    public CertificateUpdateLineCallable() {
        this(new IsinGenerator());
    }

    public CertificateUpdateLineCallable(IsinGenerator isinGenerator) {
        this.isinGenerator = isinGenerator;
    }

    @Override
    public String call() {
        return CertificateUpdate.random(isinGenerator).toCsvLine();
    }
}
