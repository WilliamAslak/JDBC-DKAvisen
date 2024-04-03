package org.example.models;

public class Address {
    private final String Street;
    private final String Civic;
    private final String ZIP;
    private final String City;

    public Address(String street, String civic, String zip, String city) {
        this.Street = street;
        this.Civic = civic;
        this.ZIP = zip;
        this.City = city;
    }

    public String getStreet() {
        return Street;
    }

    public String getCivic() {
        return Civic;
    }

    public String getZIP() {
        return ZIP;
    }

    public String getCity() {
        return City;
    }

    @Override
    public String toString() {
        return getStreet() + ": " + getCivic() + " | " + getZIP() + " " + getCity();
    }
}
