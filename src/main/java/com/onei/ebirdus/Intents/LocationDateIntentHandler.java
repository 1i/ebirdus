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
public class LocationDateIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        log.debug("input {}", input.toString());
        return input.matches(Predicates.intentName("LocationDateIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
        Map<String, Slot> slots = intentRequest.getIntent().getSlots();
        Slot slotCounty = slots.get("county");
        Slot slotDay = slots.get("day");
        String countyValue = (slotCounty != null) ? slotCounty.getValue() : "null";
        log.debug(" " + countyValue);
        log.debug("slot County " + slotCounty);
        String dayValue = (slotDay != null) ? slotDay.getValue() : LocalDate.now().toString();
        log.debug("dayValue " + dayValue);
        Request request = input.getRequest();
        String county = (String) input.getAttributesManager().getSessionAttributes().get("county");
        String day = (String) input.getAttributesManager().getSessionAttributes().get("day");

        log.debug("County " + county);
        log.debug("Day " + day);
        log.debug("Request " + request);

        String results = EbirdClient.getAllResults(Utils.getDateFromDay(dayValue), countyValue);

        return input.getResponseBuilder()
                .withSpeech(results)
                .withSimpleCard("Results for " + countyValue + " last " + dayValue, results)
                .build();
    }

}