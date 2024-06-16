package com.example.backEnd.datatables;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
@Getter
public final class DataTablesPageRequest implements Pageable {
    private final long offset;
    private final int pageSize;
    private final Sort sort;

    @Override
    public @NotNull Pageable withPage(int pageNumber) {
        throw new UnsupportedOperationException();
    }

    @Override
    @NonNull
    public Pageable next() {
        throw new UnsupportedOperationException();
    }

    @Override
    @NonNull
    public Pageable previousOrFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    @NonNull
    public Pageable first() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPrevious() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getPageNumber() {
        throw new UnsupportedOperationException();
    }
}
