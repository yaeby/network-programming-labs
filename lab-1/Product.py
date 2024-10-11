class Product:
    def __init__(self, name, link, price_new, price_old, img_src):
        self.name = name
        self.link = link
        self.price_new = price_new
        self.price_old = price_old
        self.img_src = img_src

    def __str__(self):
        return f"Product(Name: {self.name}, Price: {self.price}, Description: {self.description}, Link: {self.link})"
