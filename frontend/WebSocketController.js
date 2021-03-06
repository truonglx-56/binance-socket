// IE compatible JS

var wsController = {};

var headers = {
    'authorization': 'jwt-token',
};

wsController._onConnected = function (frame) {
    var message = document.getElementById('text').value;
    console.log(message);
    if (message === "" || message === undefined) {
        alert("Fill data to input!")
        return
    }
    this.setConnected(true);
    console.log('Connected: ' + frame);
    this.stompClient.subscribe('/topic/' + message, this.showMessage, headers);
    document.getElementById('disconnect').innerText = "Disconnect " + message + " channel"

};

wsController.setConnected = function (connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('mural').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('response').innerHTML = '';
};

wsController.onChangeText = function (connected) {
    var message = document.getElementById('text').value;
    document.getElementById('connect').innerText = "Connect " + message + " channel";
};


wsController.connect = function () {
    var socket = new SockJS('http://54.145.158.156:8005/ws-socket-alert', null);
    this.stompClient = Stomp.over(socket);

    // TODO in authorization we will put JWT token, user and password will be set by backend
    this.stompClient.connect(headers, this._onConnected);

    this._onConnected = this._onConnected.bind(this);

};

wsController.disconnect = function () {
    if (this.stompClient != null) {
        this.stompClient.disconnect(headers);
    }
    this.setConnected(false);
    document.getElementById('connect').innerText = "Connect";
    console.log("Disconnected");
};

wsController.showMessage = function (message) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(message.body));
    response.appendChild(p);
};

wsController._onConnected = wsController._onConnected.bind(wsController);


wsController.sendMessage = function () {
    var message = document.getElementById('text').value;
    if (message === "" || message === undefined) {
        alert("Fill data to input!")
        return
    }
    postData('http://54.145.158.156:8005/exchange/binance/update/symbol', {"symbol": message})
        .then(data => {
            console.log(data);
            if (data !== 200) {
                document.getElementById('text').innerText = '';
                document.getElementById('connect').innerText = "Connect";
            }
            alert(data === 200 ? "Ok" : "Binance: Invalid Symbol")// JSON data parsed by `data.json()` call
        });
};

async function postData(url = '', data = {}) {
    // Default options are marked with *
    const response = await fetch(url, {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        mode: 'cors', // no-cors, *cors, same-origin
        cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
        credentials: 'same-origin', // include, *same-origin, omit
        headers: {
            'Content-Type': 'application/json'
            // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        redirect: 'follow', // manual, *follow, error
        referrerPolicy: 'no-referrer', // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
        body: JSON.stringify(data) // body data type must match "Content-Type" header
    });
    return response.status; // parses JSON response into native JavaScript objects
}
