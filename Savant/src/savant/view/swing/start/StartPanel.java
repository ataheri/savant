/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StartPage.java
 *
 * Created on Mar 28, 2011, 8:09:04 PM
 */
package savant.view.swing.start;

import com.jidesoft.swing.AutoResizingTextArea;
import com.jidesoft.swing.JideBoxLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import savant.api.util.DialogUtils;
import savant.controller.RecentProjectsController;
import savant.controller.RecentTracksController;
import savant.settings.BrowserSettings;
import savant.settings.ColourSettings;
import savant.settings.DirectorySettings;
import savant.settings.PersistentSettings;
import savant.swing.component.HyperlinkButton;
import savant.util.DownloadFile;
import savant.view.icon.SavantIconFactory;
import savant.view.swing.ProjectHandler;
import savant.view.swing.Savant;

/**
 *
 * @author mfiume
 */
public class StartPanel extends javax.swing.JPanel implements ComponentListener {

    static Color bgcolor = Color.darkGray;
    static Color subpanelbgcolortop = new Color(50,50,50);//
    static Color subpanelbgcolor = new Color(10,10,10);
    static Color textcolortop = new Color(0,77,250); //new Color(0,47,125);
    static Color textcolor = new Color(240,240,240);
    static Color outlinecolor = new Color(10,10,10);

    private StartSubPanel recentTracksPanel;
    private StartSubPanel recentProjectsPanel;
    private StartSubPanel helpPanel;
    private StartSubPanel newsPanel;
    private JCheckBox dontShowStartPageButton;

