let client = null;
let clientConnected = false;

// note onMessageHandler will replace previous value
function subscribeHomeMonitor(topic, onMessageHandler){

    if (client == null) {
        client = new Paho.MQTT.Client("localhost", 61614, "BrowserClientId");
        client.onConnectionLost = onConnectionLost;
        client.onMessageArrived = onMessageHandler;

        client.connect(
            {onSuccess:onConnect, onFailure: onConnectionFailed}
        );
    } else {
        if ( clientConnected ){
            client.onMessageArrived = onMessageHandler;
            console.log("already connected, subscribe to " + topic);
            client.subscribe(topic);
        }
    }


    function onConnect() {
        // Once a connection has been made, make a subscription and send a message.
        console.log("onConnect, subscribe to " + topic);
        client.subscribe(topic);
        clientConnected = true;
    };

    function onConnectionFailed(responseObject) {
      if (responseObject.errorCode !== 0)
	    console.log("onConnectionFailed:"+responseObject.errorMessage);
    };

    function onConnectionLost(responseObject) {
      if (responseObject.errorCode !== 0)
	    console.log("onConnectionLost:"+responseObject.errorMessage);
    };

    function disconnect() {
        client.disconnect()
    };

}
