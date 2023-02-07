package hu.ps.common.service;

import java.util.List;

import hu.ps.common.dto.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;



public abstract class EntityPaginationService<T, D> extends EntityBaseService<T, D>{

    public Sort.Direction extractSortDirection(String order) {
        Sort.Direction sortDirection = Sort.Direction.ASC;
        if (order != null) {
            sortDirection = "DESC".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        }
        return sortDirection;
    }

    public String getSortByNameOfCols(final String originalName) {
        String sortBy = originalName;
        if ("startDateFormatted".equals(sortBy)) {
            sortBy = "startDate";
        }
        if ("endDateFormatted".equals(sortBy)) {
            sortBy = "endDate";
        }
        return sortBy;
    }

    public Sort parseSortParam(String sortBy, String order) {
        Sort.Direction sortDirection = extractSortDirection(order);
        return Sort.by(sortDirection, getSortByNameOfCols(sortBy));
    }

    public Pageable createPageDetails(Integer pageIndex, Integer pageSize, String sortBy, String order) {
        Sort sort = parseSortParam(sortBy, order);
        return PageRequest.of(pageIndex, pageSize, sort);
    }

    public PageResult<D> getPageResult(Page<T> page) {
        PageResult<D> result = new PageResult<>();
        List<D> list = getDtoList(page.getContent());
        result.setRows(list);
        result.setTotalElements(page.getTotalElements());
        result.setPageNumber(page.getNumber());
        result.setPageSize(page.getSize());
        return result;
    }

    public PageResult<D> getPageResultDto(Page<D> page) {
        PageResult<D> result = new PageResult<>();
        result.setRows(page.getContent());
        result.setTotalElements(page.getTotalElements());
        result.setPageNumber(page.getNumber());
        result.setPageSize(page.getSize());
        return result;
    }

    public PageResult<D> getPageResultDtoWithPagination(List<D> list, Pageable pageDetails) {
        PageResult<D> result = new PageResult<>();
        int fromIndex = (int)pageDetails.getOffset();
        if (list.size() <= fromIndex && fromIndex > 0) {
            return result;
        }
        int toIndex = fromIndex + pageDetails.getPageSize();
        if (list.size() <= fromIndex + pageDetails.getPageSize()) {
            toIndex = list.size();
        }

        // toindex - high endpoint (exclusive) of the subList
        List<D> sublist = list.subList(fromIndex, toIndex);
        Page<D> page = new PageImpl<>(sublist, pageDetails, list.size());
        result = getPageResultDto(page);
        return result;
    }
}
