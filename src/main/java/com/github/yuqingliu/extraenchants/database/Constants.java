package com.github.yuqingliu.extraenchants.database;

public class Constants {
    private static boolean allowReEnchanting = false;
    private static int hiddenEnchantsCount = 3;
    private static int maxEnchantLevel = 10;
    private static int bookshelfMultiplier = 5;
    private static int maxBookshelves = 15;

    public static boolean getAllowReEnchanting() {
        return allowReEnchanting;
    }

    public static void setAllowReEnchanting(boolean allowReEnchanting) {
        Constants.allowReEnchanting = allowReEnchanting;
    }

    public static int getHiddenEnchantsCount() {
        return hiddenEnchantsCount;
    }

    public static void setHiddenEnchantsCount(int hiddenEnchantsCount) {
        Constants.hiddenEnchantsCount = hiddenEnchantsCount;
    }

    public static int getMaxEnchantLevel() {
        return maxEnchantLevel;
    }

    public static void setMaxEnchantLevel(int maxEnchantLevel) {
        Constants.maxEnchantLevel = maxEnchantLevel;
    }

    public static int getBookshelfMultiplier() {
        return bookshelfMultiplier;
    }

    public static void setBookshelfMultiplier(int bookshelfMultiplier) {
        Constants.bookshelfMultiplier = bookshelfMultiplier;
    }

    public static int getMaxBookshelves() {
        return maxBookshelves;
    }

    public static void setMaxBookshelves(int maxBookshelves) {
        Constants.maxBookshelves = maxBookshelves;
    }
}


