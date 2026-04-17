import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public double distanceTo(Coordinate other) {
        double distance = Math.sqrt(Math.pow((other.x - this.x), 2) + Math.pow((other.y - this.y), 2));
        return distance;
    }
}

class CellTower {
    private String id;
    private Coordinate position;
    private String operator;
    private int radius;

    public CellTower(String id, Coordinate position, String operator, int radius) {
        this.id = id;
        this.position = position;
        this.operator = operator;
        this.radius = radius;
    }

    public String getId() {
        return this.id;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String newOp) {
        this.operator = newOp;
    }

    public Coordinate getPosition() {
        return this.position;
    }

    public int getRadius() {
        return this.radius;
    }
}
class MobileNetwork {
    private String name;
    private ArrayList<CellTower> towers;

    public ArrayList<CellTower> getTowers() {
        return this.towers;
    }

    public void addTower(CellTower tower) {
        this.towers.add(tower);
    }

    public void removeTower(CellTower tower) {
        this.towers.remove(tower);
    }

    public MobileNetwork(String name) {
        this.name = name;
        this.towers = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }
}
class Client {
    private long number;
    private Coordinate position;
    private MobileNetwork operator;
    private CellTower tower;

    public Client(long number, Coordinate position, MobileNetwork operator, CellTower tower) {
        this.number = number;
        this.position = position;
        this.operator = operator;
        this.tower = tower;
    }

    public long getNumber() {
        return this.number;
    }

    public CellTower getTower() {
        return this.tower;
    }

    public MobileNetwork getOperator() {
        return this.operator;
    }

    public void setOperator(MobileNetwork operator) {
        this.operator = operator;
    }

    public void setTower(CellTower newTower) {
        this.tower = newTower;
    }

    public Coordinate getPosition() {
        return this.position;
    }

    public void setPosition(Coordinate newPos) {
        this.position = newPos;
    }
}

class Controller {
    private ArrayList<MobileNetwork> operators;
    private ArrayList<Client> clients;
    private ArrayList<CellTower> towers;

    private CellTower findNearest(Coordinate pos, Client targetClient) {
        CellTower currentNearest = null;
        double lowestDistance = Double.MAX_VALUE;

        for (CellTower tower : this.towers) {
            if (!targetClient.getOperator().getTowers().contains(tower)) {
                continue;
            }

            double dist = tower.getPosition().distanceTo(pos);

            if (dist > tower.getRadius()) {
                continue;
            }

            if (currentNearest == null || dist < lowestDistance) {
                currentNearest = tower;
                lowestDistance = dist;
            } else if (dist == lowestDistance) {
                int currentDeviceCount = 0;
                int newDeviceCount = 0;


                for (Client client : this.clients) {
                    if (client.getTower() != null) {
                        if (client.getTower().getId().equals(currentNearest.getId())) {
                            currentDeviceCount++;
                        } else if (client.getTower().getId().equals(tower.getId())) {
                            newDeviceCount++;
                        }
                    }
                }

                if (newDeviceCount < currentDeviceCount) {
                    currentNearest = tower;
                    lowestDistance = dist;
                }
            }
        }

        return currentNearest;
    }

    public Controller() {
        this.operators = new ArrayList<>();
        this.clients = new ArrayList<>();
        this.towers = new ArrayList<>();
    }

