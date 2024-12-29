/// Base URL for the API
const BASE_URL = "http://localhost:8080/api/rooms";

// State variables for pagination and filters
let currentPage = 1;
let filters = {
    search: "",
    minPrice: 0,
    maxPrice: 2000,
    furnished: false,
    billsIncluded: false,
};

// Function to fetch rooms from the API
async function fetchRooms() {
    try {
        // Build the query string based on filters and pagination
        const queryParams = new URLSearchParams({
            page: currentPage,
            search: filters.search,
            minPrice: filters.minPrice,
            maxPrice: filters.maxPrice,
            furnished: filters.furnished,
            billsIncluded: filters.billsIncluded,
        });
        const response = await fetch(`${BASE_URL}?${queryParams}`);
        if (!response.ok) {
            throw new Error("Failed to fetch data from server");
        }
        return await response.json();
    } catch (error) {
        console.error("Error fetching rooms:", error);
        return { rooms: [], totalPages: 1 };
    }
}

// Function to render room cards
function renderRooms(rooms) {
    const roomList = document.getElementById("room-list");
    roomList.innerHTML = ""; // Clear existing rooms

    if (rooms.length === 0) {
        roomList.innerHTML = "<p>No rooms found.</p>";
        return;
    }

    rooms.forEach((room) => {
        const roomCard = document.createElement("div");
        roomCard.className = "room-card";
        roomCard.innerHTML = `
            <img src="${room.image || 'placeholder.jpg'}" alt="${room.name}">
            <div class="room-card-content">
                <h3>${room.name}</h3>
                <p>${room.location.city}, ${room.location.postcode}</p>
                <p><strong>£${room.pricePerMonthGbp}/month</strong></p>
            </div>
        `;
        roomList.appendChild(roomCard);
    });
}

// Function to handle pagination buttons
function handlePagination(totalPages) {
    const prevButton = document.getElementById("prev-page");
    const nextButton = document.getElementById("next-page");

    prevButton.disabled = currentPage <= 1;
    nextButton.disabled = currentPage >= totalPages;

    prevButton.onclick = async () => {
        if (currentPage > 1) {
            currentPage--;
            const data = await fetchRooms();
            renderRooms(data.rooms);
            handlePagination(data.totalPages);
        }
    };

    nextButton.onclick = async () => {
        if (currentPage < totalPages) {
            currentPage++;
            const data = await fetchRooms();
            renderRooms(data.rooms);
            handlePagination(data.totalPages);
        }
    };
}

// Function to apply filters
async function applyFilters() {
    filters.search = document.getElementById("search").value.toLowerCase();
    filters.minPrice = 0; // Default min price
    filters.maxPrice = document.getElementById("price-range").value || 2000;
    filters.furnished = document.getElementById("furnished").checked;
    filters.billsIncluded = document.getElementById("bills-included").checked;

    // Reset to the first page
    currentPage = 1;

    // Fetch and render filtered data
    const data = await fetchRooms();
    renderRooms(data.rooms);
    handlePagination(data.totalPages);
}

// Attach event listeners for filters and search
document.getElementById("search").addEventListener("input", applyFilters);
document.getElementById("price-range").addEventListener("change", applyFilters);
document.getElementById("furnished").addEventListener("change", applyFilters);
document.getElementById("bills-included").addEventListener("change", applyFilters);

// Initial Load
(async function initialize() {
    const data = await fetchRooms();
    renderRooms(data.rooms);
    handlePagination(data.totalPages);
})();
