package org.redcode.bookanddriveservice.page;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageResponse<T> {

    private List<T> content;
    private PageMetadata page;

    public PageResponse(List<T> content, PageMetadata page) {
        this.content = content;
        this.page = page;
    }

    public static <T> PageResponse<T> of(List<T> content, PageMetadata page) {
        return new PageResponse<>(content, page);
    }

    @Setter
    @Getter
    public static class PageMetadata {
        private int limit;
        private int page;
        private long totalElements;
        private long totalPages;

        public PageMetadata(int limit, int page, long totalElements, int totalPages) {
            this.limit = limit;
            this.page = page;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
        }
    }
}
