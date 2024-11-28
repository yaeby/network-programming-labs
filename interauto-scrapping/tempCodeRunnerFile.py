import pika
import json

# RabbitMQ connection parameters
rabbitmq_host = 'localhost'
queue_name = 'scraper_queue'
exchange_name = 'scraper_exchange'
routing_key = 'scraper_routing_json_key'

# Create a connection to RabbitMQ
connection = pika.BlockingConnection(pika.ConnectionParameters(host=rabbitmq_host))
channel = connection.channel()

# Declare the exchange and queue (optional if already created)
channel.exchange_declare(exchange=exchange_name, exchange_type='topic', durable=True)
channel.queue_declare(queue=queue_name, durable=True)
channel.queue_bind(exchange=exchange_name, queue=queue_name, routing_key=routing_key)

# Message to send (JSON format)
message = {
    "id": 1,
    "firstName": "Adrian",
    "lastName": "Copta"
}

# Publish the message
channel.basic_publish(
    exchange=exchange_name,
    routing_key=routing_key,
    body=json.dumps(message),
    properties=pika.BasicProperties(content_type='application/json')
)

print(f"Message sent: {message}")

# Close the connection
connection.close()
