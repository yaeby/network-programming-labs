import csv
import pytz
from datetime import datetime

EUR_TO_MDL = 19.5
PRICE_MIN = 100000
PRICE_MAX = 400000

class Utils:
    
    @staticmethod
    def convert_price_to_mdl(car):
        """Converts car price from EUR to MDL."""
        if car.price:  
            car.price = car.price * EUR_TO_MDL
        return car
    
    @staticmethod
    def is_in_price_range(car):
        """Filters cars within a specific price range."""
        return PRICE_MIN <= car.price <= PRICE_MAX
    
    @staticmethod
    def sum_prices(total, car):
        """Reduces the list of cars to sum up their prices."""
        return total + car.price
    
    @staticmethod
    def get_utc_timestamp():
        """Returns the current UTC timestamp."""
        return datetime.now(pytz.UTC).strftime('%d-%m-%Y %H:%M:%S %Z')
    
    @staticmethod
    def save_filtered_cars_to_csv(garage, csv_file):
        """Saves the filtered car list to a CSV file."""
        with open(csv_file, mode='w', newline='', encoding='utf-8') as file:
                writer = csv.writer(file)

                writer.writerow([
                    'Name', 'Link', 'Price', 'Old Price', 'Year', 'Mileage', 'Gearbox', 'Fuel', 'Engine',
                    'Power', 'Color', 'Traction', 'Body Type', 'Seats', 'Consumption'
                ])

                for car in garage:
                    writer.writerow([
                        car.name, car.link, car.price, car.price_old, car.year, car.mileage, car.gearbox,
                        car.fuel, car.engine, car.power, car.color, car.traction, car.body_type,
                        car.seats, car.consumption
                    ])

        print(f"Data written to {csv_file} successfully.")

    @staticmethod
    def replace_romanian_letters(text):
        """Replaces Romanian letters with English letters."""
        replacements = {
            'ă': 'a', 'â': 'a', 'î': 'i', 'ș': 's', 'ț': 't',
            'Ă': 'A', 'Â': 'A', 'Î': 'I', 'Ș': 'S', 'Ț': 'T'
        }
        for romanian_char, english_char in replacements.items():
            text = text.replace(romanian_char, english_char)
        return text

    # Example usage
    example_text = "șoferul a condus mașina în oraș"
    print(replace_romanian_letters(example_text))