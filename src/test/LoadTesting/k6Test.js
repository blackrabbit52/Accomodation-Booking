import http from "k6/http";
import { sleep } from "k6";

export let options = {
    vus: 100,  // Number of virtual users
    duration: "30s",  // Test duration
};

export default function () {
    let headers = { "Content-Type": "application/json" };

    // Small payload for POST requests
    let payload = JSON.stringify({
        userId: "123",
        roomId: "456",
        status: "pending"
    });

    // Large payload for stress testing
    let largePayload = JSON.stringify({
        userId: "123",
        roomId: "456",
        status: "pending",
        additionalData: "x".repeat(10000)  // Large data to test performance
    });

    // Simulate API calls
    http.get("http://localhost:8080/api/room/all");  // GET should not have a payload
    http.get("http://localhost:8080/api/application/all");  // GET should not have a payload
    http.post("http://localhost:8080/api/application/apply", payload, { headers: headers });  // Fixed POST request

    sleep(1);  // Wait for 1 second before the next request
}