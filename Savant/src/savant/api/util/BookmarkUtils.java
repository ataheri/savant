/*
 *    Copyright 2010-2011 University of Toronto
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package savant.api.util;

import java.util.ArrayList;
import java.util.List;

import savant.api.adapter.BookmarkAdapter;
import savant.api.adapter.RangeAdapter;
import savant.controller.BookmarkController;
import savant.controller.event.BookmarksChangedListener;
import savant.util.Bookmark;
import savant.util.Range;


/**
 * Utilities for Savant bookmarks
 *
 * @author mfiume
 */
public class BookmarkUtils {

    /**
     * Get a list of the current bookmarks
     * @return A list of bookmarks
     */
    public static List<BookmarkAdapter> getBookmarks() {
        List<BookmarkAdapter> r = new ArrayList<BookmarkAdapter>();
        for (BookmarkAdapter b : BookmarkController.getInstance().getBookmarks()) {
            r.add((BookmarkAdapter) b);
        }
        return r;
    }

    /**
     * Add a bookmark
     * @param f The bookmark to add
     */
    public static void addBookmark(BookmarkAdapter f) {
        BookmarkController.getInstance().addBookmark((Bookmark) f);
    }

    /**
     * Remove the last bookmark
     */
    public static void removeLastBookmark() {
        BookmarkController.getInstance().removeBookmark();
    }

    /**
     * Remove some bookmark
     * @param index Index of the bookmark to remove
     */
    public static void removeBookmark(int index) {
        BookmarkController.getInstance().removeBookmark(index);
    }

    /**
     * Subscribe a listener to be notified when the Bookmarks list changes
     * @param l The listener to subscribe
     */
    public static void addBookmarksChangedListener(BookmarksChangedListener l) {
        BookmarkController.getInstance().addFavoritesChangedListener(l);
    }

    /**
     * Unsubscribe a listener from being notified when the Bookmarks list changes
     * @param l The listener to unsubscribe
     */
    public static void removeFavoritesChangedListener(BookmarksChangedListener l) {
        BookmarkController.getInstance().removeFavoritesChangedListener(l);
    }

    /**
     * Add the current range as a bookmark
     */
    public static void addCurrentRangeToBookmarks() {
        BookmarkController.getInstance().addCurrentRangeToBookmarks();
    }

    /**
     * Get a bookmark from the bookmark list
     * @param index The index of the bookmark to get
     * @return The bookmark at the specified index
     */
    public static BookmarkAdapter getBookmark(int index) {
        return (BookmarkAdapter)  BookmarkController.getInstance().getBookmark(index);
    }

    /**
     * Clear the bookmarks list
     */
    public static void clearBookmarks() {
        BookmarkController.getInstance().clearBookmarks();
    }

    /**
     * Factory method for creating new BookmarkAdapter objects.
     */
    public static BookmarkAdapter createBookmark(String ref, RangeAdapter range) {
        return new Bookmark(ref, (Range)range);
    }

    /**
     * Factory method for creating new BookmarkAdapter objects.
     */
    public static BookmarkAdapter createBookmark(String ref, RangeAdapter range, String ann) {
        return new Bookmark(ref, (Range)range, ann);
    }
}
