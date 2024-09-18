document.addEventListener('DOMContentLoaded', () => {
    document.getElementById("search").addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            saveToLocal("searchKeywords", event.target.value);
            event.target.value = "";
            loadResult();
        }
    });
    document.getElementById("tags").addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            saveToLocal("filterTags", event.target.value);
            event.target.value = "";
            loadResult();
        }
    });

    getFromLocal("searchKeywords").forEach(keyword => {
        const keywordEl = createElement("div", null, "tag", keyword);
        document.getElementById("search-keywords").append(keywordEl);
    })

    loadResult();
});

const loadResult = () => {
    const searchKeywords = getFromLocal("searchKeywords");
    const filterTags = getFromLocal("filterTags");
    fetchJobs(searchKeywords, filterTags).then(jobs => createSearchResult(jobs));

    const searchKeywordsEl = document.getElementById("search-keywords");
    const filterTagsEl = document.getElementById("filter-tags");

    searchKeywordsEl.textContent = "";
    for (const keyword of searchKeywords) {
        searchKeywordsEl.append(createElement("div", null, "tag", keyword));
    }

    filterTagsEl.textContent = "";
    for (const tag of filterTags) {
        filterTagsEl.append(createElement("div", null, "tag", tag));
    }

}


async function fetchJobs(keywords, tags) {
    const params = new URLSearchParams();
    if (keywords) {
        for (const keyword of keywords) {
            params.append("keywords", keyword);
        }
    }
    if (tags) {
        for (const tag of tags) {
            params.append("filterwords", tag);
        }
    }
    const url = params.size > 0 ? "api/jobs?" + params : "/api/jobs";

    return get(url);
}


const createSearchResult = (jobs) => {
    const resultContainer = document.getElementById("search-result-container");
    resultContainer.replaceChildren();
    for (const job of jobs) {
        resultContainer.append(createResultElement(job));
    }
}

const createResultElement = (job) => {
    const {title, startDate} = job;

    const result = createElement("div", "search-result");
    const titleRow = createElement("div", null, "result-title-row");

    titleRow.append(createElement("h2", null, "result-title", title));
    titleRow.append(createElement("div", null, "", new Date(startDate).toLocaleDateString("sv-SE")));
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
