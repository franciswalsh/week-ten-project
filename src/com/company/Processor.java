package com.company;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.*;

/**
 * Created by franciswalsh on 8/1/17.
 */
public class Processor {

    Map<Status, Set<WorkOrder>> workOrders = new HashMap<>();
    Set<WorkOrder> intialOrders = new HashSet<>();
    Set<WorkOrder> assignedOrders = new HashSet<>();
    Set<WorkOrder> inProgressOrders = new HashSet<>();
    Set<WorkOrder> doneOrders = new HashSet<>();
    List<WorkOrder> allOrders = new ArrayList<>();

    public void processWorkOrders() {

        workOrders.put(Status.INITIAL, intialOrders);
        workOrders.put(Status.ASSIGNED, assignedOrders);
        workOrders.put(Status.IN_PROGRESS, inProgressOrders);
        workOrders.put(Status.DONE, doneOrders);

        Scanner scanner = new Scanner(System.in);
        String userInput = "";

        while (!userInput.equals("stop")) {
            userInput = scanner.nextLine();
            printMap(workOrders);
            moveIt();
            readIt();
            printMap(workOrders);
        }
    }

    private void moveIt() {
        // move work orders in map from one state to another
        Set<Status> statuses = workOrders.keySet();

        for (WorkOrder order : allOrders){
            if (order.getStatus() == Status.INITIAL){
                order.setStatus(Status.ASSIGNED);
                assignedOrders.add(order);
                intialOrders.remove(order);
            }
            else if (order.getStatus() == Status.ASSIGNED){
                order.setStatus(Status.IN_PROGRESS);
                inProgressOrders.add(order);
                assignedOrders.remove(order);

            }
            else if (order.getStatus() == Status.IN_PROGRESS){
                order.setStatus(Status.DONE);
                doneOrders.add(order);
                inProgressOrders.remove(order);
            }
            else {
                System.out.println("this many orders are done");
            }
        }
    }

    private void readIt() {
        // read the json files into WorkOrders and put in map
        List<WorkOrder> newOrders = new ArrayList<>();
        File fileRead = new File(".");
        for (File f : fileRead.listFiles()){
            if (f.getName().endsWith(".json")){
                File file = new File(String.valueOf(f));
                try {
                    Scanner fileScanner = new Scanner(file);
                    ObjectMapper mapper = new ObjectMapper();
                    WorkOrder wo = mapper.readValue(file, WorkOrder.class);
                    newOrders.add(wo);
                    allOrders.add(wo);
                    f.delete();

                }
                catch (IOException ex){
                    System.out.println("Could not find file *" + String.valueOf(f) + "*");
                    ex.printStackTrace();
                }
            }
        }
        for (WorkOrder wo : newOrders){
            if (wo.getStatus() == Status.INITIAL){
                intialOrders.add(wo);
            }
            else if (wo.getStatus() == Status.ASSIGNED){
                assignedOrders.add(wo);
            }
            else if (wo.getStatus() == Status.IN_PROGRESS){
                inProgressOrders.add(wo);
            }
            else {
                doneOrders.add(wo);
            }
        }

    }

    public static void main(String args[]) {
        Processor processor = new Processor();
        processor.processWorkOrders();
    }

    public static void printMap(Map<Status, Set<WorkOrder>> map){
        Set<Status> statuses = map.keySet();
        for (Status status : statuses){
            List<String> orders = new ArrayList<>();
            for (WorkOrder workOrder : map.get(status)){
                orders.add(workOrder.getSenderName() + "'s order: " + workOrder.getDescription());
            }
            System.out.println(status.toString() + " orders: " + orders);
        }
    }
}
