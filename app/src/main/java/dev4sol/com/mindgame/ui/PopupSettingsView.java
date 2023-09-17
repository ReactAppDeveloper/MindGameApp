package dev4sol.com.mindgame.ui;

/**
 * Created by Cclub on 27/06/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import dev4sol.com.mindgame.R;
import dev4sol.com.mindgame.common.Music;
import dev4sol.com.mindgame.common.Shared;
import dev4sol.com.mindgame.utils.FontLoader;
import dev4sol.com.mindgame.utils.FontLoader.Font;

public class PopupSettingsView extends LinearLayout {

    private ImageView mSoundImage;
    private TextView mSoundText;

    public PopupSettingsView(Context context) {
        this(context, null);
    }

    public PopupSettingsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource(R.drawable.settings_popup);
        LayoutInflater.from(getContext()).inflate(R.layout.popup_settings_view, this, true);
        mSoundText = (TextView) findViewById(R.id.sound_off_text);
        mSoundImage = (ImageView) findViewById(R.id.sound_image);
        View soundOff = findViewById(R.id.sound_off);
        soundOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Music.OFF = !Music.OFF;
                setMusicButton();
            }
        });
        setMusicButton();
    }

    private void setMusicButton() {
        if (Music.OFF) {
            mSoundText.setText("Sound OFF");
            mSoundImage.setImageResource(R.drawable.button_music_off);
        } else {
            mSoundText.setText("Sound ON");
            mSoundImage.setImageResource(R.drawable.button_music_on);
        }
    }
}

