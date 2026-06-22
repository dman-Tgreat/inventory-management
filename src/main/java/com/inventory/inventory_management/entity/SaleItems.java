package com.inventory.inventory_management.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sale_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sales sale;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products product;

    private Integer quantity;

    @Column(name = "selling_price", nullable = false)
    private BigDecimal sellingPrice;
}
