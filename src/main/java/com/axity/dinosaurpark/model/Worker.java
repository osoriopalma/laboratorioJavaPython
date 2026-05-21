package com.axity.dinosaurpark.model;

public class Worker {

    private final String name;
    private final WorkerRole role;
    private final double dailySalary;

    public Worker(String name, WorkerRole role, double dailySalary) {
        this.name = name;
        this.role = role;
        this.dailySalary = dailySalary;
    }

    public String getName() {
        return name;
    }

    public WorkerRole getRole() {
        return role;
    }

    public double getDailySalary() {
        return dailySalary;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "name='" + name + '\'' +
                ", role=" + role +
                ", dailySalary=" + dailySalary +
                '}';
    }

    
}
