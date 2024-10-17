import re

class Deserializer:
    @staticmethod
    def from_json(json_str, cls):
        if not getattr(cls, '_serializable', False):
            raise ValueError(f"Class {cls.__name__} is not marked as serializable")

        obj = cls()
        pattern = re.compile(r'"(\w+)"\s*:\s*(.+?)(?=,\s*"|})')
        matches = pattern.findall(json_str)

        for field_name, field_value in matches:
            field = getattr(cls, field_name, None)
            if field and getattr(field, '_serialize_field', False):
                setattr(obj, field_name, Deserializer.parse_value(field_value, type(getattr(obj, field_name, str))))
        return obj

    @staticmethod
    def from_xml(xml_str, cls):
        if not getattr(cls, '_serializable', False):
            raise ValueError(f"Class {cls.__name__} is not marked as serializable")

        obj = cls()
        pattern = re.compile(r'<(\w+)>(.*?)</\1>')
        matches = pattern.findall(xml_str)

        for field_name, field_value in matches:
            field = getattr(cls, field_name, None)
            if field and getattr(field, '_serialize_field', False):
                setattr(obj, field_name, Deserializer.parse_value(field_value, type(getattr(obj, field_name, str))))
        return obj

    @staticmethod
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
