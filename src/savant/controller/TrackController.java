/*
 *    Copyright 2010 University of Toronto
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

/*
 * TrackController.java
 * Created on Mar 11, 2010
 */

package savant.controller;

import savant.controller.event.TrackListChangedEvent;
import savant.controller.event.TrackListChangedListener;
import savant.data.sources.DataSource;

import java.util.ArrayList;
import java.util.List;
import savant.view.swing.ViewTrack;

/**
 * Singleton controller class to manage data tracks.
 */
public class TrackController {

    /**
     * Singleton instance. Use getInstance() to get a reference.
     */
    private static TrackController instance;

    // list of currently managed tracks
    private List<DataSource> tracks;

    private List<TrackListChangedListener> listeners;

    /**
     * Constructor. Private access, use getInstance() instead.
     */
    private TrackController() {
        this.tracks = new ArrayList<DataSource>();
        this.listeners = new ArrayList<TrackListChangedListener>();
    }

    public static synchronized TrackController getInstance() {
        if (instance == null) instance = new TrackController();
        return instance;
    }

    public void addTrack(DataSource track) {
        tracks.add(track);
        fireTracksChangedEvent();
    }

    public void removeDataSource(DataSource dataSource) {
        tracks.remove(dataSource);
        fireTracksChangedEvent();
    }

    public List<DataSource> getDataSources() {
        return this.tracks;
    }

    public DataSource getTrack(int index) {
        return tracks.get(index);
    }
    
    public synchronized void addTrackListChangedListener(TrackListChangedListener listener) {
        listeners.add(listener);
    }

    public synchronized void removeTrackListChangedListener(TrackListChangedListener listener) {
        listeners.remove(listener);
    }

    private void fireTracksChangedEvent() {
        TrackListChangedEvent evt = new TrackListChangedEvent(this, this.tracks);
        for (TrackListChangedListener listener : listeners) {
            listener.trackListChangeReceived(evt);
        }
    }
}
