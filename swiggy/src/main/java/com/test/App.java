package com.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

class MenuItem {
    String name;
    double price;

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

enum OrderStatus {
    ACCEPTED, CANCELLED, COMPLETED
}


class CustomerThread extends Thread {
    FoodOrderingSystem system;
    String name; 

    public CustomerThread(String name, FoodOrderingSystem system) {
        this.name = name;
        this.system = system;
    }

    @Override
    public void run(){
        try {

            // Place an order
            Map<String, Integer> orderItems = new HashMap<>();
            orderItems.put("Item1", 2);
           // orderItems.put("Item2", 1);
            Order order = new Order(orderItems);
            System.out.println(this.name + " placing order");
            Order placedOrder = system.placeOrder(order);
            System.out.println(this.name +" Order placed to "+placedOrder.restaurant.name);
            Thread.sleep(5000);

            // Complete the order when ready
            system.completeOrder(placedOrder);
            System.out.println(this.name +" order completed by "+placedOrder.restaurant.name);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        catch(IllegalStateException exception){
            System.out.println(this.name + exception.getMessage());
        }
    }
}

class Restaurant {
    String name;
    Map<String, MenuItem> menu;
    int rating;
    int maxOrders;
    List<Order> orders;

    public Restaurant(String name, int maxOrders) {
        this.name = name;
        this.menu = new HashMap<>();
        this.rating = 0;
        this.maxOrders = maxOrders;
        this.orders = new ArrayList<>();
    }

    public void addMenuItem(MenuItem item) {
        menu.put(item.name, item);
    }

    public void updateRating(int rating) {
        this.rating = rating;
    }

    public synchronized boolean acceptOrder(Order order) {
        if (orders.size() < maxOrders) {
            order.status = OrderStatus.ACCEPTED.name();
            this.orders.add(order);
            return true;
        }
        order.status = OrderStatus.CANCELLED.name();
        return false;
    }

    public void completeOrder(Order order) {
       order.status = OrderStatus.COMPLETED.name();
       this.orders.remove(order);
    }
}

class Order {
    Map<String, Integer> items;
    Restaurant restaurant;
    String status;

    public Order(Map<String, Integer> items) {
        this.items = items;
        this.restaurant = null;
    }
}

class FoodOrderingSystem {
    List<Restaurant> restaurants;
    RestaurantSelectionStrategy restaurantSelectionStrategy;

    public FoodOrderingSystem() {
        this.restaurants = new ArrayList<>();
        this.restaurantSelectionStrategy = null;
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    public Order placeOrder(Order order) {
        if (!validateOrder(order)) {
            throw new IllegalArgumentException("Order cannot be fulfilled by a single restaurant.");
        }

        Restaurant selectedRestaurant = restaurantSelectionStrategy.selectRestaurant(order, restaurants);
        if (selectedRestaurant != null && selectedRestaurant.acceptOrder(order)) {
            order.restaurant = selectedRestaurant;
            return order;
        } 
        else if(selectedRestaurant == null){
            throw new IllegalStateException(" All the restaurants are busy at the moment.");
        }
        else {
            throw new IllegalStateException(" Order cancelled by " + selectedRestaurant.name + " as it cannot accept more orders at the moment.");
        }
    }

    public void completeOrder(Order order) {
        order.restaurant.completeOrder(order);
    }

    public void setRestaurantSelectionStrategy(RestaurantSelectionStrategy strategy) {
        this.restaurantSelectionStrategy = strategy;
    }

    private boolean validateOrder(Order order) {
        // Check if all items in the order can be fulfilled by a single restaurant
        for(Restaurant restaurant: this.restaurants){
            int count = 0;
            for (String item : order.items.keySet()) {
                if(restaurant.menu.containsKey(item)){
                   count++;
                   continue;
                }
                break;
            }
            if(count == order.items.size()){
                return true;
            }
        }
        
        return false;
    }
}

interface RestaurantSelectionStrategy {
    Restaurant selectRestaurant(Order order, List<Restaurant> restaurants);
}

class CostBasedRestaurantSelection implements RestaurantSelectionStrategy {
    @Override
    public Restaurant selectRestaurant(Order order, List<Restaurant> restaurants) {
        // Implement the selection criteria based on cost (e.g., lowest cost)
        List<Pair<Double,Restaurant>> rp = new ArrayList<>();
        for(Restaurant restaurant: restaurants){
            int count = 0;
            Double totalAmount = 0D;
            if(restaurant.orders.size() < restaurant.maxOrders){
                for (String item : order.items.keySet()) {
                    if(restaurant.menu.containsKey(item)){
                    count++;
                    totalAmount += restaurant.menu.get(item).price * order.items.get(item);
                    continue;
                    }
                    break;
                }
                if(count == order.items.size()){
                    rp.add(Pair.of(totalAmount, restaurant));
                }
            }
        }
        Collections.sort(rp);
        return rp.size() > 0 ? rp.get(0).getRight(): null;
    }
}

// Usage example
public class App {
    public static void main(String[] args) throws InterruptedException {
        FoodOrderingSystem system = new FoodOrderingSystem();

        // Onboarding restaurants
        Restaurant restaurant1 = new Restaurant("Restaurant A", 1);
        Restaurant restaurant2 = new Restaurant("Restaurant B", 1);

        restaurant1.addMenuItem(new MenuItem("Item1", 10.0));
        restaurant1.addMenuItem(new MenuItem("Item2", 15.0));
        restaurant2.addMenuItem(new MenuItem("Item3", 12.0));
        restaurant2.addMenuItem(new MenuItem("Item4", 18.0));
        restaurant2.addMenuItem(new MenuItem("Item1", 9.0));

        system.addRestaurant(restaurant1);
        system.addRestaurant(restaurant2);

        // Set restaurant selection strategy
        system.setRestaurantSelectionStrategy(new CostBasedRestaurantSelection());
        for(int i=1;i<=2;i++){
            CustomerThread c = new CustomerThread("customer " + i, system);
            c.start();
            //Thread.sleep(2000);
        }

    }
}