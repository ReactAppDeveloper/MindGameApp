package dev4sol.com.mindgame.common;

/**
 * Created by Cclub on 27/06/2018.
 */

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import dev4sol.com.mindgame.engine.Engine;
import dev4sol.com.mindgame.events.EventBus;

public class Shared {

    public static Context context;
    public static FragmentActivity activity; // it's fine for this app, but better move to weak reference
    public static Engine engine;
    public static EventBus eventBus;

}

