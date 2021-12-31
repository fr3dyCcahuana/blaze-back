package com.mdp.api.test.testblaze.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", unique = true)
    private static int orderNumber;

    @Column(nullable = false)
    private String state;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(nullable = false)
    private String customer;

    @Column(name = "city_tax")
    private Double cityTax;

    @Column(name = "county_tax")
    private Double countyTax;

    @Column(name = "state_tax")
    private Double stateTax;

    @Column(name = "federal_tax")
    private Double federalTax;

    @Column(name = "total_taxes")
    private Double totalTaxe;

    @Column(name = "total_amount")
    private Double totalAmount;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @JoinColumn(name = "orders_id")
    private List<ItemOrder> itemOrders;

    @PrePersist
    public void prePersist() {
        date = new Date();
        this.orderNumber++;
        this.getCityTax();
        this.getCountyTax();
        this.getFederalTax();
        this.getStateTax();
        this.getTotalAmount();
        this.getTotalTaxe();
    }

    public void addOrderItem(ItemOrder itemOrder) {
        this.itemOrders.add(itemOrder);
    }

    public void deleteOrderItem(ItemOrder itemOrder) {
        this.itemOrders.remove(itemOrder);
    }


    public Double getSubTotal() {
        Double subTotal = 0.00;
        for (ItemOrder itemOrder1 : itemOrders) {
            subTotal += itemOrder1.getCost();
        }
        return subTotal;
    }

    public Double getCityTax() {
        Double value = 0.00;
        value = Tax.CITY_TAX.getValue() * getSubTotal();
        this.setCityTax(value);
        return cityTax;
    }

    public Double getCountyTax() {
        Double value = 0.00;
        value = (getSubTotal() + this.getCityTax()) * Tax.COUNTY_TAX.getValue();
        this.setCountyTax(value);
        return countyTax;
    }

    public Double getStateTax() {
        Double value = 0.00;
        value = (getSubTotal() + this.getCountyTax()) * Tax.STATE_TAX.getValue();
        this.setStateTax(value);
        return stateTax;
    }

    public Double getFederalTax() {
        Double value = 0.00;
        value = (getSubTotal() + this.getStateTax()) * Tax.FEDERAL_TAX.getValue();
        this.setFederalTax(value);
        return federalTax;
    }

    public Double getTotalTaxe() {

        Double value = 0.00;
        value = this.getCityTax() + this.getCountyTax() + this.getStateTax() + this.getFederalTax();
        this.setTotalTaxe(value);
        return this.totalTaxe;
    }

    public Double getTotalAmount() {
        Double value = 0.00;
        value = this.getTotalTaxe() + this.getSubTotal();
        this.setTotalAmount(value);
        return this.totalAmount;
    }
}
