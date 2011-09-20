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

package savant.view.swing.continuous;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import savant.controller.LocationController;
import savant.controller.SelectionController;
import savant.data.event.DataRetrievalEvent;
import savant.data.types.GenericContinuousRecord;
import savant.data.types.Record;
import savant.exception.RenderingException;
import savant.util.AxisRange;
import savant.util.ColourKey;
import savant.util.ColourScheme;
import savant.util.DrawingInstruction;
import savant.util.Range;
import savant.view.swing.GraphPane;
import savant.view.swing.TrackRenderer;


/**
 * Class to render continuous tracks.
 *
 * @author vwilliams, tarkvara
 */
public class ContinuousTrackRenderer extends TrackRenderer {
    private static final Log LOG = LogFactory.getLog(ContinuousTrackRenderer.class);

    public ContinuousTrackRenderer() {
    }

    @Override
    public void dataRetrievalCompleted(DataRetrievalEvent evt) {
        float[] extremes = ContinuousTrack.getExtremeValues(evt.getData());
        Range range = (Range)instructions.get(DrawingInstruction.RANGE);

        // Range will be null if we're a coverage track which is hidden by the corresponding .bam track.
        if (range != null) {
            addInstruction(DrawingInstruction.AXIS_RANGE, AxisRange.initWithRanges(range, new Range(Math.min(0, (int) Math.floor(extremes[0]*1.05)), Math.max(0,(int) Math.ceil(extremes[1]*1.05)))));
        }
        super.dataRetrievalCompleted(evt);
    }

    @Override
    public void render(Graphics2D g2, GraphPane gp) throws RenderingException {

        renderPreCheck();

        AxisRange axisRange = (AxisRange)instructions.get(DrawingInstruction.AXIS_RANGE);
        gp.setXRange(axisRange.getXRange());
        gp.setYRange(axisRange.getYRange());

        if (gp.needsToResize()) return;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ColourScheme cs = (ColourScheme)instructions.get(DrawingInstruction.COLOR_SCHEME);
        Color fillcolor = cs.getColor(ColourKey.CONTINUOUS_FILL);
        Color linecolor = cs.getColor(ColourKey.CONTINUOUS_LINE);

        GeneralPath path = new GeneralPath();
        double xFormXPos = Double.NaN, xFormYPos = Double.NaN;

        double yPixel0 = gp.transformYPos(0.0);
        LOG.debug("h=" + gp.getHeight() + ", yMin=" + gp.getYRange().getFrom() + ", unitHeight=" + gp.getUnitHeight() + " ➤ yPixel0=" + yPixel0);
        
        double maxData = 0;
        boolean haveOpenPath = false;
        boolean haveData = false;
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                GenericContinuousRecord continuousRecord = (GenericContinuousRecord)data.get(i);
                int xPos = continuousRecord.getPosition();
                float yPos = continuousRecord.getValue();
                if (Float.isNaN(yPos)) {
                    // Hit a position with no data.  May need to close off the current path.
                    if (haveOpenPath) {
                        path.lineTo(xFormXPos, yPixel0);
                        path.closePath();
                        haveOpenPath = false;
                    }
                } else {
                    haveData = true;
                    xFormXPos = gp.transformXPos(xPos);//+gp.getUnitWidth()/2;
                    xFormYPos = gp.transformYPos(yPos);
                    if (!haveOpenPath) {
                        // Start our path off with a vertical line.
                        path.moveTo(xFormXPos, yPixel0);
                        haveOpenPath = true;
                    }
                    path.lineTo(xFormXPos, xFormYPos);
                    Rectangle2D rec = new Rectangle2D.Double(xFormXPos - ((xFormXPos-path.getCurrentPoint().getX())/2),0,Math.max(xFormXPos-path.getCurrentPoint().getX(), 1),gp.getHeight());
                    recordToShapeMap.put(continuousRecord, rec);
                    xFormXPos = gp.transformXPos(xPos + 1);
                    path.lineTo(xFormXPos, xFormYPos);
                }
                if (yPos > maxData) {
                    maxData = yPos;
                }
            }
        }
        if (!haveData) {
            throw new RenderingException("No data in range", 1);
        }
        if (haveOpenPath) {
            // Path needs to be closed.
            path.lineTo(xFormXPos, yPixel0);
            path.closePath();
        }
        
        g2.setColor(fillcolor);
        g2.fill(path);
        g2.setColor(linecolor);
        g2.draw(path);

        if (axisRange.getYRange().getFrom() < 0) {
            g2.setColor(Color.darkGray);
            g2.draw(new Line2D.Double(0.0, yPixel0, gp.getWidth(), yPixel0));
        }
    }

    /**
     * Current selected shapes.
     */
    @Override
    public List<Shape> getCurrentSelectedShapes(GraphPane gp){
        List<Shape> shapes = new ArrayList<Shape>();
        List<Record> currentSelected = SelectionController.getInstance().getSelectedFromList(trackName, LocationController.getInstance().getRange(), data);
        for(int i = 0; i < currentSelected.size(); i++){
            shapes.add(continuousRecordToEllipse(gp, currentSelected.get(i)));
        }
        return shapes;
    }

    public static Shape continuousRecordToEllipse(GraphPane gp, Record o){
        GenericContinuousRecord rec = (GenericContinuousRecord) o;
        Double x = gp.transformXPos(rec.getPosition()) + (gp.getUnitWidth()/2) -4;
        Double y = gp.transformYPos(rec.getValue()) -4;// + (this.getUnitWidth()/2);
        Shape s = new Ellipse2D.Double(x, y, 8, 8);
        return s;
    }
}