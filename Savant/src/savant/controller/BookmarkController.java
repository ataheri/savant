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

package savant.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import savant.controller.event.BookmarksChangedEvent;
import savant.controller.event.BookmarksChangedListener;
import savant.util.Bookmark;
import savant.util.Range;


/**
 * Controller object to manage changes to bookmarks.
 *
 * @author mfiume
 */
public class BookmarkController {
    private static final Log LOG = LogFactory.getLog(RangeController.class);


    private static BookmarkController instance;

    private List<Bookmark> bookmarks;

    private List<BookmarksChangedListener> favoritesChangedListeners;

    public static synchronized BookmarkController getInstance() {
        if (instance == null) {
            instance = new BookmarkController();
        }
        return instance;
    }

    private BookmarkController() {
        favoritesChangedListeners = new ArrayList<BookmarksChangedListener>();
        bookmarks = new ArrayList<Bookmark>();
    }

    public List<Bookmark> getBookmarks() {
        return this.bookmarks;
    }

    public void addBookmark(Bookmark f) {
        if(this.bookmarks == null || this.bookmarks.isEmpty()) { this.bookmarks = new ArrayList<Bookmark>(); }
        this.bookmarks.add(f);
        this.fireBookmarksChangedEvent(f,true);
    }

    public void addBookmarks(List<Bookmark> bkmks){
        for (Bookmark b : bkmks) {
            addBookmark(b);
        }
        //System.out.println("Adding bookmark : " + f.getRange() );
        //this.bookmarks.add(f);
    }
    
     private static Bookmark parseBookmark(String line) {

        StringTokenizer st = new StringTokenizer(line,"\t");

        String ref = st.nextToken();
        int from = Integer.parseInt(st.nextToken());
        int to = Integer.parseInt(st.nextToken());
        String annotation = "";

        while (st.hasMoreElements()) {
            annotation += st.nextToken() + " ";
        }
        annotation.trim();

        return new Bookmark(ref, new Range(from,to), annotation);
    }

     public void addBookmarksFromFile(File f) throws FileNotFoundException, IOException {

        BufferedReader br = new BufferedReader(new FileReader(f));

        String line = "";

        List<Bookmark> newBookmarks = new ArrayList<Bookmark>();

        while ((line = br.readLine()) != null) {
            newBookmarks.add(parseBookmark(line));
        }

        //bookmarks.addAll(newBookmarks);
        addBookmarks(newBookmarks);

        br.close();
    }

    public void removeBookmark() {
        this.removeBookmark(this.bookmarks.size()-1);
    }

    public void removeBookmark(int index) {
        try {
            LOG.info("Bookmark removed.");
            Bookmark b = this.bookmarks.get(index);
            this.bookmarks.remove(index);
            this.fireBookmarksChangedEvent(b,false);
        } catch(Exception e) {}
    }

    /**
     * Fire the RangeChangedEvent
     */
    private synchronized void fireBookmarksChangedEvent(Bookmark bkmk, boolean isAdded) {
        BookmarksChangedEvent evt = new BookmarksChangedEvent(this, bkmk, isAdded);
        for (BookmarksChangedListener listener : this.favoritesChangedListeners) {
            listener.bookmarksChangeReceived(evt);
        }
    }

    public synchronized void addFavoritesChangedListener(BookmarksChangedListener l) {
        favoritesChangedListeners.add(l);
    }

    public synchronized void removeFavoritesChangedListener(BookmarksChangedListener l) {
        favoritesChangedListeners.remove(l);
    }

    public void addCurrentRangeToBookmarks() {
        RangeController rc = RangeController.getInstance();
        if (rc.getRange() != null) {
            this.addBookmark(new Bookmark(ReferenceController.getInstance().getReferenceName(), rc.getRange()));
        }
    }

    public Bookmark getBookmark(int index) {
        return this.bookmarks.get(index);
    }

    public void clearBookmarks() {
        this.bookmarks.clear();
    }
}