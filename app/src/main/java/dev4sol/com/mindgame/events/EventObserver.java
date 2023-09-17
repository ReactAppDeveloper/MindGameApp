package dev4sol.com.mindgame.events;

/**
 * Created by Cclub on 27/06/2018.
 */

import dev4sol.com.mindgame.events.engine.FlipDownCardsEvent;
import dev4sol.com.mindgame.events.engine.GameWonEvent;
import dev4sol.com.mindgame.events.engine.HidePairCardsEvent;
import dev4sol.com.mindgame.events.ui.BackGameEvent;
import dev4sol.com.mindgame.events.ui.DifficultySelectedEvent;
import dev4sol.com.mindgame.events.ui.FlipCardEvent;
import dev4sol.com.mindgame.events.ui.NextGameEvent;
import dev4sol.com.mindgame.events.ui.ResetBackgroundEvent;
import dev4sol.com.mindgame.events.ui.StartEvent;
import dev4sol.com.mindgame.events.ui.ThemeSelectedEvent;


public interface EventObserver {

    void onEvent(FlipCardEvent event);

    void onEvent(DifficultySelectedEvent event);

    void onEvent(HidePairCardsEvent event);

    void onEvent(FlipDownCardsEvent event);

    void onEvent(StartEvent event);

    void onEvent(ThemeSelectedEvent event);

    void onEvent(GameWonEvent event);

    void onEvent(BackGameEvent event);

    void onEvent(NextGameEvent event);

    void onEvent(ResetBackgroundEvent event);

}
