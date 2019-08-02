package org.csuc.dao;

import org.csuc.utils.Status;



/**
 * @author amartinez
 */
public interface DashboardDAO {

    long getStatus(Status status) throws Exception;
    long getStatus(Status status, String user) throws Exception;

    Object getDashboardRecollect(String user) throws Exception;
    Object getDashboardAnalyse(String user) throws Exception;

    long getStatusLastMonth(Status status) throws Exception;
    long getStatusLastMonth(Status status, String user) throws Exception;

    int getStatusLastMonthIncrease(Status status) throws Exception;
    int getStatusLastMonthIncrease(Status status, String user) throws Exception;

    long getStatusMonth(Status status) throws Exception;
    long getStatusMonth(Status status, String user) throws Exception;


    long getStatusLastDay(Status status);
    long getStatusLastDay(Status status, String user);

    long getStatusLastYear(Status status);
    long getStatusLastYear(Status status, String user);
}
