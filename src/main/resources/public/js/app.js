document.addEventListener('DOMContentLoaded', () => {
    document.getElementById("search").addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            saveToLocal("searchKeywords", event.target.value);
            event.target.value = "";
            loadResultList();
        }
    });

    document.getElementById("tags").addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            saveToLocal("filterTags", event.target.value);
            event.target.value = "";
            loadResultList();
        }
    });


    const showDeleted = localStorage.getItem("showDeleted");
    const showDeletedEl = document.getElementById("show-deleted");
    showDeletedEl.src = showDeleted ? "delete-filled.png" : "delete.png";
    showDeletedEl.addEventListener("click", () => {
        const showDeleted = localStorage.getItem("showDeleted");
        if (showDeleted) {
            localStorage.removeItem("showDeleted");
            showDeletedEl.src = "delete.png";
        } else {
            localStorage.setItem("showDeleted", "true");
            showDeletedEl.src = "delete-filled.png";
        }
        loadResultList();
    });

    getFromLocal("searchKeywords").forEach(keyword => {
        const keywordEl = createElement("div", null, "tag", keyword);
        document.getElementById("search-keywords").append(keywordEl);
    });

    loadResultList();
});