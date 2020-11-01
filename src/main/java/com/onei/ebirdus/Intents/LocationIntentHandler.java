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

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class LocationIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        log.debug("LocationIntent {}", input.getRequestEnvelopeJson().toString());
        return input.matches(Predicates.intentName("locationIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        Request request1 = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request1;
        Map<String, Slot> slots = intentRequest.getIntent().getSlots();
        Slot slotCounty = slots.get("county");
        String county = (slotCounty != null) ? slotCounty.getValue() : "null";
        log.debug("county " + county);
        log.debug("slot County " + slotCounty);
        Request request = input.getRequest();
        String sessionAtt = (String) input.getAttributesManager().getSessionAttributes().get("county");
        log.debug("Session " + sessionAtt);
        log.debug("County " + county);
        log.debug("Request " + request);
        String results = EbirdClient.getAllResults(LocalDate.now().minusDays(Utils.numberOfDays), county);

        return input.getResponseBuilder()
                .withSpeech(results)
                .withSimpleCard("Results for " + county, results)
                .build();
    }

}