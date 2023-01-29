package hu.ps.common.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FooDto {

    @NotNull
    private Long Id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    private Long parentId;
}
