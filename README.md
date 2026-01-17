# How to Run & Navigate the App

## 1) Run Backend (Spring Boot)
1. Open a terminal in the backend folder:
   ```bash
   cd data-management-system
2. Start the backend:

    mvn clean spring-boot:run
3. Backend will run at:

http://localhost:8080

Make sure your local application.properties is set up (PostgreSQL connection) before running.

## 2) Run Frontend (Angular)

1.Open a terminal in the frontend folder:

  cd frontend


2. Install dependencies (first time only):

  npm install


3. Start the frontend:

  ng serve


4. Open in browser:

  http://localhost:4200

## 3) Navigate the UI

Use the top navigation bar:

Generate Excel → enter number of records (e.g., 1000000) → Generate

Excel → CSV → choose generated .xlsx → Upload & Process

CSV → DB → choose generated .csv → Upload to DB

Report → paginate, search by StudentId, filter by Class, export CSV/Excel/PDF
   
