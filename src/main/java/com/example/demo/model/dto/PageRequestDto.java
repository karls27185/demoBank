package com.example.demo.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@Setter
@Getter
public class PageRequestDto {
    private Integer pageNumber = 0;//значений по умолчанию для номера страницы (0)
    private Integer pageSize = 10;//размер страницы по умолчанию (10)
    private Sort.Direction sort = Sort.Direction.ASC;//направление сортировки по умолчанию (ASC)
    private String sortByColumn = "id";//столбец сортировки по умолчанию ("id")

    /**
     * Метод getPageable принимает объект PageRequestDto в качестве аргумента и возвращает объект Pageable из Spring Data, который используется для постраничного запроса данных из базы данных
     * */
    public Pageable getPageable(PageRequestDto dto) {
        Integer page = Objects.nonNull(dto.getPageNumber()) ? dto.getPageNumber() : this.pageNumber;//проверяется, заданы ли значения номера страницы
        Integer size = Objects.nonNull(dto.getPageSize()) ? dto.getPageSize() : this.pageSize;//проверяется, размера страницы
        Sort.Direction sort = Objects.nonNull(dto.getSort()) ? dto.getSort() : this.sort;//проверяется, направления сортировки
        String sortByColumn = Objects.nonNull(dto.getSortByColumn()) ? dto.getSortByColumn() : this.sortByColumn;//проверяется, столбц сортировки

        return PageRequest.of(page, size, sort, sortByColumn);//создавать объект Pageable на основе данных, переданных через объект PageRequestDto
    }
}
