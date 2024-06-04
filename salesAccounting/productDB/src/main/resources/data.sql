DROP DATABASE IF EXISTS productsDB;
CREATE DATABASE productsDB;
--
--Создаем таблицу organization
CREATE TABLE organization (
                              id BIGSERIAL PRIMARY KEY,
                              name VARCHAR(255),
                              inn VARCHAR(12),
                              kpp VARCHAR(9),
                              address VARCHAR(255),
                              email VARCHAR(255) UNIQUE,
                              password VARCHAR(255)
);

-- Создаем таблицу employee
CREATE TABLE employee (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255),
                          position VARCHAR(255),
                          email VARCHAR(255) UNIQUE,
                          password VARCHAR(255),
                          organization_id BIGINT,
                          FOREIGN KEY (organization_id) REFERENCES organization(id)
);

-- Создаем таблицу product с внешним ключом organization_id
CREATE TABLE product (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255),
                         description VARCHAR(255),
                         quantity DECIMAL(10, 2),
                         price DECIMAL(10, 2),
                         organization_id BIGINT,
                         FOREIGN KEY (organization_id) REFERENCES organization(id)
);

-- Создаем таблицу orders с внешним ключом organization_id и employee_id
CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        buyer_organization_id BIGINT,
                        seller_organization_id BIGINT,
                        employee_id BIGINT,
                        order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        order_number VARCHAR(255),
                        status_change_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Дата и время изменения статуса заказа,
                        status VARCHAR(50) CHECK (status IN ('NEW', 'CONFIRMED', 'PAID', 'CANCELLED', 'SHIPPED')),
                        total_amount DECIMAL(10, 2),
                        document_id BIGINT,
                        comments VARCHAR(255) NULL,
                        FOREIGN KEY (buyer_organization_id) REFERENCES organization(id),
                        FOREIGN KEY (seller_organization_id) REFERENCES organization(id),
                        FOREIGN KEY (employee_id) REFERENCES employee(id)

);

-- Создаем таблицу order_item с внешним ключом order_id и product_id
CREATE TABLE order_items (
                             id BIGSERIAL PRIMARY KEY,
                             order_id BIGINT,
                             product_id BIGINT,
                             quantity DECIMAL(10, 2),
                             price DECIMAL(10, 2),
                             amount DECIMAL(10, 2),
                             FOREIGN KEY (order_id) REFERENCES orders(id),
                             FOREIGN KEY (product_id) REFERENCES product(id)
);
-- Создадим таблицу documents c внешним ключем organization_id
CREATE TABLE documents (
                           id BIGSERIAL PRIMARY KEY,
                           buyer_organization_id BIGINT,
                           seller_organization_id BIGINT,
                           employee_id BIGINT,
                           document_Number VARCHAR(255),
                           document_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           order_id BIGINT,
                           total_amount DECIMAL(10, 2),
                           FOREIGN KEY (buyer_organization_id) REFERENCES organization(id),
                           FOREIGN KEY (seller_organization_id) REFERENCES organization(id),
                           FOREIGN KEY (employee_id) REFERENCES employee(id)
            );

-- Создаем таблицу document_items с внешним ключом document_id и product_id
CREATE TABLE document_items (
                                id BIGSERIAL PRIMARY KEY,
                                document_id BIGINT,
                                product_id BIGINT,
                                quantity DECIMAL(10, 2),
                                price DECIMAL(10, 2),
                                amount DECIMAL(10, 2),
                                FOREIGN KEY (document_id) REFERENCES documents(id),
                                FOREIGN KEY (product_id) REFERENCES product(id)
);

