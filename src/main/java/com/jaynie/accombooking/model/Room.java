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
    private String availabilityDate;
    private double pricePerMonthGbp;
    private List<String> spokenLanguages;

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

    public String getAvailabilityDate() { return availabilityDate; }
    public void setAvailabilityDate(String availabilityDate) { this.availabilityDate = availabilityDate; }

    public List<String> getSpokenLanguages() { return spokenLanguages; }
    public void setSpokenLanguages(List<String> spokenLanguages) { this.spokenLanguages = spokenLanguages; }

    public double getPricePerMonthGbp() {return pricePerMonthGbp;}
    public void setPricePerMonthGbp(double pricePerMonthGbp) {this.pricePerMonthGbp = pricePerMonthGbp;}

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
        private Boolean liveInLandlord;
        private Boolean billsIncluded;
        private Boolean bathroomShared;
        private int sharedWith;
        private List<String> amenities;

        public Details() {}

        public Boolean isFurnished() { return furnished; }
        public void setFurnished(Boolean furnished) { this.furnished = furnished; }

        public Boolean getLiveInLandlord() { return liveInLandlord; }
        public void setLiveInLandlord(Boolean liveInLandlord) { this.liveInLandlord = liveInLandlord; }

        public Boolean getBillsIncluded() { return billsIncluded; }
        public void setBillsIncluded(Boolean billsIncluded) { this.billsIncluded = billsIncluded; }

        public Boolean getBathroomShared() { return bathroomShared; }
        public void setBathroomShared(Boolean bathroomShared) { this.bathroomShared = bathroomShared; }

        public int getSharedWith() { return sharedWith; }
        public void setSharedWith(int sharedWith) { this.sharedWith = sharedWith; }

        public List<String> getAmenities() { return amenities; }
        public void setAmenities(List<String> amenities) { this.amenities = amenities; }
    }
}