package com.inventory.shopcart.model;

// import org.springframework.data.annotation.Id;
import jakarta.persistence.Entity;
// import jakarta.persistence.CascadeType;
// import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="category")
public class Category{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;//category_id

    @NotBlank(message = "Name is mandatory")
    String name;
    


}
