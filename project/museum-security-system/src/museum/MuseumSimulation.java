package museum;

import museum.coordination.SecurityCoordinationHub;
import museum.core.*;
import museum.devices.*;
import museum.endpoints.*;
import java.util.List;

public class MuseumSimulation {

    static void printHeader(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  " + title);
        System.out.println("=".repeat(60));
    }

    static void printStep(String step) {
        System.out.println("\n--- " + step + " ---");
    }

    public static void main(String[] args) throws InterruptedException {

        printHeader("MUSEUM EXHIBIT SECURITY SYSTEM - SIMULATION");

        // ── 1. Set up the gallery ──────────────────────────────────
        printStep("1. Setting up Gallery");
        MuseumGallery gallery = new MuseumGallery("G01", "Ancient Civilisations", "West Wing, Floor 2");
        System.out.println("  Created: " + gallery);

        // ── 2. Create exhibits ─────────────────────────────────────
        printStep("2. Creating Exhibits");
        PaintingExhibit mona = new PaintingExhibit(101, "Starry Night Replica", "Van Gogh", "Oil on canvas", "1889");
        DisplayCaseExhibit vase = new DisplayCaseExhibit(102, "Ming Dynasty Vase", "Porcelain artefact from 15th century", "1420", false);
        DisplayCaseExhibit tablet = new DisplayCaseExhibit(103, "Rosetta Stone Fragment", "Granite decree fragment", "196 BC", true);

        gallery.addExhibit(mona);
        gallery.addExhibit(vase);
        gallery.addExhibit(tablet);

        // ── 3. Create monitoring devices ───────────────────────────
        printStep("3. Creating Monitoring Devices");
        MotionSensor motionSensor1 = new MotionSensor(1, "MS-NorthWall", 3.5f, "HIGH");
        GlassVibrationSensor vibSensor1 = new GlassVibrationSensor(2, "VS-VaseCase", 5.0f, "MEDIUM");
        GlassVibrationSensor vibSensor2 = new GlassVibrationSensor(3, "VS-TabletCase", 4.0f, "HIGH");
        ClimateSensor climateSensor = new ClimateSensor(4, "CS-MainRoom", 18f, 24f, 40f, 60f);

        // ── 4. Assign devices to exhibits ──────────────────────────
        printStep("4. Assigning Devices to Exhibits");
        mona.assignDevice(motionSensor1);
        vase.assignDevice(vibSensor1);
        vase.assignDevice(climateSensor);
        tablet.assignDevice(vibSensor2);

        // ── 5. Run self-checks ─────────────────────────────────────
        printStep("5. Running Device Self-Checks");
        motionSensor1.runSelfCheck();
        vibSensor1.runSelfCheck();
        vibSensor2.runSelfCheck();
        climateSensor.runSelfCheck();

        // ── 6. Set up the coordination hub ────────────────────────
        printStep("6. Setting Up Security Coordination Hub");
        SecurityCoordinationHub hub = new SecurityCoordinationHub();

        AlarmPanel panel = new AlarmPanel(1, "Main Entrance", "Zone-A");
        CuratorConsole console = new CuratorConsole(1, "Dr. Eleanor Walsh");
        GuardUnit guard1 = new GuardUnit(1, "Officer Brennan", "East Corridor");
        GuardUnit guard2 = new GuardUnit(2, "Officer Kelly", "West Wing");

        hub.registerEndpoint(panel);
        hub.registerEndpoint(console);
        hub.registerEndpoint(guard1);
        hub.registerEndpoint(guard2);

        hub.registerExhibit(mona);
        hub.registerExhibit(vase);
        hub.registerExhibit(tablet);

        console.connectExhibit(mona);
        console.connectExhibit(vase);
        console.connectExhibit(tablet);

        // ── 7. Check normal status ─────────────────────────────────
        printStep("7. Displaying Normal Status");
        panel.displayStatus();
        console.displayStatus();
        guard1.displayStatus();
        guard2.displayStatus();

        // ── 8. Simulate WARNING event — climate issue ──────────────
        printHeader("SCENARIO A: Climate Warning (Humidity spike near vase)");
        System.out.println("\n  > Humidity rises to 78% — outside safe range for artefacts...");
        climateSensor.setReadings(22f, 78f); // temperature fine, humidity too high
        hub.processDeviceReport(climateSensor);

        // ── 9. Simulate CRITICAL event — motion detected ───────────
        printHeader("SCENARIO B: Critical Incident (Motion detected near painting)");
        System.out.println("\n  > Unexpected movement detected near 'Starry Night Replica'...");
        motionSensor1.triggerMotion();
        hub.processDeviceReport(motionSensor1);

        // ── 10. Guard responds ─────────────────────────────────────
        printStep("10. Guard Confirms Attendance");
        Thread.sleep(100); // small pause for realism
        List<museum.core.Incident> incidents = hub.getIncidentHistory();
        if (!incidents.isEmpty()) {
            museum.core.Incident latest = incidents.get(incidents.size() - 1);
            guard1.confirmAttendance(latest.getIncidentId());
        }

        // ── 11. Simulate vibration on display case ─────────────────
        printHeader("SCENARIO C: Glass Vibration on Ming Vase");
        System.out.println("\n  > Vibration detected on vase display case — level 6.2 (threshold: 5.0)...");
        vibSensor1.recordVibration(6.2f);
        hub.processDeviceReport(vibSensor1);

        // ── 12. Silence alarm, escalate, close incidents ───────────
        printStep("12. Response Actions");
        panel.silenceTone();

        incidents = hub.getIncidentHistory();
        if (incidents.size() >= 2) {
            incidents.get(1).escalate();
            incidents.get(1).closeIncident("Guard investigated — false alarm, maintenance worker.");
        }

        // ── 13. Curator requests exhibit summary ───────────────────
        printStep("13. Curator Reviews Exhibit");
        System.out.println(console.requestExhibitSummary(102));

        // ── 14. Gallery active incidents ───────────────────────────
        printStep("14. Gallery Active Incidents");
        List<museum.core.Incident> active = gallery.getActiveIncidents();
        System.out.println("  Active (non-closed) incidents in gallery: " + active.size());
        for (museum.core.Incident i : active) {
            System.out.println("  > " + i.getSummary());
        }

        // ── 15. Full incident history ──────────────────────────────
        hub.printIncidentHistory();

        // ── 16. Final status ───────────────────────────────────────
        printHeader("FINAL SYSTEM STATUS");
        System.out.println("\n  Gallery: " + gallery);
        System.out.println("\n  Exhibits:");
        for (Exhibit e : gallery.getExhibits()) {
            System.out.println("    " + e);
        }
        System.out.println("\n  Devices:");
        for (Exhibit e : gallery.getExhibits()) {
            for (museum.devices.MonitoringDevice d : e.getDeviceList()) {
                System.out.println("    " + d);
            }
        }
        System.out.println("\n  Guard Units:");
        guard1.displayStatus();
        guard2.displayStatus();

        printHeader("SIMULATION COMPLETE");
    }
}
