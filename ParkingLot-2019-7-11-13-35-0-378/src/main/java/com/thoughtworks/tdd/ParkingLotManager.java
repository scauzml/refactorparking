package com.thoughtworks.tdd;

import com.thoughtworks.tdd.messageenum.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

public class ParkingLotManager {
    private List<ParkingBoy> parkingBoys = new ArrayList<ParkingBoy>();
    private List<ParkingLot> parkingLotList=new ArrayList<ParkingLot>();
    private  String errorMessage;
    private ParkingLot currentParingLot=null;
    public ParkingLotManager() {
    }
    public ParkingLotManager(List<ParkingLot> parkingLotList) {
        this.parkingLotList = parkingLotList;
    }

    public ParkingLotManager(List<ParkingBoy> parkingBoyList, List<ParkingLot> parkingLots) {
        this.parkingBoys=parkingBoyList;
        this.parkingLotList=parkingLots;
    }
    public List<ParkingLot> getParkingLotList() {
        return parkingLotList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setParkingLotList(List<ParkingLot> parkingLotList) {
        this.parkingLotList = parkingLotList;
    }

    public List<ParkingBoy> getParkingBoys() {
        return parkingBoys;
    }

    public void setParkingBoys(List<ParkingBoy> parkingBoys) {
        this.parkingBoys = parkingBoys;
    }

    public void addParkingBogyIntoManagementList(ParkingBoy boy) {
        parkingBoys.add(boy);
    }


    public ParkTicket specifyBoyToPark(String parkingBoyname, Car car) {
        ParkingBoy parkingBoy = getSpecifyParkingBoy(parkingBoyname);

        if (parkingBoy == null) {
            return null;
        }
        ParkTicket parkTicket = parkingBoy.park(car);

        if (parkTicket!=null) {
            return parkTicket;
        }else {
            this.errorMessage=parkingBoy.getErrorMessage();
            return null;
        }
    }

    public ParkingBoy getSpecifyParkingBoy(String parkingBoyname) {
        return this.getParkingBoys().stream().filter(e -> e.getParkingBoyName() == parkingBoyname).map(e -> (e)).findFirst().get();
    }

    public Car specifyBoyTofetch(String parkingBoyname, ParkTicket parkTicket) {
        ParkingBoy parkingBoy = getSpecifyParkingBoy(parkingBoyname);
        if (parkingBoy == null) {
            return null;
        }
        Car car = parkingBoy.fetchRightCar(parkTicket);
        if (car != null) {
            return car;
        }else {
            this.errorMessage=parkingBoy.getErrorMessage();
            return car;
        }
    }
    public ParkTicket park(Car car) {
        ParkTicket parkTicket =null;
        if (car != null) {
            boolean isParkedCar = isParkedCar(car);
            if (!isParkedCar) {
                boolean isCapacityEnough = isCapacityEnough();
                if (isCapacityEnough) {
                    parkTicket=new ParkTicket();
                    parkTicket.setCarNumber(car.getCarNumber());
                    addParkTicketAndCarIntoLot(car, parkTicket);
                }else {
                    this.errorMessage= ErrorMessage.NOT_ENOUGH_CAPACITY_MESSAGE.getValue();
                }
            }
        }
        return parkTicket;
    }

    public void addParkTicketAndCarIntoLot(Car car, ParkTicket parkTicket) {
        currentParingLot.addParTicket(parkTicket);
        currentParingLot.addCar(car);
    }

    public boolean isCapacityEnough() {
        boolean isCapacityEnough = false;
        for (ParkingLot e1:this.parkingLotList
        ) {
            isCapacityEnough=e1.isCapacityEnough();
            if (isCapacityEnough) {
                setCurrentParkingLot(e1);
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

    public Car fetchRightCar(ParkTicket parkTicket) {
        Car car=null;
        if (parkTicket != null) {
            boolean isRightTicket = isRightTicket(parkTicket);
            if (isRightTicket) {
                if (!parkTicket.isUsed()) {
                    car = getCar(parkTicket);
                    removeCarFromLot(car);
                    changeParkTicketUseStatus(parkTicket);
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

    public void changeParkTicketUseStatus(ParkTicket parkTicket) {
        parkTicket.setUsed(true);
    }

    public void removeCarFromLot(Car car) {
        this.currentParingLot.getCars().remove(car);
    }

    public Car getCar(ParkTicket parkTicket) {
        Car car;
        car=  this.currentParingLot.getCars().stream().
                filter(e->e.getCarNumber()==parkTicket.getCarNumber()).findFirst().get();
        return car;
    }

    public boolean isRightTicket(ParkTicket parkTicket) {
        boolean isRightTicket=false;
        setCurrentParkingLot(null);
        for (ParkingLot p :this.parkingLotList) {
            isRightTicket=p.isContainParkTicket(parkTicket);
            if (isRightTicket) {
                setCurrentParkingLot(p);
                break;
            }
        }
        return isRightTicket;
    }

    public void setCurrentParkingLot(ParkingLot p) {
        this.currentParingLot = p;
    }
}
