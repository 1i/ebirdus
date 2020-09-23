
package com.onei.ebirdus;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;

import java.time.LocalDate;
import java.util.Optional;

public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String results = EbirdClient.getResults(LocalDate.now().minusDays(7), "Ireland");

        return input.getResponseBuilder()
                .withSpeech(results)
                .withSimpleCard("Results for last week in Ireland ", results)
                .build();
    }

}