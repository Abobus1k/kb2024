#!/usr/bin/env python

from argparse import ArgumentParser, FileType
from configparser import ConfigParser
from multiprocessing import Queue
from consumer import start_consumer
from producer import start_producer

from confluent_kafka.admin import AdminClient, NewTopic


def newTopics(config_parser):
    admin_client = AdminClient({
        "bootstrap.servers": "kafka-1:9092"
    })
    topics = []
    print("Начали содание топиков")
    topicsFromConf = config_parser['topics']['names'].split(',')
    print("topics:", topicsFromConf)
    for topic in topicsFromConf:
        topics.append(NewTopic(topic, 1, 1))

    # Создаем топики
    fs = admin_client.create_topics(topics)
    print("Закончили создание топиков: " + str(fs))

    for topic, future in fs.items():
        try:
            future.result()  # Блокируем выполнение программы, пока операция не завершится
            print(f"Топик '{topic}' успешно создан")
        except Exception as e:
            print(f"Ошибка при создании топика '{topic}': {e}")


if __name__ == '__main__':
    # Parse the command line.
    parser = ArgumentParser()
    parser.add_argument('config_file', type=FileType('r'))
    parser.add_argument('--reset', action='store_true')
    args = parser.parse_args()

    # Parse the configuration.
    # See https://github.com/edenhill/librdkafka/blob/master/CONFIGURATION.md
    config_parser = ConfigParser()
    config_parser.read_file(args.config_file)
    config = dict(config_parser['default'])
    
    newTopics(config_parser)
    requests_queue = Queue()
    start_consumer(args, config)
    start_producer(args, config, requests_queue)    
