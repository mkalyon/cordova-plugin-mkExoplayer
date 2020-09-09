/*
 The MIT License (MIT)

 Copyright (c) 2020 mkalyon

 Original repository https://github.com/frontyard/cordova-plugin-exoplayer

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

import android.net.*;
import org.json.*;

public class Configuration {
    private final JSONObject config;

    public Configuration(JSONObject config) {

        this.config = config;
    }

    public final Uri getUri() {

        return Uri.parse(config.optString("url", ""));
    }

    public final JSONObject getDimensions() {

        return config.optJSONObject("dimensions");
    }

    public String getUserAgent() {

        return this.config.optString("userAgent", "ExoPlayerPlugin");
    }

    public boolean isAspectRatioFillScreen() {
        return config.optString("aspectRatio", "FIT_SCREEN").equalsIgnoreCase("FILL_SCREEN");
    }

    public boolean isAudioOnly() {

        return config.optBoolean("audioOnly");
    }

    public boolean autoPlay() {

        return config.optBoolean("autoPlay", true);
    }

    public long getSeekTo() {

        return config.optLong("seekTo", -1);
    }

    public final JSONObject getController() {

        return config.optJSONObject("controller");
    }

    public final JSONArray  getItems() throws JSONException {
        return config.getJSONArray("items");
    }

    public boolean getKeyEvent() {

        return config.optBoolean("keyEvent", true);
    }

    public boolean getTransParent() {
        return config.optBoolean("transParent", false);
    }

    public boolean getAutoStop() {
        return config.optBoolean("autoStop", false);
    }

    public int getHideTimeout() {

        return config.optInt("hideTimeout", 5000); // Default 5 sec.
    }

    public int getOrder() {

        return config.optInt("order", 0); // Default 0.
    }

    public int getForwardTimeMs() {

        return config.optInt("forwardTime", 60000); // Default 1 min.
    }

    public int getRewindTimeMs() {

        return config.optInt("rewindTime", 60000); // Default 1 min.
    }

    public int getTimeZone() {

        return config.optInt("timeZone", 0); // Default 0.
    }

    public String getSubtitleUrl() {

        return config.optString("subtitleUrl", null);
    }

    public int getConnectTimeout() {
        return config.optInt("connectTimeout", 10000); // Default 10 sec.
    }

    public int getReadTimeout() {
        return config.optInt("readTimeout", 10000); // Default 10 sec.
    }

    public int getRetryCount() {
        return config.optInt("retryCount", 10);
    }

    public boolean getShowBuffering() {
        return config.optBoolean("showBuffering");
    }

    public boolean getShowSpinner() {
        return config.optBoolean("showSpinner");
    }
}