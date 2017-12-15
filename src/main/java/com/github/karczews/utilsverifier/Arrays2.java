package com.github.karczews.utilsverifier;

/**
 * Internal tool that contains convenience methods to work with arrays.
 */
final class Arrays2 {
    private Arrays2() {
    }

    /**
     * Performs a check if the specified item is contained in the specified
     * array.
     *
     * @param item will be checked if it's in the array
     * @param array checked if contains the specified item
     * @param <T> type of the array elements
     * @return true if the array contains specified item, false otherwise
     * @throws NullPointerException if the passed array is null
     */
    public static <T> boolean contains(final T item, final T[] array) {
        for (int index = 0; index < array.length; index++) {
            if (array[index] == item)
                return true;
        }
        return false;
    }
}
