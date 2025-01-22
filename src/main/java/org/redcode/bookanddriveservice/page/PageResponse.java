package org.redcode.bookanddriveservice.page;

import java.util.List;
import java.util.function.Function;
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

    public static <T, R> PageResponse<R> from(PageResponse<T> response, Function<T, R> mapperFunction) {
        List<R> content = response.getContent().stream().map(mapperFunction).toList();
        PageMetadata metadata = response.getPage();
        return PageResponse.of(content, metadata);
    }

    @Setter
    @Getter
    public static class PageMetadata {
        private int page;
        private int limit;
        private long totalElements;
        private long totalPages;

        public PageMetadata(int page, int limit, long totalElements, int totalPages) {
            this.page = page;
            this.limit = limit;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
        }
    }
}
