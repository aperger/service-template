package hu.ps.common.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "foo")
public class FooEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @NotNull
    @ToString.Exclude @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private ParentEntity parent;
}
