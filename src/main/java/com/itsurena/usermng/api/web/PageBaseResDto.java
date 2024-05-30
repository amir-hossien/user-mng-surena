package com.itsurena.usermng.api.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PageBaseResDto<T> {

    private int status;

    private String message;

    private Integer pageSize;

    private Integer pageNumber;

    private long totalPage;

    private long totalElement;

    private T body;

}
