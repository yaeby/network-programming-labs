
def serializable(cls):
    cls._serializable = True
    return cls


def serialize_field(func):
    func._serialize_field = True
    return func
