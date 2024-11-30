from ftplib import FTP

def upload_to_ftp(file_path, ftp_host, ftp_user, ftp_pass):
    ftp = FTP(ftp_host)
    ftp.login(user=ftp_user, passwd=ftp_pass)
    
    with open(file_path, "rb") as file:
        ftp.storbinary(f"STOR data.json", file)
        print(f"File was successfully stored.")
    
    ftp.quit()

upload_to_ftp("D:\\FAF\\Year 3\\NP\\interauto-scrapping\\resources\\data.json", "localhost", "testuser", "testpass")