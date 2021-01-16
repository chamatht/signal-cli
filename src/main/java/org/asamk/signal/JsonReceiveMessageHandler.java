package org.asamk.signal;

import org.asamk.signal.json.JsonError;
import org.asamk.signal.json.JsonMessageEnvelope;
import org.asamk.signal.manager.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whispersystems.signalservice.api.messages.SignalServiceContent;
import org.whispersystems.signalservice.api.messages.SignalServiceEnvelope;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonReceiveMessageHandler implements Manager.ReceiveMessageHandler {

    private final static Logger logger = LoggerFactory.getLogger(JsonReceiveMessageHandler.class);

    protected final Manager m;
    private final JsonWriter jsonWriter;

    public JsonReceiveMessageHandler(Manager m) {
        this.m = m;
        jsonWriter = new JsonWriter(System.out);
    }

    @Override
    public void handleMessage(SignalServiceEnvelope envelope, SignalServiceContent content, Throwable exception) {
        final Map<String, Object> object = new HashMap<>();
        if (exception != null) {
            object.put("error", new JsonError(exception));
        }
        if (envelope != null) {
            object.put("envelope", new JsonMessageEnvelope(envelope, content, m));
        }
        try {
            jsonWriter.write(object);
        } catch (IOException e) {
            logger.error("Failed to write json object: {}", e.getMessage());
        }
    }
}
