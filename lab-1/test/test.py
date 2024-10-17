import re
import datetime
from models.Car import Car
from models.FilteredCars import FilteredCars

def to_json(obj):
    if not getattr(obj, '_serializable', False):
        raise ValueError(f"Class {obj.__class__.__name__} is not marked as serializable")

    serialized_fields = []
    for attr, value in obj.__dict__.items():
        if getattr(obj.__class__, attr, None) is not None:
            if getattr(getattr(obj.__class__, attr), '_serialize_field', False):
                serialized_fields.append(f'"{attr}": {value_to_json(value)}')

    return "{" + ", ".join(serialized_fields) + "}"

def value_to_json(value):
    if value is None:
        return "null"
    elif isinstance(value, str):
        return f'"{escape_json_string(value)}"'
    elif isinstance(value, (int, float, bool)):
        return str(value).lower()
    elif isinstance(value, list):
        return list_to_json(value)
    elif isinstance(value, datetime.datetime):
        return f'"{value.isoformat()}"'
    elif hasattr(value, '__dict__'):
        return to_json(value)
    else:
        return f'"{str(value)}"'

def list_to_json(lst):
    json_items = [value_to_json(item) for item in lst]
    return "[" + ", ".join(json_items) + "]"

def escape_json_string(input_str):
    return (input_str.replace('"', '\\"')
                     .replace('\n', '\\n')
                     .replace('\r', '\\r')
                     .replace('\t', '\\t'))

# Custom function to convert XML values
def to_xml(obj):
    if not getattr(obj.__class__, '_serializable', False):
        raise ValueError(f"Class {obj.__class__.__name__} is not marked as serializable")

    xml = [f"<{obj.__class__.__name__}>"]
    for attr, value in obj.__dict__.items():
        if getattr(obj.__class__, attr, None) is not None:
            if getattr(getattr(obj.__class__, attr), '_serialize_field', False):
                xml.append(value_to_xml(attr, value))
    xml.append(f"</{obj.__class__.__name__}>")
    return "".join(xml)

def value_to_xml(field_name, value):
    xml = [f"<{field_name}>"]
    if value is None:
        xml.append("null")
    elif isinstance(value, (str, int, float, bool)):
        xml.append(escape_xml_string(str(value)))
    elif isinstance(value, list):
        xml.append(list_to_xml(value))
    elif isinstance(value, datetime.datetime):
        xml.append(escape_xml_string(value.isoformat()))
    elif hasattr(value, '__dict__'):
        xml.append(to_xml(value))
    xml.append(f"</{field_name}>")
    return "".join(xml)

def list_to_xml(lst):
    return "".join(f"<item>{value_to_xml('value', item)}</item>" for item in lst)

def escape_xml_string(input_str):
    return (input_str.replace("&", "&amp;")
                     .replace("<", "&lt;")
                     .replace(">", "&gt;")
                     .replace("\"", "&quot;")
                     .replace("'", "&apos;"))

# Functions for deserialization
def from_json(json_str, cls):
    if not getattr(cls, '_serializable', False):
        raise ValueError(f"Class {cls.__name__} is not marked as serializable")

    obj = cls()
    pattern = re.compile(r'"(\w+)"\s*:\s*(.+?)(?=,\s*"|})')
    matches = pattern.findall(json_str)

    for field_name, field_value in matches:
        field = getattr(cls, field_name, None)
        if field and getattr(field, '_serialize_field', False):
            setattr(obj, field_name, parse_value(field_value, type(getattr(obj, field_name, str))))
    return obj

def from_xml(xml_str, cls):
    if not getattr(cls, '_serializable', False):
        raise ValueError(f"Class {cls.__name__} is not marked as serializable")

    obj = cls()
    pattern = re.compile(r'<(\w+)>(.*?)</\1>')
    matches = pattern.findall(xml_str)

    for field_name, field_value in matches:
        field = getattr(cls, field_name, None)
        if field and getattr(field, '_serialize_field', False):
            setattr(obj, field_name, parse_value(field_value, type(getattr(obj, field_name, str))))
    return obj

def parse_value(value, expected_type):
    if expected_type == str:
        return value.strip('"')
    elif expected_type == int:
        return int(value)
    elif expected_type == float:
        return float(value)
    elif expected_type == bool:
        return value.lower() == 'true'
    return value

# Example usage
car = Car("Toyota Camry", "link1", 25000, 26000, 2020, 15000, "Automatic", "Petrol", "2.5L", 200, "Red", "AWD", "Sedan", 5, 8.0)
car2 = Car("Toyota Corolla", "link1", 25000, 26000, 2020, 15000, "Automatic", "Petrol", "2.5L", 200, "Red", "AWD", "Sedan", 5, 8.0)

# Serialize Car object to JSON and XML
serialized_car_json = to_json(car)
serialized_car_xml = to_xml(car)

print("Serialized JSON:\n", serialized_car_json)
print("\nSerialized XML:\n", serialized_car_xml)

# print(from_json(serialized_car_json, Car))
# print(from_xml(serialized_car_xml, Car))

garage = []
garage.append(car)
garage.append(car2)

filetered_cars = FilteredCars(garage, 10000, "20.30.40")
# for car in filetered_cars.cars:
#     print(car)
# print(filetered_cars)
serialized_filtered_cars = to_json(filetered_cars)
print(serialized_filtered_cars + '\n')

serialized_filtered_cars = to_xml(filetered_cars)
print(serialized_filtered_cars + '\n')

new_filtered_cars = from_json(serialized_filtered_cars, FilteredCars)
print(new_filtered_cars)