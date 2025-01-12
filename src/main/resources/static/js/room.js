const BASE_URL = "http://localhost:8080/api/room/all";

// Run script after page loads
document.addEventListener("DOMContentLoaded", initialize);

let currentFilters = {
    search: "",
    maxPrice: "",
    furnished: false,
    billsIncluded: false
};

/**
 * Fetch rooms from API with filters.
 */
async function fetchRooms() {
    try {
        const queryParams = new URLSearchParams({
            search: currentFilters.search || "",
            maxPrice: currentFilters.maxPrice || "",
            furnished: currentFilters.furnished ? "true" : "false",
            billsIncluded: currentFilters.billsIncluded ? "true" : "false"
        });

        const response = await fetch(`${BASE_URL}?${queryParams}`);
        if (!response.ok) {
            throw new Error(`HTTP Error: ${response.status}`);
        }

        const data = await response.json();
        console.log("🔥 Rooms Data:", data);

        return Array.isArray(data) ? { rooms: data, totalPages: data.length } : data.rooms ? data : { rooms: [] };
    } catch (error) {
        console.error("❌ Error fetching rooms:", error);
        return { rooms: [] };
    }
}

/**
 * Applies search filters and fetches updated data.
 */
async function applyFilters() {
    const searchInput = document.getElementById("search").value.trim().toLowerCase();
    currentFilters.maxPrice = document.getElementById("price-range").value || "";
    currentFilters.furnished = document.getElementById("furnished").checked;
    currentFilters.billsIncluded = document.getElementById("bills-included").checked;

    console.log("🔍 Applying Filters:", currentFilters);
    const data = await fetchRooms();

    let filteredRooms = data.rooms.filter(room =>
        room.location.toLowerCase().startsWith(searchInput)
    );

    renderRooms(filteredRooms);
}

/**
 * Renders rooms on the webpage.
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

        const booleanIcons = (value) => (value ? "✅" : "❌");

        const amenitiesList = room.details.amenities
            .map((amenity) => `<li>⭐ ${amenity}</li>`)
            .join("");

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

        const weatherInfo = room.weather
            ? `<p>🌤️ <strong>Weather:</strong> ${room.weather.main} - ${room.weather.temperature}, ${room.weather.humidity} humidity</p>`
            : "<p>🌤️ Weather: No data</p>";

        const spokenLanguages = room.spokenLanguages && room.spokenLanguages.length > 0
            ? room.spokenLanguages.join(", ")
            : "Not specified";

        // ✅ Check Distance Feature
        const distanceCheck = `
            <div class="distance-checker">
                <input type="text" id="uni-location-${room.id}" placeholder="Enter university name">
                <button onclick="checkDistance('${room.id}', '${room.location}')">📍 Check Distance</button>
                <p id="distance-result-${room.id}"></p>
            </div>
        `;

        roomCard.innerHTML = `
            <img src="${roomImage}" alt="${room.name}" class="room-img">
            <div class="room-card-content">
                <h3>${room.name}</h3>
                <p><strong>📍 Location:</strong> ${room.location}</p>
                <p><strong>💰 Price:</strong> £${room.price_per_month_gbp}/month</p>
                ${weatherInfo}
                <p><strong>🛏️ Furnished:</strong> ${booleanIcons(room.details.furnished)}</p>
                <p><strong>💡 Bills Included:</strong> ${booleanIcons(room.details.bills_included)}</p>
                <p><strong>🚿 Bathroom Shared:</strong> ${booleanIcons(room.details.bathroom_shared)}</p>
                <p><strong>👥 Shared With:</strong> ${room.details.shared_with} people</p>
                <p><strong>🏡 Live-in Landlord:</strong> ${booleanIcons(room.details.liveInLandlord)}</p>
                <p><strong>🏠 Amenities:</strong></p>
                 <ul>${amenitiesList}</ul>
                <p><strong>🗣️ Spoken Languages:</strong> ${spokenLanguages}</p>
                ${distanceCheck}
                <button class="apply-button" onclick="applyForRoom('${room.id}', '${room.name}')">Apply Now</button>
            </div>
        `;

        roomList.appendChild(roomCard);
    });
}

/**
 * Checks the distance between a university and a room.
 */
async function checkDistance(roomId, roomLocation) {
    const uniInput = document.getElementById(`uni-location-${roomId}`).value;

    if (!uniInput) {
        alert("❌ Please enter a university name!");
        return;
    }

    try {
        const response = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${uniInput}`);
        const uniData = await response.json();

        if (uniData.length === 0) {
            document.getElementById(`distance-result-${roomId}`).innerText = "❌ University not found!";
            return;
        }

        const uniCoords = [parseFloat(uniData[0].lat), parseFloat(uniData[0].lon)];

        const roomResponse = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${roomLocation}`);
        const roomData = await roomResponse.json();

        if (roomData.length === 0) {
            document.getElementById(`distance-result-${roomId}`).innerText = "❌ Room location not found!";
            return;
        }

        const roomCoords = [parseFloat(roomData[0].lat), parseFloat(roomData[0].lon)];
        const distance = calculateDistance(roomCoords[0], roomCoords[1], uniCoords[0], uniCoords[1]);

        document.getElementById(`distance-result-${roomId}`).innerText = `📏 Distance to University: ${distance.toFixed(2)} km`;
    } catch (error) {
        console.error("❌ Error calculating distance:", error);
        document.getElementById(`distance-result-${roomId}`).innerText = "❌ Error retrieving distance!";
    }
}

/**
 * Haversine formula for distance calculation.
 */
function calculateDistance(lat1, lon1, lat2, lon2) {
    const EARTH_RADIUS = 6371;
    const dLat = Math.toRadians(lat2 - lat1);
    const dLon = Math.toRadians(lon2 - lon1);

    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);

    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return EARTH_RADIUS * c;
}

// Convert degrees to radians
Math.toRadians = function(degrees) {
    return degrees * Math.PI / 180;
};

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

/**
 * Initializes the room loading process.
 */
async function initialize() {
    console.log("⚡ Fetching rooms...");
    const data = await fetchRooms();
    console.log("✅ Rooms Loaded:", data);

    if (data.rooms && data.rooms.length > 0) {
        renderRooms(data.rooms);
    } else {
        console.error("❌ No valid data received from API.");
        document.getElementById("room-list").innerHTML = "<p>No rooms available.</p>";
    }
}