const createElement = (type, id, cssClass, content) => {
    const el = document.createElement(type);
    if (id) {
        el.setAttribute("id", id);
    }
    if (cssClass) {
        el.classList.add(cssClass);
    }
    if (content) {
        el.innerHTML = content;
    }
    return el;
}

function extractTag(tags, tagType) {
    for (const tag of tags) {
        if (tag.type === tagType) {
            return tag.name;
        }
    }
}

const get = async (url) => {
    try {
        const response = await fetch(url);
        if (response.ok) {
            return await response.json();
        } else {
            console.error("GET request error! url: " + url, response.statusText);
        }
    } catch (error) {
        console.error("GET request error: url: " + url, error);
    }
}

const post = async (url, body) => {
    try {
        const response = await fetch(url, {
            method: "POST",
            body: body ? JSON.stringify(body) : null
        });
        if (response.ok) {
            return await response.json();
        } else {
            console.error("POST request error: url: " + url, response.statusText);
        }
    } catch (error) {
        console.error("POST request error! url: " + url, error);
    }
}

const saveToLocal = (key, value) => {
    const values = localStorage.getItem(key);

    if (values) {
        const valuesArr = values.split(";");
        valuesArr.push(value);
        localStorage.setItem(key, valuesArr.join(";"))
    } else {
        localStorage.setItem(key, value);
    }
}

const getFromLocal = (key) => {
    const item = localStorage.getItem(key);
    if (item) {
        return localStorage.getItem(key).split(";");
    } else {
        return [];
    }
}

const removeFromLocal = (key, value) => {
    const values = localStorage.getItem(key);
    if (values) {
        const valuesArr = values.split(";");
        valuesArr.splice(valuesArr.indexOf(value), 1);
        localStorage.setItem(key, valuesArr.join(";"));
    }
}