package org.builder;

public class HouseInfo {
    private final HouseType houseType;
    private final double commonSquare;
    private final String wallsMaterial;
    private final String roofType;

    public HouseInfo(HouseType type, double commonSquare, String wallsMaterial, String roofType) {
        this.houseType = type;
        this.commonSquare = commonSquare;
        this.wallsMaterial = wallsMaterial;
        this.roofType = roofType;
    }

    public HouseType getHouseType() {
        return houseType;
    }

    public double getCommonSquare() {
        return commonSquare;
    }

    public String getRoofType() {
        return roofType;
    }

    public String getWallsMaterial() {
        return wallsMaterial;
    }

    public String print() {
        String info = "";
        info += "Type of house: " + houseType + "\n";
        info += "Common  square: " + commonSquare + "\n";
        info += "Walls material: " + wallsMaterial + "\n";
        info += "Roof type" + roofType + "\n";

        return info;
    }
}

