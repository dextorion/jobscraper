document.addEventListener('DOMContentLoaded', () => {

    let searchEl = document.getElementById("search");
    searchEl.addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            e.preventDefault();
            fetchJobs(e.target.value).then(jobs => createJobsResultElement(jobs));
        }
    });

    let tagsEl = document.getElementById("tags");
    tagsEl.addEventListener("change", function (e) {});

    fetchJobs().then(jobs => createJobsResultElement(jobs));
});


async function fetchJobs(keywords) {
    const params = new URLSearchParams({keywords: keywords});
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

function createJobsResultElement(jobs) {
    const resultContainer = document.getElementById("search-result-container");
    resultContainer.replaceChildren();
    for (const job of jobs) {
        resultContainer.append(createResultElement(job));
    }
}

function createResultElement(job) {
    const result = createElement("div", "search-result");
    const titleRow = createElement("div", null, "result-title-row");
    const title = createElement("h2", null, "result-title");
    const date = createElement("div", null, "");
    const companyLocation = createElement("h4", null, null);

    let location = extractTag(job, "LOCATION");
    const company = extractTag(job, "COMPANY");

    title.innerHTML = job.title;
    date.innerHTML = job.startDate;
    companyLocation.innerHTML = company + " - " + location;

    titleRow.append(title);
    titleRow.append(date);
    result.appendChild(titleRow);
    result.appendChild(companyLocation);
    return result;
}

function extractTag(job, tagType) {
    for (const tag of job.tags) {
        if(tag.type === tagType) {
            return tag.name;
        }
    }
}

function createElement(type, id, cssClass) {
    const el = document.createElement(type);

    if (id) {
        el.setAttribute("id", id);
    }

    if (cssClass) {
        el.classList.add(cssClass);
    }

    return el;
}