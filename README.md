This is a global dorm accomodation booking 

To launch this project with docker 

Step 1: Navigate through the directory AccomBooking terminal 
cd AccomBooking

Step 2: docker build -t booking-app .   

Step 3: docker run -d -p 8080:8080 --name globaldormapp booking-app 

Step 4: sh (switching to docker sh command)

Step 5:  docker logs globaldormapp


