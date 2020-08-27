# cordova-plugin-mkExoplayer

For Android, by [mkalyon](https://github.com/mkalyon)

## Description

Cordova Exoplayer Plugin

## Message from the maintainer:

Used 

-- Exoplayer v2.11.7

-- Cordova 8.0.0

-- Android Platform 8.1.0

## Installation



### Android specifics


## Usage

           `var url='http://a.com/a.mp4';
		var logo='http://a.com/logo.png';
		var title='Superman Retrun to Home';
		var epg='Epg1...............\nEpg2...............';
		var desc='Description';
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
				//subtitleUrl: 'http://a.com/subtitle.srt', // Optional subtitle url
				showBuffering: true, //buffer
				showSpinner:true, // loading spinner
				timeZone:3, // for epg
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

	window.mkExoPlayer.show(parameters, successCallback, errorCallback);`

**or You can add parameters items like that**

`var parameters={
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

	window.mkExoPlayer.show(parameters, successCallback, errorCallback);`
	
## Special Thanks

"# cordova-plugin-mkExoplayer"

@frontyard - https://github.com/frontyard/cordova-plugin-exoplayer (forked codes)

@google - https://github.com/google/ExoPlayer (usage)
