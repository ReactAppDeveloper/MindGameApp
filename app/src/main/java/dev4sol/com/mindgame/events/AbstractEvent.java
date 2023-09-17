package dev4sol.com.mindgame.events;

/**
 * Created by Cclub on 27/06/2018.
 */

public abstract class AbstractEvent implements Event {

    protected abstract void fire(EventObserver eventObserver);

}
