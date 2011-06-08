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

package savant.selection;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import savant.controller.BookmarkController;
import savant.controller.ReferenceController;
import savant.data.sources.DataSource;
import savant.data.types.*;
import savant.file.DataFormat;
import savant.util.Bookmark;
import savant.util.MiscUtils;
import savant.util.Range;
import savant.view.swing.GraphPane;
import savant.view.swing.Track;

/**
 *
 * @author AndrewBrook
 */
public class PopupPanel extends JPanel {

    protected GraphPane gp;
    protected String mode;
    protected DataFormat fileFormat;
    protected Record record;

    //info
    protected String name;
    protected String ref;
    protected int start;
    protected int end;

    //additional fields
   // protected List<String> addedFields;

    public static PopupPanel create(GraphPane parent, String mode, DataSource dataSource, Record rec){

        PopupPanel p = null;
        switch(dataSource.getDataFormat()) {
            case POINT_GENERIC:
                p = new PointGenericPopup((GenericPointRecord) rec);
                break;
            case INTERVAL_BAM:
                if(!mode.equals("SNP")){
                    p = new IntervalBamPopup((BAMIntervalRecord)rec);
                }
                break;
            case INTERVAL_BED:
                if(!mode.equals("SQUISH")){
                    p = new IntervalBedPopup((BEDIntervalRecord)rec);
                }
                break;
            case INTERVAL_GENERIC:
                p = new IntervalGenericPopup((GenericIntervalRecord)rec);
                break;
            case CONTINUOUS_GENERIC:
                p = new ContinuousPopup((GenericContinuousRecord)rec);
                break;
            case TABIX:
                p = new TabixPopup((TabixIntervalRecord)rec, dataSource);
                break;
            default:
                break;
        }

        if(p != null) p.init(parent, mode, dataSource.getDataFormat(), rec);
        return p;
    }

    protected void init(GraphPane parent, String mode, DataFormat ff, Record rec){

        this.fileFormat = ff;
        this.mode = mode;
        this.gp = parent;
        this.record = rec;

        this.setBackground(Color.WHITE);
        this.setLayout(new GridLayout(0,1));

        calculateInfo();
        initInfo();
        initStandardButtons();
        initSpecificButtons();

    }

    protected void initStandardButtons(){

        //START OF BUTTONS
        JPanel filler = new JPanel();
        filler.setPreferredSize(new Dimension(5,5));
        filler.setSize(new Dimension(5,5));
        filler.setBackground(Color.WHITE);
        this.add(filler);
        this.add(new JSeparator());

        //SELECT
        final JLabel select = new JLabel("Select/Deselect");
        select.setForeground(Color.BLUE);
        select.setCursor(new Cursor(Cursor.HAND_CURSOR));
        select.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Track t: gp.getTracks()) {
                    if (t.getDataFormat() == fileFormat){
                        t.getRenderer().addToSelected(record);
                        break;
                    }
                }
                gp.repaint();
            }
        });
        this.add(select);

        //BOOKMARKING
        if(ref == null){
            ref = ReferenceController.getInstance().getReferenceName();
        }
        if(start != -1 && end != -1){
            JLabel bookmark = new JLabel("Add to Bookmarks");
            bookmark.setForeground(Color.BLUE);
            bookmark.setCursor(new Cursor(Cursor.HAND_CURSOR));
            bookmark.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ref = homogenizeRef(ref);
                    if(name != null){
                        BookmarkController.getInstance().addBookmark(new Bookmark(ref, new Range(start, end), name));
                    } else {
                        BookmarkController.getInstance().addBookmark(new Bookmark(ref, new Range(start, end)));
                    }
                }
            });
            this.add(bookmark);
        }
    }

    protected void initSpecificButtons(){};

    //default...should be overridden in subclasses
    protected void calculateInfo(){
        name = null;
        ref = null;
        start = -1;
        end = -1;
    };

    protected void initInfo(){};

    protected String homogenizeRef(String orig){
        if(!ReferenceController.getInstance().getAllReferenceNames().contains(orig)){
            Iterator<String> it = ReferenceController.getInstance().getAllReferenceNames().iterator();
            while(it.hasNext()){
                String current = it.next();
                if(MiscUtils.homogenizeSequence(current).equals(orig)){
                    return current;
                }
            }
        }
        return orig;
    }

    public void addField(String text){
        this.add(new JSeparator());
        this.add(new JLabel(text));
    }

    public Record getRecord(){
        return record;
    }

}