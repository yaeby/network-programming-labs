import requests
from bs4 import BeautifulSoup

EUR_TO_MDL = 19.5

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

        # year_tag = product.find('span', class_='productYear')
        # year = year_tag.get_text(strip=True) if year_tag else 'NO year'

        price_new_tag = product.find('span', class_='cardojo-Price-amount')
        price_new = price_new_tag.get_text(strip=True).replace('€', '').strip() if price_new_tag else 'No Price'

        price_old_tag = product.find('del')
        price_old = price_old_tag.get_text(strip=True).replace('€', '').strip() if price_old_tag else 'No Old Price'

        if link != 'No Link':
            product_page_response = requests.get(link, headers=headers)
            if product_page_response.status_code == 200:
                product_soup = BeautifulSoup(product_page_response.text, 'html.parser')

                year_tag = product_soup.find('div', class_='col-6 col-md-4 item').find('h4') if product_soup.find('i', class_='fas fa-calendar') else None
                year = year_tag.get_text(strip=True) if year_tag else 'NO year'

                mileage_tag = product_soup.find('i', class_='fas fa-tachometer-alt')
                mileage = mileage_tag.find_next('h4').get_text(strip=True) if mileage_tag else 'NO mileage'

                gearbox_tag = product_soup.find('i', class_='fas fa-cogs')
                gearbox = gearbox_tag.find_next('h4').get_text(strip=True) if gearbox_tag else 'NO gearbox'

                fuel_tag = product_soup.find('i', class_='fas fa-bolt')
                fuel = fuel_tag.find_next('h4').get_text(strip=True) if fuel_tag else 'NO fuel'

                engine_tag = product_soup.find('i', class_='fas fa-window-maximize')
                engine = engine_tag.find_next('h4').get_text(strip=True) if engine_tag else 'NO engine'

                power_tag = product_soup.find('i', class_='fas fa-horse-head')
                power = power_tag.find_next('h4').get_text(strip=True) if power_tag else 'NO power'

                color_tag = product_soup.find('i', class_='fas fa-car-side')
                color = color_tag.find_next('h4').get_text(strip=True) if color_tag else 'NO color'

                traction_tag = product_soup.find('i', class_='fas fa-car-side')
                traction = traction_tag.find_next('h4').get_text(strip=True) if traction_tag else 'NO traction'

                body_type_tag = product_soup.find('i', class_='fas fa-car')
                body_type = body_type_tag.find_next('h4').get_text(strip=True) if body_type_tag else 'NO body type'

                seats_tag = product_soup.find('i', class_='fas fa-user-friends')
                seats = seats_tag.find_next('h4').get_text(strip=True) if seats_tag else 'NO seats'

                consumption_tag = product_soup.find('i', class_='fas fa-gas-pump')
                consumption = consumption_tag.find_next('h4').get_text(strip=True) if consumption_tag else 'NO consumption'



                product_list.append({
                    'name': name,
                    'link': link,
                    'year': year,
                    'price': price_new,
                    'price_old': price_old,
                    'mileage': mileage,
                    'gearbox': gearbox,
                    'fuel': fuel,
                    'engine': engine,
                    'power': power,
                    'color': color,
                    'traction': traction,
                    'body_type': body_type,
                    'seats': seats,
                    'consumption': consumption
                })

    product_list = list(map(lambda p: 
                            {**p, 'price_mdl': float(p['price']) * EUR_TO_MDL if p['price'].replace('.', '', 1).isdigit() else 0}, 
                            product_list
                            ))


    
    for product in product_list:
        print(f"Name: {product['name']}")
        print(f"Link: {product['link']}")
        print(f"Year: {product['year']}")
        print(f"Price: {product['price']} EUR")
        print(f"Price in MDL: {product['price_mdl']} MDL")
        print(f"Old Price: {product['price_old']} EUR")
        print(f"Year: {year}")
        print(f"Mileage: {mileage}")
        print(f"Gearbox: {gearbox}")
        print(f"Fuel: {fuel}")
        print(f"Engine: {engine}")
        print(f"Power: {power}")
        print(f"Color: {color}")
        print(f"Traction: {traction}")
        print(f"Body Type: {body_type}")
        print(f"Seats: {seats}")
        print(f"Consumption: {consumption}")
        print('-' * 40)


else:
    print(f"Failed to retrieve the webpage. Status code: {response.status_code}")
