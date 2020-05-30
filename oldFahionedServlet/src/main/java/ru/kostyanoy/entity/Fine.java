package ru.kostyanoy.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Fine {

    private Long id;

    private String type;

    private BigDecimal charge;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fine fine = (Fine) o;
        return Objects.equals(id, fine.id) &&
                Objects.equals(type, fine.type) &&
                Objects.equals(charge, fine.charge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, charge);
    }

    @Override
    public String toString() {
        return "Fine{" +
                "id=" + id +
                ", timeStamp='" + type + '\'' +
                ", carID='" + charge + '\'' +
                '}';
    }
}
