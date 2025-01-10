// page to fully load before running the script
document.addEventListener("DOMContentLoaded", function () {
    const searchButton = document.getElementById("searchApplication");
    if (searchButton) {
        // 🎯 Add event listener to the search button so it fetches applications when clicked
        searchButton.addEventListener("click", fetchApplicationsByName);
    }
});

//  Fetch applications by name when the user searches
async function fetchApplicationsByName() {
    const applicantName = document.getElementById("applicantName").value.trim();

    if (applicantName === "") {
        alert("Please enter your name to view applications."); // ⚠️ Prevent empty searches
        return;
    }

    try {
        //  Send request to the backend API with the entered name
        const response = await fetch(`http://localhost:8080/api/application/search?name=${encodeURIComponent(applicantName)}`);

        if (!response.ok) {
            throw new Error("Failed to fetch applications."); // Handle errors
        }

        const data = await response.json(); // Convert response to JSON
        displayUserApplications(data); //  Display the fetched applications
    } catch (error) {
        console.error("❌ Error fetching applications:", error);
    }
}

//  Display applications in the frontend
function displayUserApplications(applications) {
    const applicationList = document.getElementById("application-list");
    applicationList.innerHTML = ""; //  Clear previous results

    if (applications.length === 0) {
        applicationList.innerHTML = "<p>No applications found under this name.</p>"; // 🚨 Show message if no results
        return;
    }

    //  Loop through each application and create a card for it
    applications.forEach(app => {
        const appCard = document.createElement("div");
        appCard.classList.add("application-card");

        //  Fill the application card with details
        appCard.innerHTML = `
            <h3>🏠 Room: ${app.roomName}</h3>
            <p><strong>📍 Location:</strong> ${app.location}</p>
            <p><strong>💰 Price:</strong> £${app.pricePerMonth}/month</p>
            <p><strong>📌 Status:</strong> <span class="status ${app.status.toLowerCase()}">${app.status}</span></p>
            ${app.status === "PENDING" ? `<button class="cancel-button" onclick="cancelApplication(${app.id})">❌ Cancel</button>` : ""}
        `;

        applicationList.appendChild(appCard); // 📥 Add the application card to the list
    });
}

//  Cancel an application
async function cancelApplication(applicationId) {
    try {
        //  Send a request to the backend to cancel the application
        const response = await fetch(`http://localhost:8080/api/application/delete/${applicationId}`, { method: "PUT" });

        if (!response.ok) {
            throw new Error("Failed to cancel application."); //  Handle cancellation failure
        }

        alert("✅ Application cancelled successfully."); //  Show success message
        await fetchApplicationsByName(); //  Refresh the list after cancellation
    } catch (error) {
        console.error(" Error cancelling application:", error);
    }
}