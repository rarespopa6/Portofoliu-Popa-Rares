# EveX
## Descriere
EveX este o aplicație pentru gestionarea evenimentelor care permite utilizatorilor să descopere, să participe și să creeze evenimente. Oferă o experiență interactivă pentru vizualizarea biletelor și organizarea de evenimente.
Aplicația include funcționalități de căutare și filtrare a evenimentelor pentru o experiență de utilizare mai bună.

# Limbaje / Framework-uri utilizate
- Frontend: React
- Backend: Spring Boot Microservices, REST APIs
- PostgreSQL

Pentru dezvoltarea backend-ului aplicației EveX, am utilizat arhitectura bazată pe microservicii, implementând mai multe tehnologii și soluții avansate:
- Spring Cloud (Eureka): Utilizat pentru descoperirea serviciilor și gestionarea instanțelor microserviciilor în cadrul arhitecturii de microservicii.
- API Gateway: Configurat pentru a centraliza și a gestiona rutele către microservicii, asigurând un punct de acces unic și uniform pentru client.
- Microservicii: Aplicația este împărțită în trei microservicii principale
    - User-Service: Gestionarea utilizatorilor, autentificarea și autorizația.
    - Event-Service: Gestionarea evenimentelor, inclusiv crearea, actualizarea și     vizualizarea acestora.
    - Order-Service: Procesarea și gestionarea comenzilor de bilete.
- Spring Security: Implementat pentru a asigura securizarea endpoint-urilor din fiecare microserviciu prin autentificare și autorizare.
- JSON Web Tokens (JWT): Utilizate pentru a autentifica și a autoriza utilizatorii, asigurând un mod sigur și eficient de gestionare a sesiunilor de utilizator.

## Capturi de ecran
### Home Page
![Home1](frontend/public/imgs/readme-imgs/home1.jpeg)
![Home2](frontend/public/imgs/readme-imgs/home2.jpeg)
![Home3](frontend/public/imgs/readme-imgs/home3.jpeg)
![Home4](frontend/public/imgs/readme-imgs/home4.jpeg)
![Home5](frontend/public/imgs/readme-imgs/home5.jpeg)
![Home6](frontend/public/imgs/readme-imgs/home6.jpeg)

### Organizer Page
![Organizer](frontend/public/imgs/readme-imgs/organizer1.jpeg)

### Event Info
![EventInfo](frontend/public/imgs/readme-imgs/buy.jpeg)

### Buy Tickets
![BuyTickets](frontend/public/imgs/readme-imgs/buy2.jpeg)
![BuyTickets2](frontend/public/imgs/readme-imgs/buy3.jpeg)

### Profile Wallet
![BuyTickets](frontend/public/imgs/readme-imgs/profile1.jpeg)
![BuyTickets2](frontend/public/imgs/readme-imgs/profile2.jpeg)