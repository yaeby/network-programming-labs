import requests
from bs4 import BeautifulSoup

# Define headers to mimic a browser request
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36'
}

url = "https://interauto.md/automobile/?make=0&model=0&price_min=1999&price_max=289900&vehicle_year_min=0&mileage_max=2388324&mileage_min=1"
response = requests.get(url, headers=headers)

if response.status_code == 200:
    soup = BeautifulSoup(response.text, 'html.parser')

    products = soup.find_all('div', class_="col-md-6 col-lg-4")

    product_list = []
    
    for product in products:
        name_tag = product.find('h4', class_='heading')
        name = name_tag.get_text(strip=True) if name_tag else 'No Name'

        link_tag = name_tag.find('a') if name_tag else None
        link = link_tag['href'] if link_tag else 'No Link'

        year_tag = product.find('span', class_='productYear')
        year = year_tag.get_text(strip=True) if year_tag else 'NO year'

        price_new_tag = product.find('span', class_='cardojo-Price-amount')
        price_new = price_new_tag.get_text(strip=True).replace('€', '').strip() if price_new_tag else 'No Price'

        price_old_tag = product.find('del')
        price_old = price_old_tag.get_text(strip=True).replace('€', '').strip() if price_old_tag else 'No Old Price'

        if link != 'No Link':
            product_page_response = requests.get(link, headers=headers)
            if product_page_response.status_code == 200:
                product_soup = BeautifulSoup(product_page_response.text, 'html.parser')

                specifications = {}
                specs_containers = product_soup.find_all('div', class_='col-6 col-md-4 item')

                for spec in specs_containers:
                    spec_name = spec.find('h6').get_text(strip=True)
                    spec_value = spec.find('h4').get_text(strip=True)
                    specifications[spec_name] = spec_value

                product_list.append({
                    'name': name,
                    'link': link,
                    'year': year,
                    'price': price_new,
                    'price_old': price_old,
                    'specifications': specifications
                })
    
    for product in product_list:
        print(f"Name: {product['name']}")
        print(f"Link: {product['link']}")
        print(f"Year: {product['year']}")
        print(f"Price: {product['price']} EUR")
        print(f"Old Price: {product['price_old']} EUR")
        print(f"Specifications:")
        for spec_name, spec_value in product['specifications'].items():
            print(f"  {spec_name}: {spec_value}")
        print('-' * 40)

else:
    print(f"Failed to retrieve the webpage. Status code: {response.status_code}")
