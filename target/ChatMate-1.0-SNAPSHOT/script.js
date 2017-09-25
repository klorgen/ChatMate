class Slideshow {
    constructor() {
        this.images = document.querySelector("#images");
        this.load();
    }
    load() {
        fetch('api/store/images')
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    }
                    throw new Error("Failed to load list of images");
                })
                .then(json => this.addImages(json))
                .catch(e => console.log("Error: " + e.message));
    }

    addImages(json) {
        this.images.innerHTML = '';
        for (let i = 0; i < json.length; i++) {
            
            let li = document.createElement('li');
            li.src = 'api/store/' + json[i].name + "?width=250";

            let a = document.createElement('a');
            a.href = "photo.html?photo=" + json[i].name;
            a.innerHTML = json[i].name + json[i].da;
            a.appendChild(li);

            this.images.appendChild(a);
        }
    }
}
let slideshow = new Slideshow();
