import pika
import json
import time

rabbitmq_host = 'localhost'
queue_name = 'scraper_queue'
exchange_name = 'scraper_exchange'
routing_key = 'scraper_routing_json_key'

connection = pika.BlockingConnection(pika.ConnectionParameters(host=rabbitmq_host))
channel = connection.channel()

channel.exchange_declare(exchange=exchange_name, exchange_type='topic', durable=True)
channel.queue_declare(queue=queue_name, durable=True)
channel.queue_bind(exchange=exchange_name, queue=queue_name, routing_key=routing_key)

with open('resources/data.json', 'r', encoding='utf-8') as file:
    json_data = json.load(file)

cars = json_data.get('cars', [])  

limit = 5
for car in cars:
    if limit == 0:
        break
    limit -= 1

    message = json.dumps(car)  
    channel.basic_publish(
        exchange=exchange_name,
        routing_key=routing_key,
        body=message,
        properties=pika.BasicProperties(content_type='application/json')
    )

    print(f"Message sent for car: {car}")
    time.sleep(10)

connection.close()
