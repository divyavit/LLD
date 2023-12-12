package com.test;

import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

enum EventStatus {
    ONGOING,PAST,UPCOMING
}

@Data
@AllArgsConstructor
@Builder
class Member {
    private Integer memberId;
    private String name;
    private Integer superCoins;

    @Override
    public int hashCode() { return name.hashCode(); }
}

@Data
@AllArgsConstructor
@Builder
class Bid {
    @Builder.Default
    private List<Integer> superCoins = new ArrayList<>();
    private Date bidTime;
    private Integer lowestBid;
}

@Data
@AllArgsConstructor
@Builder
class Event {
    private Integer eventId;
    private String eventName;
    private String prizeName;
    private Date startDate;
    private Date endDate;
    private String status;
    private Member winner;
    @Builder.Default
    private List<Member> registeredMembers = new ArrayList<>();
    @Builder.Default
    private Map<Member,Bid> bids = new HashMap<>();
}

class BidSortingComparator implements Comparator<Map.Entry<Member,Bid>> {
 
    // Method 1
    // To compare customers
    @Override
    public int compare(Map.Entry<Member,Bid> bid1, Map.Entry<Member,Bid> bid2)
    {
        return bid1.getValue().getLowestBid() == bid2.getValue().getLowestBid() ? (int) bid1.getValue().getBidTime().getTime() - (int) bid2.getValue().getBidTime().getTime(): bid1.getValue().getLowestBid() - bid2.getValue().getLowestBid();
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class BidBlitzSystem {

    @Builder.Default
    private Map<Integer,Member> members = new HashMap<>();
    @Builder.Default
    private Map<Integer,Event> events = new HashMap<>();

    public void addMember(int superCoins, int memberId, String name) {
        Member member = Member.builder().memberId(memberId).superCoins(superCoins).name(name).build();
        members.put(memberId,member);
        System.out.println(name + " added successfully");
    }

    public void addEvent(String eventName, String prizeName, Date date,Integer eventId) {
        Event event = Event.builder().eventName(eventName).prizeName(prizeName).eventId(eventId).startDate(date).status(EventStatus.ONGOING.name()).build();
        events.put(eventId,event);
        System.out.println(eventName + " with prize " + prizeName + " added successfully");
    }

    public void registerMember(int memberId, int eventId) {
        if (memberId < 1 || memberId > members.size() || eventId < 1 || eventId > events.size()) {
            System.out.println("Invalid member ID or event ID");
            return;
        }

        Member member = members.get(memberId);
        Event event = events.get(eventId);

        if (!event.getRegisteredMembers().contains(member)) {
            event.getRegisteredMembers().add(member);
            System.out.println(member.getName() + " registered to the " + event.getEventName() + " event successfully");
        } else {
            System.out.println(member.getName() + " is already registered for this event");
        }
    }

    public void submitBid(int memberId, int eventId, List<Integer> bids) {
        if (memberId < 1 || memberId > members.size() || eventId < 1 || eventId > events.size()) {
            System.out.println("Invalid member ID or event ID");
            return;
        }

        Member member = members.get(memberId);
        Event event = events.get(eventId);

        if (!event.getRegisteredMembers().contains(member)) {
            System.out.println("Member did not register for this event");
            return;
        }

        if (bids.size() != 5 || bids.stream().anyMatch(bid -> bid <= 0)) {
            System.out.println("Invalid bids. Please submit exactly 5 positive bids.");
            return;
        }

        int maxBid = Collections.max(bids);

        if (member.getSuperCoins() < maxBid) {
            System.out.println("Insufficient super coins to place the bid.");
            return;
        }

        Bid bid = Bid.builder().bidTime(new Date()).superCoins(bids).lowestBid(Collections.min(bids)).build();

        event.getBids().put(member, bid);
        member.setSuperCoins(member.getSuperCoins() - maxBid);
        System.out.println("BIDS submitted successfully");
    }

    public void declareWinner(int eventId) {
        if (eventId < 1 || eventId > events.size()) {
            System.out.println("Invalid event ID");
            return;
        }

        Event event = events.get(eventId);

        if (event.getBids().isEmpty()) {
            System.out.println("No bids submitted for this event. No winner declared.");
            return;
        }

    
        Map.Entry<Member, Bid> winnerEntry = Collections.min(event.getBids().entrySet(),new BidSortingComparator());
        Member winner = winnerEntry.getKey();
        int lowestBid = winnerEntry.getValue().getLowestBid();

        System.out.println(winner.getName() + " wins the " + event.getPrizeName() + " with the lowest bid " + lowestBid);
        event.setStatus(EventStatus.PAST.name());
        event.setWinner(winner);
        event.setEndDate(new Date());
    }

    public List<Event> listWinners(String orderBy) {
        List<Event> sortedEvents = new ArrayList<Event>(events.values()).stream().filter(event -> event.getStatus().equalsIgnoreCase(EventStatus.PAST.name())).collect(Collectors.toList());

        if (orderBy.equals("asc")) {
            sortedEvents.sort(Comparator.comparing(Event::getStartDate));
        } else if (orderBy.equals("desc")) {
            sortedEvents.sort(Comparator.comparing(Event::getStartDate).reversed());
        }

        return sortedEvents;
    }
}

public class App {
    public static void main(String[] args) {
        BidBlitzSystem bidBlitzSystem = new BidBlitzSystem();

        // Sample commands for testing
        bidBlitzSystem.addMember(10000,1, "Akshay");
        bidBlitzSystem.addMember(5000,2, "Chris");

        bidBlitzSystem.addEvent("BBD", "IPHONE-14", new Date(),1);

        bidBlitzSystem.registerMember(1, 1);
        bidBlitzSystem.registerMember(2, 1);

        bidBlitzSystem.submitBid(1, 1, Arrays.asList(100, 200, 400, 500, 600));
        bidBlitzSystem.submitBid(10, 1, Arrays.asList(100, 200, 400, 500));

        bidBlitzSystem.declareWinner(1);

        List<Event> winners = bidBlitzSystem.listWinners("asc");
        for (Event event : winners) {
            Integer b = 0;
            if(event.getBids().size() > 0){
                if(event.getBids().containsKey(event.getWinner())){
                    b = event.getBids().get(event.getWinner()).getLowestBid();
                }
            }
            System.out.println("Event: " + event.getEventName() +
                    ", Winner: " + event.getWinner().getName() +
                    ", Lowest Bid: " + b + " Date: " + event.getStartDate());
        }
    }
}


