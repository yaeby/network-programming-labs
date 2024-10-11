import requests
from bs4 import BeautifulSoup

# Define headers to mimic a browser request
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36'
}

# Make an HTTP GET request to fetch the HTML content
url = "https://interauto.md/automobile/?make=0&model=0&price_min=1999&price_max=289900&vehicle_year_min=0&mileage_max=2388324&mileage_min=1"
response = requests.get(url, headers=headers)

# Check if the request was successful
if response.status_code == 200:
    soup = BeautifulSoup(response.text, 'html.parser')

    # Find all the product containers
    products = soup.find_all('div', class_="col-md-6 col-lg-4")

    product_list = []
    
    for product in products:
        # Extract the product name
        name_tag = product.find('h4', class_='heading')
        name = name_tag.get_text(strip=True) if name_tag else 'No Name'

        # Extract the product link
        link_tag = name_tag.find('a') if name_tag else None
        link = link_tag['href'] if link_tag else 'No Link'

        # Extract the price
        price_new_tag = product.find('span', class_='cardojo-Price-amount')
        price_new = price_new_tag.get_text(strip=True).replace('€', '').strip() if price_new_tag else 'No Price'

        # Extract the old price (if available)
        price_old_tag = product.find('del')
        price_old = price_old_tag.get_text(strip=True).replace('€', '').strip() if price_old_tag else 'No Old Price'

        # Extract the image URL
        img_tag = product.find('img', alt='car-image')
        img_src = img_tag['src'] if img_tag else 'No Image'

        # Add the product data to the list
        product_list.append({
            'name': name,
            'link': link,
            'price_new': price_new,
            'price_old': price_old,
            'image': img_src
        })

    # Print the scraped product data
    for product in product_list:
        print(f"Name: {product['name']}")
        print(f"Link: {product['link']}")
        print(f"New Price: {product['price_new']} EUR")
        print(f"Old Price: {product['price_old']} EUR")
        print(f"Image URL: {product['image']}")
        print('-' * 40)

else:
    print(f"Failed to retrieve the webpage. Status code: {response.status_code}")
