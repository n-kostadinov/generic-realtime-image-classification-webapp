FRAMES_PER_SECOND = 5;

var stompClient;
var imageCapture;
var mainCanvas;
window.onload = initialize;

function initImageCapture(){
	// Start WebCam View on Browser and init ImageCapture
	// check https://developers.google.com/web/updates/2016/12/imagecapture
	navigator.mediaDevices.getUserMedia({video: true})
	  .then(mediaStream => {
		  
	    const track = mediaStream.getVideoTracks()[0];
	    imageCapture = new ImageCapture(track);
	  }).catch(error => console.log(error));
	
}

function initialize() {

	initImageCapture()
	
	mainCanvas = document.querySelector('#mainCanvas');
	
    $("form").on('submit', function (event) {
    	event.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
	
	setInterval(updateCanvasAndSendImage, 1000 / FRAMES_PER_SECOND)
}

function updateCanvasAndSendImage() {
	
	if ( imageCapture ) {
		
		imageCapture.grabFrame()
		  .then(imageBitmap => {
		    drawCanvas(imageBitmap);
		    sendJPEGImage()
		  })
		  .catch(error => console.log(error));
	}
	
}

function sendJPEGImage(){
	
	if( stompClient ) {
		stompClient.send("/app/webcamimage", {}, mainCanvas.toDataURL("image/jpeg"));
	}
	
}

function drawCanvas(img) {
	
	  mainCanvas.width = getComputedStyle(mainCanvas).width.split('px')[0];
	  mainCanvas.height = getComputedStyle(mainCanvas).height.split('px')[0];
	  let ratio  = Math.min(mainCanvas.width / img.width, mainCanvas.height / img.height);
	  let x = (mainCanvas.width - img.width * ratio) / 2;
	  let y = (mainCanvas.height - img.height * ratio) / 2;
	  mainCanvas.getContext('2d').clearRect(0, 0, mainCanvas.width, mainCanvas.height);
	  mainCanvas.getContext('2d').drawImage(img, 0, 0, img.width, img.height,
	      x, y, img.width * ratio, img.height * ratio);

}


function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
}


function connect() {

	var socket = new SockJS('/websocket');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function (frame) {
	    setConnected(true);
	    console.log('Connected: ' + frame);
	    stompClient.subscribe('/topic/realtimeclassification', function (message) {
	        console.log(message);
	    });
	});
  
}

function disconnect() {
	
    if (stompClient != null) {
        stompClient.disconnect();
        stompClient = null;
    }
    setConnected(false);
    console.log("Disconnected");
}

// refactor
function showCatDogDTO(unparsed_message) {
	
	time = get_time()
	preffix = "<tr><td>[" + time +"] "
	suffix = "</td></tr>"
	
	message = JSON.parse(unparsed_message.body)
	if (message.resolved) {
		$("#catdogboard").prepend(
		    preffix + "Image processed by Deep-Cat-Dog" + suffix +
			preffix + "Image as seen by the Neural Network</td></tr>" + suffix + 
			preffix + "<img height=\"299\" width=\"299\" src=\"" + message.url + "\"/>" + suffix +
			preffix + "Label: " + message.label + suffix);
	} else {
		$("#catdogboard").prepend(preffix + message.content + suffix);
	}

}

