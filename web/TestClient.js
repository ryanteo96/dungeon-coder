//var ip = "ws://13.59.183.75:8080/";
var TestUser = "TestUser" + "\n";

function body_onload() {
    testBtn.onclick = testBtn_onclick;
}

function testBtn_onclick() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var myObj = this.responseText;
            if (myObj == "True") {
                console.log(myObj);
            }
            
            //alert(myObj);
        }
    };
    xmlhttp.open("GET", "demo2.php", true);
    xmlhttp.send();

    
    /*var ws = new WebSocket(ip);

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
    };*/
}