/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Fabian Mastenbroek, Christian Slothouber,
 * Laetitia Molkenboer, Nikki Bouman, Nils de Beukelaar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package nl.tudelft.fa.frontend.javafx.dispatch;

import javafx.application.Platform;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A {@link ExecutorService} that dispatches on the JavaFX event thread.
 *
 * @author Fabian Mastenbroek
 */
public class JavaFXExecutorService extends AbstractExecutorService {
    /**
     * Initiates an orderly shutdown in which previously submitted tasks are executed, but no new
     * tasks will be accepted.
     */
    @Override
    public void shutdown() {
        // not supported
    }

    /**
     * Attempts to stop all actively executing tasks, halts the processing of waiting tasks, and
     * returns a list of the tasks that were awaiting execution.
     *
     * @return A list of tasks that never commenced execution
     */
    @Override
    public List<Runnable> shutdownNow() {
        return Collections.emptyList();
    }

    /**
     * Determine whether this {@link ExecutorService} has been shutdown.
     *
     * @return <code>true</code> if this executor service has been shutdown, <code>false</code>
     *      otherwise.
     */
    @Override
    public boolean isShutdown() {
        return false;
    }

    /**
     * Determine whether this {@link ExecutorService} has been shutdown and all tasks have been
     * completed following this shutdown.
     *
     * @return <code>true</code> if this executor service has been shutdown and all tasks have been
     *      completed thereafter, <code>false</code> otherwise.
     */
    @Override
    public boolean isTerminated() {
        return false;
    }

    /**
     * Blocks until all tasks have completed execution after a shutdown request, or the timeout
     * occurs, or the current thread is interrupted, whichever happens first.
     *
     * @param timeout The maximum time to wait
     * @param unit The time unit of the timeout argument
     * @return <code>true</code> if this executor terminated and <code>false</code> if the timeout
     *      elapsed before termination.
     * @throws InterruptedException if interrupted while waiting.
     */
    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return true;
    }

    /**
     * Executes the given command at some time in the future.
     *
     * @param command The runnable task to execute.
     */
    @Override
    public void execute(Runnable command) {
        Platform.runLater(command);
    }
}
