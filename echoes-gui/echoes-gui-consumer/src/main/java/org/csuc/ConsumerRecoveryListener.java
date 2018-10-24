package org.csuc;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Recoverable;
import com.rabbitmq.client.RecoveryListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author amartinez
 */
public class ConsumerRecoveryListener implements RecoveryListener {

    private Logger logger = LogManager.getLogger(ConsumerRecoveryListener.class);

    @Override
    public void handleRecovery(Recoverable recoverable) {
        if( recoverable instanceof Channel ) {
            int channelNumber = ((Channel) recoverable).getChannelNumber();
            logger.info( "Connection to channel #" + channelNumber + " was recovered." );
        }
    }
    @Override
    public void handleRecoveryStarted(Recoverable arg0) {
        // TODO Auto-generated method stub

    }
}
