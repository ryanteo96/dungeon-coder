var ip = "ws://13.59.183.75";
var port = "37536";


function body_onload() {
    testBtn.onclick = testBtn_onclick();
}

function testBtn_onclick() {
    var ws = new WebSocket(ip, port);

    ws.onopen = function() {
        ws.send("HI from Web!");
        alert("message sent");  
    }

    ws.onmessage = function (evt) { 
       var received_msg = evt.data;
       alert("Message is received...");
    };
     
    ws.onclose = function() { 
       // websocket is closed.
       alert("Connection is closed..."); 
    };
         
    window.onbeforeunload = function(event) {
       socket.close();
    };
}