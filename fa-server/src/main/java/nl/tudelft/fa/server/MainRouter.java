/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Fabian Mastenbroek, Christian Slothouber,
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

package nl.tudelft.fa.server;

import static akka.http.javadsl.server.Directives.*;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.Route;

import nl.tudelft.fa.server.model.Information;

import java.lang.management.ManagementFactory;

/**
 * This class provides the main {@link Route}s for the Formula Andy Akka HTTP REST API.
 *
 * @author Fabian Mastenbroek
 */
public class MainRouter {
    /**
     * The name of the engine.
     */
    private static final String ENGINE_NAME = "Formula Andy X";

    /**
     * The version of the engine.
     */
    private static final String ENGINE_VERSION = "1.0-SNAPSHOT";

    /**
     * Generate the {@link Route} for the REST API server.
     *
     * @return The route of the REST API.
     */
    public static Route generate() {
        return route(
            path("information", MainRouter::information)
        );
    }

    /**
     * This route shows the information of this REST API server.
     *
     * @return The route that shows the information.
     */
    private static Route information() {
        return complete(
            StatusCodes.OK,
            new Information(ENGINE_NAME, ENGINE_VERSION,
                ManagementFactory.getRuntimeMXBean().getUptime()),
            Jackson.marshaller()
        );
    }
}
