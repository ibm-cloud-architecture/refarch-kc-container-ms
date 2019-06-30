# Use an official Python runtime as a parent image
FROM python:3

# Set the working directory to /app
WORKDIR /server

# Copy the current directory contents into the container at /app
COPY . /server

# Set the working directory to /app
WORKDIR ../assignContainerFunction

# Copy the current directory contents into the container at /app
COPY . /assignContainerFunction

# Install any needed packages specified in requirements.txt
RUN pip install --trusted-host pypi.python.org -r requirements.txt

# Make port available to the world outside this container
EXPOSE 5000

# Define environment variable
#ENV NAME World
ENV KAFKA_BROKERS kafka03-prod02.messagehub.services.us-south.bluemix.net:9093,kafka01-prod02.messagehub.services.us-south.bluemix.net:9093,kafka02-prod02.messagehub.services.us-south.bluemix.net:9093,kafka04-prod02.messagehub.services.us-south.bluemix.net:9093,kafka05-prod02.messagehub.services.us-south.bluemix.net:9093
ENV KAFKA_ENV IBMCLOUD
# Runs container API when started
CMD ["python", "./server/containerAPI.py"]


