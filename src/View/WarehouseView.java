package View;

import Model.Item;

import java.util.List;
import java.util.Scanner;

public class WarehouseView {
    private Scanner scanner;

    public WarehouseView() {
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        System.out.println("1. Add Model.Item");
        System.out.println("2. Sort Items");
        System.out.println("3. Print Items");
        System.out.println("4. Use Model.Item");
        System.out.println("5. Exit");
    }

    public int getUserChoice() {
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayItems(List<Item> items) {
        for (Item item : items) {
            System.out.println(item);
        }
    }
}