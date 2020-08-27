/**mkalyon**/
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
    showToast: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "showToast", []);
    },
    createList: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "createList", []);
    },
    setController: function (controller, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "setController", [controller]);
    },
    close: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "mkExoPlayer", "close", []);
    }
};
