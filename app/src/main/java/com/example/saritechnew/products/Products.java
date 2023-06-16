    package com.example.saritechnew.products;

    public class Products {
        private int id;
        private String name;
        private double price;
        private final String barcode;
        private int quantity;
        private String photoPath;

        public Products(int id, String name, double price, String barcode, int quantity, String photoPath) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.barcode = barcode;
            this.quantity = quantity;
            this.photoPath = photoPath;
        }

        // Getters and setters for the fields

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getPhotoPath() {
            return photoPath;
        }

        public void setPhotoPath(String photoPath) {
            this.photoPath = photoPath;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getBarcode() {
            return barcode;
        }
    }
