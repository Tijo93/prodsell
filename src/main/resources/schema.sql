CREATE TABLE IF NOT EXISTS product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL CHECK(price>=0),
    quantity INT NOT NULL CHECK(quantity>=0)
);

CREATE TABLE IF NOT EXISTS sale (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK(quantity>=0),
    sale_date DATE NOT NULL,
    CONSTRAINT fk_sale_prod FOREIGN KEY(product_id) REFERENCES product(id)
);
