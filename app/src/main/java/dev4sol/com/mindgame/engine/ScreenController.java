package dev4sol.com.mindgame.engine;

/**
 * Created by Cclub on 27/06/2018.
 */

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import dev4sol.com.mindgame.R;
import dev4sol.com.mindgame.common.Shared;
import dev4sol.com.mindgame.events.ui.ResetBackgroundEvent;
import dev4sol.com.mindgame.fragments.DifficultySelectFragment;
import dev4sol.com.mindgame.fragments.GameFragment;
import dev4sol.com.mindgame.fragments.MenuFragment;
import dev4sol.com.mindgame.fragments.ThemeSelectFragment;

public class ScreenController {

    private static ScreenController mInstance = null;
    private static List<Screen> openedScreens = new ArrayList<Screen>();
    private FragmentManager mFragmentManager;

    private ScreenController() {
    }

    public static ScreenController getInstance() {
        if (mInstance == null) {
            mInstance = new ScreenController();
        }
        return mInstance;
    }

    public static enum Screen {
        MENU,
        GAME,
        DIFFICULTY,
        THEME_SELECT
    }

    public static Screen getLastScreen() {
        return openedScreens.get(openedScreens.size() - 1);
    }

    public void openScreen(Screen screen) {
        mFragmentManager = Shared.activity.getSupportFragmentManager();

        if (screen == Screen.GAME && openedScreens.get(openedScreens.size() - 1) == Screen.GAME) {
            openedScreens.remove(openedScreens.size() - 1);
        } else if (screen == Screen.DIFFICULTY && openedScreens.get(openedScreens.size() - 1) == Screen.GAME) {
            openedScreens.remove(openedScreens.size() - 1);
            openedScreens.remove(openedScreens.size() - 1);
        }
        Fragment fragment = getFragment(screen);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        openedScreens.add(screen);
    }

    public boolean onBack() {
        if (openedScreens.size() > 0) {
            Screen screenToRemove = openedScreens.get(openedScreens.size() - 1);
            openedScreens.remove(openedScreens.size() - 1);
            if (openedScreens.size() == 0) {
                return true;
            }
            Screen screen = openedScreens.get(openedScreens.size() - 1);
            openedScreens.remove(openedScreens.size() - 1);
            openScreen(screen);
            if ((screen == Screen.THEME_SELECT || screen == Screen.MENU) &&
                    (screenToRemove == Screen.DIFFICULTY || screenToRemove == Screen.GAME)) {
                Shared.eventBus.notify(new ResetBackgroundEvent());
            }
            return false;
        }
        return true;
    }

    private Fragment getFragment(Screen screen) {
        switch (screen) {
            case MENU:
                return new MenuFragment();
            case DIFFICULTY:
                return new DifficultySelectFragment();
            case GAME:
                return new GameFragment();
            case THEME_SELECT:
                return new ThemeSelectFragment();
            default:
                break;
        }
        return null;
    }
}
