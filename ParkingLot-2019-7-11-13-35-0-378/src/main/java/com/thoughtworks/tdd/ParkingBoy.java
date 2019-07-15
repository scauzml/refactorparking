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
                    if (this.parkingBoyName == ParingBoyName.SMART_PARKING_BOY.getValue()) {
                        currentParingLot = findMoreEmptyOptionParkingLOt();
                    }else if(this.parkingBoyName == ParingBoyName.SUPER_SMART_PARKING_BOY.getValue()){
                        currentParingLot=findLargerPositonRatePraringLot();
                    }
                    parkTicket=new ParkTicket();
                    //关联ticket,与car,而且停车场添加ticket
                    parkTicket.setCarNumber(car.getCarNumber());
                    currentParingLot.addParTicket(parkTicket);
                    currentParingLot.addCar(car);
                }else {
                    this.errorMessage=ErrorMessage.NOT_ENOUGH_CAPACITY_MESSAGE.getValue();
                }


            }

        }


        return parkTicket;
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
        //验证pakTicket是wrong
        if (parkTicket != null) {
            boolean isRightTicket=false;
            ParkingLot currentParkingLot=null;
            for (ParkingLot p :this.parkingLotList) {
                isRightTicket=p.isContainParkTicket(parkTicket);
                if (isRightTicket) {
                    currentParkingLot=p;
                    break;
                }
            }

            if (isRightTicket) {
                if (!parkTicket.isUsed()) {
                    //没有被使用则获取正确car

                   car= currentParkingLot.getCars().stream().
                           filter(e->e.getCarNumber()==parkTicket.getCarNumber()).findFirst().get();
                   currentParkingLot.getCars().remove(car);
                  parkTicket.setUsed(true);

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

    public ParkingLot findMoreEmptyOptionParkingLOt() {

        return this.parkingLotList.stream().max(Comparator.comparingInt(ParkingLot::getAllowance)).get();

    }

    public ParkingLot findLargerPositonRatePraringLot() {

        return this.parkingLotList.stream().max(Comparator.comparingDouble(ParkingLot::getPositionrate)).get();
    }

}
