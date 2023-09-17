package dev4sol.com.mindgame.events.engine;

/**
 * Created by Cclub on 27/06/2018.
 */

import dev4sol.com.mindgame.events.AbstractEvent;
import dev4sol.com.mindgame.events.EventObserver;
import dev4sol.com.mindgame.model.GameState;

/**
 * When the 'back to menu' was pressed.
 */
public class GameWonEvent extends AbstractEvent {

    public static final String TYPE = GameWonEvent.class.getName();

    public GameState gameState;


    public GameWonEvent(GameState gameState) {
        this.gameState = gameState;
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

