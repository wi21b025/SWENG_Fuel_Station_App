package org.example.PdfGenerator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.example.Data.Customer;
import org.example.Service.Queue;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

public class PDFGenerator {

    public static void main(String[] args) {
        Queue queue = new Queue();

        listen(queue);
    }

    private static void listen(Queue queue) {
        try {
            queue.receive();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public static void generate(float kwh, Customer customer) { // guide https://www.youtube.com/watch?v=K6QBMh1oA-w
        System.out.println("Generating PDF...");

        PDDocument document = new PDDocument();
        PDPage firstPage = new PDPage();
        document.addPage(firstPage);

        int pageHeight = (int) firstPage.getTrimBox().getHeight();
        int pageWidth = (int) firstPage.getTrimBox().getWidth();

        PDFont font = PDType1Font.HELVETICA_BOLD;
        PDPageContentStream contentStream;
        try {
            contentStream = new PDPageContentStream(document, firstPage);
            contentStream.beginText();
            contentStream.setLeading(16.0f);
            contentStream.newLineAtOffset(50, pageHeight-50);

            contentStream.setFont(font, 20 );
            contentStream.showText("Receipt");
            contentStream.newLine();
            contentStream.newLine();

            contentStream.setFont(font, 16 );
            contentStream.showText("Created on: " + date());
            contentStream.newLine();
            contentStream.newLine();

            contentStream.setFont(font, 12 );
            contentStream.showText("Customer Details");
            contentStream.newLine();
            contentStream.showText("Name: " + customer.getFirstName() + " " + customer.getLastName());
            contentStream.newLine();
            contentStream.showText("No.: " + customer.getId());
            contentStream.newLine();
            contentStream.newLine();
            contentStream.newLine();

            contentStream.setFont(font, 16 );
            contentStream.showText("Charge details");
            contentStream.newLine();
            contentStream.newLine();

            contentStream.setFont(font, 12 );
            contentStream.showText(String.format("Total charges: %.2f kwH", kwh));
            contentStream.newLine();
            contentStream.showText(String.format("Total price: %.2f €", kwh*0.5));

            contentStream.endText();
            contentStream.close();

            document.save(".\\Backend\\FileStorage\\" + customer.getId() + ".pdf");
            document.close();
            System.out.println("PDF created!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String date() {
        return String.format(
                "%s %02d, %04d (%02d:%02d:%02d)",
                LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.US),
                LocalDate.now().getDayOfMonth(),
                LocalDate.now().getYear(),
                LocalTime.now().getHour(),
                LocalTime.now().getMinute(),
                LocalTime.now().getSecond());
    }
}