-- Вставляем демонстрационные данные в таблицу organization
INSERT INTO organization (name, inn, kpp, address, email, password) VALUES
                                                                        ('ООО "Металл-Инструмент"', '123456789012', '12301001', 'ул. Пушкина, д. Колотушкина', 'info@rogakopyta.com', 'securePassword'),
                                                                        ('ЗАО "Сладкий рай"', '987654321098', '987654321', 'пр. Ленина, д. Молоток', 'sales@kopytaroga.com', 'securePassword'),
                                                                        ('ООО "Теремок"', '183217245685', '183201001', 'г.Ижевск,ул. Пушкинская, д. 125 ', 'sales@teremok.com', 'securePassword'),
                                                                        ('ООО "Мир Инструмента"', '121098765432', '12101001', 'г.Сарапул, ул. Советская, д.15', 'info@mirinstrumenta.com', 'securePassword');

-- Вставляем демонстрационные данные в таблицу employee
INSERT INTO employee (name, position, email, password, organization_id) VALUES
                                                                            ('Иван Иванов', 'Директор', 'ivanov@rogakopyta.com', 'Pass', 1),
                                                                            ('Сидор Сидоров', 'Бухгалтер', 'sidorov@rogakopyta.com', 'sidorPass', 1),
                                                                            ('Петр Петров', 'Кладовщик', 'petrov@rogakopyta.com', 'petrPass', 1),
                                                                            ('Михаил Петров', 'Менеджер', 'mikhail@rogakopyta.com', 'petrPass', 1),
                                                                            ('Алексей Алексеев', 'Директор', 'alekseev@kopytaroga.com', 'Pass', 2),
                                                                            ('Анна Сидорова', 'Бухгалтер', 'sidorova@kopytaroga.com', 'sidorovaPass', 2),
                                                                            ('Александр Петров', 'Кладовщик', 'aleksandr@kopytaroga.com', 'petrovPass', 2),
                                                                            ('Алексей Михайлов', 'Менеджер', 'aleksey@kopytaroga.com', 'petrovPass', 2),
                                                                            ('Алексей Мышкин', 'Директор', 'myshkin@teremok.com', 'Pass', 3),
                                                                            ('Анна Шишкина', 'Бухгалтер', 'shishkin@teremok.com', 'shishkinPass', 3),
                                                                            ('Александр Петров', 'Кладовщик', 'aleksandr@teremok.com', 'petrovPass', 3),
                                                                            ('Алексей Михайлов', 'Менеджер', 'aleksey@teremok.com', 'petrovPass', 3),
                                                                            ('Алексей Иванов', 'Директор', 'alekseev@mirinstrumenta.com', 'Pass', 4),
                                                                            ('Анна Сидорова', 'Бухгалтер', 'sidorova@mirinstrumenta.com', 'sidorovaPass', 4),
                                                                            ('Александр Петров', 'Кладовщик', 'aleksandr@mirinstrumenta.com', 'petrovPass', 4),
                                                                            ('Алексей Михайлов', 'Менеджер', 'aleksey@mirinstrumenta.com', 'petrovPass', 4);

-- Вставляем демонстрационные данные в таблицу product
INSERT INTO product (name, description, quantity, price, organization_id) VALUES
                                                                              ('Плашка', 'для нарезания наружной резьбы', 5.00, 500.00, 1),
                                                                              ('Плашка М5', 'для нарезания наружной резьбы', 5.00, 50.00, 1),
                                                                              ('Метчик', 'для нарезания внутренний резьбы', 40.00, 60.00, 1),
                                                                              ('Метчик М27', 'для нарезания внутренний резьбы', 0.00, 400.00, 1),
                                                                              ('Резец', 'для придания металлическому изделию формы', 0.00, 40.00, 1),
                                                                              ('Резец проходной', 'для придания металлическому изделию формы', 20.00, 450.00, 1),
                                                                              ('Фреза червячная', 'для вырезания отверстия', 30.00, 300.00, 1),
                                                                              ('Фреза', 'для вырезания отверстия', 30.00, 45.00, 1),
                                                                              ('конфета Мишка косолапый', 'конфета шоколадная', 5.00, 500.00, 2),
                                                                              ('конфета Аленка', 'плитка шоколада 100 гр.', 0.00, 50.00, 2),
                                                                              ('конфета Маска', 'конфета шоколадная с начинкой', 40.00, 600.00, 2),
                                                                              ('батончик Марс', 'шоколадный батончик 50 гр', 50.00, 400.00, 2),
                                                                              ('батончик Сникерс', 'шоколадный батончик 50 гр', 0.00, 40.00, 2),
                                                                              ('стол столовый', 'обеденный стол раскладывающийся', 2, 3000.00, 3),
                                                                              ('стол офисный', 'стол для компьютера', 5, 2000.00, 3),
                                                                              ('стол чайный', 'стол круглый стеклянный', 3, 4000.00, 3),
                                                                              ('стол журнальный', 'стол невысокий с полками', 6, 2500.00, 3),
                                                                              ('микроволновка', 'микроволновка для кухни', 3.00, 300.00, 4),
                                                                              ('лопата совковая', 'лопата совковая', 100.00, 250.00, 4),
                                                                              ('лопата металлическая', 'лопата металлическая', 50.00, 300.00, 4),
                                                                              ('молоток', 'боек 400гр.', 20.00, 500.00, 4);

