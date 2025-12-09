package org.builder;

public class Director {
    public void constructTownHouse(Builder builder) {
        builder.setHouseType(HouseType.TOWNHOUSE);
        builder.setCommonSquare(140);
        builder.setWallsMaterial("brick");
        builder.setRoofType("metal tile");
    }

    public void constructDuplex(Builder builder) {
        builder.setHouseType(HouseType.DUPLEX);
        builder.setCommonSquare(170);
        builder.setWallsMaterial("aerated concrete blocks");
        builder.setRoofType("onduline");
    }

    public void constructSingleFamily(Builder builder) {
        builder.setHouseType(HouseType.SINGLE_FAMILY);
        builder.setCommonSquare(240);
        builder.setWallsMaterial("glued timber");
        builder.setRoofType("roof tiles");
    }
}
