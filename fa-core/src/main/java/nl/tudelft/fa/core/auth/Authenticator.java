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

package nl.tudelft.fa.core.auth;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import akka.japi.pf.ReceiveBuilder;

import nl.tudelft.fa.core.auth.message.AuthenticationRequest;
import nl.tudelft.fa.core.auth.message.Authenticated;
import nl.tudelft.fa.core.auth.message.InvalidCredentials;
import nl.tudelft.fa.core.user.User;

import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.Optional;
import javax.persistence.EntityManager;


/**
 * The {@link Authenticator} actor authenticates users via its persistence unit and
 * creates a session for each user.
 *
 * @author Fabian Mastenbroek
 */
public class Authenticator extends AbstractActor {
    /**
     * The {@link EntityManager} this actor uses to manage entities.
     */
    private EntityManager entityManager;

    /**
     * The {@link LoggingAdapter} of this class.
     */
    private final LoggingAdapter log = Logging.getLogger(context().system(), this);

    /**
     * Construct a {@link Authenticator} instance.
     *
     * @param entityManager The JPA entity manager to manage the {@link User} entities.
     */
    private Authenticator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * This method defines the initial actor behavior, it must return a partial function with the
     * actor logic.
     *
     * @return The initial actor behavior as a partial function.
     */
    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder
            .match(AuthenticationRequest.class, this::authenticate)
            .build();
    }

    /**
     * Handle the given {@link AuthenticationRequest} request.
     *
     * @param req The {@link AuthenticationRequest} request to handle.
     */
    private void authenticate(AuthenticationRequest req) {
        log.info("Received authentication request {}", req);

        Optional<User> user = entityManager
            .createQuery("SELECT t FROM Users t where t.credentials.username = :username",
                User.class)
            .setParameter("username", req.getCredentials().getUsername())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();

        // If the user cannot be found, or the password is incorrect, return a InvalidCredentials
        // error message.
        if (!user.isPresent() || !user.get()
                .getCredentials().getPassword().equals(req.getCredentials().getPassword())) {
            sender().tell(new InvalidCredentials(), self());
            return;
        }

        User userUnwrapped = user.get();
        log.info("User {} has been authenticated", userUnwrapped
            .getCredentials().getUsername());

        sender().tell(new Authenticated(userUnwrapped), self());
    }

    /**
     * Create {@link Props} instance for an actor of this type.
     *
     * @param entityManager The magic number to be passed to this actor’s constructor.
     * @return A Props for creating this actor, which can then be further configured
     *         (e.g. calling `.withDispatcher()` on it)
     */
    public static Props props(EntityManager entityManager) {
        return Props.create(Authenticator.class, () -> new Authenticator(entityManager));
    }
}
