-- Создаем таблицу organization
CREATE TABLE organization (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              name VARCHAR(255),
                              inn VARCHAR(12),
                              kpp VARCHAR(9),
                              address VARCHAR(255),
                              email VARCHAR(255) UNIQUE,
                              password VARCHAR(255)
);

-- Создаем таблицу employee
CREATE TABLE employee (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255),
                          position VARCHAR(255),
                          email VARCHAR(255) UNIQUE,
                          password VARCHAR(255),
                          organization_id BIGINT,
                          FOREIGN KEY (organization_id) REFERENCES organization(id)
);

-- Создаем таблицу counterparty
CREATE TABLE counterparty (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              name VARCHAR(255),
                              inn VARCHAR(12),
                              kpp VARCHAR(9),
                              address VARCHAR(255),
                              email VARCHAR(255) UNIQUE,
                              organization_id BIGINT,
                              FOREIGN KEY (organization_id) REFERENCES organization(id)
);

-- Создаем таблицу product с внешним ключом organization_id
CREATE TABLE product (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255),
                         description VARCHAR(255),
                         quantity DECIMAL(10, 2),
                         price DECIMAL(10, 2),
                         organization_id BIGINT,
                         FOREIGN KEY (organization_id) REFERENCES organization(id)
);

-- Вставляем демонстрационные данные в таблицу organization
INSERT INTO organization (name, inn, kpp, address, email, password) VALUES
                                                                        ('ООО Рога и Копыта', '123456789012', '123456789', 'ул. Пушкина, д. Колотушкина', 'info@rogakopyta.com', 'securePassword'),
                                                                        ('ЗАО Копыта и Рога', '987654321098', '987654321', 'пр. Ленина, д. Молоток', 'sales@kopytaroga.com', 'password123');

-- Вставляем демонстрационные данные в таблицу employee
INSERT INTO employee (name, position, email, password, organization_id) VALUES
                                                                            ('Иван Иванов', 'Директор', 'ivanov@rogakopyta.com', 'ivanPass', 1),
                                                                            ('Сидор Сидоров', 'Бухгалтер', 'sidorov@rogakopyta.com', 'sidorPass', 1),
                                                                            ('Петр Петров', 'Кладовщик', 'petrov@rogakopyta.com', 'petrPass', 1),
                                                                            ('Михаил Петров', 'Менеджер', 'mikhail@rogakopyta.com', 'petrPass', 1),

                                                                            ('Алексей Алексеев', 'Директор', 'alekseev@kopytaroga.com', 'alekseiPass', 2),
                                                                            ('Анна Сидорова', 'Бухгалтер', 'sidorova@kopytaroga.com', 'sidorovaPass', 2),
                                                                            ('Александр Петров', 'Кладовщик', 'aleksandr@kopytaroga.com', 'petrovPass', 2),
                                                                            ('Алексей Михайлов', 'Менеджер', 'aleksey@kopytaroga.com', 'petrovPass', 2)                                                                            ;



-- Вставляем демонстрационные данные в таблицу counterparty
INSERT INTO counterparty (name, inn, kpp, address, email, organization_id) VALUES
                                                                               ('ООО Винтик и Шпунтик', '111222333444', '555666777', 'г. Винтовка, ул. Шпунтовая, д.1', 'info@vintikshpuntik.com', 1),
                                                                               ('ИП Болтунов', '999888777666', '444333222', 'г. Гайка, ул. Болтовая, д.2', 'boltunov@gaika.com', 2);
-- Вставляем демонстрационные данные в таблицу product
INSERT INTO product (name, description, quantity, price, organization_id) VALUES
                                                                              ('Плашка', 'для нарезения наружной резьбы', 5.00, 500.00, 1),
                                                                              ('Плашка М5', 'для нарезения наружной резьбы', 5.00, 50.00, 1),
                                                                              ('Метчик', 'для нарезения внутренной резьбы', 40.00, 60.00, 1),
                                                                              ('Метчик М27', 'для нарезения внутренной резьбы', 0.00, 400.00, 1),
                                                                              ('Резец', 'для придания металлическому изделию формы', 0.00, 40.00, 1),
                                                                              ('Резец проходной', 'для придания металлическому изделию формы', 20.00, 450.00, 1),
                                                                              ('Фреза червячная', 'для вырезания отверстия', 30.00, 300.00, 1),
                                                                              ('Фреза', 'для вырезания отверстия', 30.00, 45.00, 1),

                                                                              ('конфета Мишка косолапый', 'конфета шоколадная', 5.00, 500.00, 2),
                                                                              ('конфета Аленка', 'плитка шоколада 100 гр.', 0.00, 50.00, 2),
                                                                              ('конфета Маска', 'конфета шоколадная с начинкой', 40.00, 60.00, 2),
                                                                              ('батончик Марс', 'шоколадный батончик 50 гр', 50.00, 400.00, 2),
                                                                              ('батончик Сникерс', 'шоколадный батончик 50 гр', 0.00, 40.00, 2);

