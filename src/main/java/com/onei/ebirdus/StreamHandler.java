package com.onei.ebirdus;


import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.onei.ebirdus.Intents.*;


public class StreamHandler extends SkillStreamHandler {

    public StreamHandler() {
        super(getSkill());
    }

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new LaunchRequestHandler(),
                        new CancelandStopIntentHandler(),
                        new HelpIntentHandler(),
                        new LocationIntentHandler(),
                        new FallbackIntentHandler(),
                        new DateIntentHandler(),
                        new DayIntentHandler(),
                        new LocationDateIntentHandler(),
                        new SessionEndHandler()
                )
                .build();
    }

}