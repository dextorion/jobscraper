const loadResultList = () => {
    const searchKeywords = getFromLocal("searchKeywords");
    const filterTags = getFromLocal("filterTags");
    const showDeleted = localStorage.getItem("showDeleted");

    fetchJobs(searchKeywords, filterTags, showDeleted).then(jobs => createSearchResult(jobs));

    const searchKeywordsEl = document.getElementById("search-keywords");
    searchKeywordsEl.textContent = "";
    for (const keyword of searchKeywords) {
        searchKeywordsEl.append(createTagElement(searchKeywords, keyword));
    }

    const filterTagsEl = document.getElementById("filter-tags");
    filterTagsEl.textContent = "";
    for (const tag of filterTags) {
        filterTagsEl.append(createTagElement(tag, "filterTags"));
    }
}

async function fetchJobs(keywords, tags, showDeleted) {
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
    if (showDeleted) {
        params.append("attribute", "DELETED");
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

const createTagElement = (tagName, localePersistenceKey) => {
    const tagEl = createElement("div", null, "tag", tagName);
    tagEl.addEventListener("click", () => {
        removeFromLocal(localePersistenceKey, tagName);
        loadResultList();
    });
    return tagEl;
}


const setAttribute = async (id, attribute, value) => {
    const params = new URLSearchParams();
    params.append(attribute, value);
    return post("api/jobs/" + id + "?" + params);
}


const createResultElement = (job) => {
    const {title, startDate} = job;

    const result = createElement("div", "search-result");
    const titleRow = createElement("div", null, "result-title-row");

    titleRow.append(createElement("h2", null, "result-title", title));
    titleRow.append(createElement("div", null, "", new Date(startDate).toLocaleDateString("sv-SE")));
    result.appendChild(titleRow);

    const resultActionsTagsRow = createElement("div", null, "result-actions-tags-row");
    const resultActions = createElement("div", null, "result-actions");
    const deleteEl = createElement("img", "delete-job", null, null);
    deleteEl.src = "delete.png";
    deleteEl.addEventListener("click", () => setAttribute(job.id, "deleted", true).then(() => loadResultList()));
    const applyEl = createElement("img", "apply-job", null, null);
    applyEl.src = "check.png";
    deleteEl.addEventListener("click", () => setAttribute(job.id, "applied", true).then(() => loadResultList()));
    const bookmark = createElement("img", "bookmark", null, null);
    bookmark.src = "bookmark.png";
    deleteEl.addEventListener("click", () => setAttribute(job.id, "bookmarked", true).then(() => loadResultList()));

    resultActions.append(deleteEl);
    resultActions.append(applyEl);
    resultActions.append(bookmark);
    resultActionsTagsRow.append(resultActions);

    const tagsEl = createElement("div", null, "tags");

    tagsEl.append(createElement("div", null, "tag", extractTag(job.tags, "COMPANY")));
    tagsEl.append(createElement("div", null, "tag", extractTag(job.tags, "LOCATION")));

    resultActionsTagsRow.append(tagsEl);

    result.appendChild(resultActionsTagsRow);

    return result;
}
