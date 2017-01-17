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

package nl.tudelft.fa.client.lobby.controller;

import akka.http.javadsl.model.headers.Authorization;
import akka.http.javadsl.model.ws.WebSocketRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.fa.client.Client;
import nl.tudelft.fa.client.auth.Credentials;

/**
 * A {@link LobbyController} for an authorized user.
 *
 * @author Fabian Mastenbroek
 */
public class AuthorizedLobbyController extends LobbyController {
    /**
     * The credentials of the user to manage the teams of.
     */
    private final Credentials credentials;

    /**
     * Construct a {@link AuthorizedLobbyController} instance.
     *
     * @param client The client of this controller.
     * @param mapper The object mapper to use.
     * @param id The identifier of the lobby.
     * @param credentials The credentials to authorize with.
     */
    public AuthorizedLobbyController(Client client, ObjectMapper mapper, String id,
                                     Credentials credentials) {
        super(client, mapper, id);
        this.credentials = credentials;
    }

    /**
     * Return the {@link WebSocketRequest} instance to retrieve the lobby's feed.
     *
     * @return The {@link WebSocketRequest} instance to retrieve the lobby's feed.
     */
    @Override
    protected WebSocketRequest feedRequest() {
        return super.feedRequest()
            .addHeader(Authorization.basic(credentials.getUsername(), credentials.getPassword()));
    }
}