    public boolean processCommand(String command, Scanner stringScanner) {
        switch (command) {
            case "ADD_TOWER": {
                String towerId = stringScanner.nextLine();
                int x = Integer.parseInt(stringScanner.nextLine());
                int y = Integer.parseInt(stringScanner.nextLine());
                int coverageRadius = Integer.parseInt(stringScanner.nextLine());

                Coordinate position = new Coordinate(x, y);

                CellTower newTower = new CellTower(towerId, position, null, coverageRadius);
                this.towers.add(newTower);
                return true;
            }

            case "REGISTER_OPERATOR_TOWER": {
                String operatorName = stringScanner.nextLine();
                String towerId = stringScanner.nextLine();


                CellTower selectedTower = null;

                for (CellTower tower : this.towers) {
                    if (tower.getId().equals(towerId)) {
                        tower.setOperator(operatorName);
                        selectedTower = tower;
                    }
                }

                for (MobileNetwork op : this.operators) {
                    if (op.getName().equals(operatorName)) {
                        op.addTower(selectedTower);
                    }
                }

                return true;
            }

            case "REMOVE_TOWER": {
                String towerId = stringScanner.nextLine();

                for (CellTower tower : this.towers) {
                    if (tower.getId().equals(towerId)) {
                        this.towers.remove(tower);
                        for (MobileNetwork op : this.operators) {
                            op.removeTower(tower);
                        }
                    }
                }

                for (Client client : this.clients) {
                    if (client.getTower() != null && client.getTower().getId().equals(towerId)) {
                        CellTower nearestTower = findNearest(client.getPosition(), client);
                        
                        client.setTower(nearestTower);
                    }
                }


                return true;
            }

            case "ADD_CLIENT": {
                Long phoneNumber = Long.parseLong(stringScanner.nextLine());
                String operatorName = stringScanner.nextLine();
                String x = stringScanner.nextLine();
                String y = stringScanner.nextLine();
                Coordinate clientPosition = new Coordinate(Integer.parseInt(x), Integer.parseInt(y));

                MobileNetwork operator = null;
                for (MobileNetwork op : this.operators) {
                    if (op.getName().equals(operatorName)) {
                        operator = op;
                    }
                }

                Client newClient = new Client(phoneNumber, clientPosition, operator, null);

                CellTower nearestTower = findNearest(clientPosition, newClient);

                newClient.setTower(nearestTower);

                clients.add(newClient);
                return true;
            }

            case "REMOVE_CLIENT": {
                long clientNumber = Long.parseLong(stringScanner.nextLine());

                for (Client client : this.clients) {
                    if (client.getNumber() == clientNumber) {
                        this.clients.remove(client);
                        return true;
                    }
                }

                return false;
            }

            case "MOVE_CLIENT": {
                long clientNumber = Long.parseLong(stringScanner.nextLine());
                int newX = Integer.parseInt(stringScanner.nextLine());
                int newY = Integer.parseInt(stringScanner.nextLine());
                Coordinate newPos = new Coordinate(newX, newY);

                for (Client client : this.clients) {
                    if (client.getNumber() == clientNumber) {
                        client.setPosition(newPos);
                        CellTower newTower = findNearest(newPos, client);
                        client.setTower(newTower);
                        return true;
                    }
                }

                return false;
            }

            case "ADD_OPERATOR": {
                String name = stringScanner.nextLine();

                MobileNetwork newNetwork = new MobileNetwork(name);

                operators.add(newNetwork);


                return true;
            }

            case "CHANGE_OPERATOR": {
                long clientNumber = Long.parseLong(stringScanner.nextLine());
                String newOperator = stringScanner.nextLine();


                MobileNetwork operator = null;
                for (MobileNetwork op : this.operators) {
                    if (op.getName().equals(newOperator)) {
                        operator = op;
                    }
                }

                for (Client client : this.clients) {
                    if (client.getNumber() == clientNumber) {

                        client.setOperator(operator);
                        CellTower newTower = findNearest(client.getPosition(), client);
                        client.setTower(newTower);
                    }
                }

                return true;
            }

            case "TOWER_CLIENT_COUNT": {
                String towerId = stringScanner.nextLine();
                int count = 0;

                for (Client client : this.clients) {
                    if (client.getTower() != null && client.getTower().getId().equals(towerId)) {
                        count++;
                    }
                }

                System.out.println(count);
                return true;
            }

            case "OPERATOR_SUBSCRIBER_COUNT": {
                String operatorName = stringScanner.nextLine();
                int count = 0;

                for (Client client : this.clients) {
                    if (client.getOperator().getName().equals(operatorName)) {
                        count++;
                    }
                }

                System.out.println(count);
                return true;
            }

            case "NO_SIGNAL_COUNT": {
                HashMap<String, Integer> noSignals = new HashMap<>();

                for (Client client : this.clients) {
                    if (client.getTower() == null) {
                        String operataorName = client.getOperator().getName();

                        if (noSignals.containsKey(operataorName)) {
                            int newCount = noSignals.get(operataorName) + 1;
                            noSignals.put(operataorName, newCount);
                        } else {

                            noSignals.put(operataorName, 1);
                        }
                    }
                }

                for (MobileNetwork operator : this.operators) {
                    System.out.println(operator.getName() + ": " + noSignals.getOrDefault(operator.getName(), 0) + " phones without signal.");
                }

                return true;
            }

            default: {
                System.out.println("Default - " + command);
                return false;
            }
        }
    }
}

public class NetworkSimulation {
    public static void main(String args[]) {
        Scanner stringScanner = new Scanner(System.in);
        String instruction;

        Controller controller = new Controller();

        while (stringScanner.hasNextLine()) {

            instruction = stringScanner.nextLine();

            Boolean success = controller.processCommand(instruction.trim(), stringScanner);
        }
    }
}