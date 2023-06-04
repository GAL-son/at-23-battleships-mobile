package com.battleships.server.api.model;

import java.util.List;

public class ShipFields {
    List<Field> shipFields;

    public ShipFields(List<Field> fields) {
        this.shipFields = fields;
    }

    public List<Field> getShipFields() {
        return shipFields;
    }

    public int getSize() {
        return this.shipFields.size();
    }
}
