import socket
import ssl

class Requester:
    def __init__(self):
        pass

    def send_http_request(self, host, path):
        """Create a TCP socket"""
        context = ssl.create_default_context()  
        with socket.create_connection((host, 443)) as sock:  
            with context.wrap_socket(sock, server_hostname=host) as ssock:  

                request = f"GET {path} HTTP/1.1\r\nHost: {host}\r\nConnection: close\r\n\r\n"
                ssock.sendall(request.encode())

                response = b""
                while True:
                    chunk = ssock.recv(4096)
                    if not chunk:
                        break
                    response += chunk

        return response.decode()
