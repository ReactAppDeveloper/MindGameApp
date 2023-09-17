package dev4sol.com.mindgame.fragments;

/**
 * Created by Cclub on 27/06/2018.
 */

import android.support.v4.app.Fragment;
import dev4sol.com.mindgame.events.EventObserver;
import dev4sol.com.mindgame.events.engine.FlipDownCardsEvent;
import dev4sol.com.mindgame.events.engine.GameWonEvent;
import dev4sol.com.mindgame.events.engine.HidePairCardsEvent;
import dev4sol.com.mindgame.events.ui.BackGameEvent;
import dev4sol.com.mindgame.events.ui.FlipCardEvent;
import dev4sol.com.mindgame.events.ui.NextGameEvent;
import dev4sol.com.mindgame.events.ui.ResetBackgroundEvent;
import dev4sol.com.mindgame.events.ui.ThemeSelectedEvent;
import dev4sol.com.mindgame.events.ui.DifficultySelectedEvent;
import dev4sol.com.mindgame.events.ui.StartEvent;

public class BaseFragment extends Fragment implements EventObserver {

    @Override
    public void onEvent(FlipCardEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(DifficultySelectedEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(HidePairCardsEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(FlipDownCardsEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(StartEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(ThemeSelectedEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(GameWonEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(BackGameEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(NextGameEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(ResetBackgroundEvent event) {
        throw new UnsupportedOperationException();
    }

}

