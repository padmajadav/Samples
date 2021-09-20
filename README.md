# Samples

#Docker desktop is required to load test container
#Steps to reproduce : mvn -Dtest=EventIT test 
# App will work fine if Rabbit-MQ is manually up 
docker pull rabbitmq:3-management
docker run --rm -it -p 15672:15672 -p 5672:5672 rabbitmq:3-management
