import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class DispatchCenter {
    Queue<Passenger> passengerQueue = new LinkedList<>();
    Queue<Taxi> taxiQueue = new LinkedList<>();
    List<Taxi> assignedTaxis = new ArrayList<>();

    public void registerTaxi(Taxi taxi) {
        taxi.dispatch = this;
    }

    public void assignTaxi(Passenger passenger) {
        Taxi firstAvailable = taxiQueue.poll();
        Passenger firstPassenger = passengerQueue.poll();
        if (firstAvailable != null && firstPassenger != null) {
            System.out.printf("Dispatch assigned Taxi %s to passenger %s.%n", firstAvailable.taxiId, firstPassenger.name);
        }
    }
    // Stores the queue for taxi assignment through request ride
}

class Taxi {
    String taxiId;
    boolean isAvailable;
    DispatchCenter dispatch;
    
    public Taxi(String taxiId) {
        this.taxiId = taxiId;
    }

    public void setAvailable(boolean availability) {
        this.isAvailable = true;
        dispatch.taxiQueue.add(this);
        System.out.printf("Taxi %s is now available.%n", this.taxiId);
        Passenger waiting = dispatch.passengerQueue.peek();
        if (waiting != null) {
            dispatch.assignTaxi(waiting);
        }

    }

    public void respondToRide(boolean response) {
        Passenger passenger = dispatch.passengerQueue.peek();

        if (response == true) {
            dispatch.passengerQueue.poll();
            dispatch.taxiQueue.poll();
            System.out.printf("Taxi accepted the ride to %s.%n", taxiId, passenger.destination);
        } else {
            dispatch.passengerQueue.poll();
            System.out.printf("Taxi %s rejected the ride to %s. Searching for another taxi...%n", taxiId, passenger.destination);
        }
    }
}

class Passenger {
    String name;
    String destination;

    public Passenger(String name) {
        this.name = name;
    }

    public void requestRide(String destination, DispatchCenter center) {
        this.destination = destination;
        center.passengerQueue.add(this);
        System.out.printf("Passenger %s requested a ride to %s.%n", this.name, this.destination);
    }
}
