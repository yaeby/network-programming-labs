import re
from functools import reduce
from bs4 import BeautifulSoup
from models.Car import Car
from utils.Utils import Utils
from utils.Requester import Requester
from models.FilteredCars import FilteredCars
from parsers.Serializer import Serializer
from parsers.Deserializer import Deserializer

url = "https://interauto.md/automobile/"
host = "interauto.md"
path = "/automobile/"

requester = Requester()
response_text = requester.send_http_request(host, path)
headers, html_content = response_text.split("\r\n\r\n", 1)
# print(headers)

if "200 OK" in headers:
    soup = BeautifulSoup(html_content, 'html.parser')
    products_soup = soup.find_all('div', class_="col-md-6 col-lg-4")

    garage = []
    
    for product_soup in products_soup:
        name_tag = product_soup.find('h4', class_='heading')
        name = name_tag.get_text(strip=True) if name_tag else 'No Name'
        name = name[:-4].strip() if len(name) > 4 else name

        link_tag = name_tag.find('a') if name_tag else None
        link = link_tag['href'] if link_tag else 'No Link'

        price_new_tag = product_soup.find('span', class_='cardojo-Price-amount')
        price = int(price_new_tag.get_text(strip=True).replace('€', '').replace('.', '').strip()) if price_new_tag else None

        price_old_tag = product_soup.find('del')
        price_old = int(price_old_tag.get_text(strip=True).replace('€', '').replace('.', '').strip()) if price_old_tag else None

        if link != 'No Link':
            product_page_response = requester.send_http_request(host, link)

            if "200 OK" in product_page_response:
                product_soup = BeautifulSoup(product_page_response.split("\r\n\r\n", 1)[1], 'html.parser')
                h2_tag = product_soup.find('h2', string='Caracteristici')
                row_div = h2_tag.find_next_sibling('div', class_='row')

                year_tag = row_div.find('i', class_='fas fa-calendar')
                year = year_tag.find_next('h4').get_text(strip=True) if year_tag else 'No year'

                mileage_tag = row_div.find('i', class_='fas fa-tachometer-alt')
                mileage = mileage_tag.find_next('h4').get_text(strip=True) if mileage_tag else 'No mileage'

                gearbox_tag = row_div.find('i', class_='fas fa-cogs')
                gearbox = gearbox_tag.find_next('h4').get_text(strip=True) if gearbox_tag else 'No gearbox'
                gearbox = Utils.replace_romanian_letters(gearbox)

                fuel_tag = row_div.find('i', class_='fas fa-bolt')
                fuel = fuel_tag.find_next('h4').get_text(strip=True) if fuel_tag else 'No fuel'
                fuel = Utils.replace_romanian_letters(fuel)

                engine_tag = row_div.find('i', class_='fas fa-window-maximize')
                engine = engine_tag.find_next('h4').get_text(strip=True) if engine_tag else 'No engine'

                power_tag = row_div.find('i', class_='fas fa-horse-head')
                power = power_tag.find_next('h4').get_text(strip=True) if power_tag else 'No power'

                color_tag = row_div.find('div', class_='rounded-circle float-left car-color')
                color = color_tag.find_next('h4').get_text(strip=True) if color_tag else 'No color'
                color = Utils.replace_romanian_letters(color)

                traction_tag = row_div.find('i', class_='fas fa-car-side')
                traction = traction_tag.find_next('h4').get_text(strip=True) if traction_tag else 'No traction'
                traction = Utils.replace_romanian_letters(traction)

                body_tag = row_div.find('i', class_='fas fa-car')
                body = body_tag.find_next('h4').get_text(strip=True) if body_tag else 'No body'
                body = Utils.replace_romanian_letters(body)

                seats_tag = row_div.find('i', class_='fas fa-user-friends')
                seats = seats_tag.find_next('h4').get_text(strip=True) if seats_tag else 'No seats'

                consumption_tag = row_div.find('ul', class_='list-unstyled mb-0').find_all('h4')
                if consumption_tag:
                    extraurban = consumption_tag[0].get_text(strip=True)
                    extraurban = re.sub(r'\s+', ' ', extraurban)
                    urban = consumption_tag[1].get_text(strip=True)
                    urban = re.sub(r'\s+', ' ', urban)
                    consumption = f'{extraurban}, {urban}'
                else:
                    consumption = 'No consumption'

            else:
                print('Page not responding')
            car = Car(name, link, price, price_old, year, mileage, gearbox, fuel, engine, power, color, traction, body, seats, consumption)
            garage.append(car)

    garage = list(map(Utils.convert_price_to_mdl, garage))
    filtered_garage = list(filter(Utils.is_in_price_range, garage))
    total_price = reduce(Utils.sum_prices, filtered_garage, 0)
    utc_timestamp = Utils.get_utc_timestamp()

    csv_file = 'resources/cars.csv'
    Utils.save_filtered_cars_to_csv(filtered_garage, csv_file)

    dream_garage = FilteredCars(filtered_garage, total_price, utc_timestamp)

    csv_file = 'resources/cars.csv'
    Utils.save_filtered_cars_to_csv(filtered_garage, csv_file)

    json_data = Serializer.to_json(dream_garage)

    with open('resources/data.json', 'w', encoding='utf-8') as f:
        f.write(json_data)

    # print("\nJSON Representation:\n", json_data)

    # xml_data = Serializer.to_xml(dream_garage)
    # print("\nXML Representation:\n", xml_data)

    # temp_car = Deserializer.from_json(json_data, FilteredCars)
    # print(temp_car)
    # temp_car = Deserializer.from_xml(xml_data, FilteredCars)
    # print(temp_car)

else:
    print("Failed to retrieve the webpage.")
