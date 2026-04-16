import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface Reader {
    Scanner scanner = new Scanner(System.in);
}

abstract class Document implements Reader {
    protected List<String> content = new ArrayList<>();
    String docType = this.getClass().getSimpleName();


    public final void generateDocument() {
        createHeader();
        createBody();
        createFooter();
        printDocument();
    }


    public final void createHeader() {
        // Collects company name and date
        System.out.print("Enter company name: ");
        String companyName = scanner.nextLine();
        if (companyName.isEmpty()) throw new IllegalArgumentException("Error: Company name cannot be empty.");

        System.out.print("Enter date (DD/MM/YYYY): ");
        String date = scanner.nextLine();
        if (date.isEmpty()) throw new IllegalArgumentException("Error: Date cannot be empty.");

        content.add("Company: " + companyName);
        content.add("Date: " + date);
    }

    abstract public void createBody();
    
    public void createFooter() {
        content.add("Prepared by: AutoDoc System");
        content.add("Document Type: " + docType.toUpperCase());
    }

    public void printDocument() {
        System.out.printf("%n=== Printing Document ===%n");
        System.out.printf("=== %s ===%n", docType.toUpperCase());
        for (String line : content) {
            System.out.println(line);
        }
        System.out.println("=========================");
    }
}

class Invoice extends Document {

    @Override
    public void createBody() {
        System.out.print("Enter total amount: ");
        String totalDue = scanner.nextLine();
        float value;
        try {
            value = Float.parseFloat(totalDue);
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Error: Total amount must be numeric.");
        }
        if (value <= 0) throw new IllegalArgumentException("Error: Total amount must be positive.");
        content.add("Total Due: €" + totalDue.trim());
    }
    
}

class Report extends Document {
    // Report functions
    @Override
    public void createBody() {
        System.out.print("Enter report summary: ");
        String summary = scanner.nextLine();
        if (summary.isEmpty()) System.out.println("Warning: Summary is empty.");
        content.add("Report Summary: " + summary);

        content.add("Reviewed by: Management Department");
    }

    @Override
    public void createFooter() {}
}

class Receipt extends Document {
    // Receipt functions
    @Override
    public void createBody() {
        System.out.print("Enter amount paid: ");
        String totalPaid = scanner.nextLine();
        float value;
        try {
            value = Float.parseFloat(totalPaid);
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Error: Total amount must be numeric.");
        }
        if (value <= 0) throw new IllegalArgumentException("Total paid must be positive.");
        content.add("Total Paid: €" + String.format("%.1f", value));
        System.out.print("Enter number of items: ");
        String totalItems = scanner.nextLine();
        int items = Integer.parseInt(totalItems);
        if (items <= 0) throw new IllegalArgumentException("Error: Items count must be positive.");
        content.add("Items Purchased: " + totalItems); 
        float pricePerItem = value / items;
        content.add("Price per Item: €" + String.format("%.1f", pricePerItem));
    }
}

class DocumentGenerator implements Reader {
    public static void main(String[] args) {
        try {
            System.out.println("Choose document type: (INV) Invoice, (REP) Report, (REC) Receipt");
            String choice = scanner.nextLine();
            Document document;
            switch (choice) {
                case "INV":
                    document = new Invoice();
                    break;
                case "REP":
                    document = new Report();
                    break;
                case "REC":
                    document = new Receipt();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid choice. Exiting.");
            }
            document.generateDocument();
        } catch (IllegalArgumentException error) {
            System.out.println(error.getMessage());
        } 
    }
}