-- Вставляем демонстрационные данные в таблицу orders
INSERT INTO orders (buyer_organization_id, seller_organization_id, employee_id, order_number, order_date, status, status_change_date, total_amount, document_id,comments) VALUES
                                                      (1, 2, 1, 'ORD_1_2_1/1', CURRENT_TIMESTAMP, 'CONFIRMED', CURRENT_TIMESTAMP, 3000.00, null, 'No comments provided'), -- 1
                                                      (2, 1, 5, 'ORD_2_1_4/1', CURRENT_TIMESTAMP, 'CONFIRMED', CURRENT_TIMESTAMP, 2750.00, null, 'No comments provided'), -- 2
                                                      (4, 1, 13, 'ORD_3_4_13/1',  CURRENT_TIMESTAMP, 'CONFIRMED', CURRENT_TIMESTAMP, 135.00, null, 'No comments provided'), -- 3
                                                      (1, 4, 3, 'ORD_4_1_3/1',  CURRENT_TIMESTAMP,  'NEW', CURRENT_TIMESTAMP, 2800.00, null, 'No comments provided'), -- 4
                                                      (1, 2, 2, 'ORD_5_1_2/1',  CURRENT_TIMESTAMP,  'NEW',  CURRENT_TIMESTAMP, 5000.00, null, 'No comments provided'); --5

-- Вставляем демонстрационные данные в таблицу order_item
INSERT INTO order_items (order_id, product_id,  quantity, price, amount) VALUES
                                                                             (1, 9, 5, 500.00, 2500.00),  -- Добавляем товар "Плашка" в заказ "ORD_1_2_1/1"
                                                                             (1, 11, 10, 50.00, 500.00),   -- Добавляем товар "Метчик" в заказ "ORD_1_2_1/1"
                                                                             (2, 1, 5, 500.00, 2500.00), -- Добавляем товар "Плашка" в заказ "ORD_2_1_4/1"
                                                                             (2, 2, 5, 50.00, 250.00),  -- Добавляем товар "Плашка М5" в заказ "ORD_2_1_4/1"
                                                                             (3, 8, 3, 45.00, 135.00),   -- Добавляем товар "Фреза" в заказ "ORD_3_4_13/1"
                                                                             (4, 19, 10, 250.00, 2500.00), -- Добавляем товар "Резец проходной" в заказ "ORD_4_1_3/1"
                                                                             (4, 18, 1, 300.00, 300.00), -- Добавляем товар "Фреза" в заказ "ORD_4_1_3/1"
                                                                             (5, 12, 5, 400, 2000), --  ('батончик Марс', 'шоколадный батончик 50 гр', 50.00, 400.00, 2)
                                                                             (5, 11, 5, 600, 3000); --  ('конфета Маска', 'конфета шоколадная с начинкой', 40.00, 600.00, 2)
-- Вставляем демонстрационные данные в таблицу documents

-- Вставляем демонстрационные данные в таблицу documents_items