package com.example.springapp.Service;

import com.example.springapp.Queue.Queue;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {

    private final Queue queue = new Queue();

    public boolean createInvoice(String customerID) {
        return queue.send(customerID);
    }

    public List<String> getInvoice(int customerID) {
        String fileStoragePath = ".\\Backend\\FileStorage\\";
        String filePath = fileStoragePath + customerID + ".pdf";
        Path path = Paths.get(filePath);
        if (Files.exists(path) && Files.isRegularFile(path)) {
            List<String> invoiceInfo = new ArrayList<>();
            invoiceInfo.add(filePath);
            try {
                FileTime creationTime = Files.getLastModifiedTime(path);
                String formattedCreationTime = creationTime.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm"));
                invoiceInfo.add("Created: " + formattedCreationTime.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return invoiceInfo;
        }
        return null;
    }


}