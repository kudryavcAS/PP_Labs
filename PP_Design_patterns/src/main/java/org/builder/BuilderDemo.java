package org.builder;

public class BuilderDemo {
    public static void main(String[] args) {
        Director director = new Director();

        HouseBuilder builder = new HouseBuilder();
        director.constructTownHouse(builder);
        House house = builder.getResult();
        System.out.println("House built:\n" + house.getHouseType());

        HouseInfoBuilder infoBuilder = new HouseInfoBuilder();

        director.constructDuplex(infoBuilder);
        HouseInfo info = infoBuilder.getResult();
        System.out.println("House info built:\n" + info.print());

    }
}
