package com.mycompany.rideapp;

import javax.swing.JTextArea;

public class Driver implements RideDetails {

    // 🔹 NEW (from database)
    public int driverId;

    // 🔹 Existing
    public String name;
    public String destination;
    public Vehicle vehicle;
    public int fare;

    // 🔹 Old constructor (keep it if used elsewhere)
    public Driver(String name, Vehicle vehicle, String destination, int fare) {
        this.name = name;
        this.vehicle = vehicle;
        this.destination = destination;
        this.fare = fare;
    }

    // 🔹 NEW constructor (USED BY DATABASE)
    public Driver(int driverId, String name, Vehicle vehicle, String destination, int fare) {
        this.driverId = driverId;
        this.name = name;
        this.vehicle = vehicle;
        this.destination = destination;
        this.fare = fare;
    }

    @Override
    public void showDetails(JTextArea area) {
        area.append("Driver Name: " + name + "\n");
        area.append("Vehicle: " + vehicle.brand + "\n");
        area.append("Passenger Capacity: " + vehicle.capacity + "\n");
        area.append("Destination: " + destination + "\n");
        area.append("Fare: Rs." + fare + "\n\n");
    }
}
