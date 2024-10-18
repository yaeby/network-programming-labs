from parsers.decorators import serializable, serialize_field

@serializable
class Car:
    def __init__(self, name=None, link=None, price=None, price_old=None, year=None, mileage=None, gearbox=None,
                 fuel=None, engine=None, power=None, color=None, traction=None, body_type=None, seats=None,
                 consumption=None):
        self.name = name
        self.link = link
        self.price = price
        self.price_old = price_old
        self.year = year
        self.mileage = mileage
        self.gearbox = gearbox
        self.fuel = fuel
        self.engine = engine
        self.power = power
        self.color = color
        self.traction = traction
        self.body_type = body_type
        self.seats = seats
        self.consumption = consumption

    @serialize_field
    def name(self):
        return self.name

    @serialize_field
    def link(self):
        return self.link

    @serialize_field
    def price(self):
        return self.price

    @serialize_field
    def price_old(self):
        return self.price_old

    @serialize_field
    def year(self):
        return self.year

    @serialize_field
    def mileage(self):
        return self.mileage

    @serialize_field
    def gearbox(self):
        return self.gearbox

    @serialize_field
    def fuel(self):
        return self.fuel

    @serialize_field
    def engine(self):
        return self.engine

    @serialize_field
    def power(self):
        return self.power

    @serialize_field
    def color(self):
        return self.color

    @serialize_field
    def traction(self):
        return self.traction

    @serialize_field
    def body_type(self):
        return self.body_type

    @serialize_field
    def seats(self):
        return self.seats

    @serialize_field
    def consumption(self):
        return self.consumption

    def __str__(self):
        return (f"Name: {self.name}\n"
                f"Link: {self.link}\n"
                f"Price: {self.price} MDL\n"
                f"Old Price: {self.price_old} EUR\n"
                f"Year: {self.year}\n"
                f"Mileage: {self.mileage}\n"
                f"Gearbox: {self.gearbox}\n"
                f"Fuel: {self.fuel}\n"
                f"Engine: {self.engine}\n"
                f"Power: {self.power}\n"
                f"Color: {self.color}\n"
                f"Traction: {self.traction}\n"
                f"Body Type: {self.body_type}\n"
                f"Seats: {self.seats}\n"
                f"Consumption: {self.consumption}\n")