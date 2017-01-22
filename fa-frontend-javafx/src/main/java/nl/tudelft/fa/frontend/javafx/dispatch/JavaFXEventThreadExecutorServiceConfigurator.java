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

import akka.dispatch.DispatcherPrerequisites;
import akka.dispatch.ExecutorServiceConfigurator;
import akka.dispatch.ExecutorServiceFactory;
import com.typesafe.config.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * A {@link ExecutorServiceConfigurator} for the {@link JavaFXExecutorService} class.
 *
 * @author Fabian Mastenbroek
 */
public class JavaFXEventThreadExecutorServiceConfigurator extends ExecutorServiceConfigurator {
    /**
     * The {@link ExecutorServiceFactory} for this class.
     */
    private final ExecutorServiceFactory factory = JavaFXExecutorService::new;

    /**
     * Construct a {@link JavaFXEventThreadExecutorServiceConfigurator} instance.
     *
     * @param config The configuration to use.
     * @param prerequisites The prerequisites of the dispatcher.
     */
    public JavaFXEventThreadExecutorServiceConfigurator(Config config,
                                                        DispatcherPrerequisites prerequisites) {
        super(config, prerequisites);
    }

    /**
     * Create a {@link ExecutorServiceFactory} with the given id and thread factory.
     *
     * @param id The id of the factory.
     * @param threadFactory The thread factory to use.
     * @return The {@link ExecutorServiceFactory} that creates {@link ExecutorService} instances.
     */
    @Override
    public ExecutorServiceFactory createExecutorServiceFactory(String id,
                                                               ThreadFactory threadFactory) {
        return factory;
    }
}
