package com.driver.services.impl;

import com.driver.Exceptions.NoSpotAvailable;
import com.driver.Exceptions.ParkingLotNotAvailable;
import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    private final ParkingLotRepository parkingLotRepository;
    private final SpotRepository spotRepository1;

    List<Spot> spotList;
    List<Spot> spots;

    @Autowired
    public ParkingLotServiceImpl(ParkingLotRepository parkingLotRepository, SpotRepository spotRepository1) {
        this.parkingLotRepository = parkingLotRepository;
        this.spotRepository1 = spotRepository1;
    }

    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setAddress(address);
        parkingLot.setName(name);
        parkingLotRepository.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Spot spot = new Spot();

        spot.setPricePerHour(pricePerHour);

        if(numberOfWheels == 2){
            spot.setSpotType(SpotType.TWO_WHEELER);
        }
        else if(numberOfWheels == 4){
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }
        else{
            spot.setSpotType(SpotType.OTHERS);
        }
        spot.setOccupied(false);
        spotRepository1.save(spot);

        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {

        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
//        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotId)
//                .orElseThrow(() -> new ParkingLotNotAvailable("ParkingLot Not Available for Id " + parkingLotId));
//        return parkingLot.getSpotList()
//                .stream()
//                .filter(spot -> spot.getId() == spotId)
//                .findFirst()
//                .map(spot -> {
//                    spot.setPricePerHour(pricePerHour);
//                    return spotRepository1.save(spot);
//                })
//                .orElseThrow(() -> new NoSpotAvailable( "No spot is available with Id" + spotId));
        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotId).orElse(null);

        spots = parkingLot.getSpotList();  Spot spot = new Spot();

        for(Spot spt : spots){
            if(spt.getId() == spotId){
                spot = spt;
            }
        }
        spot.setId(spotId);

        spot.setPricePerHour(pricePerHour);

        spot.setOccupied(false);
        spotRepository1.save(spot);

        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository.deleteById(parkingLotId);
    }
}
