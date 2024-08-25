package org.prashant.blog.blogapplicationapi.payload;

import java.util.List;

public record NotificationPageResponse(List<NotificationDTO> notifications,
                                       Integer pageNumber,
                                       Integer pageSize,
                                       Long totalRecords,
                                       Integer totalPages,
                                       Boolean isLastPage) {
}
