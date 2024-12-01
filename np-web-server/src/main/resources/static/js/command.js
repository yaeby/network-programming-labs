const socket = new WebSocket("ws://localhost:8080/cmd");

// Listen for the WebSocket connection opening
socket.addEventListener("open", function (event) {
    console.log("WebSocket connection opened");
});

// Listen for messages from the server
socket.addEventListener("message", function (event) {
    console.log("Message from server:", event.data);
    document.getElementById("response").innerText = event.data;
});

// Handle form submission and send the command via WebSocket
const form = document.querySelector("form");
form.addEventListener("submit", (e) => {
    e.preventDefault();
    const command = document.getElementById("command").value;

    // Send the command through the WebSocket
    if (socket.readyState === WebSocket.OPEN) {
        socket.send(command);
    } else {
        console.error("WebSocket connection is not open");
    }
});

// Optional: Listen for the WebSocket connection closing
socket.addEventListener("close", function (event) {
    console.log("WebSocket connection closed");
});

// Optional: Handle WebSocket errors
socket.addEventListener("error", function (event) {
    console.error("WebSocket error:", event);
});