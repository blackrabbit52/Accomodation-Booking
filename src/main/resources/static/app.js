// Base API URL
//const API_URL = "http://localhost:8080/rooms";

// Fetch rooms and render them
async function fetchRooms(filters = {}) {
    let query = "?";
    for (const key in filters) {
        if (filters[key]) query += `${key}=${filters[key]}&`;
    }

    try {
        const response = await fetch(API_URL + query);
        const rooms = await response.json();
        renderRooms(rooms);
    } catch (error) {
        console.error("Error fetching rooms:", error);
        document.getElementById("roomsContainer").innerHTML = `
            <p>Error loading rooms. Please try again later.</p>
        `;
    }
}

// Render rooms
function renderRooms(rooms) {
    const container = document.getElementById("roomsContainer");
    container.innerHTML = ""; // Clear previous results

    if (rooms.length === 0) {
        container.innerHTML = "<p>No rooms found.</p>";
        return;
    }

    rooms.forEach(room => {
        const roomCard = document.createElement("div");
        roomCard.classList.add("room");

        roomCard.innerHTML = `
            <h3>${room.name}</h3>
            <p><strong>City:</strong> ${room.location?.city || "N/A"}</p>
            <p><strong>Price:</strong> £${room.pricePerMonthGbp}</p>
            <p><strong>Furnished:</strong> ${room.details?.furnished ? "Yes" : "No"}</p>
            <p><strong>Languages:</strong> ${room.spokenLanguages?.join(", ") || "N/A"}</p>
        `;

        container.appendChild(roomCard);
    });
}

// Handle form submission
document.getElementById("searchForm").addEventListener("submit", function (e) {
    e.preventDefault();
    const city = document.getElementById("city").value;
    const furnished = document.getElementById("furnished").value;
    const minPrice = document.getElementById("minPrice").value;
    const maxPrice = document.getElementById("maxPrice").value;

    const filters = { city, furnished, minPrice, maxPrice };
    fetchRooms(filters);
});

// Initial fetch
fetchRooms();