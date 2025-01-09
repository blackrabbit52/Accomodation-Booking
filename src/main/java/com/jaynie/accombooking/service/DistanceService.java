package com.jaynie.accombooking.service;

import org.springframework.stereotype.Service;

@Service
public class DistanceService {
    private static final double EARTH_RADIUS = 6371; // earth radius in km
//coverting the difference into radians
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
//harvesine formula to calculate distance around the earth
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
//calculates the distance
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // Distance in km
    }
}
