package com.app.cms.pojos;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class PageResponse<T> extends Response<T> {
    private int pageNo;
    private int totalPages;
}
