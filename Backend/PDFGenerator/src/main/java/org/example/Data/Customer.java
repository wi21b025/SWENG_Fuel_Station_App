package org.example.Data;

public class Customer {
    private static int id = 0;
    private static String firstName = null;
    private static String lastName = null;

    public Customer(int id, String firstName, String lastName) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{id='"+ id + "', firstName='" + firstName + "', lastName='" + lastName + "'}\n";
    }
}
