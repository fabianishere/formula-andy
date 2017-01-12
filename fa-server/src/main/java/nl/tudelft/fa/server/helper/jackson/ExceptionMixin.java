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

package nl.tudelft.fa.server.helper.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Mix-in for exceptions thrown by the lobby and serialized by Jackson.
 *
 * @author Fabian Mastenbroek
 */
public abstract class ExceptionMixin {
    /**
     * Returns an array containing all of the exceptions that were suppressed, typically by the
     * try-with-resources statement, in order to deliver this exception.
     *
     * @return An array containing all of the exceptions that were suppressed to deliver this
     *     exception.
     */
    @JsonIgnore
    public abstract Throwable[] getSuppressed();

    /**
     * Return the stacktrace of this exception.
     *
     * @return The stacktrace of this exception.
     */
    @JsonIgnore
    public abstract StackTraceElement[] getStackTrace();

    /**
     * Return the cause of this exception.
     *
     * @return The cause of this exception.
     */
    @JsonIgnore
    public abstract Throwable getCause();

    /**
     * Return the message of this exception.
     *
     * @return The message of this exception.
     */
    @JsonProperty("message")
    public abstract String getMessage();

    /**
     * Return the localized message of this exception.
     *
     * @return The localized message of this exception.
     */
    @JsonIgnore
    public abstract String getLocalizedMessage();
}
