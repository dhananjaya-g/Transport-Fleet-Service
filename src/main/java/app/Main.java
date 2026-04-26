package app;
import fleet.FleetManager;
import vehicles.Vehicle;
import vehicles.land.vehicle.Car;
import vehicles.land.vehicle.Truck;
import vehicles.land.vehicle.Bus;
import vehicles.air.vehicle.Airplane;
import vehicles.water.vehicle.CargoShip;
import exceptions.*;
import io.CsvSerializer;
import java.util.*;
import java.io.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final FleetManager fm = new FleetManager();
    private static final CsvSerializer csv = new CsvSerializer();

    //assuming all the fields related to vehicle need to be provided by user
    public static void main(String[] args) {
        seedDemo();
        while (true) {
            printMenu();
            int choice = readInt("Choose option: ");
            try {
                switch (choice) {
                    case 1 -> addVehicleFlow();
                    case 2 -> removeVehicleFlow();
                    case 3 -> generateReportFlow();
                    case 4 -> saveFleetFlow();
                    case 5 -> loadFleetFlow();
                    case 6 -> listVehiclesFlow();
                    case 7 -> sortViews();
                    case 8 -> { System.out.println("Bye!"); return; }
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("=== Welcome to Aditri's Transport fleet Service 2 ===");
        System.out.println("1. Add Vehicle");
        System.out.println("2. Remove Vehicle");
        System.out.println("3. Generate Report");
        System.out.println("4. Save Fleet (CSV)");
        System.out.println("5. Load Fleet (CSV)");
        System.out.println("6. List All Vehicles");
        System.out.println("7. Sort by (Efficiency/Model/MaxSpeed)");
        System.out.println("8. Exit");
    }

    private static void seedDemo() {
        try {
            Car c = new Car("C1", "Swift", 150, 4, 5); ; c.boardPassengers(2);
            Truck t = new Truck("T1", "Tata407", 110, 6, 5000); t.refuel(60); t.loadCargo(1200);
            Bus b = new Bus("B1", "Volvo", 90, 6, 45, 600); b.refuel(80); b.boardPassengers(30); b.loadCargo(200);
            Airplane a = new Airplane("A1", "A320", 850, 12000, 180, 10000); a.refuel(5000); a.boardPassengers(120); a.loadCargo(3000);
            CargoShip s = new CargoShip("S1", "Evergreen", 60, false, 80000); s.refuel(2000); s.loadCargo(12000);
            fm.addVehicle(c); fm.addVehicle(t); fm.addVehicle(b); fm.addVehicle(a); fm.addVehicle(s);
        } catch (Exception e) { System.out.println("Demo seed failed: " + e.getMessage()); }
    }


    private static void addVehicleFlow() throws InvalidOperationException {
        System.out.println("Add which type? (Car/Truck/Bus/Airplane/CargoShip)");
        String type = sc.nextLine().trim().toLowerCase(java.util.Locale.ROOT);

        switch (type) {
            case "car" -> {
                String id = readStr("id: ").trim();
                String model = readStr("model: ").trim();
                if (id.isEmpty() || model.isEmpty())
                    throw new InvalidOperationException("ID and model cannot be blank.");

                double maxSpeed = readDouble("maxSpeed km/h: ");
                int wheels = readInt("numWheels: ");
                int paxCap = readInt("passengerCapacity: ");

                Car c = new Car(id, model, maxSpeed, wheels, paxCap);
                fm.addVehicle(c); // will throw if ID duplicates (case-insensitive) and will update model sets
                System.out.println("Added Car " + id);
            }

            case "truck" -> {
                String id = readStr("id: ").trim();
                String model = readStr("model: ").trim();
                if (id.isEmpty() || model.isEmpty())
                    throw new InvalidOperationException("ID and model cannot be blank.");

                double maxSpeed = readDouble("maxSpeed km/h: ");
                int wheels = readInt("numWheels: ");
                double cap = readDouble("cargoCapacity kg: ");

                Truck t = new Truck(id, model, maxSpeed, wheels, cap);
                fm.addVehicle(t);
                System.out.println("Added Truck " + id);
            }

            case "bus" -> {
                String id = readStr("id: ").trim();
                String model = readStr("model: ").trim();
                if (id.isEmpty() || model.isEmpty())
                    throw new InvalidOperationException("ID and model cannot be blank.");

                double maxSpeed = readDouble("maxSpeed km/h: ");
                int wheels = readInt("numWheels: ");
                int paxCap = readInt("passengerCapacity: ");
                double cargoCap = readDouble("cargoCapacity kg: ");

                Bus b = new Bus(id, model, maxSpeed, wheels, paxCap, cargoCap);
                fm.addVehicle(b);
                System.out.println("Added Bus " + id);
            }

            case "airplane" -> {
                String id = readStr("id: ").trim();
                String model = readStr("model: ").trim();
                if (id.isEmpty() || model.isEmpty())
                    throw new InvalidOperationException("ID and model cannot be blank.");

                double maxSpeed = readDouble("maxSpeed km/h: ");
                double alt = readDouble("maxAltitude m: ");
                int paxCap = readInt("passengerCapacity: ");
                double cargoCap = readDouble("cargoCapacity kg: ");

                Airplane a = new Airplane(id, model, maxSpeed, alt, paxCap, cargoCap);
                fm.addVehicle(a);
                System.out.println("Added Airplane " + id);
            }

            case "cargoship" -> {
                String id = readStr("id: ").trim();
                String model = readStr("model: ").trim();
                if (id.isEmpty() || model.isEmpty())
                    throw new InvalidOperationException("ID and model cannot be blank.");

                double maxSpeed = readDouble("maxSpeed km/h: ");
                boolean sail = readBool("hasSail (true/false): ");
                double cargoCap = readDouble("cargoCapacity kg: ");

                CargoShip s = new CargoShip(id, model, maxSpeed, sail, cargoCap);
                fm.addVehicle(s);
                System.out.println("Added CargoShip " + id);
            }

            default -> throw new InvalidOperationException("Unknown type: " + type);
        }
    }


    private static void removeVehicleFlow() throws InvalidOperationException {
        String raw = readStr("Enter id to remove: ");
        String id = (raw == null) ? "" : raw.trim();
        if (id.isEmpty()) {
            System.out.println("ID cannot be blank.");
            return;
        }
        boolean removed = fm.removeVehicle(id); // your FM returns boolean
        if (removed) {
            System.out.println("Removed vehicle(s) with id (case-insensitive): " + id);
        } else {
            System.out.println("No vehicle found with id: " + id);
        }
    }

    private static void listVehiclesFlow() {
        for (Vehicle v : fm.getFleet()) v.displayInfo();
    }

    private static void generateReportFlow() {
        System.out.println(fm.generateSummary());
    }

    private static void saveFleetFlow() throws IOException {
        String p = readStr("CSV path: ");
        csv.saveToFile(fm.getFleet(), p);
        System.out.println("Saved to " + p);
        // removed fm.clear(); seedDemo(); since FleetManager doesn't expose clear()
    }

    private static void loadFleetFlow() throws IOException, InvalidOperationException {
        String p = readStr("CSV path: ");
        var list = csv.loadFromFile(p);

        // remove current vehicles (case-insensitive remove inside FM)
        for (Vehicle v : new ArrayList<>(fm.getFleet())) {
            try { fm.removeVehicle(v.getId()); } catch (Exception ignored) {}
        }
        // add loaded vehicles (FM handles duplicate IDs)
        for (Vehicle v : list) fm.addVehicle(v);

        System.out.println("Loaded " + list.size() + " vehicles.");
    }

    private static void sortViews() {
        System.out.print("Sort by (model/speed/eff) : ");
        String s = sc.nextLine().trim().toLowerCase();
        switch (s) {
            case "model":
                fm.sortByModel().forEach(Vehicle::displayInfo);
                break;
            case "speed":
                fm.sortByMaxSpeed().forEach(Vehicle::displayInfo);
                break;
            case "eff":
                fm.sortByEfficiency().forEach(Vehicle::displayInfo);
                break;
            default:
                System.out.println("Unknown sort key.");
        }
    }

    private static String readStr(String p) { System.out.print(p); return sc.nextLine().trim(); }
    private static int readInt(String p) {
        while (true) { try { System.out.print(p); return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Enter integer."); } }
    }
    private static double readDouble(String p) {
        while (true) { try { System.out.print(p); return Double.parseDouble(sc.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Enter number."); } }
    }
    private static boolean readBool(String p) {
        while (true) {
            System.out.print(p); String s = sc.nextLine().trim().toLowerCase();
            if (s.matches("^(true|t|yes|y)$")) return true;
            if (s.matches("^(false|f|no|n)$")) return false;
            System.out.println("Enter true/false or yes/no.");
        }
    }
}
