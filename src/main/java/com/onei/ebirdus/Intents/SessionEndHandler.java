package com.onei.ebirdus.Intents;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.SessionEndedRequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;

import java.util.Optional;

public class SessionEndHandler implements SessionEndedRequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput, SessionEndedRequest sessionEndedRequest) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, SessionEndedRequest sessionEndedRequest) {
        return handlerInput.getResponseBuilder().build();
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        return Optional.empty();
    }
}
