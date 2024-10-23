import datetime

class Serializer:
    @staticmethod
    def to_json(obj):
        if not getattr(obj.__class__, '_serializable', False):
            raise ValueError(f"Class {obj.__class__.__name__} is not marked as serializable")

        serialized_fields = []
        for attr, value in obj.__dict__.items():
            if getattr(obj.__class__, attr, None) is not None:
                if getattr(getattr(obj.__class__, attr), '_serialize_field', False):
                    serialized_fields.append(f'"{attr}": {Serializer._value_to_json(value)}')

        return "{" + ", ".join(serialized_fields) + "}"

    @staticmethod
    def to_xml(obj):
        if not getattr(obj.__class__, '_serializable', False):
            raise ValueError(f"Class {obj.__class__.__name__} is not marked as serializable")

        xml = [f"<{obj.__class__.__name__}>"]
        for attr, value in obj.__dict__.items():
            if getattr(obj.__class__, attr, None) is not None:
                if getattr(getattr(obj.__class__, attr), '_serialize_field', False):
                    xml.append(Serializer._value_to_xml(attr, value))
        xml.append(f"</{obj.__class__.__name__}>")
        return "".join(xml)

    @staticmethod
    def _value_to_json(value):
        if value is None:
            return "null"
        elif isinstance(value, str):
            return f'"{Serializer._escape_json_string(value)}"'
        elif isinstance(value, (int, float, bool)):
            return str(value).lower()
        elif isinstance(value, list):
            return Serializer._list_to_json(value)
        elif isinstance(value, datetime.datetime):
            return f'"{value.isoformat()}"'
        elif hasattr(value, '__dict__'):
            return Serializer.to_json(value)
        else:
            return f'"{str(value)}"'
        
    @staticmethod
    def _list_to_json(lst):
        json_items = [Serializer._value_to_json(item) for item in lst]
        return "[" + ", ".join(json_items) + "]"

    @staticmethod
    def _value_to_xml(field_name, value):
        xml = [f"<{field_name}>"]
        if value is None:
            xml.append("null")
        elif isinstance(value, (str, int, float, bool)):
            xml.append(Serializer._escape_xml_string(str(value)))
        elif isinstance(value, list):
            xml.append(Serializer._list_to_xml(value))
        elif isinstance(value, datetime.datetime):
            xml.append(Serializer._escape_xml_string(value.isoformat()))
        elif hasattr(value, '__dict__'):
            xml.append(Serializer.to_xml(value))
        xml.append(f"</{field_name}>")
        return "".join(xml)

    @staticmethod
    def _list_to_xml(lst):
        return "".join(f"<item>{Serializer._value_to_xml('value', item)}</item>" for item in lst)

    @staticmethod
    def _escape_json_string(input_str):
        return (input_str.replace('"', '\\"')
                         .replace('\n', '\\n')
                         .replace('\r', '\\r')
                         .replace('\t', '\\t'))

    @staticmethod
    def _escape_xml_string(input_str):
        return (input_str.replace("&", "&amp;")
                         .replace("<", "&lt;")
                         .replace(">", "&gt;")
                         .replace('"', "&quot;")
                         .replace("'", "&apos;"))
