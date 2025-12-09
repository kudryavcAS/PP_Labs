package org.builder;

public class HouseBuilder implements Builder {
    private double commonSquare;
    private String wallsMaterial;
    private String roofType;
    private HouseType type;

    @Override
    public void setHouseType(HouseType type) {
        this.type = type;
    }

    @Override
    public void setCommonSquare(double commonSquare) {
        this.commonSquare = commonSquare;
    }

    @Override
    public void setWallsMaterial(String wallsMaterial) {
        this.wallsMaterial = wallsMaterial;
    }

    @Override
    public void setRoofType(String roofType) {
        this.roofType = roofType;
    }

    public House getResult() {
        return new House(type, commonSquare, wallsMaterial, roofType);
    }
}