    /** Creates new form StartPage */
    public StartPanel() {

        initComponents();
        init();
        configureSizes();

        this.addComponentListener(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 787, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 555, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void init() {
        this.setBackground(bgcolor);
        this.setOpaque(false);

        recentTracksPanel = new StartSubPanel("Recent Tracks", getRecentTracksInnerPanel());
        recentProjectsPanel = new StartSubPanel("Recent Projects", getRecentProjectsInnerPanel());
        newsPanel = new StartSubPanel("Latest News", getNewsInnerPanel());
        helpPanel = new StartSubPanel("Become a Genome Savant", getHelpPanel());

        this.add(recentTracksPanel);
        this.add(recentProjectsPanel);
        this.add(newsPanel);
        //this.add(helpPanel);

        dontShowStartPageButton = new JCheckBox("Don't show Start Page");
        dontShowStartPageButton.setOpaque(false);
        dontShowStartPageButton.setForeground(textcolor);
        dontShowStartPageButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                BrowserSettings.setShowStartPage(!dontShowStartPageButton.isSelected());
                try {
                    PersistentSettings.getInstance().store();
                } catch (IOException ex) {
                    Logger.getLogger(StartPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        this.add(dontShowStartPageButton);
    }
    int buffer = 10;
    ImageIcon logo = SavantIconFactory.getInstance().getIcon("/savant/images/logo_large.png");
    ImageIcon hint_loadtracks = SavantIconFactory.getInstance().getIcon("/savant/images/hint_loadtracks.png");
    ImageIcon hint_activeplugins = SavantIconFactory.getInstance().getIcon("/savant/images/hint_activeplugins.png");
    ImageIcon hint_bookmarks = SavantIconFactory.getInstance().getIcon("/savant/images/hint_bookmarks.png");

    private void drawScaledImage(Graphics g, Image img, int startx, int starty, int width, int height) {
            g.drawImage(img,
                           startx, starty, startx+width, starty+height,
                           0, 0, logo.getImage().getWidth(this), logo.getImage().getHeight(this),
                           this);
    }

    @Override
    public void paintComponent(Graphics g) {
        
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
        RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(bgcolor);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        //centerImage(g, logo.getImage(), new Point(this.getWidth()/2, 50));

        //centerImage(g, logo.getImage()/*scaleImage(logo.getImage(),0.7)*/, new Point(this.getWidth()/2, this.getHeight()/2));

        int totalwidth = this.getWidth();
        int totalheight = this.getHeight();

        int sideoffset = (int) (totalwidth * percentsidemargin);
        int topoffset = (int) (totalheight * percenttopmargin);

        int unitheight = (int) (totalheight - (2 * topoffset) - vertsep) / 2;
        int unitwidth = (int) (totalwidth - (2 * sideoffset) - horsep) / 2;

        g.setFont(new Font("Tahoma", Font.BOLD, 60));
        FontMetrics fm = g.getFontMetrics();
        String bannerstr = "Savant Genome Browser";
        int bannershift = fm.stringWidth(bannerstr)/2;

        int stringx = totalwidth/2 + - bannershift;
        int stringy = topoffset+pushdownoffset- 20;
        //GradientPaint gp = new GradientPaint(stringx,stringy-fm.getMaxAscent(),textcolortop,stringx,stringy-fm.getMaxAscent()+40,textcolor);
        //((Graphics2D)g).setPaint(gp);
        g.setColor(textcolor);
        g.drawString(bannerstr, stringx, stringy);
        //drawScaledImage(g, logo.getImage(), totalwidth/2 - bannershift, topoffset-5, pushdownoffset, pushdownoffset);

        //g.drawImage(logo.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH), sideoffset, topoffset, this);

        /*
        g.drawImage(hint_loadtracks.getImage(), buffer, buffer, this);
        g.drawImage(hint_activeplugins.getImage(), buffer, this.getHeight() - hint_activeplugins.getIconHeight() - buffer, this);
        g.drawImage(hint_bookmarks.getImage(), this.getWidth() - hint_bookmarks.getIconWidth() - buffer, buffer, this);
         * 
         */

        //g.drawImage(hint_loadtracks.getImage().getScaledInstance(20, -1, Image.SCALE_DEFAULT), buffer, buffer, this);
    }

    public static void main(String[] argv) {
        JFrame f = new JFrame();
        f.setBackground(Color.darkGray);
        f.add(new StartPanel());
        f.setVisible(true);
    }

    private void centerImage(Graphics g, Image img, Point center) {
        int x = (int) center.getX() - img.getWidth(this) / 2;
        int y = (int) center.getY() - img.getHeight(this) / 2;
        g.drawImage(img, x, y, this);
    }

    private Image scaleImage(Image image, double d) {
        System.out.println("Width: " + image.getWidth(null) + " scaled width: " + ((int) (image.getHeight(null) * d)));
        return image.getScaledInstance((int) (image.getWidth(null) * d), (int) (image.getHeight(null) * d), Image.SCALE_SMOOTH);
    }
    
    int vertsep = 10;
    int horsep = 10;
    int pushdownoffset = 30;
    double percenttopmargin = 0.1;
    double percentsidemargin = 0.05;

    private void configureSizes() {
        int totalwidth = this.getWidth();
        int totalheight = this.getHeight();

        int sideoffset = (int) (totalwidth * percentsidemargin);
        int topoffset = (int) (totalheight * percenttopmargin);

        int unitheight = (int) (totalheight - (2 * topoffset) - vertsep) / 2;
        int unitwidth = (int) (totalwidth - (2 * sideoffset) - horsep) / 2;

        Dimension unitDim = new Dimension(unitwidth, unitheight);
        Dimension bigUnitDim = new Dimension(unitwidth, unitheight*2 + vertsep);

        placeComponent(recentTracksPanel, new Point(sideoffset, pushdownoffset+topoffset), unitDim);
        placeComponent(recentProjectsPanel, new Point(sideoffset, pushdownoffset+totalheight - topoffset - unitheight), unitDim);
        placeComponent(newsPanel, new Point(sideoffset + unitwidth + horsep, pushdownoffset + topoffset), bigUnitDim);
        placeComponent(helpPanel, new Point(sideoffset + unitwidth + horsep, pushdownoffset + totalheight - topoffset - unitheight), unitDim);
        placeComponent(dontShowStartPageButton, new Point(totalwidth-(int) dontShowStartPageButton.getWidth()-5,totalheight-(int) dontShowStartPageButton.getHeight()-5), dontShowStartPageButton.getPreferredSize() );
        //placeComponent(dontShowStartPageButton, new Point(totalwidth - (int) dontShowStartPageButton.getWidth() - sideoffset, totalheight - (int) dontShowStartPageButton.getHeight() - sideoffset), dontShowStartPageButton.getPreferredSize() );
    }

    private void placeComponent(JComponent c, Point p, Dimension dim) {
        c.setBounds(p.x, p.y, dim.width, dim.height);
        c.revalidate();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        configureSizes();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    private JPanel getRecentTracksInnerPanel() {

        JPanel pan = new JPanel();
        pan.setOpaque(false);
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

        try {
            List<String> tracks = RecentTracksController.getInstance().getRecentTracks();
            for (final String t : tracks) {

                pan.add(HyperlinkButton.createHyperlinkButton(t, StartPanel.textcolor, new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            Savant.getInstance().addTrackFromFile(t);
                        } catch (Exception ex) {
                            DialogUtils.displayError("Problem opening track from file " + t);
                        }
                    }
                }));
            }
            if (tracks.isEmpty()) {
                pan.add(createLabel("No recent tracks"));
            }
        } // Variables declaration - do not modify
        // End of variables declaration
        catch (IOException ex) {
            pan.add(createLabel("No recent tracks"));
        }

        return pan;
    }

    private JPanel getRecentProjectsInnerPanel() {

        JPanel pan = new JPanel();
        pan.setOpaque(false);
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

        try {
            List<String> projects = RecentProjectsController.getInstance().getRecentProjects();
            for (final String t : projects) {

                pan.add(HyperlinkButton.createHyperlinkButton(t, StartPanel.textcolor, new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            ProjectHandler.getInstance().loadProjectFrom(new File(t));
                        } catch (Exception ex) {
                            DialogUtils.displayError("Problem opening track from file " + t);
                        }
                    }
                }));
            }

            if (projects.isEmpty()) {
                pan.add(createLabel("No recent projects"));
            }
        } // Variables declaration - do not modify
        // End of variables declaration
        catch (IOException ex) {
        }

