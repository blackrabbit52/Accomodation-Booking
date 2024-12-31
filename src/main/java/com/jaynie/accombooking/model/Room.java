package com.jaynie.accombooking.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "rooms")
public class Room {
    @Id
    private String id;
    private String name;
    private Location location;
    private Details details;
    private boolean isAvailable;
    private String availability_date;
    private int price_per_month_gbp;
    private List<String> spoken_languages;

    public Room() {}
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public Details getDetails() { return details; }
    public void setDetails(Details details) { this.details = details; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getAvailability_date() { return availability_date; }
    public void setAvailability_date(String availability_date) { this.availability_date = availability_date; }

    public List<String> getSpokenLanguages() { return spoken_languages; }
    public void setSpokenLanguages(List<String> spokenLanguages) { this.spoken_languages = spokenLanguages; }

    public int getPrice_per_month_gbp() {return price_per_month_gbp;}
    public void setPrice_per_month_gbp(int price_per_month_gbp) {this.price_per_month_gbp = price_per_month_gbp;}

    public static class Location {
        private String city;
        private String county;
        private String postcode;

        public Location() {}

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getCounty() { return county; }
        public void setCounty(String county) { this.county = county; }

        public String getPostcode() { return postcode; }
        public void setPostcode(String postcode) { this.postcode = postcode; }
    }

    public static class Details {
        private Boolean furnished;
        private Boolean live_in_landlord;
        private Boolean bills_included;
        private Boolean bathroom_shared;
        private int shared_with;
        private List<String> amenities;

        public Details() {}

        public Boolean isFurnished() { return furnished; }
        public void setFurnished(Boolean furnished) { this.furnished = furnished; }

        public Boolean getLiveInLandlord() { return live_in_landlord; }
        public void setLiveInLandlord(Boolean liveInLandlord) { this.live_in_landlord = liveInLandlord; }

        public Boolean getBills_included() { return bills_included; }
        public void setBills_included(Boolean bills_included) { this.bills_included = bills_included; }

        public Boolean getBathroom_shared() { return bathroom_shared; }
        public void setBathroom_shared(Boolean bathroom_shared) { this.bathroom_shared = bathroom_shared; }

        public int getShared_with() { return shared_with; }
        public void setShared_with(int shared_with) { this.shared_with = shared_with; }

        public List<String> getAmenities() { return amenities; }
        public void setAmenities(List<String> amenities) { this.amenities = amenities; }
    }
}