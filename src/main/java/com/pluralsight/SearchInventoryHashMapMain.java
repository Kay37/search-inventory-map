package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class SearchInventoryHashMapMain {

    public static void main(String[] args) {

        //TODO: Will be an app to manage and search the product inventory of a store using java collections.
        //*Creating a scanner so we can ask the user some questions
        Scanner userInput =
                new Scanner(System.in);

        //TODO: Create an initial inventory of at least 5 products.
        String fileName = ("inventory.csv");

        //*I completed this within the getInventory method

        //*Using getInventory method to load hardwareInventory
        HashMap<String, Product> inventory = loadInventory(fileName);

        //*Creating a variable to keep the menu looping
        boolean menuRunning = true;

        //*Looping the menu
        while (menuRunning) {

            //*Display the main menu and store the user's command
            System.out.println("Welcome to the Inventory Management System!\n");
            int userCommand = displayMainMenu(userInput);
            switch (userCommand) {
                case 1:
                    listInventory(inventory);
                    break;
                case 2:
                    System.out.println("Enter product name: ");
                    String nameSearch = userInput.nextLine();
                    searchName(inventory, nameSearch);
                    break;
                case 3:
                    System.out.println("Enter product description: ");
                    String descriptionSearch = userInput.nextLine();
                    searchDescription(inventory, descriptionSearch);
                    break;
                case 4:
                    System.out.println("Minimum quantity in stock to search for: ");
                    int quantitySearch = userInput.nextInt();
                    searchMinQuantity(inventory, quantitySearch);
                    break;
                case 5:
                    System.out.println("Enter product minimum price: ");
                    double minProductPrice = userInput.nextDouble();
                    System.out.println("Enter product maximum price: ");
                    double maxProductPrice = userInput.nextDouble();
                    if (minProductPrice > maxProductPrice) {
                        System.out.println("Minimum price is greater than maximum price. Try again");
                    } else {
                        searchPrice(inventory, minProductPrice, maxProductPrice);
                    }

                    break;
                case 6:
                    addProduct(userInput, inventory, fileName);
                    break;
                case 7:
                    menuRunning = false;
                    break;
                default:
                    System.out.println("=== Invalid command entered. Try again ===");
            }
        }

    }

    //TODO: listInventory method - list the inventory, alphabetically sorted.
    public static void listInventory(HashMap<String, Product> hardwareInventory) {
        ArrayList<Product> sortedInventory = new ArrayList<>(hardwareInventory.values());
        Collections.sort(sortedInventory, Comparator.comparing(Product::getProductName));
        for (Product product : sortedInventory) {
            System.out.printf("%n%s - $%.2f%n", product.getProductName(), product.getProductPrice());
        }

    }

    //TODO: searchName method - search the inventory for a name
    public static void searchName(HashMap<String, Product> hardwareInventory, String productName) {
        Product match = hardwareInventory.get(productName.toLowerCase());
        if (match == null) {
            System.out.println("Product " + productName + " does not exist. Try again");
        } else {
            System.out.println("Product found for search: " + "'" + productName + "'");
            System.out.println(match.getProductName() + " - $" + match.getProductPrice());
        }
    }

    //TODO: searchDescription method - search the inventory for a description
    public static void searchDescription(HashMap<String, Product> hardwareInventory, String productDescription) {
        boolean found = false;
        for (Product product : hardwareInventory.values()) {
            if (product.getProductDescription().contains(productDescription)) {
                found = true;
                System.out.printf("%n%s - $%.2f%n", product.getProductName(), product.getProductPrice());
            }
        }
        if (!found) {
            System.out.println("No product with that description found.\n");
        }
    }

    //TODO: searchQuantity method - search the inventory for products with at least this quantity
    public static void searchMinQuantity(HashMap<String, Product> hardwareInventory, int minQuantity) {
        boolean found = false;
        for (Product product : hardwareInventory.values()) {
            if (product.getProductQuantity() >= minQuantity) {
                found = true;
                System.out.printf("%n%s - $%.2f%n", product.getProductName(), product.getProductPrice());
            }
        }
        if (!found) {
            System.out.println("No product with that quantity or greater found.\n");
        }
    }

    //TODO: searchPrice method - search the inventory at or under a price
    public static void searchPrice(HashMap<String, Product> hardwareInventory, double minProductPrice, double maxProductPrice) {
        boolean found = false;
        for (Product product : hardwareInventory.values()) {
            if (product.getProductPrice() <= maxProductPrice && product.getProductPrice() >= minProductPrice) {
                found = true;
                System.out.printf("%n%s - $%.2f%n", product.getProductName(), product.getProductPrice());
            }
        }
        if (!found) {
            System.out.println("No products in that price range found.\n");
        }
    }

    //TODO: addProduct method - add product to the inventory
    public static void addProduct(Scanner userInput, HashMap<String, Product> hardwareInventory, String fileName) {
        System.out.println("Enter product name: ");
        String productName = userInput.nextLine();
        System.out.println("Enter product description: ");
        String productDescription = userInput.nextLine();
        System.out.println("Enter product quantity: ");
        int productQuantity = userInput.nextInt();
        userInput.nextLine();
        System.out.println("Enter product price: ");
        double productPrice = userInput.nextDouble();
        userInput.nextLine();

        String productKey = productName.toLowerCase();

        if (hardwareInventory.containsKey(productKey)) {
            System.out.println("There is already a product with the name " + productKey + ".\n");
        } else {
            hardwareInventory.put(productKey, new Product(productName, productDescription, productQuantity, productPrice));
            try {
                BufferedWriter eWriter = new BufferedWriter(new FileWriter(fileName, true));
                eWriter.write(productName + "|" + productDescription + "|" + productQuantity + "|" + productPrice + "\n");
                eWriter.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("There was an error while writing to the file.\n");
            }
        }


    }

    //TODO: displayMenu method - display user options for managing and searching inventory
    public static int displayMainMenu(Scanner userInput) {

        //? ask the user some questions
        System.out.println("""
                What would you like to do?
                    1. List the inventory
                    2. Search by name
                    3. Search by description
                    4. Search for items with at least X in stock
                    5. Search for items within a price range
                    6. Add to the inventory
                    7. Quit
                    Enter your command:""");
        //?store the user's answer / command
        int menuSelection = userInput.nextInt();
        userInput.nextLine();
        return menuSelection;
    }

    //TODO: getInventory() method - retrieving our inventory
    public static HashMap<String, Product> loadInventory(String fileName) {

        //! Using an array list to store our hardware inventory
        HashMap<String, Product> inventory = new HashMap<>();
//!
//!       //!Creating some hardware/tools
//!        //!Exercise says at least 5 initial products
//        Product product1 =
//                new Product("Hammer", "A hand tool with a heavy metal head", 25, 5.97);
//        Product product2 =
//                new Product("Hand Saw", "A hand tool with a toothed metal blade", 15, 8.62);
//        Product product3 =
//                new Product("3/8 in. Zinc-Plated Steel USS Nylon Lock Nut 100 pk", "Lock nut for securely fastening a bolt", 50, 9.99);
//        Product product4 =
//                new Product("20V Cordless Brushed Compact Drill", "Handheld power tool that spins bits to bore holes and drive screws.", 50, 115);
//        Product product5 =
//                new Product("20V Cordless Brushed Impact Driver", "Handheld power tool for driving long screws and bolts", 50, 115);
//
//        hardwareInventory.add(product1);
//        hardwareInventory.add(product2);
//        hardwareInventory.add(product3);
//        hardwareInventory.add(product4);
//        hardwareInventory.add(product5);
//        return hardwareInventory;
        //* ^ The code above was manual input into the ArrayList. Below will be code for using a fileReader to pull the
        //* info from inventory.csv or the fileName String.
        try {
            BufferedReader eReader = new BufferedReader(new FileReader(fileName));

            String csvLine;
            eReader.readLine();
            while ((csvLine = eReader.readLine()) != null) {
                String[] inventoryFileArray = csvLine.split("\\|");
                String productName = inventoryFileArray[0];
                String productDescription = inventoryFileArray[1];
                int productQuantity = Integer.parseInt(inventoryFileArray[2]);
                double productPrice = Double.parseDouble(inventoryFileArray[3]);
                Product product = new Product(productName, productDescription, productQuantity, productPrice);
                inventory.put(product.getProductName().toLowerCase(), product);
            }
            eReader.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Something went wrong. Try again");
        }
        return inventory;
    }
}
