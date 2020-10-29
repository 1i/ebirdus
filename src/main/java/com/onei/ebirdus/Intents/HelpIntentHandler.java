package com.onei.ebirdus.Intents;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

@Slf4j
public class HelpIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        log.debug("HelpIntent {}", input.getRequestEnvelopeJson().toString());
        return input.matches(intentName("AMAZON.HelpIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        log.debug("Input", input);
        String speechText = "Try asking for a days sightings, Monday Sightings.";
        speechText = speechText + "Or try asking for a county, Kerry Sightings.";

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("Need Help?", speechText)
                .build();
    }
}
