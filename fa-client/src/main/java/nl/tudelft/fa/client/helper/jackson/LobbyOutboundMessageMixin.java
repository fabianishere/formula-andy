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

package nl.tudelft.fa.client.helper.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nl.tudelft.fa.client.lobby.Lobby;
import nl.tudelft.fa.client.lobby.message.*;
import nl.tudelft.fa.client.net.message.NotAuthorizedException;

/**
 * This mixin creates an envelope around the outbound messages received from the lobby.
 *
 * @author Fabian Mastenbroek
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes(
    {
        @JsonSubTypes.Type(value = Lobby.class, name = "info"),
        @JsonSubTypes.Type(value = JoinSuccess.class, name = "join.success"),
        @JsonSubTypes.Type(value = LeaveSuccess.class, name = "leave.success"),
        @JsonSubTypes.Type(value = UserJoined.class, name = "join.event"),
        @JsonSubTypes.Type(value = UserLeft.class, name = "leave.event"),
        @JsonSubTypes.Type(value = LobbyStatusChanged.class, name = "status.event"),
        @JsonSubTypes.Type(value = TeamConfigurationSubmitted.class, name = "team.event"),

        /* Error types */
        @JsonSubTypes.Type(value = LobbyFullException.class, name = "join.full"),
        @JsonSubTypes.Type(value = NotInLobbyException.class, name = "leave.not-in-lobby"),
        @JsonSubTypes.Type(value = NotAuthorizedException.class, name = "unauthorized"),
    }
)
public abstract class LobbyOutboundMessageMixin {}
