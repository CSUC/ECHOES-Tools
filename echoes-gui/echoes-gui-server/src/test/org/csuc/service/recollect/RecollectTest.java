package org.csuc.service.recollect;

import org.csuc.Producer;
import org.csuc.client.Client;
import org.csuc.dao.RecollectDAO;
import org.csuc.dao.impl.RecollectDAOImpl;
import org.csuc.entities.Recollect;
import org.csuc.typesafe.consumer.ProducerAndConsumerConfig;
import org.csuc.typesafe.consumer.RabbitMQConfig;
import org.csuc.typesafe.server.Application;
import org.csuc.typesafe.server.ServerConfig;
import org.csuc.utils.Status;
import org.junit.Test;
import org.mongodb.morphia.Key;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class RecollectTest {

    private URL applicationResource = getClass().getClassLoader().getResource("echoes-gui-server.conf");
    private Application applicationConfig = new ServerConfig((Objects.isNull(applicationResource)) ? null : new File(applicationResource.getFile()).toPath()).getConfig();

    private URL rabbitmqResource = getClass().getClassLoader().getResource("rabbitmq.conf");
    private RabbitMQConfig config = new ProducerAndConsumerConfig((Objects.isNull(rabbitmqResource)) ? null : new File(rabbitmqResource.getFile()).toPath()).getRabbitMQConfig();

    @Test
    public void create() throws Exception {
        org.csuc.entities.Recollect recollect = testInsert();

        HashMap<String, Object> message = new HashMap<>();

        message.put("_id", recollect.get_id());
        message.put("host", "https://webservices.picturae.com/a2a/b3d793d4-f737-11e5-903e-60f81db16928");
        message.put("set", "t11_a");
        message.put("metadataPrefix", "oai_a2a");
        message.put("format", "RDFXML");
        message.put("schema", "A2A");
        message.put("properties", properties());

        new Producer(config.getRecollectQueue(), config).sendMessage(message);
    }


    private Recollect testInsert() throws Exception {
        Client client = new Client(applicationConfig.getMongoDB().getHost(), applicationConfig.getMongoDB().getPort(),applicationConfig.getMongoDB().getDatabase());
        RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, client.getDatastore());

        org.csuc.entities.Recollect recollect = new org.csuc.entities.Recollect();

        recollect.setHost("https://webservices.picturae.com/a2a/b3d793d4-f737-11e5-903e-60f81db16928");
        recollect.setSet("t11_a");
        recollect.setMetadataPrefix("oai_a2a");
        recollect.setProperties(properties());
        recollect.setUser("user");
        recollect.setFormat("RDFXML");
        recollect.setSchema("A2A");

        recollect.setStatus(Status.QUEUE);

        Key<Recollect> key = recollectDAO.insert(recollect);

        System.out.print(key);

        return recollect;

    }

    private Map<String, String> properties() {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("edmType", "IMAGE");
        properties.put("provider", "provider");
        properties.put("dataProvider", "dataProvider");
        properties.put("language", "language");
        properties.put("rights", "rights");
        properties.put("set", "set");

        return properties;
    }
}
