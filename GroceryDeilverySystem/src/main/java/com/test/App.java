package com.test;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Data
@Builder
@AllArgsConstructor
class Item {
    private String brand;
    private String category;
    private int price;
    private int remainingInventory;

    public synchronized void updateInventory(int quantity) {
        remainingInventory += quantity;
        if (remainingInventory < 0) {
            remainingInventory = 0;
        }
    }
    

}

@Data
@Builder
@AllArgsConstructor
class User {
    private String name;
    private String address;
    private int walletAmount;
    @Default
    private Map<Item, Integer> cart = new HashMap<>();

    public String getCartDetails() {
        StringBuilder cartDetails = new StringBuilder();
        cartDetails.append("Cart details for ").append(name).append(":\n");

        if (cart.isEmpty()) {
            cartDetails.append("Empty Cart\n");
        } else {
            for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
                Item item = entry.getKey();
                int quantity = entry.getValue();
                cartDetails.append(item.getBrand())
                        .append(" - ")
                        .append(item.getCategory())
                        .append(" -> ")
                        .append(quantity)
                        .append(" -> Total Price: ")
                        .append(quantity * item.getPrice())
                        .append("\n");
            }
        }

        return cartDetails.toString();
    }

    public void addToCart(Item item, int quantity) {
       int availableQuantity = item.getRemainingInventory();

        if (availableQuantity >= quantity) {
            cart.put(item, cart.getOrDefault(item, 0) + quantity);
            System.out.println("Added to cart successfully");
        } else {
            System.out.println("Failed to add to cart. Insufficient quantity in inventory.");
        }
    }

    public void removeFromCart(Item item, int quantity) {
        cart.put(item, cart.getOrDefault(item, 0) - quantity);
        if (cart.get(item) <= 0) {
            cart.remove(item);
        }
    }

    public void checkout() {
        int totalAmount = cart.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();

        if (totalAmount > walletAmount) {
            System.out.println("Checkout failed. Insufficient funds.");
        } else {
            for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
                Item item = entry.getKey();
                int quantity = entry.getValue();
                item.updateInventory(-quantity); // Reduce quantity from inventory
            }
            walletAmount -= totalAmount;
            System.out.println("Successful checkout, new wallet amount: " + walletAmount);
            cart.clear();
        }
    }
}

@Data
@Builder
@AllArgsConstructor
class Inventory {
    @Default
    private Map<String, Map<String, Item>> items = new HashMap<>();

    public Inventory() {
        this.items = new HashMap<>();
    }

    public void addItem(String category, String brand, int price) {
        items.computeIfAbsent(brand, k -> new HashMap<>())
                .computeIfAbsent(category, k -> new Item(brand, category, price, 0))
                .updateInventory(0);
    }

    public void addInventory(String category, String brand, int quantity) {
        items.computeIfAbsent(brand, k -> new HashMap<>())
                .computeIfAbsent(category, k -> new Item(brand, category, 0, 0))
                .updateInventory(quantity);
    }

    public List<Item> searchItems(String brand, String category) {
        if (items.containsKey(brand) && items.get(brand).containsKey(category)) {
            return Collections.singletonList(items.get(brand).get(category));
        }
        return Collections.emptyList();
    }

    public List<Item> searchItems(String brand, String category, int price) {
        return items.values().stream()
                .flatMap(categoryMap -> categoryMap.values().stream())
                .filter(item -> item.getBrand().equals(brand) && item.getCategory().equals(category) && item.getPrice() == price)
                .collect(Collectors.toList());
    }

    public List<Item> searchItems(List<String> brands,String category) {
        List<Item> searchedItems = new ArrayList<>();
        for(String brand: brands){
            searchedItems.addAll(items.getOrDefault(brand, Collections.emptyMap())
                .values()
                .stream().filter(categoryMap -> categoryMap.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList()));
        }
       return searchedItems;
    }

    public List<Item> searchItems(List<String> brands,List<String> categories) {
        List<Item> searchedItems = new ArrayList<>();
        for(String brand: brands){
            searchedItems.addAll(items.getOrDefault(brand, Collections.emptyMap())
                .values()
                .stream().filter(categoryMap ->  categories.contains(categoryMap.getCategory()))
                .collect(Collectors.toList()));
        }
       return searchedItems;
    }

    public List<Item> searchItems(List<String> brands) {
        List<Item> searchedItems = new ArrayList<>();
        for(String brand: brands){
            searchedItems.addAll(items.getOrDefault(brand, Collections.emptyMap())
                .values()
                .stream()
                .collect(Collectors.toList()));
        }
       return searchedItems;
    }

    public List<Item> searchItems(String brand, int price) {
        return items.values().stream()
                .flatMap(categoryMap -> categoryMap.values().stream())
                .filter(item -> item.getBrand().equals(brand) && item.getPrice() == price)
                .collect(Collectors.toList());
    }

    public void printItems(List<Item> items){
        
        for(Item i:items){
            System.out.println(i.getBrand() + " -> "+i.getCategory() + " -> "+i.getPrice()+" -> "+i.getRemainingInventory());
        }
    }
}

public class LibraryManagement {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();

        inventory.addItem("Milk", "Amul", 100);
        inventory.addItem("Curd", "Amul", 50);
        inventory.addItem("Milk", "Nestle", 60);
        inventory.addItem("Curd", "Nestle", 90);

        inventory.addInventory("Milk", "Amul", 10);
        inventory.addInventory("Milk", "Nestle", 15);
        inventory.addInventory("Curd", "Nestle", 10);
        inventory.addInventory("Milk", "Amul", 10);
        inventory.addInventory("Curd", "Amul", 5);

        for(Map.Entry<String,Map<String,Item>> i: inventory.getItems().entrySet()){
            for(Map.Entry<String,Item> j: i.getValue().entrySet()){
                System.out.println(i.getKey() + " -> " + j.getKey() + " -> " + j.getValue().getRemainingInventory());
            }
        }

        User user1 = User.builder().name("Johnny").address("Flipkart Bellandur, Bangalore 560068").walletAmount(600).build();
        User user2 = User.builder().name("Naruto").address("BTM Layout, Bengaluru, 560042").walletAmount(500).build();
        User user3 =   User.builder().name("Goku").address("Vijay Nagar, Indore, MP").walletAmount(3000).build();
        
        user1.addToCart(inventory.searchItems("Nestle", "Milk").get(0), 5);
        inventory.searchItems("Nestle", "Milk").get(0).updateInventory(5);

        user2.addToCart(inventory.searchItems("Nestle", "Milk").get(0), 10);
        
        System.out.println(user1.getCartDetails());
        System.out.println(user2.getCartDetails());
        System.out.println(user3.getCartDetails());

        //user3.addToCart(inventory.searchItems("Curd", "Nestle").get(0), 10);

        inventory.printItems(inventory.searchItems(Collections.singletonList("Nestle")));
        inventory.printItems(inventory.searchItems(Arrays.asList("Nestle","Amul"),"Curd"));
        inventory.printItems(inventory.searchItems(Arrays.asList("Nestle"),Arrays.asList("Curd","Milk","Paneer")));

        user1.checkout();
        user2.checkout();
        user3.checkout();
    }
}

