class Car:
    def __init__(self, name, link, price, price_old, year, mileage, gearbox, fuel, engine, power, color, traction, body_type, seats, consumption):
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

    def __str__(self):
        return (f"Name: {self.name}\n"
                f"Link: {self.link}\n"
                f"Price: {self.price} EUR\n"
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