package com.myplay.mule.connectors.mplog;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.mule.api.MuleEvent;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.display.UserDefinedMetaData;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Payload;
import org.mule.api.annotations.param.Optional;

@Connector(name="mplog", friendlyName="Mplog", schemaVersion = "1.0")
public class MplogConnector {

    private Logger logger = Logger.getLogger(MplogConnector.class);

    @Processor
    @UserDefinedMetaData
    public void log(MuleEvent muleEvent,
                    @Required String level,
                    @Optional String message,
                    @Optional Map<String, String> entry) throws Exception {

        LogMessageBuilder builder = new LogMessageBuilder();

        if (message != null && !message.isEmpty()) {
            builder.addText(message);
        }

        builder.addPair("message_id", muleEvent.getMessage().getUniqueId());

        if ( entry != null ) {
            for (Map.Entry<String, String> e : entry.entrySet()) {
                builder.addPair(e.getKey(), e.getValue());
            }
        }

        switch (level) {
            case "DEBUG":
                logger.debug(builder.build());
                break;
            case "INFO":
                logger.info(builder.build());
                break;
            case "WARN":
                logger.warn(builder.build());
                break;
            case "ERROR":
                logger.error(builder.build());
                break;
            default:
                throw new IllegalArgumentException("Log level can be BEBUG, INFO, WARN or ERROR, but \"" + level + "\" passed.");
        }
    }
}
