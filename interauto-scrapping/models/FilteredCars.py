from parsers.decorators import serializable, serialize_field

@serializable
class FilteredCars:
    def __init__(self, cars=None, total_price=None, timestamp=None):
        self.cars = cars 
        self.total_price = total_price
        self.timestamp = timestamp

    @serialize_field
    def cars(self):
        return self.cars
    
    @serialize_field
    def total_price(self):
        return self.total_price
    
    @serialize_field
    def timestamp(self):
        return self.timestamp