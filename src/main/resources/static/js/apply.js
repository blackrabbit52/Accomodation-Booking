const API_APPLICATION_URL = "http://localhost:8080/api/application/user";
const API_ROOM_URL = "http://localhost:8080/api/room";

// Wait for the page to fully load
document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ JavaScript Loaded");

    const searchButton = document.getElementById("searchApplication");
    if (searchButton) {
        searchButton.addEventListener("click", fetchApplicationsByUserId);
    } else {
        console.error("❌ Search button not found in DOM.");
    }
});

// Fetch applications by userId
async function fetchApplicationsByUserId() {
    const userId = document.getElementById("userId");

    if (!userId) {
        console.error("❌ User ID input field not found!");
        return;
    }

    if (!userId) {
        alert("❌ Please enter a valid User ID.");
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/${userId}`); // Fetch data from API
        if (!response.ok) {
            throw new Error("❌ No applications found for this user.");
        }

        const applications = await response.json(); // Convert response to JSON
        displayUserApplications(applications, userId); // Call function to display results

    } catch (error) {
        console.error("❌ Error fetching applications:", error);
        document.getElementById("application-list").innerHTML = "<p>No applications found.</p>";
    }
}


// Display user applications with room details
function displayUserApplications(applications, userId) {
    const applicationList = document.getElementById("application-list");
    applicationList.innerHTML = ""; // Clear previous results

    if (!applications || applications.length === 0) {
        applicationList.innerHTML = "<p>No applications found.</p>";
        return;
    }

    applications.forEach((app) => {
        const appCard = document.createElement("div");
        appCard.classList.add("application-card");
        appCard.innerHTML = `
            <h3>🏠 Room: ${room.name}</h3>
            <p><strong>📍 Location:</strong> ${app.location}</p>
            <p><strong>💰 Price:</strong> £${app.price_per_month_gbp}/month</p>
            <p><strong>📅 Applied On:</strong> ${new Date(app.applicationDate).toLocaleDateString()}</p>
            <p><strong>📌 Status:</strong> <span class="status ${app.status.toLowerCase()}">${app.status}</span></p>
            <button class="cancel-button" onclick="cancelApplication('${app.id}', '${userId}')">❌ Cancel Application</button>
     
        `;

        applicationList.appendChild(appCard);
    });
}

// Cancel an application
async function cancelApplication(applicationId, userId) {
    if (!confirm("Are you sure you want to cancel this application?")) {
        return;
    }

    try {
        console.log(`🗑️ Deleting application with ID: ${applicationId}`);
        const response = await fetch(`${API_APPLICATION_URL}/delete/${id}`, { method: "DELETE" });

        if (!response.ok) {
            throw new Error("Failed to cancel application.");
        }

        alert("✅ Application cancelled successfully.");
        fetchApplicationsByUserId(userId); // Refresh applications after deletion
    } catch (error) {
        console.error("❌ Error cancelling application:", error);
    }
}