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
import static akka.pattern.PatternsCS.ask;

import akka.actor.ActorRef;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.server.directives.SecurityDirectives;
import nl.tudelft.fa.core.auth.actor.Authenticator;
import nl.tudelft.fa.core.auth.message.AuthenticationRequest;
import nl.tudelft.fa.core.auth.message.AuthenticationSuccess;
import nl.tudelft.fa.core.user.User;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;


/**
 * A controller that handles the authorization of users.
 *
 * @author Fabian Mastenbroek
 */
public class AuthorizationController {
    /**
     * The reference to the {@link Authenticator} actor.
     */
    private final ActorRef authenticator;

    /**
     * Construct a {@link AuthorizationController} instance.
     *
     * @param authenticator The reference to the {@link Authenticator} actor.
     */
    public AuthorizationController(ActorRef authenticator) {
        this.authenticator = authenticator;
    }

    /**
     * Create the main {@link Route} for the controller.
     *
     * @return The route of the controller.
     */
    public Route createRoute() {
        return route(
            path("basic", () -> this.authenticateBasic(user -> complete(StatusCodes.OK)))
        );
    }

    /**
     * Return the middleware {@link Route} instance that authenticates the user using HTTP Basic
     * authentication.
     *
     * @param inner The inner route producer to use on success.
     * @return The {@link Route} instance that authenticates the user.
     */
    public Route authenticateBasic(Function<User, Route> inner) {
        return authenticateBasicAsync("fa", this::authenticate, inner);
    }

    /**
     * Authenticate the user with the {@link SecurityDirectives.ProvidedCredentials} instance.
     *
     * @param opt The optional credentials to authenticate with.
     * @return The {@link CompletionStage} of the authentication returning the user.
     */
    public CompletionStage<Optional<User>> authenticate(
            Optional<SecurityDirectives.ProvidedCredentials> opt) {
        return opt.map(credentials ->
            ask(authenticator, new AuthenticationRequest(credentials.identifier(), user ->
                credentials.verify(user.getCredentials().getPassword())), 1000)
                .thenApply(AuthenticationSuccess.class::cast)
                .thenApply(AuthenticationSuccess::getUser)
                .thenApply(Optional::of)
                .exceptionally(exc -> Optional.empty())
        ).orElse(CompletableFuture.completedFuture(Optional.empty()));
    }
}
