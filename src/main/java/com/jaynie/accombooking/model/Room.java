package com.jaynie.accombooking.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "rooms")
public class Room {
    @Id
    private String id;
    private String objectId; // MongoDB ObjectId
    private String name;
    // Fields from "Location"
    private String city;
    private String county;
    private String postcode;
    // Fields from "Details"
    private boolean isAvailable;
    private boolean furnished;
    private List<String> amenities;
    private boolean liveInLandlord;
    private int sharedWith;
    private boolean billsIncluded;
    private boolean bathroomShared;
    private int pricePerMonthGbp;
    private String availabilityDate;
    private List<String> spokenLanguages;

    // Getters and Setters for all fields
    public String getObjectId() {return objectId;}
    public void setObjectId(String objectId) {this.objectId = objectId;}

    public boolean isAvailable() {return isAvailable;}
    public void setAvailable(boolean available) {isAvailable = available;}

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getCity() {return city;}
    public void setCity(String city) {this.city = city;}

    public String getCounty() {return county;}
    public void setCounty(String county) {this.county = county;}

    public String getPostcode() {return postcode;}
    public void setPostcode(String postcode) {this.postcode = postcode;}

    public boolean isFurnished() {return furnished;}
    public void setFurnished(boolean furnished) {this.furnished = furnished;}

    public List<String> getAmenities() {return amenities;}
    public void setAmenities(List<String> amenities) {this.amenities = amenities;}

    public boolean isLiveInLandlord() {return liveInLandlord;}
    public void setLiveInLandlord(boolean liveInLandlord) {this.liveInLandlord = liveInLandlord;}

    public int getSharedWith() {return sharedWith;}
    public void setSharedWith(int sharedWith) {this.sharedWith = sharedWith;}

    public boolean isBillsIncluded() {return billsIncluded;}
    public void setBillsIncluded(boolean billsIncluded) {this.billsIncluded = billsIncluded;}

    public boolean isBathroomShared() {return bathroomShared;}
    public void setBathroomShared(boolean bathroomShared) {this.bathroomShared = bathroomShared;}

    public int getPricePerMonthGbp() {return pricePerMonthGbp;}
    public void setPricePerMonthGbp(int pricePerMonthGbp) {this.pricePerMonthGbp = pricePerMonthGbp;}

    public String getAvailabilityDate() {return availabilityDate;}
    public void setAvailabilityDate(String availabilityDate) {this.availabilityDate = availabilityDate;}

    public List<String> getSpokenLanguages() {return spokenLanguages;}
    public void setSpokenLanguages(List<String> spokenLanguages) {this.spokenLanguages = spokenLanguages;}
}