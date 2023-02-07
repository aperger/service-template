package hu.ps.common.dto;

import hu.ps.common.model.FooEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentDto {

    @NotNull
    private Long Id;

    @NotNull
    @Size(max = 100)
    private String name;

    @ToString.Exclude @EqualsAndHashCode.Exclude

    private List<@Valid FooDto> fooList;

}
