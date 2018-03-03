var stompClient;
var imageCapture;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#catdogboard").html("");
}


function connect() {
//    var socket = new SockJS('/catdog-websocket');
//    stompClient = Stomp.over(socket);
//    stompClient.connect({}, function (frame) {
//        setConnected(true);
//        console.log('Connected: ' + frame);
//        stompClient.subscribe('/topic/catdog', function (message) {
//            showCatDogDTO(message);
//        });
//    });
	
  navigator.mediaDevices.getUserMedia({video: true})
  .then(mediaStream => {
    document.querySelector('video').srcObject = mediaStream;

    const track = mediaStream.getVideoTracks()[0];
    imageCapture = new ImageCapture(track);
  
  }).catch(error => console.log(error));
	
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendUserMessage() {
    stompClient.send("/app/usermessage", {}, JSON.stringify({'message': $("#usermessage").val()}));
}

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

function get_time() {
	return new Date().toTimeString().replace(/.*(\d{2}:\d{2}:\d{2}).*/, "$1");
}


$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendUserMessage(); });
});

