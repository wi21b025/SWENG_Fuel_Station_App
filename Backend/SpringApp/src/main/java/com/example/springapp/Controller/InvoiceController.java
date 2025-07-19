package com.example.springapp.Controller;

import com.example.springapp.Service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InvoiceController {
    private final InvoiceService invSer;

    public InvoiceController(InvoiceService invSer) {
        this.invSer = invSer;
    }

    @PostMapping("/invoices/{customerID}")
    public ResponseEntity<String> gatherData(@PathVariable String customerID) {
        boolean requestSent = invSer.createInvoice(customerID);
        if (requestSent) return new ResponseEntity<>("Request to gather data sent!", HttpStatus.OK);
        return new ResponseEntity<>("Request to gather data could not be sent!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/invoices/{customerID}")
    public ResponseEntity<List<String>> gatherInvoice(@PathVariable String customerID) {
        List<String> invoiceInfo = invSer.getInvoice(Integer.parseInt(customerID));
        if (invoiceInfo != null) {
            return new ResponseEntity<>(invoiceInfo, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
