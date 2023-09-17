package dev4sol.com.mindgame.events.engine;

/**
 * Created by Cclub on 27/06/2018.
 */

import dev4sol.com.mindgame.events.AbstractEvent;
import dev4sol.com.mindgame.events.EventObserver;

/**
 * When the 'back to menu' was pressed.
 */
public class FlipDownCardsEvent extends AbstractEvent {

    public static final String TYPE = FlipDownCardsEvent.class.getName();

    public FlipDownCardsEvent() {
    }

    @Override
    protected void fire(EventObserver eventObserver) {
        eventObserver.onEvent(this);
    }

    @Override
    public String getType() {
        return TYPE;
    }

}

