package com.inventory.inventory_management.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "purchase_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchases purchase;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products product;

    private Integer quantity;

    @Column(name = "unit_cost", nullable = false)
    private BigDecimal unitCost;
}
