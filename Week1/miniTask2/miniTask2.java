public class miniTask2 {
    public static void main(String[] args) {

        // Create a document (independent object)
        Document doc = new Document("CSC1031 Handout", -1);

        // Create a cartridge (independent object)
        InkCartridge cart = new InkCartridge("HP-301", 40);

        // Create a printer (printer owns its PrinterConfig internally)
        Printer printer = new Printer(
                "LaserFox",
                "Misha",
                "DCU-WiFi",
                "1234-very-secret",
                "v1.0.0"
        );

        // Install cartridge (aggregation)
        printer.installCartridge(cart);

        // Printer uses the document (association)
        int printedPages = printer.print(doc);

        System.out.println("Printer brand: " + printer.getBrand());
        System.out.println("Owner: " + printer.getConfig().getOwnerName());
        System.out.println("WiFi: " + printer.getConfig().getWifiName());
        System.out.println("Firmware: " + printer.getConfig().getFirmwareVersion());
        System.out.println("Printed pages: " + printedPages);
        System.out.println("Document Title: " + doc.getTitle());

        System.out.println("Ink after printing: " + printer.getCartridge().getInkLevel() + "%");

        // Remove cartridge (cartridge continues to exist)
        InkCartridge removed = printer.removeCartridge();
        System.out.println("Removed cartridge model: " + removed.getModel());

        // Try printing without cartridge
        int printed = printer.print(new Document("Another doc", 3));
        System.out.println("Printed pages without cartridge: " + printed);

        // Cartridge still exists and can be re-installed
        printer.installCartridge(removed);
        printer.print(new Document("Short note", 2));

        System.out.println("Ink at the end: " + printer.getCartridge().getInkLevel() + "%");

        // Update config (still the same config object owned by the printer)
        printer.getConfig().setFirmwareVersion("v1.0.1");
        System.out.println("Firmware after update: " + printer.getConfig().getFirmwareVersion());
    }
}

// -------------------- Document --------------------
class Document {
    private String title;
    private int pages;

    //Constructor - a method that creates an instance
    public Document(String dcument_title, int document_pages) {
        if (document_pages <= 0) {
            //safe default value
            title = "INVALID DOCUMENT";
            pages = 1;
        } else {
            title = dcument_title;
            pages = document_pages;
        }
    }

    // Getters to access the document
    public String getTitle() {
        return title;
    }

    public int getPages() {
        return pages;
    }
}

// -------------------- InkCartridge --------------------
class InkCartridge {
    private String model;
    private int inkLevel; // 0..100

    // Constructor
    public InkCartridge(String model_name, int inkLevel) {
        model = model_name;
        setInkLevel(inkLevel); // validation in one place
    }

    //Getters
    public String getModel() {
        return model;
    }

    public int getInkLevel() {
        return inkLevel;
    }

    //Setters
    // Setter exists because ink changes over time and we must validate it.
    public void setInkLevel(int newLevel) {
        if (newLevel < 0) newLevel = 0;
        if (newLevel > 100) newLevel = 100;
        inkLevel = newLevel;
    }

    // Simple rule: 1 page = 2% ink
    public boolean hasEnoughInkFor(int pages) {
        int needed = pages * 2;
        return inkLevel >= needed;
    }

    public void consumeInkFor(int pages) {
        int needed = pages * 2;
        setInkLevel(inkLevel - needed);
    }
}

// -------------------- PrinterConfig --------------------
class PrinterConfig {
    private String ownerName;
    private String wifiName;
    private String wifiPassword;
    private String firmwareVersion;

    //Construtor
    public PrinterConfig(String owner_Name, String wifi_Name, String wifi_Password, String firmware_Version) {
        ownerName = owner_Name;
        wifiName = wifi_Name;
        wifiPassword = wifi_Password;
        firmwareVersion = firmware_Version;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String Name) {
        if (Name == null || Name.trim().isEmpty())
            return;
        ownerName = Name;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String name) {
        if (name == null || name.trim().isEmpty()) return;
        wifiName = name;
    }

    public String getWifiPassword() {
        return wifiPassword;
    }

    public void setWifiPassword(String Password) {
        if (Password == null || Password.trim().isEmpty()) return;
        wifiPassword = Password;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String  Version) {
        if (Version == null || Version.trim().isEmpty()) return;
        firmwareVersion = Version;
    }
}

// -------------------- Printer --------------------
class Printer {
    private String brand;

    // Aggregation (weak has-a): can be installed/removed
    private InkCartridge cartridge;

    // Composition (strong has-a): owned internal component
    private PrinterConfig config;

    public Printer(String printer_brand, String ownerName, String wifiName, String wifiPassword, String firmwareVersion) {
        brand = printer_brand;

        // Created internally -> composition
        // We call PrinterConfig constructor inside this class
        // PrinterConfig object will be destroyed once Printer is gone
        config = new PrinterConfig(ownerName, wifiName, wifiPassword, firmwareVersion);
    }

    public String getBrand() {
        return brand;
    }

    public InkCartridge getCartridge() {
        return cartridge;
    }

    public boolean hasCartridge() {
        return cartridge != null;
    }

    public void installCartridge(InkCartridge new_cartridge) {
        if (new_cartridge == null) return;
        cartridge = new_cartridge;
    }

    public InkCartridge removeCartridge() {
        InkCartridge tmp = cartridge; //save for later
        cartridge = null; //remove from printer
        return tmp; //return to "outside" of printer world
    }

    // Access to config (read-only object reference, but values can be changed via setters)
    public PrinterConfig getConfig() {
        return config;
    }

    // Printer uses Document (association)
    public int print(Document doc) {
        if (doc == null) return 0;
        if (cartridge == null) return 0;

        int pages = doc.getPages();
        if (!cartridge.hasEnoughInkFor(pages)) {
            return 0; // just fail safely if we don't have enough ink
        }

        cartridge.consumeInkFor(pages);
        return pages;
    }
}
