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

import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import nl.tudelft.fa.core.lobby.Lobby;
import nl.tudelft.fa.core.lobby.LobbyBalancer;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.message.*;
import nl.tudelft.fa.core.race.CarConfiguration;
import nl.tudelft.fa.core.race.CarParameters;
import nl.tudelft.fa.core.race.TeamConfiguration;
import nl.tudelft.fa.core.team.Aerodynamicist;
import nl.tudelft.fa.core.team.Driver;
import nl.tudelft.fa.core.team.Mechanic;
import nl.tudelft.fa.core.team.Strategist;
import nl.tudelft.fa.core.team.inventory.Car;
import nl.tudelft.fa.core.team.inventory.Engine;
import nl.tudelft.fa.core.team.inventory.Tire;
import nl.tudelft.fa.core.user.User;

import javax.persistence.EntityManager;

/**
 * This class is a module for the Jackson library to provide serializers for the lobby package
 * of the <i>fa-core</i> module.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyModule extends SimpleModule {
    /**
     * The entity manager to use.
     */
    private final EntityManager entityManager;

    /**
     * Construct a {@link LobbyModule} instance.
     *
     * @param entityManager The {@link EntityManager} to deserialize persistent entities.
     */
    public LobbyModule(EntityManager entityManager) {
        super(LobbyModule.class.getName());
        this.entityManager = entityManager;
    }

    /**
     * Setup this module.
     *
     * {@inheritDoc}
     * @param context The setup context.
     */
    @Override
    public void setupModule(SetupContext context) {
        SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(LobbyBalancer.class, new LobbyBalancerInformationSerializer());
        serializers.addSerializer(User.class, new UserSerializer());
        context.addSerializers(serializers);

        SimpleDeserializers deserializers = new SimpleDeserializers();
        deserializers.addDeserializer(Car.class, new CarDeserializer(entityManager));
        deserializers.addDeserializer(Engine.class, new EngineDeserializer(entityManager));
        deserializers.addDeserializer(Driver.class, new DriverDeserializer(entityManager));
        deserializers.addDeserializer(Mechanic.class, new MechanicDeserializer(entityManager));
        deserializers.addDeserializer(Strategist.class, new StrategistDeserializer(entityManager));
        deserializers.addDeserializer(Aerodynamicist.class,
            new AerodynamicistDeserializer(entityManager));
        deserializers.addDeserializer(Tire.class, new TireDeserializer(entityManager));
        context.addDeserializers(deserializers);

        context.setMixInAnnotations(Lobby.class, LobbyMixin.class);
        context.setMixInAnnotations(LobbyConfiguration.class, LobbyConfigurationMixin.class);
        context.setMixInAnnotations(Exception.class, ExceptionMixin.class);
        context.setMixInAnnotations(LobbyException.class, LobbyExceptionMixin.class);
        context.setMixInAnnotations(LobbyInboundMessage.class, LobbyInboundMessageMixin.class);
        context.setMixInAnnotations(LobbyOutboundMessage.class, LobbyOutboundMessageMixin.class);
        context.setMixInAnnotations(Join.class, JoinMixin.class);
        context.setMixInAnnotations(JoinSuccess.class, JoinSuccessMixin.class);
        context.setMixInAnnotations(CarParametersSubmission.class,
            CarParametersSubmissionMixin.class);
        context.setMixInAnnotations(CarParameters.class, CarParametersMixin.class);
        context.setMixInAnnotations(TeamConfiguration.class, TeamConfigurationMixin.class);
        context.setMixInAnnotations(TeamConfigurationSubmission.class,
            TeamConfigurationSubmissionMixin.class);
        context.setMixInAnnotations(CarConfiguration.class, CarConfigurationMixin.class);
    }
}
