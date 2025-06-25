# 🛒 OnePlace E-Commerce Backend (Spring Boot)

An extensible, full-featured e-commerce backend system designed for multi-vendor operations. Built with Spring Boot, Razorpay & Stripe payment integration, secure JWT-based authentication, Redis caching, and PostgreSQL database.

## 📖 Project Overview

OnePlace is a multi-vendor e-commerce backend service that handles user management, product listings, orders, payment processing (Razorpay & Stripe), webhooks for real-time payment status updates, order cancellation, inventory restocking, and efficient product filtering with caching support.

## 🚀 Tech Stack

- **Java 21**
- **Spring Boot**
- **Spring Security (JWT)**
- **Spring Data JPA**
- **PostgreSQL**
- **Razorpay API**
- **Stripe API**
- **Redis Cache**
- **Ngrok (for local webhook testing)**

## 📦 Features

- User & Seller Authentication (JWT secured)
- Product Management with advanced filtering, sorting & pagination
- Redis-based caching for filtered product lists
- Multi-order creation for carts
- Razorpay Payment Link creation & webhook integration
- Stripe Payment integration
- Real-time payment status updates via webhook
- Order Cancellation & automatic product restocking
- Refund initiation on order cancellation (Razorpay)
- Admin, Seller, and User-level APIs

## 📊 Architecture

+------------------------+
| API Consumer |
| (Postman / Frontend UI) |
+-----------+------------+
|
+-----------v------------+
| Spring Boot Backend |
+-----+----+----+----+----+
| | | |
v v v v
Auth Products Orders Payments
| | | |
+-----+----+----+----+------+
| PostgreSQL DB |
+---------------------------+
| Redis Cache |
+---------------------------+
| Razorpay & Stripe APIs |
| + Webhooks |
+---------------------------+



## 📦 How to Run This Project Locally

1️⃣ **Clone the repository**
git clone <your-github-repo-url>
cd Ecommerce-multi-vendor


2️⃣ **Configure application.properties**
- Add your PostgreSQL, Razorpay, Stripe, and Redis credentials.

3️⃣ **Start Redis Server** (if not running)
redis-server

4️⃣ **Run the Spring Boot Application**
./mvnw spring-boot:ru


5️⃣ **Test APIs via Postman / Swagger**

---

## 📌 Razorpay Webhook Setup

1. Go to Razorpay Dashboard → Settings → Webhooks
2. Enter your public ngrok URL `/api/payment/webhook/razorpay`
3. Add Webhook Secret
4. Subscribe to `payment.captured`, `payment.failed` events

---

## 📚 API Modules

- **/auth/signup/** — JWT registration, login
- **/products/** — Product listing, filtering, sorting
- **/api/cart/add** — Add/Remove products to cart
- **/api/orders/** — Order placement, cancellation, tracking
- **/webhook/razorpay/** — webhook reciever
- **/api/admin/** — Admin-only controls (manage sellers, categories)

---

## 📝 Future Enhancements

- Review & Ratings service
- Image CDN hosting & optimization
- Automated email notifications
- Seller performance dashboard
- Distributed service migration (microservices)

---

## 📎 License

This project is for educational and portfolio-building purposes only.

---

## 🙌 Connect with Me

**Omprakash M**  
[LinkedIn]([https://www.linkedin.com](https://www.linkedin.com/in/omprakash-m-6176b31ba/)) | [LeetCode]([https://github.com](https://leetcode.com/u/OmprakashM24/))

