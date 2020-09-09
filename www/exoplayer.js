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
module.exports = {
    show: function (parameters, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "show", [parameters]);
    },
    setStream: function (url, controller, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "setStream", [url, controller]);
    },
    playPause: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "playPause", []);
    },
    playPrevious: function (successCallback, errorCallback) {
            cordova.exec(successCallback, errorCallback, "mkExoPlayer", "playPrevious", []);
    },
    playNext: function (successCallback, errorCallback) {
             cordova.exec(successCallback, errorCallback, "mkExoPlayer", "playNext", []);
    },
    stop: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "stop", []);
    },
    seekTo: function (milliseconds, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "seekTo", [milliseconds]);
    },
    seekBy: function (milliseconds, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "seekBy", [milliseconds]);
    },
    getState: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "getState", []);
    },
    showController: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "showController", []);
    },
    hideController: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "hideController", []);
    },
    getSubtitles: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "getSubtitles", []);
    },
    getAudios: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "getAudios", []);
    },
    getVideos: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "getVideos", []);
    },
    getPlaylist: function (successCallback, errorCallback) {
            cordova.exec(successCallback, errorCallback, "mkExoPlayer", "getPlaylist", []);
    },
    showToast: function (message, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "showToast", [message]);
    },
    showMenu: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "showMenu", []);
    },
    setController: function (controller, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "setController", [controller]);
    },
    close: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "close", []);
    }
};