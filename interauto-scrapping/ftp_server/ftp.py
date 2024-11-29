import os
from ftplib import FTP

def upload_to_ftp(file_path, ftp_host, ftp_user, ftp_pass):
    ftp = FTP(ftp_host)
    ftp.login(user=ftp_user, passwd=ftp_pass)
    
    with open(file_path, "rb") as file:
        ftp.storbinary(f"STOR data.json", file) 
    
    ftp.quit()

base_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))  
file_path = os.path.join(base_dir, "resources", "data.json")
upload_to_ftp("resources/data.json", "localhost", "testuser", "testpass")