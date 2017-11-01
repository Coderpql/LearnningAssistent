// IStockQuoteService.aidl
package com.learning.wow.learningassistence.Tools.aidl;

// Declare any non-default types here with import statements

interface IStockQuoteService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    double getMyTime(int myHour1,int myMinute1,int myHour2,int myMinute2);
}
