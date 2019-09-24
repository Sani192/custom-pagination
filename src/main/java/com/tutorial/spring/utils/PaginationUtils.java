package com.tutorial.spring.utils;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

public class PaginationUtils {

	public static int 		RECORDS_PER_PAGE		= 5;
	public static int 		VIEW_PAGE_COUNT			= 5;
	public static String	LIMIT_START				= "LIMIT_START";
	public static String	LIMIT_END				= "LIMIT_END";
	
	private static int getPageNumber(String requestedPage) {
		int page = (StringUtils.isNotBlank(requestedPage) && StringUtils.isNumeric(requestedPage) ? Integer.parseInt(requestedPage) : 1); 
		return (page <= 0 ? 1 : page);
	}

	private static int getRecordsPerPage(String requestedRecordsPerPage) {
		int page = (StringUtils.isNumeric(requestedRecordsPerPage) ? Integer.parseInt(requestedRecordsPerPage) : RECORDS_PER_PAGE); 
		return (page <= 0 ? RECORDS_PER_PAGE : page);
	}
	
	private static int getLimitStart(int page, int recordsPerPage) {
		return ((page * recordsPerPage) - recordsPerPage);
	}
	
	private static int getLimitFirstIndex(int page, int recordsPerPage, int totalRecords) {
		int index = getLimitStart(page, recordsPerPage);
		// if requested page exceed total records then get last page start limit
		if(index > totalRecords) {
			index = getLimitStart(getTotalPages(totalRecords, recordsPerPage), recordsPerPage);
		}
		return index;
	}
	
	public static int getTotalPages(int totalRecords, int recordsPerPage) {
		int count = (int) Math.ceil((double) totalRecords / (double) recordsPerPage);
		return count;
	}
	
	public static int getTotalViewPageCount(int totalPages) {
		int count = (totalPages > VIEW_PAGE_COUNT ? VIEW_PAGE_COUNT : totalPages) ;
		return count;
	}
	
	public static HashMap<String, Object> addPaginationLimit(HashMap<String, Object> paramMap, int totalRecords, String requestedRecordsPerPage, String requestedPage) {
		int recordsPerPage = getRecordsPerPage(requestedRecordsPerPage);
		paramMap.put(LIMIT_START, getLimitFirstIndex(getPageNumber(requestedPage), recordsPerPage, totalRecords));
		paramMap.put(LIMIT_END, recordsPerPage);
		return paramMap;
	}
}
