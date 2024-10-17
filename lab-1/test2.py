import re
import datetime

def serializable(cls):
    cls._serializable = True
    return cls

def serialize_field(func):
    func._serialize_field = True
    return func

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

@serializable
class Car:
    def __init__(self, name=None, price=None, year=None, features=None):
        self.name = name
        self.price = price
        self.year = year
        self.features = features

    @serialize_field
    def name(self):
        return self.name

    @serialize_field
    def price(self):
        return self.price

    @serialize_field
    def year(self):
        return self.year

    @serialize_field
    def features(self):
        return self.features

# Example usage
car = Car("Toyota Camry", 25000, 2020, ["Air Conditioning", "GPS", "Bluetooth"])

serialized_car_json = to_json(car)
serialized_car_xml = to_xml(car)

print("Serialized JSON:\n", serialized_car_json)
print("\nSerialized XML:\n", serialized_car_xml)

print(from_json(serialized_car_json, Car))

car_json = '{"name": "Toyota Camry", "price": 25000, "year": 2020, "features": ["Air Conditioning", "GPS", "Bluetooth"]}'
car_xml = "<Car><name>Toyota Camry</name><price>25000</price><year>2020</year><features><item>value>Air Conditioning</value></item><item>value>GPS</value></item><item>value>Bluetooth</value></item></features></Car>"

car_from_json = from_json(car_json, Car)
car_from_xml = from_xml(car_xml, Car)

print(car_from_json)  
print(car_from_xml)    
