package com.example.monthly_apha;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BudgetManager {

    public static final String BUDGET_KEY = "budget";

    private static Context context;

    public BudgetManager(Context context) {
        BudgetManager.context = context;
    }

    public static void setBudget(double budget) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putLong(BUDGET_KEY, Double.doubleToRawLongBits(budget)).apply();
    }

    public static double getBudget() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return Double.longBitsToDouble(sharedPreferences.getLong(BUDGET_KEY, 0));
    }

    public static double calculateDailySpendingLimit(double budget) {
        // Calculate the daily spending limit based on the budget
        return budget / 30;
    }

    public static double calculateDailySpendingLimitForCurrentMonth(double budget, String a) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Get the last day of the current month
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        // Calculate the number of days left in the current month
        long daysLeftInMonth = ChronoUnit.DAYS.between(currentDate, lastDayOfMonth);

        // Cast the number of days to an integer
        int daysLeftInMonthInt = (int) daysLeftInMonth;

        // Calculate the daily spending limit
        return (double) (budget / daysLeftInMonthInt);
    }
    public static double calculateDailySpendingLimitForCurrentMonth( double totalSpending) {

        double budget = getBudget();
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Get the last day of the current month
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        // Calculate the number of days left in the current month
        long daysLeftInMonth = ChronoUnit.DAYS.between(currentDate, lastDayOfMonth);

        // Cast the number of days to an integer
        int daysLeftInMonthInt = (int) daysLeftInMonth;

        double moneyLeft = budget - totalSpending;
        double dailyLimit = moneyLeft / daysLeftInMonthInt;

        // Calculate the daily spending limit
        return (dailyLimit);
    }

}
