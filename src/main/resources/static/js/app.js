const BASE_URL = "http://localhost:8080/api/room/all";
let currentPage = 1; // Stores the current page for pagination
let searchQuery = ""; // Stores search input

/**
 * Fetches rooms from the API with pagination and search filters.
 */
async function fetchRooms(searchQuery = "") {
    try {
        const queryParams = new URLSearchParams({
            page: currentPage,
            search: searchQuery,
        });

        const response = await fetch(`${BASE_URL}?${queryParams}`);
        if (!response.ok) {
            throw new Error(`HTTP Error: ${response.status}`);
        }

        const data = await response.json();
        console.log("🔥 Rooms Data:", data);

        if (Array.isArray(data)) {
            return { rooms: data, totalPages: 1 };
        } else if (data.rooms) {
            return data;
        } else {
            throw new Error("Invalid API response format.");
        }
    } catch (error) {
        console.error("❌ Error fetching rooms:", error);
        return { rooms: [], totalPages: 1 };
    }
}

/**
 * Updates the room list on the webpage with the latest fetched data.
 */
function renderRooms(rooms) {
    const roomList = document.getElementById("room-list");
    roomList.innerHTML = ""; // Clear existing room cards

    if (!rooms || rooms.length === 0) {
        roomList.innerHTML = "<p>No rooms found.</p>";
        return;
    }

    rooms.forEach((room) => {
        const roomCard = document.createElement("div");
        roomCard.className = "room-card";
        roomCard.style.cursor = "pointer";

        // Define default images for locations if a room image is not provided
        const defaultImages = {
            "Nottingham": "/static/images/nottingham.jpg",
            "London": "/static/images/london.jpg",
            "Manchester": "/static/images/manchester.jpg",
            "Edinburgh": "/static/images/edinburgh.jpg",
            "Bristol": "/static/images/bristol.jpg"
        };

        const roomImage = room.image && room.image !== ""
            ? `/static/images/${room.image}`
            : defaultImages[room.location] || "/static/images/default-room.jpg";

        const price = room.price_per_month_gbp
            ? `£${room.price_per_month_gbp}/month`
            : "Contact for Price";

        // Weather Data Handling
        const weather = room.weather;
        let weatherHTML = "";
        if (weather) {
            weatherHTML = `
                <div class="weather-info">
                    <p><strong>🌡️ Temperature:</strong> ${weather.temperature}</p>
                    <p><strong>💧 Humidity:</strong> ${weather.humidity}</p>
                    <p><strong>🌤️ Condition:</strong> ${weather.main} - ${weather.description}</p>
                </div>
            `;
        }

        // **Make the whole card clickable**
        roomCard.innerHTML = `
            <a href="room.html?roomId=${room.id}" class="room-link">
                <img src="${roomImage}" alt="${room.name}" class="room-img">
                <div class="room-card-content">
                    <h3>${room.name}</h3>
                    <h4>${room.location ? room.location : "Unknown Location"}</h4>
                    ${weatherHTML}
                    <p><strong>${price}</strong></p>
                    <button class="apply-button">Apply Now</button>
                </div>
            </a>
        `;

        roomList.appendChild(roomCard);

        // Ensure Apply Button does not trigger navigation
        const applyButton = roomCard.querySelector(".apply-button");
        applyButton.addEventListener("click", function (event) {
            event.stopPropagation(); // Prevent click event from affecting <a> navigation
            event.preventDefault(); // Stop default behavior

            applyForRoom(room.id, room.name);
        });
    });
}

/**
 * Applies search and filters, then refreshes the displayed rooms.
 */
async function applyFilters() {
    const searchInput = document.getElementById("search").value.trim().toLowerCase();
    console.log("🔍 Searching for city:", searchInput);
    const data = await fetchRooms();

    if (!data.rooms || data.rooms.length === 0) {
        console.log(" No rooms found");
        renderRooms([]);
        return;
    }

    let filteredRooms = searchInput
        ? data.rooms.filter(room => room.location.toLowerCase().startsWith(searchInput))
        : data.rooms;

    if (filteredRooms.length === 0) {
        console.log("⚠️ No matching rooms found.");
    }
    renderRooms(filteredRooms);
}

/**
 * Initializes the room data fetching and rendering.
 */
async function initialize() {
    console.log("⚡ Initializing... Fetching rooms...");

    const data = await fetchRooms();
    console.log("✅ Rooms Loaded:", data);

    if (data.rooms && data.rooms.length > 0) {
        renderRooms(data.rooms);
        handlePagination(data.totalPages || 1);
    } else {
        console.error("❌ No valid data received from API.");
        document.getElementById("room-list").innerHTML = "<p>No rooms available.</p>";
    }
}

// Ensure the script runs after the page loads
document.addEventListener("DOMContentLoaded", initialize);

/**
 * Fetches and displays data for the current page.
 */
function handlePagination(totalPages) {
    console.log("📌 Handling pagination for", totalPages, "pages");

    const paginationContainer = document.getElementById("pagination");
    if (!paginationContainer) {
        console.error("❌ Pagination container not found!");
        return;
    }

    paginationContainer.innerHTML = ""; // Clear old pagination

    for (let page = 1; page <= totalPages; page++) {
        const pageButton = document.createElement("button");
        pageButton.innerText = page;
        pageButton.classList.add("pagination-button");

        if (page === currentPage) {
            pageButton.classList.add("active");
        }

        pageButton.addEventListener("click", async () => {
            currentPage = page;
            console.log(`🔄 Loading page ${currentPage}...`);
            const data = await fetchRooms();
            renderRooms(data.rooms);
            handlePagination(data.totalPages);
        });

        paginationContainer.appendChild(pageButton);
    }
}

/**
 * Event listeners for search and filter inputs.
 */
document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("search").addEventListener("input", applyFilters);
    const searchForm = document.querySelector(".search-form");
    const searchInput = document.getElementById("search");

    if (searchForm) {
        searchForm.addEventListener("submit", function (event) {
            event.preventDefault();

            const searchQuery = searchInput.value.trim();
            if (searchQuery === "") {
                alert("Please enter a city.");
                return;
            }

            console.log("Searching for:", searchQuery);
            fetchRooms(searchQuery);
        });
    } else {
        console.error("❌ Search form not found.");
    }
});

/**
 * Handles the application process for a selected room.
 */
async function applyForRoom(roomId, roomName) {
    const userName = prompt(`Apply for: ${roomName}\n\nEnter your full name:`);
    const userEmail = prompt("Enter your email:");

    if (!userName || !userEmail) {
        alert("❌ Application cancelled. You must enter both name and email.");
        return;
    }

    const applicationData = {
        roomId: roomId,
        userId: "user-123",
        name: userName,
        email: userEmail,
        roomName: roomName,
        status: "Pending"
    };

    try {
        const response = await fetch("http://localhost:8080/api/application/apply", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(applicationData)
        });

        if (!response.ok) {
            throw new Error("Failed to submit application");
        }

        alert("🎉 Application submitted successfully!");
        localStorage.setItem("appliedRoom", JSON.stringify(applicationData));
        window.location.href = "apply.html";
    } catch (error) {
        console.error("❌ Error submitting application:", error);
    }
}