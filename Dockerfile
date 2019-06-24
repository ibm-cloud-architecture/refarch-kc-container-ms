# Use an official Python runtime as a parent image
FROM python:3.6-stretch
# Set the working directory to /app
WORKDIR /refarch-kc-container-ms

# Copy the current directory contents into the container at /app
COPY . /refarch-kc-container-ms


ENV KAFKA_BROKERS kafka03-prod02.messagehub.services.us-south.bluemix.net:9093,kafka01-prod02.messagehub.services.us-south.bluemix.net:9093,kafka02-prod02.messagehub.services.us-south.bluemix.net:9093,kafka04-prod02.messagehub.services.us-south.bluemix.net:9093,kafka05-prod02.messagehub.services.us-south.bluemix.net:9093
ENV KAFKA_ENV IBMCLOUD


ENV PATH=/root/.local/bin:$PATH
ENV PYTHONPATH=$(pwd)
RUN pip install --upgrade pip \
  && pip install --user pipenv requests black pytest numpy pandas confluent_kafka asyncio

CMD bash


