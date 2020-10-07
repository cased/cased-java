package com.cased.api;

import java.util.List;

interface ResultsListInterface {
    List<Result> getResults();

    int getTotalCount();
    int getTotalPages();

    String getFirstPage();
    String getLastPage();
    String getPreviousPage();
    String getNextPage();
}