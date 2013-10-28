/**
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package savant.util;

import java.io.File;

/**
 * Event which is sent by asynchronous downloads.
 *
 * @author tarkvara
 */
public class DownloadEvent {
    public enum Type {
        STARTED,
        COMPLETED,
        FAILED,
        PROGRESS
    }

    final Type type;
    final double progress;
    final File file;
    final Throwable error;

    private DownloadEvent(Type type, double progress, File file, Throwable error) {
        this.type = type;
        this.progress = progress;
        this.file = file;
        this.error = error;
    }

    /**
     * A download event indicating that the process has started.
     */
    public DownloadEvent(Type type) {
        this(type, Double.NaN, null, null);
    }

    /**
     * A download event represent progress towards our goal.
     *
     * @param progress a value from 0.0 to 1.0 indicating the amount of progress completed
     */
    public DownloadEvent(double progress) {
        this(Type.PROGRESS, progress, null, null);
    }

    /**
     * A download event representing successful completion of the download.
     *
     * @param file the destination file
     */
    public DownloadEvent(File file) {
        this(Type.COMPLETED, Double.NaN, file, null);
    }

    /**
     * A download event indicating that the download has failed.
     *
     * @param error the exception which caused the download to fail
     */
    public DownloadEvent(Throwable error) {
        this(Type.FAILED, Double.NaN, null, error);
    }

    public Type getType() {
        return type;
    }

    public double getProgress() {
        return progress;
    }

    public File getFile() {
        return file;
    }

    public Throwable getError() {
        return error;
    }
}
