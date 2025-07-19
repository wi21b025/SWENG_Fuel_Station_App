package com.example.springapp;

import com.example.springapp.Controller.InvoiceController;
import com.example.springapp.Service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class SpringAppApplicationTests {
	InvoiceService invSer = mock(InvoiceService.class);


	@Test
	void contextLoads() {
	}

	@Test
	void gatherDataTestFail() {
		InvoiceController invCon = new InvoiceController(invSer);

		when(invSer.createInvoice("6")).thenReturn(false);

		ResponseEntity<String> data = invCon.gatherData("6");

		assertEquals(new ResponseEntity<>("Request to gather data could not be sent!", HttpStatus.INTERNAL_SERVER_ERROR), data);
	}

	@Test
	void gatherDataTestSuccess() {
		InvoiceController invCon = new InvoiceController(invSer);

		when(invSer.createInvoice("3")).thenReturn(true);

		ResponseEntity<String> data = invCon.gatherData("3");

		assertEquals(new ResponseEntity<>("Request to gather data sent!", HttpStatus.OK), data);
	}

	@Test
	void gatherInvoiceTestFail() {
		InvoiceController invCon = new InvoiceController(invSer);

		when(invSer.getInvoice(10)).thenReturn(null);

		ResponseEntity<List<String>> data = invCon.gatherInvoice("10");

		assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND), data);
	}

	@Test
	void gatherInvoiceTestSuccess() {
		InvoiceController invCon = new InvoiceController(invSer);

		when(invSer.getInvoice(3)).thenReturn(
				List.of(
						"3.pdf",
						"Created: 13.06.2023 - 21:00"
				)
		);

		ResponseEntity<List<String>> data = invCon.gatherInvoice("3");

		assertEquals(new ResponseEntity<>(List.of("3.pdf", "Created: 13.06.2023 - 21:00"), HttpStatus.OK), data);
	}
}
