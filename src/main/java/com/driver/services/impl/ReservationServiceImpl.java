package com.driver.services.impl;

import com.driver.Exceptions.ParkingLotNotAvailable;
import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.PaymentService;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Autowired
    PaymentService paymentService;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).orElseThrow(()->new RuntimeException("Cannot make reservation"));
        List<Spot> list = parkingLot.getSpotList();
        int minPrice = Integer.MAX_VALUE;
        Spot spot = list.stream()
                .filter(s -> !s.getOccupied())
                .filter(s -> validType(s, numberOfWheels))
                .min(Comparator.comparingDouble(Spot::getPricePerHour))
                .orElse(null);
//        for(Spot spt : list){
//            if(!spt.getOccupied()){
//                if(validType(spt,numberOfWheels)){
//                    if(spt.getPricePerHour() < minPrice){
//                        spot = spt;
//                    }
//                }
//            }
//        }

        if(spot != null){
            spot.setOccupied(true);
        } else {
            throw new RuntimeException("Cannot make reservation");
        }
        Reservation reservation = new Reservation();
        reservation.setSpot(spot);
        User user = userRepository3.findById(userId).orElseThrow(()->new RuntimeException("Cannot make reservation"));
        reservation.setUser(user);
        reservation.setSpot(spot);
        return reservation;
    }
    private boolean validType(Spot spot, Integer wheels) {
        if (spot == null || wheels == null) {
            return false;
        }

        SpotType spotType = spot.getSpotType();
        if (wheels <= 2) {
            return spotType == SpotType.TWO_WHEELER ||
                    spotType == SpotType.FOUR_WHEELER ||
                    spotType == SpotType.OTHERS;
        } else if (wheels <= 4) {
            return spotType == SpotType.FOUR_WHEELER ||
                    spotType == SpotType.OTHERS;
        } else {
            return spotType == SpotType.OTHERS;
        }
    }
}
