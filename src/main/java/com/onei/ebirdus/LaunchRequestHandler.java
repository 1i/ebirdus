
package com.onei.ebirdus;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;
import lombok.SneakyThrows;

import java.util.Optional;

public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.requestType(LaunchRequest.class));
    }

    @SneakyThrows
    @Override
    public Optional<Response> handle(HandlerInput input) {
        String result = EbirdClient.doConcurrentRequests();

        return input.getResponseBuilder()
                .withSpeech(result)
                .withSimpleCard("Results for in Ireland & England ", result)
                .build();
    }

}