        return pan;
    }

    private JTextArea createTextArea(String lab) {
        JTextArea l = new JTextArea();
        l.setText(lab);
        l.setHighlighter(null);
        l.setLineWrap(true);
        l.setWrapStyleWord(true);
        l.setEditable(false);
        l.setForeground(StartPanel.textcolor);
        l.setOpaque(false);
        return l;
    }

    private JLabel createLabel(String lab) {
        JLabel l = new JLabel(lab);
        l.setOpaque(false);
        l.setForeground(StartPanel.textcolor);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);

        //l.setFont(new Font("Arial", Font.PLAIN, 12));
        return l;
    }

    private JComponent getNewsInnerPanel() {

        JPanel p = new JPanel();
        p.setBackground(Color.red);
        p.setOpaque(false);

        try {
            File newsFile = DownloadFile.downloadFile(new URL(BrowserSettings.NEWS_URL), DirectorySettings.getTmpDirectory());
            if (newsFile != null) {
                p = parseNewsFile(newsFile);
            }
            if (newsFile.exists()) { newsFile.delete(); }

        } catch (Exception ex) {
            p.add(this.createLabel("Problem getting news"));
        }

        return p;

    }

    private JPanel parseNewsFile(File newsFile) {
        JPanel p = null;

        try {

            p = new JPanel();
            p.setOpaque(false);
            //p.setBackground(Color.red);
            BoxLayout bl = new BoxLayout(p,BoxLayout.Y_AXIS);
            p.setLayout(bl);

            Document d = new SAXBuilder().build(newsFile);
            Element root = d.getRootElement();

            List<Element> newsEntries = root.getChildren("entry");

            for (Element e : newsEntries) {

                final String text = e.getChildText("description");

                AutoResizingTextArea ta = new AutoResizingTextArea(text);
                ta.setMaximumSize(new Dimension(99999,1));
                ta.setForeground(StartPanel.textcolor);
                ta.setLineWrap(true);
                ta.setHighlighter(new BlankHighlighter());
                ta.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        e.consume();
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        e.consume();
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        e.consume();
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        e.consume();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        e.consume();
                    }

                });
                ta.setOpaque(false);
                ta.setWrapStyleWord(true);
                ta.setEditable(false);

                JLabel title = new JLabel(e.getChildText("title"));
                title.setFont(new Font("Arial", Font.BOLD, 16));
                title.setForeground(StartPanel.textcolor);

                JLabel date = new JLabel(e.getChildText("date"));
                date.setFont(new Font("Arial", Font.ITALIC, 12));
                date.setForeground(StartPanel.textcolor);

                p.add(Box.createVerticalStrut(10));

                title.setAlignmentX(Component.LEFT_ALIGNMENT);
                p.add(title);
                date.setAlignmentX(Component.LEFT_ALIGNMENT);
                p.add(date);
                ta.setAlignmentX(Component.LEFT_ALIGNMENT);
                p.add(ta);
            }

            p.add(Box.createVerticalGlue());

        } catch (Exception e) {
            JLabel l = new JLabel("Problem getting news");
            l.setForeground(StartPanel.textcolor);
            p.add(l);
        }

        return p;
    }

    public JTextArea createTextAreaFitToText(String message, Dimension minimalSize) {

        JTextArea aMessagePanel = new JTextArea();
        aMessagePanel.setText(message);

        /*for modelToView to work, the text area has to be sized. It doesn't matter if it's visible or not.*/
        aMessagePanel.setPreferredSize(minimalSize);
        aMessagePanel.setSize(minimalSize);

        Rectangle r;
        try {
            r = aMessagePanel.modelToView(aMessagePanel.getDocument().getLength());
            Dimension d = new Dimension(minimalSize.width, r.y + r.height);
            aMessagePanel.setPreferredSize(d);
        } catch (BadLocationException ex) {
        }

        return aMessagePanel;

    }

    private void p(StyledDocument doc, String text) throws BadLocationException {
        doc.insertString(doc.getLength(), text, getParagraphAttributes());
    }

    public static SimpleAttributeSet getParagraphAttributes() {
        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setFontSize(att, 12);
        return att;
    }

    private void wrapLabelText(JLabel label, String text) {

        System.out.println("Wrapping text: " + text);

        FontMetrics fm = label.getFontMetrics(label.getFont());
        Container container = label.getParent();
        int containerWidth = container.getWidth();

        System.out.println("Container width: " + containerWidth);

        BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(text);

        StringBuffer trial = new StringBuffer();
        StringBuilder real = new StringBuilder("<html>");

        int start = boundary.first();
        for (int end = boundary.next(); end != BreakIterator.DONE;
                start = end, end = boundary.next()) {
            String word = text.substring(start, end);
            System.out.println(word);

            trial.append(word);
            int trialWidth = SwingUtilities.computeStringWidth(fm,
                    trial.toString());
            if (trialWidth > containerWidth) {
                trial = new StringBuffer(word);
                real.append("<br>");
            }
            real.append(word);
        }

        real.append("</html>");

        label.setText(real.toString());
        System.out.println("Sone wrapping text: " + real.toString());


    }

    private JComponent getHelpPanel() {
        JPanel pan = new JPanel();
        pan.setOpaque(false);
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

        pan.add(HyperlinkButton.createHyperlinkButton("Video Tutorials", StartPanel.textcolor, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create(BrowserSettings.DOCUMENTATION_URL));
                } catch (IOException ex) {
                    DialogUtils.displayError("Uh oh", "Could not open browser");
                }
            }
        }));

        pan.add(HyperlinkButton.createHyperlinkButton("Useful Shortcuts", StartPanel.textcolor, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create(BrowserSettings.SHORTCUTS_URL));
                } catch (IOException ex) {
                    DialogUtils.displayError("Uh oh", "Could not open browser");
                }
            }
        }));


        return pan;
    }


    class BlankHighlighter extends DefaultHighlighter {

        private boolean enabled = false;

        public void enableHighlighting(boolean enable) {
            this.enabled = enable;
        }

        public Object addHighlight(int p0, int p1, Highlighter.HighlightPainter p) throws BadLocationException {
            return (enabled) ? super.addHighlight(p0, p1, p) : null;
        }
    };

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
