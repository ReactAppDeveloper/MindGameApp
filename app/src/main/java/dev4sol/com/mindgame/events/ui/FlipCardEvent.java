package dev4sol.com.mindgame.events.ui;

/**
 * Created by Cclub on 27/06/2018.
 */

import dev4sol.com.mindgame.events.AbstractEvent;
import dev4sol.com.mindgame.events.EventObserver;

/**
 * When the 'back to menu' was pressed.
 */
public class FlipCardEvent extends AbstractEvent {

    public static final String TYPE = FlipCardEvent.class.getName();

    public final int id;

    public FlipCardEvent(int id) {
        this.id = id;
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