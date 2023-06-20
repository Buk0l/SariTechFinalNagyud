    package com.example.saritechnew.products;

    import android.os.Parcel;
    import android.os.Parcelable;

    public class Products implements Parcelable {
        private int id;
        private String name;
        private double price;
        private final String barcode;
        private int quantity;
        private String photoPath;
        private int selectedQuantity;

        public Products(int id, String name, double price, String barcode, int quantity, String photoPath) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.barcode = String.valueOf(barcode);
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

        @Override
        public int describeContents() {
            return 0;
        }

        public int getSelectedQuantity(){
            return selectedQuantity;
        }

        public void setSelectedQuantity(int selectedQuantity){
            this.selectedQuantity = selectedQuantity;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeDouble(price);
            dest.writeString(barcode);
            dest.writeInt(quantity);
            dest.writeString(photoPath);
        }

        // Add a Creator for the Parcelable interface
        public static final Parcelable.Creator<Products> CREATOR = new Parcelable.Creator<Products>() {
            @Override
            public Products createFromParcel(Parcel in) {
                return new Products(in);
            }

            @Override
            public Products[] newArray(int size) {
                return new Products[size];
            }
        };

        // Add a constructor that reads from a Parcel
        protected Products(Parcel in) {
            id = in.readInt();
            name = in.readString();
            price = in.readDouble();
            barcode = in.readString();
            quantity = in.readInt();
            photoPath = in.readString();
        }
    }
