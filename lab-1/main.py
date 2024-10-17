import re
import requests
from bs4 import BeautifulSoup
from Car import Car

EUR_TO_MDL = 19.5

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36'
}

url = "https://interauto.md/automobile/"
response = requests.get(url, headers=headers)

if response.status_code == 200:
    soup = BeautifulSoup(response.text, 'html.parser')
    products_soup = soup.find_all('div', class_="col-md-6 col-lg-4")

    garage = []
    
    for product_soup in products_soup:
        name_tag = product_soup.find('h4', class_='heading')
        name = name_tag.get_text(strip=True) if name_tag else 'No Name'
        name = name[:-4].strip() if len(name) > 4 else name

        link_tag = name_tag.find('a') if name_tag else None
        link = link_tag['href'] if link_tag else 'No Link'

        price_new_tag = product_soup.find('span', class_='cardojo-Price-amount')
        price = price_new_tag.get_text(strip=True).replace('€', '').strip() if price_new_tag else 'No Price'

        price_old_tag = product_soup.find('del')
        price_old = price_old_tag.get_text(strip=True).replace('€', '').strip() if price_old_tag else 'No Old Price'

        if link != 'No Link':
            product_page_response = requests.get(link, headers=headers)

            if product_page_response.status_code == 200:
                product_soup = BeautifulSoup(product_page_response.text, 'html.parser')
                h2_tag = product_soup.find('h2', string='Caracteristici')
                row_div = h2_tag.find_next_sibling('div', class_='row')

                year_tag = row_div.find('i', class_='fas fa-calendar').find_next('h4')
                year = year_tag.get_text(strip=True) if year_tag else 'No year'

                mileage_tag = row_div.find('i', class_='fas fa-tachometer-alt').find_next('h4')
                mileage = mileage_tag.get_text(strip=True) if mileage_tag else 'No mialge'

                gearbox_tag = row_div.find('i', class_='fas fa-cogs').find_next('h4')
                gearbox = gearbox_tag.get_text(strip=True) if gearbox_tag else 'No gearbox'

                fuel_tag = row_div.find('i', class_='fas fa-bolt').find_next('h4')
                fuel = fuel_tag.get_text(strip=True) if fuel_tag else 'No fuel'

                engine_tag = row_div.find('i', class_='fas fa-window-maximize')
                engine = engine_tag.find_next('h4').get_text(strip=True) if engine_tag else 'No engine'

                power_tag = row_div.find('i', class_='fas fa-horse-head').find_next('h4')
                power = power_tag.get_text(strip=True) if power_tag else 'No power'

                color_tag = row_div.find('div', class_='rounded-circle float-left car-color').find_next('h4')
                color = color_tag.get_text(strip=True) if color_tag else 'No color'

                traction_tag = row_div.find('i', class_='fas fa-car-side').find_next('h4')
                traction = traction_tag.get_text(strip=True) if traction_tag else 'No traction'

                body_tag = row_div.find('i', class_='fas fa-car').find_next('h4')
                body = body_tag.get_text(strip=True) if body_tag else 'No body'

                seats_tag = row_div.find('i', class_='fas fa-user-friends').find_next('h4')
                seats = seats_tag.get_text(strip=True) if seats_tag else 'No seats'

                consumption_tag = row_div.find('ul', class_='list-unstyled mb-0').find_all('h4')
                if consumption_tag:
                    extraurban = consumption_tag[0].get_text(strip=True)
                    extraurban = re.sub(r'\s+', ' ', extraurban)
                    urban = consumption_tag[1].get_text(strip=True)
                    urban = re.sub(r'\s+', ' ', urban)
                    consumption = f'{extraurban}, {urban}'
                else:
                    consumption = 'No consumpion'

            else:
                print(f'Page not responding')
            car = Car(name, link, price, price_old, year, mileage, gearbox, fuel, engine, power, color, traction, body, seats, consumption)
            garage.append(car)

    for car in garage:
        print(car)
        print('-' * 40)

    print(len(products_soup))

else:
    print(f"Failed to retrieve the webpage. Status code: {response.status_code}")
