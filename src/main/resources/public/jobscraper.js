document.addEventListener('DOMContentLoaded', () => {

    document.getElementById("search").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            e.preventDefault();
            fetchJobs(e.target.value).then(jobs => createJobsResultElement(jobs));
        }
    });

    document.getElementById("tags").addEventListener("change", function (e) {
    });

    fetchJobs().then(jobs => createJobsResultElement(jobs));
});


/* *********************************************************************************************** */


async function fetchJobs(keywords) {
    const params = new URLSearchParams();
    if (keywords) {
        for (const keyword of keywords.split(" ")) {
            params.append("keywords", keyword);
        }
    }
    const url = keywords ? "api/jobs?" + params : "/api/jobs";

    try {
        const response = await fetch(url);
        if (response.ok) {
            return await response.json();
        } else {
            console.error("Error fetching jobs: ", response.statusText);
        }
    } catch (error) {
        console.error("Fetch Jobs failed: ", error);
    }
}

const createJobsResultElement = (jobs) => {
    const resultContainer = document.getElementById("search-result-container");
    resultContainer.replaceChildren();
    for (const job of jobs) {
        resultContainer.append(createResultElement(job));
    }
}


/* ******************************************************************************************** */

function createResultElement(job) {
    const {title, startDate} = job;
    var date = new Date(startDate);
    const result = createElement("div", "search-result");
    const titleRow = createElement("div", null, "result-title-row");

    titleRow.append(createElement("h2", null, "result-title", title));
    titleRow.append(createElement("div", null, "", date.toLocaleDateString("sv-SE")));
    result.appendChild(titleRow);

    const tagsEl = createElement("div", null, "tags");
    tagsEl.append(createElement("div", null, "tag", extractTag(job, "COMPANY")));
    tagsEl.append(createElement("div", null, "tag", extractTag(job, "LOCATION")));

    result.appendChild(tagsEl);

    return result;
}

function extractTag(job, tagType) {
    for (const tag of job.tags) {
        if (tag.type === tagType) {
            return tag.name;
        }
    }
}


function createElement(type, id, cssClass, content) {
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