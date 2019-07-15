package com.thoughtworks.tdd;

import com.thoughtworks.tdd.messageenum.ErrorMessage;
import com.thoughtworks.tdd.messageenum.ParingBoyName;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ParkingBoy {
     private List<ParkingLot> parkingLotList;
     private String errorMessage;
     private String parkingBoyName;
     private ParkingLot currentParingLot=null;
    public ParkingBoy(List<ParkingLot> parkingLotList, String parkingBoyName) {
        this.parkingLotList = parkingLotList;
        this.parkingBoyName = parkingBoyName;
    }
    public String getParkingBoyName() {
        return parkingBoyName;
    }

    public void setParkingBoyName(String parkingBoyName) {
        this.parkingBoyName = parkingBoyName;
    }

    public List<ParkingLot> getParkingLotList() {
        return parkingLotList;
    }

    public void setParkingLotList(List<ParkingLot> parkingLotList) {
        this.parkingLotList = parkingLotList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ParkingBoy(List<ParkingLot> parkingLotList) {
        this.parkingLotList = parkingLotList;
    }

    public ParkingBoy() {
    }
    public ParkTicket park(Car car) {
        ParkTicket parkTicket =null;
        if (car != null) {
            boolean isParkedCar = isParkedCar(car);
            if (!isParkedCar) {
                boolean isCapacityEnough = isCapacityEnough();
                if (isCapacityEnough) {
                    findRightLotByParkingBoyName();
                    parkTicket=new ParkTicket();
                    parkTicket.setCarNumber(car.getCarNumber());
                    addParkTicketAndCarIntoLot(car, parkTicket);
                }else {
                    this.errorMessage=ErrorMessage.NOT_ENOUGH_CAPACITY_MESSAGE.getValue();
                }
            }
        }
        return parkTicket;
    }

    public void addParkTicketAndCarIntoLot(Car car, ParkTicket parkTicket) {
        currentParingLot.addParTicket(parkTicket);
        currentParingLot.addCar(car);
    }

    public void findRightLotByParkingBoyName() {
        if (this.parkingBoyName == ParingBoyName.SMART_PARKING_BOY.getValue()) {
            currentParingLot = findMoreEmptyOptionParkingLOt();
        }else if(this.parkingBoyName == ParingBoyName.SUPER_SMART_PARKING_BOY.getValue()){
            currentParingLot=findLargerPositonRatePraringLot();
        }
    }

    public boolean isCapacityEnough() {
        boolean isCapacityEnough = false;

        for (ParkingLot e1:this.parkingLotList
            ) {
                isCapacityEnough=e1.isCapacityEnough();
                if (isCapacityEnough) {
                    this.currentParingLot=e1;
                    break;
                }
            }
        return isCapacityEnough;
    }

    public boolean isParkedCar(Car car) {
        boolean isParkedCar = false;
        for (ParkingLot e:this.parkingLotList
             ) {
            isParkedCar= e.isContainCar(car);

        }
        return isParkedCar;
    }

    public List<ParkTicket> parkCarList(List<Car> carList) {
        List<ParkTicket> list = new ArrayList<>();
        carList.forEach(e->{
            ParkTicket parkTicket = this.park(e);
            list.add(parkTicket);
        });
        return list;
    }

    public Car fetchRightCar(ParkTicket parkTicket) {
        Car car=null;
        if (parkTicket != null) {
            boolean isRightTicket = isRightTicket(parkTicket);
            if (isRightTicket) {
                if (!parkTicket.isUsed()) {
                    car= getCar(parkTicket);
                    removeCarFromParkingLot(car);
                    changeTicktStatus(parkTicket);
                }else {
                    this.errorMessage= ErrorMessage.WRONG_TICKET_MESSAGE.getValue();
                }
            }else {
                this.errorMessage= ErrorMessage.WRONG_TICKET_MESSAGE.getValue();
            }
        }else {
            this.errorMessage=ErrorMessage.NOT_PROVIDE_TOKET_MESSAGE.getValue();
        }
        return car;
    }

    public void changeTicktStatus(ParkTicket parkTicket) {
        parkTicket.setUsed(true);
    }

    public void removeCarFromParkingLot(Car car) {
        this.currentParingLot.getCars().remove(car);
    }

    public Car getCar(ParkTicket parkTicket) {
        return this.currentParingLot.getCars().stream().filter(e->e.getCarNumber()==parkTicket.getCarNumber()).findFirst().get();
    }

    public boolean isRightTicket(ParkTicket parkTicket) {
        boolean isRightTicket=false;
        this.currentParingLot=null;
        for (ParkingLot p :this.parkingLotList) {
            isRightTicket=p.isContainParkTicket(parkTicket);
            if (isRightTicket) {
                this.currentParingLot=p;
                break;
            }
        }
        return isRightTicket;
    }

    public ParkingLot findMoreEmptyOptionParkingLOt() {

        return this.parkingLotList.stream().max(Comparator.comparingInt(ParkingLot::getAllowance)).get();

    }

    public ParkingLot findLargerPositonRatePraringLot() {
        return this.parkingLotList.stream().max(Comparator.comparingDouble(ParkingLot::getPositionrate)).get();
    }
}
