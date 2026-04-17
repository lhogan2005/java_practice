import java.util.LinkedList;
import java.util.Queue;

class DispatchCenter {
    Queue<Passenger> passengerQueue = new LinkedList<>();
    Queue<Taxi> taxiQueue = new LinkedList<>();

    public void registerTaxi(Taxi taxi) {
        taxi.dispatch = this;
    }

    public void assignTaxi(Passenger passenger, Taxi taxi) {
        if (passenger != null) {
            passenger.hasTaxi = true;
        }
        System.out.printf("Dispatch assigned Taxi %s to passenger %s.%n", taxi.taxiId, passenger.name);
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
        Passenger waiting = null;
        System.out.printf("Taxi %s is now available.%n", this.taxiId);
        Passenger[] passengers = dispatch.passengerQueue.toArray(new Passenger[0]);
        for (Passenger current : passengers) {
            if (current.hasTaxi == false) {
                waiting = current;
                break;
            }   
        }
        if (waiting != null) {
            dispatch.assignTaxi(waiting, this);
        }
    }

    public void respondToRide(boolean response) {
        Passenger passenger = dispatch.passengerQueue.peek();

        if (response == true) {
            dispatch.passengerQueue.poll();
            dispatch.taxiQueue.remove(this);
            this.isAvailable = false;
            System.out.printf("Taxi %s accepted the ride to %s.%n", this.taxiId, passenger.destination);
        } else {
            dispatch.taxiQueue.remove(this);
            passenger.hasTaxi = false;
            System.out.printf("Taxi %s rejected the ride to %s. Searching for another taxi...%n", taxiId, passenger.destination);
            if (!dispatch.passengerQueue.isEmpty()) {
                    Taxi nextTaxi = dispatch.taxiQueue.peek();
                    if (!dispatch.taxiQueue.isEmpty()) {
                        dispatch.assignTaxi(passenger, nextTaxi);
                    }
                }
            }
        }
    }

class Passenger {
    String name;
    String destination;
    boolean hasTaxi;

    public Passenger(String name) {
        this.name = name;
        this.hasTaxi = false;
    }

    public void requestRide(String destination, DispatchCenter center) {
        this.destination = destination;
        center.passengerQueue.add(this);
        System.out.printf("Passenger %s requested a ride to %s.%n", this.name, this.destination);
    }
}
