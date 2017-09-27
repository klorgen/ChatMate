class Slideshow {
    constructor() {
        this.rooms = document.querySelector("#chat");
        this.user = document.querySelector("#user");
        this.load();

    }

    load() {
        fetch('api/messages/chatrooms')
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    }
                    throw new Error("Failed to load list of chatrooms");
                })
                .then(json => this.addChatRooms(json))
                .catch(e => console.log("Error: " + e.message));
    }

    addChatRooms(json) {
        this.rooms.innerHTML = '';
        for (let i = 0; i < json.length; i++) {

            let ul = document.createElement('ul');


            let a = document.createElement('a');
            a.href = "chat.html?room=" + json[i].id + "&user=" + this.user.value;
            a.innerHTML = "#" + json[i].id + " Last changed: " + json[i].version;
            a.appendChild(ul);

            this.rooms.appendChild(a);
        }
    }

}
let slideshow = new Slideshow();

function addNewChatroom() {
    this.rooms = document.querySelector("#chat");

    elementCount = rooms.childElementCount + 1;
    window.location = "chat.html?room=chatroom" + elementCount;
}

function updateUser(value) {
    this.user = value;
    console.log(this.user);
}