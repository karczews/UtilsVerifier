/**
 * Copyright (c) 2017-present, UtilsVerifier Contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
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
