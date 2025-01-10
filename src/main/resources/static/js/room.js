const BASE_URL = "http://localhost:8080/api/room/all";

//   object stores the filters that users select
let currentFilters = {
    search: "",          // User's search input
    maxDistance: 100,    // Default max distance in km
    maxPrice: "",        // Default max price
    furnished: false,    // Whether the room is furnished
    billsIncluded: false // Whether bills are included
};

//  fetch rooms from API using the selected filters
async function fetchRooms() {
    try {
        const queryParams = new URLSearchParams({
            search: currentFilters.search || "",    //  Search filter
            maxPrice: currentFilters.maxPrice || "", //  Price filter
            furnished: currentFilters.furnished ? "true" : "false",
            billsIncluded: currentFilters.billsIncluded ? "true" : "false"
        });

        console.log("🔍 Fetching rooms with filters:", queryParams.toString());

        const response = await fetch(`${BASE_URL}?${queryParams}`);
        if (!response.ok) {
            throw new Error("Failed to fetch rooms");
        }

        const data = await response.json();
        console.log("🔥 Rooms Data:", data);

        // 🔄 Ensure the data format is correct
        if (Array.isArray(data)) {
            return { rooms: data };
        } else if (data.rooms) {
            return data;
        } else {
            throw new Error("Invalid API response format.");
        }
    } catch (error) {
        console.error("❌ Error fetching rooms:", error);
        return { rooms: [] };
    }
}

//  apply search and filter settings, then refresh the displayed rooms
async function applyFilters() {
    console.log("🔄 Applying filters...");

    // taking values from the input fields and checkboxes
    currentFilters.search = document.getElementById("search").value.trim().toLowerCase();
    currentFilters.maxPrice = document.getElementById("price-range").value || "";
    currentFilters.furnished = document.getElementById("furnished").checked;
    currentFilters.billsIncluded = document.getElementById("bills-included").checked;

    console.log("✅ Filters Updated:", currentFilters);

    //  Fetch new room data with applied filters
    const data = await fetchRooms();
    renderRooms(data.rooms);
}

//  Render the list of rooms on the page
function renderRooms(rooms) {
    const roomList = document.getElementById("room-list");
    roomList.innerHTML = ""; //  Clear existing rooms

    if (!rooms || rooms.length === 0) {
        roomList.innerHTML = "<p>No rooms found.</p>";
        return;
    }

    rooms.forEach((room) => {
        const roomCard = document.createElement("div");
        roomCard.className = "room-card";

        //  Use icons to indicate true/false values
        const booleanIcons = (value) => (value ? "✅" : "❌");

        //  Generate list of amenities
        const amenitiesList = room.details.amenities
            .map((amenity) => `<li>⭐ ${amenity}</li>`)
            .join("");

        //  Distance checker input
        const distanceCheck = `
            <div class="distance-checker">
                <input type="text" id="uni-location-${room.id}" placeholder="Enter university name">
                <button onclick="checkDistance('${room.id}', '${room.location}')">📍 Check Distance</button>
                <p id="distance-result-${room.id}"></p>
            </div>
        `;

        //  Select room image (default if not provided)
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

        //  Weather information
        const weatherInfo = room.weather
            ? `<p>🌤️ <strong>Weather:</strong> ${room.weather.main} - ${room.weather.temperature}, ${room.weather.humidity} humidity</p>`
            : "<p>🌤️ Weather: No data</p>";

        //  Spoken languages
        const spokenLanguages = room.spoken_languages && room.spoken_languages.length > 0
            ? room.spoken_languages.join(", ")
            : "Not specified";

        //  Create the room card HTML
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
                <p><strong>🗣️ Spoken Languages:</strong> ${spoken_languages}</p>

                ${distanceCheck}
                <button class="apply-button" onclick="applyForRoom('${room.id}', '${room.name}')">Apply Now</button>
            </div>
        `;

        roomList.appendChild(roomCard); // 📥 Add room to the list
    });
}

//  Fetch and display rooms on page load
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

//  Run the script after the page loads
document.addEventListener("DOMContentLoaded", initialize);
document.getElementById("search").addEventListener("input", applyFilters);
document.getElementById("distance-range").addEventListener("change", applyFilters);
document.getElementById("price-range").addEventListener("change", applyFilters);
document.getElementById("furnished").addEventListener("change", applyFilters);
document.getElementById("bills-included").addEventListener("change", applyFilters);