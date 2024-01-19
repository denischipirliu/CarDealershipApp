--
-- PostgreSQL database dump
--

-- Dumped from database version 16.0
-- Dumped by pg_dump version 16.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- Name: create_client(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.create_client() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.role = 'client' THEN
        INSERT INTO clients(user_id, first_name, last_name, email, phone_number, address)
        VALUES (NEW.id, '', '', '', '', ''); -- Populate client data as needed
    END IF;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.create_client() OWNER TO postgres;

--
-- Name: create_order_detail_on_order_insert(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.create_order_detail_on_order_insert() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    car_price DECIMAL(10, 2);
BEGIN
    -- Fetching the car price from the 'cars' table based on the car_id in the new order
    SELECT price INTO car_price FROM cars WHERE car_id = NEW.car_id;

    -- Inserting into 'order_details' with the current date and fetched values
    INSERT INTO order_details (order_id, order_date, pickup_date, price, payment_method)
    VALUES (NEW.order_id, CURRENT_DATE, NULL, car_price, 'Your_Payment_Method_Here');

    RETURN NEW;
END;
$$;


ALTER FUNCTION public.create_order_detail_on_order_insert() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: car_features; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.car_features (
    car_id integer NOT NULL,
    feature_id integer NOT NULL
);


ALTER TABLE public.car_features OWNER TO postgres;

--
-- Name: cars; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cars (
    car_id integer NOT NULL,
    make_id integer,
    model_id integer,
    year integer,
    price numeric(10,2),
    color character varying(20),
    mileage integer,
    vin_number character varying(50),
    engine_type character varying(50),
    transmission_type character varying(20),
    fuel_type character varying(20),
    seating_capacity integer,
    image_path character varying(255),
    status character varying(20) DEFAULT 'available'::character varying,
    CONSTRAINT cars_mileage_check CHECK ((mileage >= 0)),
    CONSTRAINT cars_price_check CHECK ((price >= (0)::numeric)),
    CONSTRAINT cars_seating_capacity_check CHECK ((seating_capacity > 0)),
    CONSTRAINT cars_year_check CHECK (((year >= 1900) AND ((year)::numeric <= EXTRACT(year FROM now()))))
);


ALTER TABLE public.cars OWNER TO postgres;

--
-- Name: cars_car_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cars_car_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.cars_car_id_seq OWNER TO postgres;

--
-- Name: cars_car_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.cars_car_id_seq OWNED BY public.cars.car_id;


--
-- Name: clients; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.clients (
    id integer NOT NULL,
    user_id integer,
    first_name character varying(50),
    last_name character varying(50),
    email character varying(100),
    phone_number character varying(20),
    address character varying(100)
);


ALTER TABLE public.clients OWNER TO postgres;

--
-- Name: clients_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.clients_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.clients_id_seq OWNER TO postgres;

--
-- Name: clients_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.clients_id_seq OWNED BY public.clients.id;


--
-- Name: features; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.features (
    feature_id integer NOT NULL,
    feature_name character varying(50)
);


ALTER TABLE public.features OWNER TO postgres;

--
-- Name: features_feature_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.features_feature_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.features_feature_id_seq OWNER TO postgres;

--
-- Name: features_feature_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.features_feature_id_seq OWNED BY public.features.feature_id;


--
-- Name: makes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.makes (
    make_id integer NOT NULL,
    make_name character varying(50)
);


ALTER TABLE public.makes OWNER TO postgres;

--
-- Name: makes_make_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.makes_make_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.makes_make_id_seq OWNER TO postgres;

--
-- Name: makes_make_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.makes_make_id_seq OWNED BY public.makes.make_id;


--
-- Name: models; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.models (
    model_id integer NOT NULL,
    model_name character varying(50),
    make_id integer
);


ALTER TABLE public.models OWNER TO postgres;

--
-- Name: models_model_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.models_model_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.models_model_id_seq OWNER TO postgres;

--
-- Name: models_model_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.models_model_id_seq OWNED BY public.models.model_id;


--
-- Name: order_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.order_details (
    order_detail_id integer NOT NULL,
    order_id integer,
    order_date date,
    pickup_date date,
    price numeric(10,2),
    payment_method character varying(50)
);


ALTER TABLE public.order_details OWNER TO postgres;

--
-- Name: order_details_order_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.order_details_order_detail_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.order_details_order_detail_id_seq OWNER TO postgres;

--
-- Name: order_details_order_detail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.order_details_order_detail_id_seq OWNED BY public.order_details.order_detail_id;


--
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orders (
    order_id integer NOT NULL,
    client_id integer,
    car_id integer,
    status character varying(20)
);


ALTER TABLE public.orders OWNER TO postgres;

--
-- Name: orders_order_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.orders_order_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.orders_order_id_seq OWNER TO postgres;

--
-- Name: orders_order_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.orders_order_id_seq OWNED BY public.orders.order_id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(100) NOT NULL,
    role character varying(10) NOT NULL,
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['admin'::character varying, 'client'::character varying])::text[])))
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: cars car_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cars ALTER COLUMN car_id SET DEFAULT nextval('public.cars_car_id_seq'::regclass);


