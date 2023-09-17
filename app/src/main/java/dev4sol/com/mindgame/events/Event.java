package dev4sol.com.mindgame.events;

/**
 * Created by Cclub on 27/06/2018.
 */

/**
 * The event that is invoked from the low levels of this game (like engine) and
 * not from the ui.
 *
 * @author sromku
 */
public interface Event {

    String getType();

}
