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

import com.fasterxml.jackson.databind.module.SimpleModule;
import nl.tudelft.fa.client.lobby.Lobby;
import nl.tudelft.fa.client.lobby.LobbyBalancer;
import nl.tudelft.fa.client.lobby.LobbyConfiguration;
import nl.tudelft.fa.client.lobby.message.*;
import nl.tudelft.fa.client.race.*;
import nl.tudelft.fa.client.team.Driver;
import nl.tudelft.fa.client.team.Member;
import nl.tudelft.fa.client.team.Specialist;
import nl.tudelft.fa.client.team.Team;
import nl.tudelft.fa.client.team.inventory.Car;
import nl.tudelft.fa.client.team.inventory.Engine;
import nl.tudelft.fa.client.team.inventory.InventoryItem;
import nl.tudelft.fa.client.team.inventory.Tire;
import nl.tudelft.fa.client.user.User;

/**
 * This class is a module for the Jackson library to provide (de)serializers and mix-ins for m
 * messages received/sent by/from the lobby.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyModule extends SimpleModule {
    /**
     * Construct a {@link LobbyModule} instance.
     */
    public LobbyModule() {
        super(LobbyModule.class.getName());
    }

    /**
     * Setup this module.
     *
     * {@inheritDoc}
     * @param context The setup context.
     */
    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(LobbyInboundMessage.class, LobbyInboundMessageMixin.class);
        context.setMixInAnnotations(LobbyOutboundMessage.class, LobbyOutboundMessageMixin.class);
        context.setMixInAnnotations(LobbyBalancer.class, LobbyBalancerMixin.class);
        context.setMixInAnnotations(Lobby.class, LobbyMixin.class);
        context.setMixInAnnotations(LobbyConfiguration.class, LobbyConfigurationMixin.class);
        context.setMixInAnnotations(User.class, UserMixin.class);
        context.setMixInAnnotations(UserJoined.class, UserJoinedMixin.class);
        context.setMixInAnnotations(UserLeft.class, UserLeftMixin.class);
        context.setMixInAnnotations(LobbyStatusChanged.class, LobbyStatusChangedMixin.class);

        context.setMixInAnnotations(Member.class, MemberMixin.class);
        context.setMixInAnnotations(InventoryItem.class, InventoryItemMixin.class);
        //context.setMixInAnnotations(Driver.class, DriverMixin.class);
        //context.setMixInAnnotations(Engine.class, EngineMixin.class);
        //context.setMixInAnnotations(Car.class, CarMixin.class);
        //context.setMixInAnnotations(Tire.class, TireMixin.class);

        context.setMixInAnnotations(TeamConfigurationSubmission.class,
            TeamConfigurationSubmissionMixin.class);
        context.setMixInAnnotations(TeamConfigurationSubmitted.class,
            TeamConfigurationSubmittedMixin.class);
        context.setMixInAnnotations(CarConfiguration.class, CarConfigurationMixin.class);
        context.setMixInAnnotations(CarParametersSubmission.class,
            CarParametersSubmissionMixin.class);
        context.setMixInAnnotations(CarParameters.class, CarParametersMixin.class);

        context.setMixInAnnotations(RaceSimulationStarted.class, RaceSimulationStartedMixin.class);
        context.setMixInAnnotations(RaceSimulationResult.class, RaceSimulationResult.class);

        context.setMixInAnnotations(GrandPrix.class, GrandPrixMixin.class);
        context.setMixInAnnotations(Circuit.class, CircuitMixin.class);
    }
}
