package dev4sol.com.mindgame;

/**
 * Created by Cclub on 27/06/2018.
 */

import android.app.Application;
import dev4sol.com.mindgame.utils.FontLoader;

public class GameApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FontLoader.loadFonts(this);

    }
}
