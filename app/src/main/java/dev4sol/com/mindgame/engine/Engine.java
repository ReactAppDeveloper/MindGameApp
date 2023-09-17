package dev4sol.com.mindgame.engine;

/**
 * Created by Cclub on 27/06/2018.
 */

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ImageView;

import dev4sol.com.mindgame.R;
import dev4sol.com.mindgame.common.Memory;
import dev4sol.com.mindgame.common.Music;
import dev4sol.com.mindgame.common.Shared;
import dev4sol.com.mindgame.engine.ScreenController.Screen;
import dev4sol.com.mindgame.events.EventObserverAdapter;
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
import dev4sol.com.mindgame.model.BoardArrangment;
import dev4sol.com.mindgame.model.BoardConfiguration;
import dev4sol.com.mindgame.model.Game;
import dev4sol.com.mindgame.model.GameState;
import dev4sol.com.mindgame.themes.Theme;
import dev4sol.com.mindgame.themes.Themes;
import dev4sol.com.mindgame.ui.PopupManager;
import dev4sol.com.mindgame.utils.Clock;
import dev4sol.com.mindgame.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Engine extends EventObserverAdapter {

    private static Engine mInstance = null;
    private Game mPlayingGame = null;
    private int mFlippedId = -1;
    private int mToFlip = -1;
    private ScreenController mScreenController;
    private Theme mSelectedTheme;
    private ImageView mBackgroundImage;
    private Handler mHandler;

    private Engine() {
        mScreenController = ScreenController.getInstance();
        mHandler = new Handler();
    }

    public static Engine getInstance() {
        if (mInstance == null) {
            mInstance = new Engine();
        }
        return mInstance;
    }

    public void start() {
        Shared.eventBus.listen(DifficultySelectedEvent.TYPE, this);
        Shared.eventBus.listen(FlipCardEvent.TYPE, this);
        Shared.eventBus.listen(StartEvent.TYPE, this);
        Shared.eventBus.listen(ThemeSelectedEvent.TYPE, this);
        Shared.eventBus.listen(BackGameEvent.TYPE, this);
        Shared.eventBus.listen(NextGameEvent.TYPE, this);
        Shared.eventBus.listen(ResetBackgroundEvent.TYPE, this);
    }

    public void stop() {
        mPlayingGame = null;
        mBackgroundImage.setImageDrawable(null);
        mBackgroundImage = null;
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;

        Shared.eventBus.unlisten(DifficultySelectedEvent.TYPE, this);
        Shared.eventBus.unlisten(FlipCardEvent.TYPE, this);
        Shared.eventBus.unlisten(StartEvent.TYPE, this);
        Shared.eventBus.unlisten(ThemeSelectedEvent.TYPE, this);
        Shared.eventBus.unlisten(BackGameEvent.TYPE, this);
        Shared.eventBus.unlisten(NextGameEvent.TYPE, this);
        Shared.eventBus.unlisten(ResetBackgroundEvent.TYPE, this);

        mInstance = null;
    }

    @Override
    public void onEvent(ResetBackgroundEvent event) {
        Drawable drawable = mBackgroundImage.getDrawable();
        if (drawable != null) {
            ((TransitionDrawable) drawable).reverseTransition(2000);
        } else {
            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... params) {
                    Bitmap bitmap = Utils.scaleDown(R.drawable.background, Utils.screenWidth(), Utils.screenHeight());
                    return bitmap;
                }

                protected void onPostExecute(Bitmap bitmap) {
                    mBackgroundImage.setImageBitmap(bitmap);
                };

            }.execute();
        }
    }

    @Override
    public void onEvent(StartEvent event) {
        mScreenController.openScreen(Screen.THEME_SELECT);
    }

    @Override
    public void onEvent(NextGameEvent event) {
        PopupManager.closePopup();
        int difficulty = mPlayingGame.boardConfiguration.difficulty;
        if (mPlayingGame.gameState.achievedStars == 3 && difficulty < 6) {
            difficulty++;
        }
        Shared.eventBus.notify(new DifficultySelectedEvent(difficulty));
    }

    @Override
    public void onEvent(BackGameEvent event) {
        PopupManager.closePopup();
        mScreenController.openScreen(Screen.DIFFICULTY);
    }

    @Override
    public void onEvent(ThemeSelectedEvent event) {
        mSelectedTheme = event.theme;
        mScreenController.openScreen(Screen.DIFFICULTY);
        AsyncTask<Void, Void, TransitionDrawable> task = new AsyncTask<Void, Void, TransitionDrawable>() {

            @Override
            protected TransitionDrawable doInBackground(Void... params) {
                Bitmap bitmap = Utils.scaleDown(R.drawable.background, Utils.screenWidth(), Utils.screenHeight());
                Bitmap backgroundImage = Themes.getBackgroundImage(mSelectedTheme);
                backgroundImage = Utils.crop(backgroundImage, Utils.screenHeight(), Utils.screenWidth());
                Drawable backgrounds[] = new Drawable[2];
                backgrounds[0] = new BitmapDrawable(Shared.context.getResources(), bitmap);
                backgrounds[1] = new BitmapDrawable(Shared.context.getResources(), backgroundImage);
                TransitionDrawable crossfader = new TransitionDrawable(backgrounds);
                return crossfader;
            }

            @Override
            protected void onPostExecute(TransitionDrawable result) {
                super.onPostExecute(result);
                mBackgroundImage.setImageDrawable(result);
                result.startTransition(2000);
            }
        };
        task.execute();
    }

    @Override
    public void onEvent(DifficultySelectedEvent event) {
        mFlippedId = -1;
        mPlayingGame = new Game();
        mPlayingGame.boardConfiguration = new BoardConfiguration(event.difficulty);
        mPlayingGame.theme = mSelectedTheme;
        mToFlip = mPlayingGame.boardConfiguration.numTiles;

        // arrange board
        arrangeBoard();

        // start the screen
        mScreenController.openScreen(Screen.GAME);
    }

    private void arrangeBoard() {
        BoardConfiguration boardConfiguration = mPlayingGame.boardConfiguration;
        BoardArrangment boardArrangment = new BoardArrangment();

        // build pairs
        // result {0,1,2,...n} // n-number of tiles
        List<Integer> ids = new ArrayList<Integer>();
        for (int i = 0; i < boardConfiguration.numTiles; i++) {
            ids.add(i);
        }
        // shuffle
        // result {4,10,2,39,...}
        Collections.shuffle(ids);

        // place the board
        List<String> tileImageUrls = mPlayingGame.theme.tileImageUrls;
        Collections.shuffle(tileImageUrls);
        boardArrangment.pairs = new HashMap<Integer, Integer>();
        boardArrangment.tileUrls = new HashMap<Integer, String>();
        int j = 0;
        for (int i = 0; i < ids.size(); i++) {
            if (i + 1 < ids.size()) {
                // {4,10}, {2,39}, ...
                boardArrangment.pairs.put(ids.get(i), ids.get(i + 1));
                // {10,4}, {39,2}, ...
                boardArrangment.pairs.put(ids.get(i + 1), ids.get(i));
                // {4,
                boardArrangment.tileUrls.put(ids.get(i), tileImageUrls.get(j));
                boardArrangment.tileUrls.put(ids.get(i + 1), tileImageUrls.get(j));
                i++;
                j++;
            }
        }

        mPlayingGame.boardArrangment = boardArrangment;
    }

    @Override
    public void onEvent(FlipCardEvent event) {
        // Log.i("my_tag", "Flip: " + event.id);
        int id = event.id;
        if (mFlippedId == -1) {
            mFlippedId = id;
            // Log.i("my_tag", "Flip: mFlippedId: " + event.id);
        } else {
            if (mPlayingGame.boardArrangment.isPair(mFlippedId, id)) {
                // Log.i("my_tag", "Flip: is pair: " + mFlippedId + ", " + id);
                // send event - hide id1, id2
                Shared.eventBus.notify(new HidePairCardsEvent(mFlippedId, id), 1000);
                // play music
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Music.playCorrent();
                    }
                }, 1000);
                mToFlip -= 2;
                if (mToFlip == 0) {
                    int passedSeconds = (int) (Clock.getInstance().getPassedTime() / 1000);
                    Clock.getInstance().pause();
                    int totalTime = mPlayingGame.boardConfiguration.time;
                    GameState gameState = new GameState();
                    mPlayingGame.gameState = gameState;
                    // remained seconds
                    gameState.remainedSeconds = totalTime - passedSeconds;
                    gameState.passedSeconds = passedSeconds;

                    // calc stars
                    if (passedSeconds <= totalTime / 2) {
                        gameState.achievedStars = 3;
                    } else if (passedSeconds <= totalTime - totalTime / 5) {
                        gameState.achievedStars = 2;
                    } else if (passedSeconds < totalTime) {
                        gameState.achievedStars = 1;
                    } else {
                        gameState.achievedStars = 0;
                    }

                    // calc score
                    gameState.achievedScore = mPlayingGame.boardConfiguration.difficulty * gameState.remainedSeconds * mPlayingGame.theme.id;

                    // save to memory
                    Memory.save(mPlayingGame.theme.id, mPlayingGame.boardConfiguration.difficulty, gameState.achievedStars);
                    Memory.saveTime(mPlayingGame.theme.id, mPlayingGame.boardConfiguration.difficulty ,gameState.passedSeconds);



                    Shared.eventBus.notify(new GameWonEvent(gameState), 1200);
                }
            } else {
                // Log.i("my_tag", "Flip: all down");
                // send event - flip all down
                Shared.eventBus.notify(new FlipDownCardsEvent(), 1000);
            }
            mFlippedId = -1;
            // Log.i("my_tag", "Flip: mFlippedId: " + mFlippedId);
        }
    }

    public Game getActiveGame() {
        return mPlayingGame;
    }

    public Theme getSelectedTheme() {
        return mSelectedTheme;
    }

    public void setBackgroundImageView(ImageView backgroundImage) {
        mBackgroundImage = backgroundImage;
    }
}
