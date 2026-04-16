package museum.endpoints;

import museum.core.Incident;

public interface AlertEndpoint {
    void receiveAlert(Incident incident);
    void displayStatus();
}
