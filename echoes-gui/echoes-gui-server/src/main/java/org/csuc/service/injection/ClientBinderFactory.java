package org.csuc.service.injection;

import org.csuc.client.Client;
import org.csuc.typesafe.server.Application;
import org.glassfish.hk2.api.Factory;

import javax.inject.Inject;


/**
 * @author amartinez
 */
public class ClientBinderFactory implements Factory<Client> {

    @Inject
    private Application application;

    @Override
    public Client provide() {
        return new Client(application.getMongoDB().getHost(), application.getMongoDB().getPort(), application.getMongoDB().getDatabase());
    }

    @Override
    public void dispose(Client client) {

    }
}