--
-- Name: clients id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clients ALTER COLUMN id SET DEFAULT nextval('public.clients_id_seq'::regclass);


--
-- Name: features feature_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.features ALTER COLUMN feature_id SET DEFAULT nextval('public.features_feature_id_seq'::regclass);


--
-- Name: makes make_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.makes ALTER COLUMN make_id SET DEFAULT nextval('public.makes_make_id_seq'::regclass);


--
-- Name: models model_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.models ALTER COLUMN model_id SET DEFAULT nextval('public.models_model_id_seq'::regclass);


--
-- Name: order_details order_detail_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_details ALTER COLUMN order_detail_id SET DEFAULT nextval('public.order_details_order_detail_id_seq'::regclass);


--
-- Name: orders order_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders ALTER COLUMN order_id SET DEFAULT nextval('public.orders_order_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: car_features; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.car_features VALUES (3, 2);
INSERT INTO public.car_features VALUES (3, 3);
INSERT INTO public.car_features VALUES (3, 5);
INSERT INTO public.car_features VALUES (3, 6);
INSERT INTO public.car_features VALUES (3, 9);
INSERT INTO public.car_features VALUES (3, 10);
INSERT INTO public.car_features VALUES (3, 13);
INSERT INTO public.car_features VALUES (3, 14);
INSERT INTO public.car_features VALUES (3, 16);
INSERT INTO public.car_features VALUES (3, 18);
INSERT INTO public.car_features VALUES (3, 20);
INSERT INTO public.car_features VALUES (3, 21);
INSERT INTO public.car_features VALUES (6, 2);
INSERT INTO public.car_features VALUES (6, 3);
INSERT INTO public.car_features VALUES (6, 7);
INSERT INTO public.car_features VALUES (6, 11);
INSERT INTO public.car_features VALUES (6, 14);
INSERT INTO public.car_features VALUES (6, 17);
INSERT INTO public.car_features VALUES (6, 22);
INSERT INTO public.car_features VALUES (6, 24);
INSERT INTO public.car_features VALUES (6, 28);
INSERT INTO public.car_features VALUES (5, 4);
INSERT INTO public.car_features VALUES (5, 7);
INSERT INTO public.car_features VALUES (5, 10);
INSERT INTO public.car_features VALUES (5, 14);
INSERT INTO public.car_features VALUES (5, 20);
INSERT INTO public.car_features VALUES (5, 26);
INSERT INTO public.car_features VALUES (4, 5);
INSERT INTO public.car_features VALUES (4, 7);
INSERT INTO public.car_features VALUES (4, 10);
INSERT INTO public.car_features VALUES (4, 14);


--
-- Data for Name: cars; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.cars VALUES (4, 8, 22, 2022, 89000.00, 'White', 60000, '1N4AL3AP0FN304875', '2.2L 4-Cylinder', 'AWD', 'Gas', 4, 'file:/C:/Users/dench/Documents/FACULTATE/AN%202-SEM%201/OOP/car_dealership/src/main/resources/com/example/car_dealership/car_pictures/2022%20C-Class.jpg', 'sold');
INSERT INTO public.cars VALUES (3, 16, 47, 2016, 23000.00, 'White', 145000, '1XPCDB9X5MD240705', '2.2L 4-Cylinder', 'AWD', 'Diesel', 5, 'file:/C:/Users/dench/Documents/FACULTATE/AN%202-SEM%201/OOP/car_dealership/src/main/resources/com/example/car_dealership/car_pictures/2016%20XC-60.jpg', 'available');
INSERT INTO public.cars VALUES (5, 3, 8, 2016, 50000.00, 'Yellow', 87000, '1HGCD7256VA048480', '3.0L V6', 'RWD', 'Gas', 2, 'file:/C:/Users/dench/Documents/FACULTATE/AN%202-SEM%201/OOP/car_dealership/src/main/resources/com/example/car_dealership/car_pictures/2016%20Mustang.jpg', 'sold');
INSERT INTO public.cars VALUES (7, 17, 49, 2022, 200000.00, 'Red', 8000, '1J4GK48KX5W681109', '3.0L V6', 'AWD', 'Gas', 2, 'file:/C:/Users/dench/Documents/FACULTATE/AN%202-SEM%201/OOP/car_dealership/src/main/resources/com/example/car_dealership/car_pictures/2022%20911.jpg', 'available');
INSERT INTO public.cars VALUES (6, 2, 4, 2017, 34000.00, 'White', 67000, 'WA1LGAFE0ED019529', '1.8L 4-Cylinder', 'FWD', 'Gas', 4, 'file:/C:/Users/dench/Documents/FACULTATE/AN%202-SEM%201/OOP/car_dealership/src/main/resources/com/example/car_dealership/car_pictures/2017%20Civic.jpg', 'sold');


--
-- Data for Name: clients; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.clients VALUES (1, 2, 'Denis', 'Chipirliu', '', '', '');
INSERT INTO public.clients VALUES (2, 3, 'Cristi', 'Rotaru', '', '', '');
INSERT INTO public.clients VALUES (3, 5, 'Ciprian', 'Chipirliu', '', '', '');
INSERT INTO public.clients VALUES (4, 6, 'Stefan', 'Chipirliu', 'stefanchipirliu@gmail.com', '0712312312', 'Barlad');


--
-- Data for Name: features; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.features VALUES (1, 'Air Conditioning');
INSERT INTO public.features VALUES (2, 'Bluetooth Connectivity');
INSERT INTO public.features VALUES (3, 'Cruise Control');
INSERT INTO public.features VALUES (4, 'Navigation System');
INSERT INTO public.features VALUES (5, 'Heated Seats');
INSERT INTO public.features VALUES (6, 'Backup Camera');
INSERT INTO public.features VALUES (7, 'Parking Sensors');
INSERT INTO public.features VALUES (8, 'Leather Seats');
INSERT INTO public.features VALUES (9, 'Sunroof/Moonroof');
INSERT INTO public.features VALUES (10, 'Keyless Entry');
INSERT INTO public.features VALUES (11, 'Remote Start');
INSERT INTO public.features VALUES (12, 'Alloy Wheels');
INSERT INTO public.features VALUES (13, 'Fog Lights');
INSERT INTO public.features VALUES (14, 'Blind Spot Monitoring');
INSERT INTO public.features VALUES (15, 'Lane Departure Warning');
INSERT INTO public.features VALUES (16, 'Apple CarPlay');
INSERT INTO public.features VALUES (17, 'Android Auto');
INSERT INTO public.features VALUES (18, 'Wireless Charging');
INSERT INTO public.features VALUES (19, 'Adaptive Cruise Control');
INSERT INTO public.features VALUES (20, 'Heads-Up Display');
INSERT INTO public.features VALUES (21, 'Power Liftgate');
INSERT INTO public.features VALUES (22, 'Towing Package');
INSERT INTO public.features VALUES (23, 'Panoramic Roof');
INSERT INTO public.features VALUES (24, 'Adaptive Headlights');
INSERT INTO public.features VALUES (25, 'Rain-Sensing Wipers');
INSERT INTO public.features VALUES (26, '360-Degree Camera');
INSERT INTO public.features VALUES (27, 'Third-Row Seating');
INSERT INTO public.features VALUES (28, 'Adjustable Suspension');
INSERT INTO public.features VALUES (29, 'Electric/Power Seats');
INSERT INTO public.features VALUES (30, 'Premium Sound System');
INSERT INTO public.features VALUES (31, 'Auto Pilot');


--
-- Data for Name: makes; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.makes VALUES (1, 'Toyota');
INSERT INTO public.makes VALUES (2, 'Honda');
INSERT INTO public.makes VALUES (3, 'Ford');
INSERT INTO public.makes VALUES (4, 'Chevrolet');
INSERT INTO public.makes VALUES (5, 'Volkswagen');
INSERT INTO public.makes VALUES (6, 'Nissan');
INSERT INTO public.makes VALUES (7, 'BMW');
INSERT INTO public.makes VALUES (8, 'Mercedes-Benz');
INSERT INTO public.makes VALUES (9, 'Audi');
INSERT INTO public.makes VALUES (10, 'Hyundai');
INSERT INTO public.makes VALUES (11, 'Kia');
INSERT INTO public.makes VALUES (12, 'Subaru');
INSERT INTO public.makes VALUES (13, 'Tesla');
INSERT INTO public.makes VALUES (14, 'Lexus');
INSERT INTO public.makes VALUES (15, 'Mazda');
INSERT INTO public.makes VALUES (16, 'Volvo');
INSERT INTO public.makes VALUES (17, 'Porsche');
INSERT INTO public.makes VALUES (18, 'Jaguar');
INSERT INTO public.makes VALUES (19, 'Land Rover');
INSERT INTO public.makes VALUES (20, 'Fiat');
INSERT INTO public.makes VALUES (21, 'Mitsubishi');
INSERT INTO public.makes VALUES (22, 'Jeep');
INSERT INTO public.makes VALUES (23, 'GMC');
INSERT INTO public.makes VALUES (24, 'Chrysler');
INSERT INTO public.makes VALUES (25, 'Dodge');
INSERT INTO public.makes VALUES (27, 'Bentley');


--
-- Data for Name: models; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.models VALUES (1, 'Corolla', 1);
INSERT INTO public.models VALUES (2, 'Camry', 1);
INSERT INTO public.models VALUES (3, 'Rav4', 1);
INSERT INTO public.models VALUES (4, 'Civic', 2);
INSERT INTO public.models VALUES (5, 'Accord', 2);
INSERT INTO public.models VALUES (6, 'CR-V', 2);
INSERT INTO public.models VALUES (7, 'F-150', 3);
INSERT INTO public.models VALUES (8, 'Mustang', 3);
INSERT INTO public.models VALUES (9, 'Escape', 3);
INSERT INTO public.models VALUES (10, 'Silverado', 4);
INSERT INTO public.models VALUES (11, 'Equinox', 4);
INSERT INTO public.models VALUES (12, 'Malibu', 4);
INSERT INTO public.models VALUES (13, 'Jetta', 5);
INSERT INTO public.models VALUES (14, 'Passat', 5);
INSERT INTO public.models VALUES (15, 'Tiguan', 5);
INSERT INTO public.models VALUES (16, 'Altima', 6);
INSERT INTO public.models VALUES (17, 'Sentra', 6);
INSERT INTO public.models VALUES (18, 'Rogue', 6);
INSERT INTO public.models VALUES (19, '3 Series', 7);
INSERT INTO public.models VALUES (20, '5 Series', 7);
INSERT INTO public.models VALUES (21, 'X5', 7);
INSERT INTO public.models VALUES (22, 'C-Class', 8);
INSERT INTO public.models VALUES (23, 'E-Class', 8);
INSERT INTO public.models VALUES (24, 'GLC', 8);
INSERT INTO public.models VALUES (25, 'A3', 9);
INSERT INTO public.models VALUES (26, 'A4', 9);
INSERT INTO public.models VALUES (27, 'Q5', 9);
INSERT INTO public.models VALUES (28, 'Elantra', 10);
INSERT INTO public.models VALUES (29, 'Tucson', 10);
INSERT INTO public.models VALUES (30, 'Santa Fe', 10);
INSERT INTO public.models VALUES (31, 'Optima', 11);
INSERT INTO public.models VALUES (32, 'Sorento', 11);
INSERT INTO public.models VALUES (33, 'Sportage', 11);
INSERT INTO public.models VALUES (34, 'Outback', 12);
INSERT INTO public.models VALUES (35, 'Forester', 12);
INSERT INTO public.models VALUES (36, 'Impreza', 12);
INSERT INTO public.models VALUES (37, 'Model 3', 13);
INSERT INTO public.models VALUES (38, 'Model S', 13);
INSERT INTO public.models VALUES (39, 'Model X', 13);
INSERT INTO public.models VALUES (40, 'ES', 14);
INSERT INTO public.models VALUES (41, 'RX', 14);
INSERT INTO public.models VALUES (42, 'NX', 14);
INSERT INTO public.models VALUES (43, 'Mazda3', 15);
INSERT INTO public.models VALUES (44, 'Mazda6', 15);
INSERT INTO public.models VALUES (45, 'CX-5', 15);
INSERT INTO public.models VALUES (46, 'S60', 16);
INSERT INTO public.models VALUES (47, 'XC60', 16);
INSERT INTO public.models VALUES (48, 'XC90', 16);
INSERT INTO public.models VALUES (49, '911', 17);
INSERT INTO public.models VALUES (50, 'Cayenne', 17);
INSERT INTO public.models VALUES (51, 'Macan', 17);
INSERT INTO public.models VALUES (52, 'XE', 18);
INSERT INTO public.models VALUES (53, 'XF', 18);
INSERT INTO public.models VALUES (54, 'F-Pace', 18);
INSERT INTO public.models VALUES (55, 'Range Rover', 19);
INSERT INTO public.models VALUES (56, 'Range Rover Sport', 19);
INSERT INTO public.models VALUES (57, 'Discovery', 19);
INSERT INTO public.models VALUES (58, '500', 20);
INSERT INTO public.models VALUES (59, '500X', 20);
INSERT INTO public.models VALUES (60, '500L', 20);
INSERT INTO public.models VALUES (61, 'Outlander', 21);
INSERT INTO public.models VALUES (62, 'Mirage', 21);
INSERT INTO public.models VALUES (63, 'Eclipse Cross', 21);
INSERT INTO public.models VALUES (64, 'Grand Cherokee', 22);
INSERT INTO public.models VALUES (65, 'Cherokee', 22);
INSERT INTO public.models VALUES (66, 'Wrangler', 22);
INSERT INTO public.models VALUES (67, 'Sierra 1500', 23);
INSERT INTO public.models VALUES (68, 'Acadia', 23);
INSERT INTO public.models VALUES (69, 'Terrain', 23);
INSERT INTO public.models VALUES (70, '300', 24);
INSERT INTO public.models VALUES (71, 'Pacifica', 24);
INSERT INTO public.models VALUES (72, 'Pacifica Hybrid', 24);
INSERT INTO public.models VALUES (73, 'Charger', 25);
INSERT INTO public.models VALUES (74, 'Challenger', 25);
INSERT INTO public.models VALUES (75, 'Durango', 25);
INSERT INTO public.models VALUES (76, 'Continental', 27);


--
-- Data for Name: order_details; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.order_details VALUES (3, 3, '2023-12-20', '2023-12-28', 50000.00, 'CASH');
INSERT INTO public.order_details VALUES (1, 1, '2023-12-20', '2024-01-03', 50000.00, 'LEASING');
INSERT INTO public.order_details VALUES (2, 2, '2023-12-20', '2023-12-29', 50000.00, 'CASH');
INSERT INTO public.order_details VALUES (4, 4, '2023-12-20', '2023-12-21', 89000.00, 'LEASING');
INSERT INTO public.order_details VALUES (5, 5, '2023-12-22', '2024-01-02', 23000.00, 'LEASING');
INSERT INTO public.order_details VALUES (6, 6, '2024-01-03', '2024-01-05', 34000.00, 'CREDIT_CARD');


--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.orders VALUES (4, 2, 4, 'COMPLETED');
INSERT INTO public.orders VALUES (2, 1, 5, 'COMPLETED');
INSERT INTO public.orders VALUES (3, 1, 5, 'CANCELLED');
INSERT INTO public.orders VALUES (1, 1, 5, 'CANCELLED');
INSERT INTO public.orders VALUES (5, 3, 3, 'CANCELLED');
INSERT INTO public.orders VALUES (6, 4, 6, 'COMPLETED');


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users VALUES (1, 'admin', 'admin', 'admin');
INSERT INTO public.users VALUES (2, 'denis', 'parola', 'client');
INSERT INTO public.users VALUES (5, 'ciprian', 'parola', 'client');
INSERT INTO public.users VALUES (3, 'cristi', 'parola', 'client');
INSERT INTO public.users VALUES (6, 'Stefan', 'parola', 'client');


--
-- Name: cars_car_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cars_car_id_seq', 7, true);


--
-- Name: clients_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.clients_id_seq', 4, true);


--
-- Name: features_feature_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.features_feature_id_seq', 31, true);


--
-- Name: makes_make_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.makes_make_id_seq', 27, true);


--
-- Name: models_model_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.models_model_id_seq', 76, true);


--
-- Name: order_details_order_detail_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.order_details_order_detail_id_seq', 6, true);


--
-- Name: orders_order_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.orders_order_id_seq', 6, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 6, true);


--
-- Name: car_features car_features_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.car_features
    ADD CONSTRAINT car_features_pkey PRIMARY KEY (car_id, feature_id);


--
-- Name: cars cars_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cars
    ADD CONSTRAINT cars_pkey PRIMARY KEY (car_id);


--
-- Name: cars cars_vin_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cars
    ADD CONSTRAINT cars_vin_number_key UNIQUE (vin_number);


--
-- Name: clients clients_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT clients_pkey PRIMARY KEY (id);


--
-- Name: features features_feature_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.features
    ADD CONSTRAINT features_feature_name_key UNIQUE (feature_name);


--
-- Name: features features_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.features
    ADD CONSTRAINT features_pkey PRIMARY KEY (feature_id);


--
-- Name: makes makes_make_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.makes
    ADD CONSTRAINT makes_make_name_key UNIQUE (make_name);


--
-- Name: makes makes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.makes
    ADD CONSTRAINT makes_pkey PRIMARY KEY (make_id);


--
-- Name: models models_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.models
    ADD CONSTRAINT models_pkey PRIMARY KEY (model_id);


--
-- Name: order_details order_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_details
    ADD CONSTRAINT order_details_pkey PRIMARY KEY (order_detail_id);


--
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (order_id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- Name: users create_client_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER create_client_trigger AFTER INSERT ON public.users FOR EACH ROW EXECUTE FUNCTION public.create_client();


--
-- Name: orders create_order_detail_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER create_order_detail_trigger AFTER INSERT ON public.orders FOR EACH ROW EXECUTE FUNCTION public.create_order_detail_on_order_insert();


--
-- Name: car_features car_features_car_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.car_features
    ADD CONSTRAINT car_features_car_id_fkey FOREIGN KEY (car_id) REFERENCES public.cars(car_id);


--
-- Name: car_features car_features_feature_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.car_features
    ADD CONSTRAINT car_features_feature_id_fkey FOREIGN KEY (feature_id) REFERENCES public.features(feature_id);


--
-- Name: cars cars_make_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cars
    ADD CONSTRAINT cars_make_id_fkey FOREIGN KEY (make_id) REFERENCES public.makes(make_id) ON DELETE CASCADE;


--
-- Name: cars cars_model_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cars
    ADD CONSTRAINT cars_model_id_fkey FOREIGN KEY (model_id) REFERENCES public.models(model_id) ON DELETE CASCADE;


--
-- Name: clients clients_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT clients_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: models models_make_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.models
    ADD CONSTRAINT models_make_id_fkey FOREIGN KEY (make_id) REFERENCES public.makes(make_id);


--
-- Name: order_details order_details_order_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_details
    ADD CONSTRAINT order_details_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.orders(order_id);


--
-- Name: orders orders_car_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_car_id_fkey FOREIGN KEY (car_id) REFERENCES public.cars(car_id);


--
-- Name: orders orders_client_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_client_id_fkey FOREIGN KEY (client_id) REFERENCES public.clients(id);


--
-- PostgreSQL database dump complete
--

