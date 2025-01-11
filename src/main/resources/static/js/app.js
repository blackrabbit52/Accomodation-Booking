const BASE_URL = "http://localhost:8080/api/room/all";
let currentPage = 1; // Stores the current page for pagination
let searchQuery = ""; // Stores search input
let filters = {
    minPrice: 0,
    maxPrice: 2000,
    furnished: false,
    billsIncluded: false
};

/**
 * Fetches rooms from the API with pagination and search filters.
 */
async function fetchRooms(searchQuery = "") {
    try { // Construct query parameters with the current search and filter values

        const queryParams = new URLSearchParams({
            page: currentPage,
            search: searchQuery,
            minPrice: filters.minPrice,
            maxPrice: filters.maxPrice,
            furnished: filters.furnished,
            billsIncluded: filters.billsIncluded
        });

        const response = await fetch(`${BASE_URL}?${queryParams}`);
        if (!response.ok) {
            throw new Error(`HTTP Error: ${response.status}`);
        }

        const data = await response.json();
        console.log("🔥 Rooms Data:", data);

        // Handle both possible API response formats
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

    // Show a message if no rooms are found
    if (!rooms || rooms.length === 0) {
        roomList.innerHTML = "<p>No rooms found.</p>";
        return;
    }
    // Loop through the list of rooms and create a card for each one
    rooms.forEach((room) => {
        const roomCard = document.createElement("div");
        roomCard.className = "room-card";

        // Define default images for locations if a room image is not provided
        const defaultImages = {
            "Nottingham": "/static/images/nottingham.jpg",
            "London": "/static/images/london.jpg",
            "Manchester": "/static/images/manchester.jpg",
            "Edinburgh": "/static/images/edinburgh.jpg",
            "Bristol": "/static/images/bristol.jpg"
        };

        //  image loading
        const roomImage = room.image && room.image !== ""
            ? `/static/images/${room.image}`
            : defaultImages[room.location] || "/static/images/default-room.jpg";

        // Set price correctly
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

        // Render HTML for each room
        roomCard.innerHTML = `
            <img src="${roomImage}" alt="${room.name}" class="room-img">
            <div class="room-card-content">
                <h3>${room.name}</h3>
                <h4>${room.location ? room.location : "Unknown Location"}</h4>
                ${weatherHTML}
                <p><strong>${price}</strong></p>
               
                    <button class="apply-button" onclick="applyForRoom('${room.id}', '${room.name}', '${room.location}', '${room.price_per_month_gbp}')"> Apply Now</button>  
            </div>
        `;
        roomList.appendChild(roomCard);
    });
}
/**
 * Applies search and filters, then refreshes the displayed rooms.
 */

async function applyFilters() {
    searchQuery = document.getElementById("search").value.trim();
    filters.maxPrice = document.getElementById("price-range").value || 2000;
    filters.furnished = document.getElementById("furnished").checked;
    filters.billsIncluded = document.getElementById("bills-included").checked;

    currentPage = 1; // Reset to first page when searching
    await loadPageData();
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
        localStorage.setItem("appliedRoom", JSON.stringify(savedApplication));
        window.location.href = "apply.html";
    } catch (error) {
        console.error("❌ Error submitting application:", error);
    }
}


/**
 * Fetches and displays data for the current page.
 */
async function loadPageData() {
    console.log(`🔄 Loading page ${currentPage}...`);
    const data = await fetchRooms();
    renderRooms(data.rooms);
    handlePagination(data.totalPages);
}

/**
 * Attach event listeners for search and filter inputs.
 */
document.addEventListener("DOMContentLoaded", function () {
    const searchForm = document.querySelector(".search-form"); // Select the form
    const searchInput = document.getElementById("search");

    if (searchForm) {
        searchForm.addEventListener("submit", function (event) {
            event.preventDefault(); // Prevent page reload

            const searchQuery = searchInput.value.trim();
            if (searchQuery === "") {
                alert("Please enter a search term.");
                return;
            }

            console.log("Searching for:", searchQuery);
            fetchRooms(searchQuery); // Call function to fetch data
        });
    } else {
        console.error("❌ Search form not found.");
    }
});

document.getElementById("search").addEventListener("input", applyFilters);
document.getElementById("price-range").addEventListener("change", applyFilters);
document.getElementById("furnished").addEventListener("change", applyFilters);
document.getElementById("bills-included").addEventListener("change", applyFilters);