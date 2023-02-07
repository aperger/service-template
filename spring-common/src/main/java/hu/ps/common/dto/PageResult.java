package hu.ps.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PageResult<T> {

    private long pageNumber;
    private long pageSize;
    private List<T> rows;
    private long totalElements;

}
