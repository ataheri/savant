/*
 * DataSource.java
 * Created on Aug 23, 2010
 *
 *
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

package savant.data.sources;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Set;

import savant.api.adapter.RangeAdapter;
import savant.data.types.Record;
import savant.file.DataFormat;
import savant.util.Resolution;


/**
 * Interface for a data source which contains records associated with a reference sequence.
 *
 * @param <E> record type
 */
public interface DataSource<E extends Record> {

    /**
     * Get the list of references for which this RecordTrack contains data
     * @return A set of reference names
     */
    public Set<String> getReferenceNames();

    /**
     * Get all records in the given range at the given resolution
     *
     * @param reference the reference sequence name for which to fetch records
     * @param range
     * @param resolution
     * @return an ordered list of records
     */
    public List<E> getRecords(String reference, RangeAdapter range, Resolution resolution) throws IOException;

    public URI getURI();

    public String getName();
    
    /**
     * Close the source
     */
    public void close();

    public DataFormat getDataFormat();

    /**
     * For some DataSources (currently just the BedSQLDataSource) it's useful to provide
     * access to extra out-of-band data.
     */
    public Object getExtraData();
}