class ChatRoom {
    constructor() {
        this.chat = document.querySelector("#chatroom");
        this.message = document.querySelector("#message");


        this.name = new URL(document.URL).searchParams.get("room");
        this.user = new URL(document.URL).searchParams.get("user");
        document.getElementById("headline").textContent = "Welcome to " + this.name + " " + this.user;


        this.message.onchange = event => {
            fetch('api/messages/add?name=' + this.name,
                    {
                        method: 'POST',
                        body: JSON.stringify(new Message(this.user, event.target.value)),
                        headers: {'Content-Type': 'application/json; charset=UTF-8'}
                    })
                    .then(response => {
                        if (response.ok) {
                            return response.json();
                        }
                        throw new Error("Failed to send message '" + event.target.value + "' from user: " + this.user);
                    })
                    .then(message => {
                        this.message.value = "";
                    })
                    .catch(exception => console.log("Error: " + exception));
        };

        this.worker = new Worker("worker.js");
        this.worker.postMessage({"name": this.name});

        this.worker.onmessage = event => {
            this.chat.innerHTML = '';
            let ul = document.createElement('ul');
            event.data.map(message => {
                let li = document.createElement('li');
                li.innerHTML = `${message.user} - ${message.text}`;
                ul.appendChild(li);
            });
            this.chat.appendChild(ul);
        };
    }
}


class Message {
    constructor(user, text) {
        this.text = text;
        this.user = user;
        this.version = new Date();
    }
}

let chat = new ChatRoom();