package com.onei.ebirdus.Intents;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.onei.ebirdus.EbirdClient;
import com.onei.ebirdus.Utils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

@Slf4j
public class FallbackIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        log.debug("input", input.toString());
        return input.matches(intentName("AMAZON.FallbackIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        log.debug("Input {}", input);
        String speechText = "Did you want the sightings for yesterday? If not ask for Help. ";
        String results = EbirdClient.getResults(LocalDate.now().minusDays(Utils.numberOfDays), "Ireland");
        return input.getResponseBuilder()
                .withSpeech(speechText + results)
                .withSimpleCard("Need Help?", speechText + results)
                .build();
    }
}