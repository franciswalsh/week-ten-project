package com.company;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Creator {

    public static void main(String[] args) {

	// write your code here
        Creator creator = new Creator();
        creator.createWorkOrders();
    }

    public void createWorkOrders() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("What is your name?");
        String nameOfSender = scanner.nextLine();
        System.out.println("Please describe your work order");
        String descriptionOfWorkOrder = scanner.nextLine();
        int idForOrder = (int) (Math.random() * 100000);
        Status statusOfOrder = Status.INITIAL;

        WorkOrder workOrder = new WorkOrder(idForOrder,  descriptionOfWorkOrder, nameOfSender, statusOfOrder );


        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(workOrder);
            File file = new File(workOrder.getId() + ".json");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.close();
        }
        catch (JsonProcessingException ex){
            System.out.println("Json processing exception");
        }
        catch (IOException ex){
            ex.printStackTrace();
        }

        System.out.println(workOrder.getId());
        System.out.println(workOrder.getSenderName());
        System.out.println(workOrder.getStatus());
        System.out.println(workOrder.getDescription());
    }
}
