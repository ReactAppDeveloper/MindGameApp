package dev4sol.com.mindgame.fragments;

/**
 * Created by Cclub on 27/06/2018.
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import dev4sol.com.mindgame.R;
import dev4sol.com.mindgame.common.Shared;
import dev4sol.com.mindgame.events.engine.FlipDownCardsEvent;
import dev4sol.com.mindgame.events.engine.GameWonEvent;
import dev4sol.com.mindgame.events.engine.HidePairCardsEvent;
import dev4sol.com.mindgame.model.Game;
import dev4sol.com.mindgame.ui.BoardView;
import dev4sol.com.mindgame.ui.PopupManager;
import dev4sol.com.mindgame.utils.Clock;
import dev4sol.com.mindgame.utils.Clock.OnTimerCount;
import dev4sol.com.mindgame.utils.FontLoader;
import dev4sol.com.mindgame.utils.FontLoader.Font;

public class GameFragment extends BaseFragment {

    private BoardView mBoardView;
    private TextView mTime;
    private ImageView mTimeImage;
    private LinearLayout ads;
    InterstitialAd interstitialAd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.game_fragment, container, false);
        view.setClipChildren(false);
        ((ViewGroup)view.findViewById(R.id.game_board)).setClipChildren(false);
        mTime = (TextView) view.findViewById(R.id.time_bar_text);
        mTimeImage = (ImageView) view.findViewById(R.id.time_bar_image);
        FontLoader.setTypeface(Shared.context, new TextView[] {mTime}, Font.GROBOLD);
        mBoardView = BoardView.fromXml(getActivity().getApplicationContext(), view);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.game_container);
        frameLayout.addView(mBoardView);
        frameLayout.setClipChildren(false);

        // build board
        buildBoard();
        Shared.eventBus.listen(FlipDownCardsEvent.TYPE, this);
        Shared.eventBus.listen(HidePairCardsEvent.TYPE, this);
        Shared.eventBus.listen(GameWonEvent.TYPE, this);
        interstitialAd = new InterstitialAd(getActivity());
        interstitialAd.setAdUnitId("ca-app-pub-6668447434437594/1211268842");
        AdRequest adRequest =  new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitialAd.show();
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        Shared.eventBus.unlisten(FlipDownCardsEvent.TYPE, this);
        Shared.eventBus.unlisten(HidePairCardsEvent.TYPE, this);
        Shared.eventBus.unlisten(GameWonEvent.TYPE, this);
        super.onDestroy();
    }

    private void buildBoard() {
        Game game = Shared.engine.getActiveGame();
        int time = game.boardConfiguration.time;
        setTime(time);
        mBoardView.setBoard(game);

        startClock(time);
    }

    private void setTime(int time) {
        int min = time / 60;
        int sec = time - min*60;
        mTime.setText(" " + String.format("%02d", min) + ":" + String.format("%02d", sec));
    }

    private void startClock(int sec) {
        Clock clock = Clock.getInstance();
        clock.startTimer(sec*1000, 1000, new OnTimerCount() {

            @Override
            public void onTick(long millisUntilFinished) {
                setTime((int) (millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                setTime(0);
            }
        });
    }

    @Override
    public void onEvent(GameWonEvent event) {
        mTime.setVisibility(View.GONE);
        mTimeImage.setVisibility(View.GONE);
        PopupManager.showPopupWon(event.gameState);
    }

    @Override
    public void onEvent(FlipDownCardsEvent event) {
        mBoardView.flipDownAll();
    }

    @Override
    public void onEvent(HidePairCardsEvent event) {
        mBoardView.hideCards(event.id1, event.id2);
    }

}
