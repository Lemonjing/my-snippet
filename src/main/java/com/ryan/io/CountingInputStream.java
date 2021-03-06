package com.ryan.io;

import com.ryan.util.Parameters;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A decorating {@code InputStream} that counts the number of bytes that have
 * been read from the underlying stream. Bytes read multiple times are counted
 * as many times as they have been read. Skipped bytes are not counted.
 *
 * @author Osman KOCAK
 */
public final class CountingInputStream extends InputStream {
    private final InputStream in;
    private final AtomicLong counter;

    /**
     * Creates a new {@code CountingInputStream}.
     *
     * @param in the underlying stream.
     * @throws NullPointerException if {@code in} is {@code null}.
     */
    public CountingInputStream(InputStream in) {
        Parameters.checkNotNull(in);
        this.in = in;
        this.counter = new AtomicLong();
    }

    /**
     * Returns the number of bytes that have been read from the underlying
     * stream so far.
     *
     * @return the number of bytes that have been read so far.
     */
    public long getCount() {
        return counter.get();
    }

    /**
     * Sets the counter to 0 and returns its value before resetting it.
     *
     * @return the number of bytes that have been read so far.
     */
    public long resetCount() {
        return counter.getAndSet(0);
    }

    @Override
    public int available() throws IOException {
        return in.available();
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    @Override
    public void mark(int readLimit) {
        in.mark(readLimit);
    }

    @Override
    public boolean markSupported() {
        return in.markSupported();
    }

    @Override
    public int read() throws IOException {
        int b = in.read();
        count(b != -1 ? 1 : -1);
        return b;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int n = in.read(b);
        count(n);
        return n;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int n = in.read(b, off, len);
        count(n);
        return n;
    }

    @Override
    public void reset() throws IOException {
        in.reset();
    }

    @Override
    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    private void count(int n) {
        if (n != -1) {
            counter.addAndGet(n);
        }
    }
}
