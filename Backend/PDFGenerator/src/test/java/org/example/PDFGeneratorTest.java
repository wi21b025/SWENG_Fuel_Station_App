package org.example;

import org.example.Data.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PDFGeneratorTest {
    Customer customer = mock(Customer.class);

    @Test
    public void getIdTest() {
        when(customer.getId()).thenReturn(3);

        int id = customer.getId();

        assertEquals(3, id);
    }

    @Test
    public void getFirstNameTest() {
        when(customer.getFirstName()).thenReturn("First");

        String first = customer.getFirstName();

        assertEquals("First", first);
    }

    @Test
    public void getLastNameTest() {
        when(customer.getLastName()).thenReturn("Last");

        String last = customer.getLastName();

        assertEquals("Last", last);
    }

}
