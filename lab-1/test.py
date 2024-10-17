import re
import csv
import socket
from functools import reduce
from bs4 import BeautifulSoup
from Car import Car
from Utils import Utils

EUR_TO_MDL = 19.5

def send_http_request(host, path):
    # Create a TCP socket
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
        # Connect to the server
        sock.connect((host, 80))
        
        # Send the HTTP GET request
        request = f"GET {path} HTTP/1.1\r\nHost: {host}\r\nConnection: close\r\n\r\n"
        sock.sendall(request.encode())

        # Receive the response
        response = b""
        while True:
            chunk = sock.recv(4096)
            if not chunk:
                break
            response += chunk

    # Decode the response bytes to string
    return response.decode()

# Define URL components
url = "https://interauto.md/automobile/"
host = "interauto.md"
path = "/automobile/"

# Send HTTP request
response_text = send_http_request(host, path)
headers, html_content = response_text.split("\r\n\r\n", 1)
print(html_content)

# Check if the response is valid
if response_text:
    # Split headers and body
    header, body = response_text.split("\r\n\r\n", 1)
    
    # Parse the HTML body
    soup = BeautifulSoup(body, 'html.parser')
    products_soup = soup.find_all('div', class_="col-md-6 col-lg-4")