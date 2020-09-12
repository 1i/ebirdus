package com.onei.ebirdus.Intents;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.request.Predicates;
import com.onei.ebirdus.EbirdClient;
import com.onei.ebirdus.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class DateIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        log.debug("input", input);
        return input.matches(Predicates.intentName("dateIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        log.debug("DateIntent");

        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Map<String, Slot> slots = intentRequest.getIntent().getSlots();
        Slot date = slots.get("date");
        String dateValue = (date != null) ? date.getValue() : "null";
        log.debug("dateValue " + dateValue);
        log.debug("slot  " + date);
        EbirdClient.getRequestURL(Utils.formatDate(dateValue), true, "IE");
        String results = "birdusS3Client.getResultsForDate(com.onei.ebirdus.Utils.formatDate(dateValue))";
        return input.getResponseBuilder()
                .withSpeech(results)
                .withSimpleCard("Results for " + dateValue, results)
                .build();
    }

}