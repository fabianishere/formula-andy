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


package nl.tudelft.fa.server.controller;

import static akka.http.javadsl.server.Directives.*;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.Route;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.user.User;
import nl.tudelft.fa.server.helper.jackson.UserModule;

import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

/**
 * A controller that manages the users of the server.
 *
 * @author Fabian Mastenbroek
 */
public class UserController {
    /**
     * The {@link EntityManager} to use.
     */
    private final EntityManager entityManager;

    /**
     * The {@link ObjectMapper} to use.
     */
    private final ObjectMapper mapper;

    /**
     * Construct a {@link UserController} instance.
     *
     * @param entityManager The {@link EntityManager} to use.
     */
    public UserController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new UserModule());
    }

    /**
     * Create the main {@link Route} for the controller.
     *
     * @return The route of the controller.
     */
    public Route createRoute() {
        return route(
            post(() -> entity(Jackson.unmarshaller(mapper, Credentials.class), this::register))
        );
    }

    /**
     * Register a new {@link User} with the given credentials.
     *
     * @param credentials The credentials of the user to register.
     * @return The {@link Route} that represents the result.
     */
    private Route register(Credentials credentials) {
        User user = new User(UUID.randomUUID(), credentials);
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
        } catch (RollbackException exc) {
            return complete(StatusCodes.FORBIDDEN, "The username is already in use.");
        }
        return completeOK(user, Jackson.marshaller(mapper));
    }

}
