### cordova-plugin-mkExoplayer (NOT READY and NOT COMPLETED)

For Android, by [mkalyon](https://github.com/mkalyon)

## Description

Cordova Exoplayer Plugin

## Message from the maintainer:

Used 

- Exoplayer v2.11.8
- Cordova 8.0.0
- Android Platform 8.1.0

New Features:

- Sending Playlist
- Selection Subtitle/Audio/Quality(hls) tracks (if media has them)
- Loading spinner
- Epg data line
- Description line
- keyEvent control
- timeZone parameter for clock
- Clock
- Toast message
- Added getSubtitles / getAudios / getVideos / getPlaylist / showToast / showMenu 


<img src="https://raw.githubusercontent.com/mkalyon/screenshoots/master/1a.png" >
<img src="https://raw.githubusercontent.com/mkalyon/screenshoots/master/2a.png" >
<img src="https://raw.githubusercontent.com/mkalyon/screenshoots/master/3a.png" >
<img src="https://raw.githubusercontent.com/mkalyon/screenshoots/master/4a.png" >
<img src="https://raw.githubusercontent.com/mkalyon/screenshoots/master/5a.png" >

## Installation

Create a new Cordova Project

    $ cordova create hello com.example.helloapp Hello
    
Install the plugin

    $ cd hello
    $ cordova plugin add https://github.com/mkalyon/cordova-plugin-mkExoplayer.git
    

Edit `www/js/index.js` and add the following code inside `onDeviceReady`


### Android specifics

If you use android 8.1.0 sdk change build.gradle

open app_location/platform/android/build.gradle
find classpath 'com.android.tools.build:gradle:x.x.x' then change
classpath 'com.android.tools.build:gradle:3.0.1'

If you use android 9.0.0 sdk add cordova-plugin-androidx plugin

##Cordova execs

```js

    setStream(url, controllerConfig) // switch stream without disposing of the player. controllerConfig is "controller" part of the inital parameters. 
    playPause() // will pause if playing and play if paused :-)
    stop() // will stop the current stream
    seekTo(milliseconds) // jump to particular poing into the stream
    getState(successCallback, errorCallback) // returns player state
    showController() // shows player controller
    hideController() // hides player controller
    setController() // sets `controller` part of configuration related to the info bar and control buttons.
    close() // close and dispose of player, very important to call this method when your app exits!
    playPrevious() // play previous playlist item
    playNext // play next playlist item
    getSubtitles() // get captions if exits
    getAudios() // show audios if exits
    getVideos() // show video qualities if exits (only for hls links)
    getPlaylist() // show playlist 
    showToast(message) // show toast message 
    showMenu() // open tools menu (playlist/subtitles/audios/qualties)

```

## Usage
```js
	var successCallback = function(json) {
    	};

    	var errorCallback = function(error) {
    	};
                var url="http://a.com/a.mp4";
		var logo="http://a.com/logo.png";
		var title="Superman Retrun to Home";
		var epg="Epg1...............\nEpg2...............";
		var desc="Description";
		var parameters={
				url: url, // First url
				order:0,  // Playlist order
				aspectRatio: 'FIT_SCREEN', // default is FIT_SCREEN
				hideTimeout: 5000, // Hide controls after this many milliseconds, default is 5 sec
				forwardTime: 60 * 1000, // Amount of time to use when skipping forward, default is 1 min
				rewindTime: 60 * 1000, // Amount of time to use when skipping backward, default is 1 min
				//userAgent:"mkplayer",
				keyEvent:false, // true return key events to cordova, false key event using player
				//audioOnly: true, // Only play audio in the backgroud, default is false.
				//subtitleUrl: "http://a.com/subtitle.srt", // Optional subtitle url
				showBuffering: true, //buffer
				showSpinner:true, // loading spinner
				timeZone:3, // for clock
				transParent:true, // alert dialog theme
				autoStop:true, // when video is finished then auto stop video
				controller: { // If this object is not present controller will not be visible
					streamImage: logo,
					streamTitle: title,
					streamEpg: epg,
					streamDescription: desc,
					controlIcons: {
						'exo_rew': null, 
						'exo_play': null,
						'exo_pause': null,
						'exo_ffwd': null,
					},
					textColor: '#FF00A6FF', //'#ffffffff', // These colors can be any valid Android color
					buttonsColor: '#FF00A6FF', //'#ffffffff', // This example uses hex values including alpha (first byte)
					bufferingColor: '#ff0000ff', // Alpha of 'ff' makes it 100% opaque
					hideProgress: false, // Hide entire progress timebar
					hidePosition: false, // If timebar is visible hide current position from it
					hideDuration: false // If timebar is visible Hide stream duration from it

				},
				items: [
				      { title: "Superman Retrun to Home", url: "http://a.com/a.mp4", logo: "http://a.com/logo.png" },
				      { title: "TR: STAR TV UHD", url: "http://a.com/85977", logo: "http://a.com/star.png" },
				      { title: "TR: TV8 UHD", url: "http://a.com/85960", logo: "http://a.com/tv8.png" },
				      { title: "TR: ATV UHD", url: "http://a.com/85971", logo: "http://a.com/atvhd.png" },
				      { title: "TR: SHOW TV UHD", url: "http://a.com/85976", logo: "http://a.com/showhd.png" },
				      { title: "TR: KANAL D UHD", url: "http://a.com/85975", logo: "http://a.com/kanaldhd.png" },
				      { title: "TR: STAR TV UHD", url: "http://a.com/85977", logo: "http://a.com/star.png" },
				      { title: "TR: FOX TV UHD", url: "http://a.com/85973", logo: "http://a.com/foxhd2.png" },
				      { title: "TR: TV8 UHD", url: "http://a.com/85960", logo: "http://a.com/tv8.png" },
				      { title: "TR: ATV UHD", url: "http://a.com/85971", logo: "http://a.com/atvhd.png" },
				      { title: "TR: SHOW TV UHD", url: "http://a.com/85976", logo: "http://a.com/showhd.png" },                    
				      { title: "TR: TV8 UHD", url: "http://a.com/85960", logo: "http://a.com/tv8.png" }
				]
		};
	window.mkExoPlayer.show(parameters, successCallback, errorCallback);
```
**or You can add parameters items like that**
```js
			var parameters={
				url: url,
				order:0,
				aspectRatio: 'FIT_SCREEN', // default is FIT_SCREEN
				hideTimeout: 5000, // Hide controls after this many milliseconds, default is 5 sec
				forwardTime: 60 * 1000, // Amount of time to use when skipping forward, default is 1 min
				rewindTime: 60 * 1000, // Amount of time to use when skipping backward, default is 1 min
				userAgent:"mkplayer",
				keyEvent:false, // true return key events, false key event using player
				//audioOnly: true, // Only play audio in the backgroud, default is false.
				//subtitleUrl: 'http://a.com/subtitle.srt', // Optional subtitle url
				showBuffering: true,
				showSpinner:true,
				timeZone:3,
				transParent:true, // alert dialog theme
				autoStop:true, // when video is finished then auto stop video
				controller: { // If this object is not present controller will not be visible
					streamImage: logo,
					streamTitle: title,
					streamEpg: '',
					streamDescription: '',
					controlIcons: {
						'exo_rew': null, 
						'exo_play': null,
						'exo_pause': null,
						'exo_ffwd': null,
					},
					textColor: '#FF00A6FF', //'#ffffffff', // These colors can be any valid Android color
					buttonsColor: '#FF00A6FF', //'#ffffffff', // This example uses hex values including alpha (first byte)
					bufferingColor: '#ff0000ff', // Alpha of 'ff' makes it 100% opaque
					hideProgress: false, // Hide entire progress timebar
					hidePosition: false, // If timebar is visible hide current position from it
					hideDuration: false // If timebar is visible Hide stream duration from it

				},
				items: [] //empty items
			};	

	(parameters.items).push({title: value.title, url: lnk, logo: logo}); // use loop for all links

	window.mkExoPlayer.show(parameters, successCallback, errorCallback);
	
**Remote control usage**

	keyEvent:false  ==> Player used defined keys
	Up   : play next
	Down :play previous
	Left / Right : seek
	back: stop and close video
	enter/ok/dpad_center : once showController twice showMenu
	menu key : showMenu
	
```
## More Info

For more information on setting up Cordova see [the documentation](http://cordova.apache.org/docs/en/latest/guide/cli/index.html)

For more info on plugins see the [Plugin Development Guide](http://cordova.apache.org/docs/en/latest/guide/hybrid/plugins/index.html)

General ExoPlayer [documentation](https://google.github.io/ExoPlayer/)

ExoPlayer [source code](https://github.com/google/ExoPlayer)
https://github.com/mkalyon/cordova-plugin-mkExoplayer

## Special Thanks

"# cordova-plugin-mkExoplayer"

@frontyard - https://github.com/frontyard/cordova-plugin-exoplayer (forked codes)

@google - https://github.com/google/ExoPlayer (usage)
