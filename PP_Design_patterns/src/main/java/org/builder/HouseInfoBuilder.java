package org.builder;

public class HouseInfoBuilder implements Builder {
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

    public HouseInfo getResult() {
        return new HouseInfo(type, commonSquare, wallsMaterial, roofType);
    }
}
