
package com.onei.ebirdus;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;

import java.util.List;
import java.util.Optional;

public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "LaunchRequest";


        List<Model> recentSighting = EbirdClient.getDefaultRequest();

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard(speechText, speechText)
                .build();
    }

}