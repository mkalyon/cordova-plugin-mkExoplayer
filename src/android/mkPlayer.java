/*
 The MIT License (MIT)

 Copyright (c) 2020 mkalyon

 Original repositoryy https://github.com/frontyard/cordova-plugin-exoplayer

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
package com.mkalyon.cordova.plugins.mkEXO;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.RandomTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DefaultTrackNameProvider;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.ui.TrackSelectionDialogBuilder;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.widget.TextView;
import android.widget.Toast;

public class mkPlayer{
    public static final String TAG = "ExoPlayerPlugin";
    private final Activity activity;
    private final CallbackContext callbackContext;
    private final Configuration config;
    private Dialog dialog;
    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView exoView;
    private CordovaWebView webView;
    private int controllerVisibility;
    private boolean paused = false;
    private AudioManager audioManager;
    private DefaultTrackSelector trackSelector;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    DefaultTrackSelector.ParametersBuilder trackSelectorParamsBuilder;
    DefaultTrackSelector.SelectionOverride override;
    private List<String> items_url = new ArrayList<>();
    private List<String> items_title = new ArrayList<>();
    private List<String> items_logo = new ArrayList<>();
    private int sira=0;
    private int say=0;
    private boolean keyEvent;
    private boolean transParent;
    private boolean autoStop;
    private int ok=0;

    public mkPlayer(Configuration config, Activity activity, CallbackContext callbackContext, CordovaWebView webView) {
        this.config = config;
        this.activity = activity;
        this.callbackContext = callbackContext;
        this.webView = webView;
        this.audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);

        keyEvent = config.getKeyEvent();
        transParent = config.getTransParent();
        autoStop = config.getAutoStop();
        sira= config.getOrder();
        JSONArray items = null;
        try {
            items = config.getItems();
            for(int i = 0; i < items.length(); i++) {
                JSONObject item = null;
                item = items.getJSONObject(i);
                items_url.add(item.getString("url"));
                items_title.add(item.getString("title"));
                items_logo.add(item.getString("logo"));
            }
            say = items_url.size();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private ExoPlayer.EventListener playerEventListener = new ExoPlayer.EventListener() {
        @Override
        public void onLoadingChanged(boolean isLoading) {
            JSONObject payload = com.mkalyon.cordova.plugins.mkEXO.Payload.loadingEvent(mkPlayer.this.exoPlayer, isLoading);
            new com.mkalyon.cordova.plugins.mkEXO.CallbackResponse(mkPlayer.this.callbackContext).send(PluginResult.Status.OK, payload, true);
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            // Log.i(TAG, "Playback parameters changed");
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            JSONObject payload = com.mkalyon.cordova.plugins.mkEXO.Payload.playerErrorEvent(mkPlayer.this.exoPlayer, error, null);
            new com.mkalyon.cordova.plugins.mkEXO.CallbackResponse(mkPlayer.this.callbackContext).send(PluginResult.Status.ERROR, payload, true);
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (config.getShowBuffering()) {
                LayoutProvider.setBufferingVisibility(exoView, activity, playbackState == ExoPlayer.STATE_BUFFERING);
            }


            ProgressBar prgs= (ProgressBar)  findView(exoView , activity, "spinnerVideoDetails");
            switch (playbackState) {

                case Player.STATE_BUFFERING:
                    if (config.getShowSpinner()) {
                        //showToast("Loading...!");
                        prgs.setVisibility(View.VISIBLE);
                    }

                    break;
                case Player.STATE_ENDED:
                    // Activate the force enable
                    if(autoStop) {
                        stop();
                        close();
                    }
                    break;
                case Player.STATE_IDLE:

                    break;
                case Player.STATE_READY:
                    if (config.getShowSpinner()) {
                        prgs.setVisibility(View.INVISIBLE);
                    }
                    break;
                default:
                    // status = PlaybackStatus.IDLE;
                    break;
            }
            JSONObject payload = com.mkalyon.cordova.plugins.mkEXO.Payload.stateEvent(mkPlayer.this.exoPlayer, playbackState, mkPlayer.this.controllerVisibility == View.VISIBLE);
            new com.mkalyon.cordova.plugins.mkEXO.CallbackResponse(mkPlayer.this.callbackContext).send(PluginResult.Status.OK, payload, true);
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            JSONObject payload = com.mkalyon.cordova.plugins.mkEXO.Payload.positionDiscontinuityEvent(mkPlayer.this.exoPlayer, reason);
            new com.mkalyon.cordova.plugins.mkEXO.CallbackResponse(mkPlayer.this.callbackContext).send(PluginResult.Status.OK, payload, true);
        }

        @Override
        public void onRepeatModeChanged(int newRepeatMode) {
            // Need to see if we want to send this to Cordova.
        }

        @Override
        public void onSeekProcessed() {
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        }

        //@Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {

            JSONObject payload = Payload.timelineChangedEvent(mkPlayer.this.exoPlayer, timeline, manifest);
            new CallbackResponse(mkPlayer.this.callbackContext).send(PluginResult.Status.OK, payload, true);
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                        == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                    showToast("Media includes video tracks, but none are playable by this device");
                }
                if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
                        == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                    showToast("Media includes audio tracks, but none are playable by this device");
                }
            }


        }
    };
    private DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            if (exoPlayer != null) {
                exoPlayer.release();
            }
            exoPlayer = null;
            JSONObject payload = Payload.stopEvent(exoPlayer);
            new CallbackResponse(mkPlayer.this.callbackContext).send(PluginResult.Status.OK, payload, true);
        }
    };

    private static View findView(View view, Activity activity, String name) {
        int viewId = activity.getResources().getIdentifier(name, "id", activity.getPackageName());
        return view.findViewById(viewId);
    }

    private DialogInterface.OnKeyListener onKeyListener = (dialog, keyCode, event) -> {
        int action = event.getAction();
        String key = KeyEvent.keyCodeToString(event.getKeyCode());
        // We need android to handle these key events
        if(keyEvent) {
            if (key.equals("KEYCODE_VOLUME_UP") ||
                    key.equals("KEYCODE_VOLUME_DOWN") ||
                    key.equals("KEYCODE_VOLUME_MUTE")) {
                return false;
            }
            else{
                JSONObject payload = Payload.keyEvent(event);
                new CallbackResponse(mkPlayer.this.callbackContext).send(PluginResult.Status.OK, payload, true);
                return true;
            }
        }
        else {
            if (key.equals("KEYCODE_VOLUME_UP") ||
                    key.equals("KEYCODE_VOLUME_DOWN") ||
                    key.equals("KEYCODE_VOLUME_MUTE")) {
                return false;
            } else if (key.equals("KEYCODE_DPAD_UP") && event.getAction() == KeyEvent.ACTION_DOWN) {
                play_next();
                return false;
            } else if (key.equals("KEYCODE_DPAD_DOWN") && event.getAction() == KeyEvent.ACTION_DOWN) {
                play_prev();
                return false;
            } else if (key.equals("KEYCODE_MENU") && event.getAction() == KeyEvent.ACTION_DOWN) {
                showMenu();
                return false;
            } else if (key.equals("KEYCODE_BACK") && event.getAction() == KeyEvent.ACTION_DOWN) {
               stop();
               close();
                return false;
            } else if (key.equals("KEYCODE_ESCAPE") && event.getAction() == KeyEvent.ACTION_DOWN) {
                stop();
                close();
                return false;
            } else if (key.equals("KEYCODE_ENTER") && event.getAction() == KeyEvent.ACTION_DOWN) {
                showController();
                ok++;
                if(ok>1) {
                    hideController() ;
                    showMenu();
                    ok=0;
                }
                return false;
            } else if (key.equals("KEYCODE_DPAD_CENTER") && event.getAction() == KeyEvent.ACTION_DOWN) {
                showController();
                ok++;
                if(ok>1) {
                    hideController() ;
                    showMenu();
                    ok=0;
                }
                return false;
            }

            else{
                JSONObject payload = Payload.keyEvent(event);
                new CallbackResponse(mkPlayer.this.callbackContext).send(PluginResult.Status.OK, payload, true);
                return true;
            }
        }
    };
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        int previousAction = -1;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int eventAction = event.getAction();
            if (previousAction != eventAction) {
                previousAction = eventAction;
                JSONObject payload = Payload.touchEvent(event);
                new CallbackResponse(mkPlayer.this.callbackContext).send(PluginResult.Status.OK, payload, true);
            }
            return true;
        }
    };


    private PlaybackControlView.VisibilityListener playbackControlVisibilityListener = new PlaybackControlView.VisibilityListener() {
        @Override
        public void onVisibilityChange(int visibility) {
            mkPlayer.this.controllerVisibility = visibility;
        }
    };

    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                JSONObject payload = Payload.audioFocusEvent(mkPlayer.this.exoPlayer, "AUDIOFOCUS_LOSS_TRANSIENT");
                new CallbackResponse(mkPlayer.this.callbackContext).send(PluginResult.Status.OK, payload, true);
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                JSONObject payload = Payload.audioFocusEvent(mkPlayer.this.exoPlayer, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                new CallbackResponse(mkPlayer.this.callbackContext).send(PluginResult.Status.OK, payload, true);
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                JSONObject payload = Payload.audioFocusEvent(mkPlayer.this.exoPlayer, "AUDIOFOCUS_GAIN");
                new CallbackResponse(mkPlayer.this.callbackContext).send(PluginResult.Status.OK, payload, true);
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                JSONObject payload = Payload.audioFocusEvent(mkPlayer.this.exoPlayer, "AUDIOFOCUS_LOSS");
                new CallbackResponse(mkPlayer.this.callbackContext).send(PluginResult.Status.OK, payload, true);
            }
        }
    };
    public void createPlayer() {
        if (!config.isAudioOnly()) {
            createDialog();
        }
        preparePlayer(config.getUri());
    }
    public void createDialog() {
        dialog = new Dialog(this.activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setOnKeyListener(onKeyListener);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View decorView = dialog.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        dialog.setCancelable(true);
        dialog.setOnDismissListener(dismissListener);

        FrameLayout mainLayout = LayoutProvider.getMainLayout(this.activity);
        exoView = LayoutProvider.getExoPlayerView(this.activity, config);
        exoView.setControllerVisibilityListener(playbackControlVisibilityListener);

        mainLayout.addView(exoView);
        dialog.setContentView(mainLayout);
        dialog.show();

        dialog.getWindow().setAttributes(LayoutProvider.getDialogLayoutParams(activity, config, dialog));
        exoView.requestFocus();
        exoView.setOnTouchListener(onTouchListener);


        LayoutProvider.setupController(exoView, activity, config.getController());
        if (!config.getShowBuffering()) {
            LayoutProvider.setBufferingVisibility(exoView, activity, false);
        }
    }
    private int setupAudio() {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        return audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }
    private void preparePlayer(Uri uri) {

        int audioFocusResult = setupAudio();
        String audioFocusString = audioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_FAILED ?
                "AUDIOFOCUS_REQUEST_FAILED" :
                "AUDIOFOCUS_REQUEST_GRANTED";
        mclock() ;
        TrackSelection.Factory trackSelectionFactory;
        trackSelectionFactory = new RandomTrackSelection.Factory();
        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        trackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();

        exoPlayer  = ExoPlayerFactory.newSimpleInstance(this.activity , trackSelector, loadControl);
        exoPlayer.addListener(playerEventListener);
        if (null != exoView) {
            exoView.setPlayer(exoPlayer);
        }
        MediaSource mediaSource = getMediaSource(uri, bandwidthMeter);
        if (mediaSource != null) {
            long offset = config.getSeekTo();
            boolean autoPlay = config.autoPlay();
            if (offset > -1) {
                exoPlayer.seekTo(offset);
            }
            exoPlayer.prepare(mediaSource);

            exoPlayer.setPlayWhenReady(autoPlay);
            paused = !autoPlay;

            JSONObject payload = Payload.startEvent(exoPlayer, audioFocusString);
            new CallbackResponse(mkPlayer.this.callbackContext).send(PluginResult.Status.OK, payload, true);



        }
        else{
            showToast("Failed to construct mediaSource for " + uri) ;
            sendError("Failed to construct mediaSource for " + uri);
        }

    }
    private MediaSource getMediaSource(Uri uri, DefaultBandwidthMeter bandwidthMeter) {
        String userAgent = Util.getUserAgent(this.activity, config.getUserAgent());
        Handler mainHandler = new Handler();
        int connectTimeout = config.getConnectTimeout();
        int readTimeout = config.getReadTimeout();
        int retryCount = config.getRetryCount();

        HttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter, connectTimeout, readTimeout, true);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this.activity, bandwidthMeter, httpDataSourceFactory);
        MediaSource mediaSource;
        String extension="";
        String uritoString=uri.toString();
        if(uritoString.indexOf("|")>0){
            String[] parts = uritoString.split("\\|");
            uri = Uri.parse(parts[0]);
            extension = parts[1];
        }
        Log.i(TAG, "URLto String:"+uritoString);
        Log.i(TAG, "URL:"+uri.toString());
        Log.i(TAG, "EXTENSION:"+extension);
        int type = Util.inferContentType(uri,extension);
        switch (type) {
            case C.TYPE_DASH:
                Log.d(TAG, "buildMediaSource TYPE_DASH: TYPE_DASH");
                mediaSource = new DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                break;
            case C.TYPE_SS:
                Log.d(TAG, "buildMediaSource TYPE_DASH: TYPE_SS");
                mediaSource =  new SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                break;
            case C.TYPE_HLS:
                Log.d(TAG, "buildMediaSource TYPE_DASH: TYPE_HLS");
                mediaSource =  new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                break;
            case C.TYPE_OTHER:
                Log.d(TAG, "buildMediaSource TYPE_DASH: TYPE_OTHER");
                mediaSource =  new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                break;
            default:
                Log.d(TAG, "buildMediaSource TYPE_DASH: DEFAULT");
                mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                break;

        }

        String subtitleUrl = config.getSubtitleUrl();
        if (null != subtitleUrl) {
            Uri subtitleUri = Uri.parse(subtitleUrl);
            String subtitleType = inferSubtitleType(subtitleUri);
            com.google.android.exoplayer2.Format textFormat = com.google.android.exoplayer2.Format.createTextSampleFormat(null, subtitleType, null, com.google.android.exoplayer2.Format.NO_VALUE, com.google.android.exoplayer2.Format.NO_VALUE, "tr", null, 0);
            MediaSource subtitleSource = new SingleSampleMediaSource(subtitleUri, httpDataSourceFactory, textFormat, C.TIME_UNSET);
            return new MergingMediaSource(mediaSource, subtitleSource);
        }
        else {
            return mediaSource;
        }
    }

    private static String inferSubtitleType(Uri uri) {
        String fileName = uri.getPath().toLowerCase();

        if (fileName.endsWith(".vtt")) {
            return MimeTypes.TEXT_VTT;
        }
        else {
            // Assume it's srt.
            return MimeTypes.APPLICATION_SUBRIP;
        }
    }
    public void showToast(String message) {
        Toast.makeText(activity , message, Toast.LENGTH_LONG).show();
    }

    public void mclock(){
        TextView saat = (TextView) findView(exoView , activity, "clock");
        saat.setText("00:00");
        Calendar cal = Calendar.getInstance();
        int minutes = cal.get(Calendar.MINUTE);

        if (DateFormat.is24HourFormat(activity )) {
            int hours = cal.get(Calendar.HOUR_OF_DAY);
            hours=hours + config.getTimeZone();
            saat.setText((hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes));
        }
        else {
            int hours = cal.get(Calendar.HOUR);
            hours=hours + config.getTimeZone();
            saat.setText(hours + ":" + (minutes < 10 ? "0" + minutes : minutes));
        }
    }
    public void close() {
        audioManager.abandonAudioFocus(audioFocusChangeListener);
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
            dialog.dismiss();
        }
        if (this.dialog != null) {
            dialog.dismiss();
        }
    }
    public void setStream(Uri uri, JSONObject controller) {
        if (null != uri && null != exoPlayer) {
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            MediaSource mediaSource = getMediaSource(uri, bandwidthMeter);
            exoPlayer.prepare(mediaSource);
            play();
        }
        setController(controller);
    }
    public void play_next(){
        if(say>0) {
            sira++;
            if (sira >= say) {
                sira = 0;
            }
            if (sira < 0) {
                sira = say - 1;
            }
            JSONObject controller= config.getController();
            try {
                Uri link = Uri.parse(items_url.get(sira));
                controller.put("streamImage", items_logo.get(sira));
                controller.put("streamTitle", items_title.get(sira));
                setStream(link,controller) ;
                showController();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void play_prev(){
        if(say>0) {
            sira--;
            if (sira >= say) {
                sira = 0;
            }
            if (sira < 0) {
                sira = say - 1;
            }
            JSONObject controller= config.getController();
            try {
                Uri link = Uri.parse(items_url.get(sira));
                controller.put("streamImage", items_logo.get(sira));
                controller.put("streamTitle", items_title.get(sira));
                setStream(link,controller);
                showController();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void playPause() {
        if (this.paused) {
            play();
        }
        else {
            pause();
        }
    }

    private void pause() {
        if (null != exoPlayer) {
            paused = true;
            exoPlayer.setPlayWhenReady(false);
        }
    }

    private void play() {
        paused = false;
        exoPlayer.setPlayWhenReady(true);
    }

    public void stop() {
        paused = false;
        exoPlayer.stop();
    }

    private long normalizeOffset(long newTime) {
        long duration = exoPlayer.getDuration();
        return duration == 0 ? 0 : Math.min(Math.max(0, newTime), duration);
    }

    public JSONObject seekTo(long timeMillis) {
        long newTime = normalizeOffset(timeMillis);
        exoPlayer.seekTo(newTime);
        JSONObject payload = com.mkalyon.cordova.plugins.mkEXO.Payload.seekEvent(mkPlayer.this.exoPlayer, newTime);
        return payload;
    }

    public JSONObject seekBy(long timeMillis) {
        long newTime = normalizeOffset(exoPlayer.getCurrentPosition() + timeMillis);
        exoPlayer.seekTo(newTime);
        JSONObject payload = com.mkalyon.cordova.plugins.mkEXO.Payload.seekEvent(mkPlayer.this.exoPlayer, newTime);
        return payload;
    }

    public JSONObject getPlayerState() {
        return com.mkalyon.cordova.plugins.mkEXO.Payload.stateEvent(exoPlayer,
                null != exoPlayer ? exoPlayer.getPlaybackState() : SimpleExoPlayer.STATE_ENDED,
                mkPlayer.this.controllerVisibility == View.VISIBLE);
    }

    public void showController() {
        if (null != exoView) {
            JSONObject controller= config.getController();
            String textColor = controller.optString("textColor");
            if (null != textColor) {
                TextView titleView = (TextView) findView(exoView , activity, "exo_title");
                TextView epgView = (TextView) findView(exoView, activity, "exo_epg");
                TextView subtitleView = (TextView) findView(exoView, activity, "exo_subtitle");
                View timebarView = findView(exoView, activity, "exo_timebar");
                TextView positionView = (TextView) findView(timebarView, activity, "exo_position");
                TextView durationView = (TextView) findView(timebarView, activity, "exo_duration");
                TextView saat = (TextView) findView(exoView , activity, "clock");
                int intTextColor = Color.parseColor(textColor);
                if (null != titleView) {
                    titleView.setTextColor(intTextColor);
                }
                if (null != epgView) {
                    epgView.setTextColor(intTextColor);
                }
                if (null != subtitleView) {
                    subtitleView.setTextColor(intTextColor);
                }
                if (null != positionView) {
                    positionView.setTextColor(intTextColor);
                }
                if (null != durationView) {
                    durationView.setTextColor(intTextColor);
                }
                if (null != saat) {
                    saat.setTextColor(intTextColor);
                }
            }
            exoView.showController();
        }
    }

    public void hideController() {
        if (null != exoView) {
            exoView.hideController();
            ok=0;
        }
    }

    public void setController(JSONObject controller) {
        if (null != exoView) {
            com.mkalyon.cordova.plugins.mkEXO.LayoutProvider.setupController(exoView, activity, controller);
        }
    }
    public void showMenu(){
        List<String> list = new ArrayList<String>();
        list.add("PLAY/PAUSE");
        list.add("Playlist");
        list.add("Subtitles");
        list.add("Audios");
        list.add("Qualities");

        if(list.size() >0){
            final CharSequence[] texts = list.toArray(new CharSequence[list.size()]);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, android.R.style.Theme_Holo_Dialog_MinWidth);
            if(transParent) {
                builder = new AlertDialog.Builder(activity);
            }
            //builder.setTitle("TOOLS MENU");
            builder.setCancelable(true);
            builder.setSingleChoiceItems(texts, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            playPause();
                            break;
                        case 1:
                            if (say>0) {
                                getPlaylist();
                            }
                            else {
                                showToast("Playlist cannot found...!") ;
                            }
                            break;
                        case 2:
                            getSubtitles();
                            break;
                        case 3:
                            getAudios();
                            break;
                        case 4:
                            getVideos();
                            break;
                    }
                    dialog.dismiss();
                }
            });
            if(transParent) {
                builder.create();
                AlertDialog dialog = builder.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setGravity(Gravity.CENTER);

            }
            else{
                builder.create().show();

            }


        }

    }
    public void getPlaylist(){
        List<String> list = new ArrayList<String>();
        final List<Integer> listm = new ArrayList<>();

        for (int i = 0; i < items_url.size(); i++) {
            list.add(items_title.get(i).toString());
        }
        if(list.size() >0){
            final CharSequence[] texts = list.toArray(new CharSequence[list.size()]);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, android.R.style.Theme_Holo_Dialog_MinWidth);
            if(transParent) {
                builder = new AlertDialog.Builder(activity);
            }
            builder.setTitle("PLAYLIST("+items_url.size()+")");
            builder.setCancelable(true);
            builder.setSingleChoiceItems(texts, sira, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sira=which;
                    JSONObject controller= config.getController();
                    try {
                        Uri link = Uri.parse(items_url.get(sira));
                        controller.put("streamImage", items_logo.get(sira));
                        controller.put("streamTitle", items_title.get(sira));
                        setStream(link,controller);
                        showController();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();

                }
            });
            if(transParent) {
                builder.create();
                AlertDialog dialog = builder.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
            else{
                builder.create().show();

            }
        }
    }

    public void test(){
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            CharSequence title = "Video";
            int rendererIndex = 2;
            int rendererType = mappedTrackInfo.getRendererType(rendererIndex);
            boolean allowAdaptiveSelections =
                    rendererType == C.TRACK_TYPE_VIDEO
                            || (rendererType == C.TRACK_TYPE_AUDIO
                            && mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_NO_TRACKS);
            TrackSelectionDialogBuilder build = new TrackSelectionDialogBuilder(activity, "title", trackSelector, rendererIndex);

            build.setAllowAdaptiveSelections(allowAdaptiveSelections);
            build.build().show();
        }
    }
    public void offSubTitle(){
        trackSelector.setParameters(new DefaultTrackSelector.ParametersBuilder()
                .setRendererDisabled(C.TRACK_TYPE_VIDEO, true)
                .build()
        );
    }
    public void onSubTitle(){
        trackSelector.setParameters(new DefaultTrackSelector.ParametersBuilder()
                .setRendererDisabled(C.TRACK_TYPE_VIDEO, false)
                .build()
        );
    }
    public void getSubtitles(){
        String[] st = new String []{};
        List<String> list = new ArrayList<String>();
        list.add("None");
        onSubTitle();
        MappedTrackInfo mappedTrackInfo = Assertions.checkNotNull(trackSelector.getCurrentMappedTrackInfo());
        TrackGroupArray mrendererTrackGroups = mappedTrackInfo.getTrackGroups(2);
        for (int groupIndex = 0; groupIndex < mrendererTrackGroups.length; groupIndex++) {
            TrackGroup mtrackGroup = mrendererTrackGroups.get(groupIndex);
            //Log.d(TAG, groupIndex );
            for (int trackIndex = 0; trackIndex < mtrackGroup.length; trackIndex++) {
                Boolean isTrackSupported = mappedTrackInfo.getTrackSupport(2, groupIndex, trackIndex) == RendererCapabilities.FORMAT_HANDLED;
                String trackName = new DefaultTrackNameProvider(activity.getResources()).getTrackName(mrendererTrackGroups.get(groupIndex).getFormat(trackIndex ));
                String track = Format.toLogString(mtrackGroup.getFormat(trackIndex));
                String lang = mtrackGroup.getFormat(trackIndex).language;
                //Log.d(TAG, "      " + status + " mkTrack:" + lang + ", " + Format.toLogString(mtrackGroup.getFormat(trackIndex)) + ", supported=" + formatSupport);
                Log.d(TAG,"Trackmk Name: "+trackName + ", Lang: "+ lang+ ", isSupported: "+ isTrackSupported +", Track: "+ track);
                if(isTrackSupported) {
                    list.add(trackName + "(" + lang + ")");
                }
            }
        }
        if(list.size() >0){
            final CharSequence[] texts = list.toArray(new CharSequence[list.size()]);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, android.R.style.Theme_Holo_Dialog_MinWidth);
            if(transParent) {
                builder = new AlertDialog.Builder(activity);
            }
            builder.setTitle("Select Subtitle");
            builder.setCancelable(true);
            builder.setSingleChoiceItems(texts, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TrackGroupArray trackGroups = trackSelector.getCurrentMappedTrackInfo().getTrackGroups(2);
                    DefaultTrackSelector.ParametersBuilder parametersBuilder = trackSelector.buildUponParameters();

                    if(which == 0){
                        offSubTitle();
                    }
                    else {
                        which=which - 1;
                        DefaultTrackSelector.SelectionOverride override = new DefaultTrackSelector.SelectionOverride(which, 0);
                        boolean isDisabled = trackSelector.getParameters().getRendererDisabled(2);
                        parametersBuilder.setRendererDisabled(2, isDisabled);
                        if (override != null) {
                            parametersBuilder.setSelectionOverride(2, trackGroups, override);
                        } else {
                            parametersBuilder.clearSelectionOverrides(2);
                        }
                        trackSelector.setParameters(parametersBuilder);
                    }
                    dialog.dismiss();
                }
            });
            if(transParent) {
                builder.create();
                AlertDialog dialog = builder.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
            else{
                builder.create().show();

            }
        }
        else{
            showToast("Subtitle Tracks cannot found...!") ;
        }
    }
    public void getAudios(){
        String[] st = new String []{};
        List<String> list = new ArrayList<String>();
        Log.d(TAG,"Trackmk Audio: ");
        MappedTrackInfo mappedTrackInfo = Assertions.checkNotNull(trackSelector.getCurrentMappedTrackInfo());
        TrackGroupArray mrendererTrackGroups = mappedTrackInfo.getTrackGroups(1);
        for (int groupIndex = 0; groupIndex < mrendererTrackGroups.length; groupIndex++) {
            TrackGroup mtrackGroup = mrendererTrackGroups.get(groupIndex);
            for (int trackIndex = 0; trackIndex < mtrackGroup.length; trackIndex++) {
                Boolean isTrackSupported = mappedTrackInfo.getTrackSupport(1, groupIndex, trackIndex) == RendererCapabilities.FORMAT_HANDLED;
                String trackName = new DefaultTrackNameProvider(activity.getResources()).getTrackName(mrendererTrackGroups.get(groupIndex).getFormat(trackIndex ));
                String track = Format.toLogString(mtrackGroup.getFormat(trackIndex));
                String lang = mtrackGroup.getFormat(trackIndex).language;
                //Log.d(TAG, "      " + status + " mkTrack:" + lang + ", " + Format.toLogString(mtrackGroup.getFormat(trackIndex)) + ", supported=" + formatSupport);
                Log.d(TAG,"Trackmk Name: "+trackName + ", Lang: "+ lang+ ", isSupported: "+ isTrackSupported +", Track: "+ track);
                //st[trackIndex]=lang;
                if(isTrackSupported) {
                    list.add(trackName + "(" + lang + ")");
                }
            }
        }
        if(list.size() >0){
            final CharSequence[] texts = list.toArray(new CharSequence[list.size()]);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, android.R.style.Theme_Holo_Dialog_MinWidth);
            if(transParent) {
                builder = new AlertDialog.Builder(activity);
            }
            builder.setTitle("Select Audio");
            builder.setCancelable(true);
            builder.setSingleChoiceItems(texts, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TrackGroupArray trackGroups = trackSelector.getCurrentMappedTrackInfo().getTrackGroups(1);
                    DefaultTrackSelector.ParametersBuilder parametersBuilder = trackSelector.buildUponParameters();
                    DefaultTrackSelector.SelectionOverride override = new DefaultTrackSelector.SelectionOverride(which, 0);
                    boolean isDisabled = trackSelector.getParameters().getRendererDisabled(1);
                    parametersBuilder.setRendererDisabled(1, isDisabled);
                    if (override != null) {
                        Log.d(TAG,"mk null");
                        parametersBuilder.setSelectionOverride(1, trackGroups, override);
                    }
                    else {
                        Log.d(TAG,"nk dolu");
                        parametersBuilder.clearSelectionOverrides(1);
                    }
                    trackSelector.setParameters(parametersBuilder);
                    dialog.dismiss();
                }
            });
            if(transParent) {
                builder.create();
                AlertDialog dialog = builder.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
            else{
                builder.create().show();

            }
        }
        else{
            showToast("Audio Tracks cannot found...!");
        }
    }
    public void getVideos(){
        String[] st = new String []{};
        List<String> list = new ArrayList<String>();
        List<Integer> bitrate = new ArrayList<>();
        Log.d(TAG,"Trackmk Video: ");
        MappedTrackInfo mappedTrackInfo = Assertions.checkNotNull(trackSelector.getCurrentMappedTrackInfo());
        TrackGroupArray mrendererTrackGroups = mappedTrackInfo.getTrackGroups(0);
        for (int groupIndex = 0; groupIndex < mrendererTrackGroups.length; groupIndex++) {
            TrackGroup mtrackGroup = mrendererTrackGroups.get(groupIndex);
            for (int trackIndex = 0; trackIndex < mtrackGroup.length; trackIndex++) {
                Boolean isTrackSupported = mappedTrackInfo.getTrackSupport(0, groupIndex, trackIndex) == RendererCapabilities.FORMAT_HANDLED;
                String trackName = new DefaultTrackNameProvider(activity.getResources()).getTrackName(mrendererTrackGroups.get(groupIndex).getFormat(trackIndex ));
                String track = Format.toLogString(mtrackGroup.getFormat(trackIndex));
                bitrate.add(mtrackGroup.getFormat(trackIndex).bitrate);
               // Log.d(TAG,"Trackmk Name: "+trackName +  ", isSupported: "+ isTrackSupported +", Track: "+ track);
                if(isTrackSupported) {
                    list.add(trackName);
                }
            }
        }
        if(list.size() >0){
            final CharSequence[] texts = list.toArray(new CharSequence[list.size()]);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, android.R.style.Theme_Holo_Dialog_MinWidth);
            if(transParent) {
                builder = new AlertDialog.Builder(activity);
            }
            builder.setTitle("Select Quality");
            builder.setCancelable(true);
            builder.setSingleChoiceItems(texts, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   /* TrackGroupArray trackGroups = trackSelector.getCurrentMappedTrackInfo().getTrackGroups(0);
                    DefaultTrackSelector.ParametersBuilder parametersBuilder = trackSelector.buildUponParameters();
                    DefaultTrackSelector.SelectionOverride override = new DefaultTrackSelector.SelectionOverride(which, 0);
                    boolean isDisabled = trackSelector.getParameters().getRendererDisabled(0);
                    parametersBuilder.setRendererDisabled(0, isDisabled);
                    if (override != null) {
                        Log.d(TAG,"mk null");
                        parametersBuilder.setSelectionOverride(0, trackGroups, override);
                    }
                    else {
                        Log.d(TAG,"nk dolu");
                        parametersBuilder.clearSelectionOverrides(0);
                    }
                    trackSelector.setParameters(parametersBuilder);*/
                  // showToast(bitrate.get(which).toString()) ;
                    Integer mBitrate=bitrate.get(which);
                    if(mBitrate > 0) {
                        DefaultTrackSelector.Parameters parameters = trackSelector.buildUponParameters()
                                .setMaxVideoBitrate(mBitrate)
                                .setForceHighestSupportedBitrate(true)
                                .build();
                        trackSelector.setParameters(parameters);
                    }
                    dialog.dismiss();
                }
            });
            if(transParent) {
                builder.create();
                AlertDialog dialog = builder.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
            else{
                builder.create().show();

            }
        }
    }

    private void sendError(String msg) {
        showToast("Error:" + msg);
        JSONObject payload = com.mkalyon.cordova.plugins.mkEXO.Payload.playerErrorEvent(mkPlayer.this.exoPlayer, null, msg);
        new com.mkalyon.cordova.plugins.mkEXO.CallbackResponse(mkPlayer.this.callbackContext).send(PluginResult.Status.ERROR, payload, true);
    }

}
