//License: GPL. For details, see Readme.txt file.
//package org.openstreetmap.gui.jmapviewer;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;
import org.openstreetmap.gui.jmapviewer.interfaces.TileLoader;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.BingAerialTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;
import org.openstreetmap.gui.jmapviewer.*;

import java.awt.Color;
import java.awt.image.ColorModel;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;



/**
 * Demonstrates the usage of {@link JMapViewer}
 *
 * @author Jan Peter Stotz
 *
 */
public class Demo extends JFrame implements JMapViewerEventListener {

    private static final long serialVersionUID = 1L;

    private final JMapViewerTree treeMap;

    private final JLabel zoomLabel;
    private final JLabel zoomValue;

    private final JLabel mperpLabelName;
    private final JLabel mperpLabelValue;


    public static String [] pubtime = new String[523];
    public static double [] pubmag = new double[523];
    public static double [] pubnorth = new double[523];
    public static double [] pubeast = new double[523];
    public static String [] publoca = new String[523];
    
    
    
    
    /**
     * @param args Main program arguments
     */
    public static void main(String[] args) {
        Demo d = new Demo();
        d.setVisible(true);
        
    }

    /**
     * Constructs the {@code Demo}.
     */
    public Demo() {
        super("JMapViewer Demo");
        setSize(400, 400);
        
        treeMap = new JMapViewerTree("Zones");

        // Listen to the map viewer for user operations so components will
        // receive events and update
        map().addJMVListener(this);

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelTop = new JPanel();
        JPanel panelBottom = new JPanel();
        JPanel helpPanel = new JPanel();

        mperpLabelName = new JLabel("Meters/Pixels: ");
        mperpLabelValue = new JLabel(String.format("%s", map().getMeterPerPixel()));

        zoomLabel = new JLabel("Zoom: ");
        zoomValue = new JLabel(String.format("%s", map().getZoom()));

        add(panel, BorderLayout.NORTH);
        add(helpPanel, BorderLayout.SOUTH);
        panel.add(panelTop, BorderLayout.NORTH);
        panel.add(panelBottom, BorderLayout.SOUTH);
        JLabel helpLabel = new JLabel("Use right mouse button to move,\n "
                + "left double click or mouse wheel to zoom.");
        helpPanel.add(helpLabel);
        JButton button = new JButton("setDisplayToFitMapMarkers");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map().setDisplayToFitMapMarkers();
            }
        });
        JComboBox<TileSource> tileSourceSelector = new JComboBox<>(new TileSource[] {
                new OsmTileSource.Mapnik(),
                //new OsmTileSource.CycleMap(),
                new BingAerialTileSource(),
        });
        tileSourceSelector.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                map().setTileSource((TileSource) e.getItem());
            }
        });
        JComboBox<TileLoader> tileLoaderSelector;
        tileLoaderSelector = new JComboBox<>(new TileLoader[] {new OsmTileLoader(map())});
        tileLoaderSelector.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                map().setTileLoader((TileLoader) e.getItem());
            }
        });
        map().setTileLoader((TileLoader) tileLoaderSelector.getSelectedItem());
        panelTop.add(tileSourceSelector);
        panelTop.add(tileLoaderSelector);
        final JCheckBox showMapMarker = new JCheckBox("Map markers visible");
        showMapMarker.setSelected(map().getMapMarkersVisible());
        showMapMarker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map().setMapMarkerVisible(showMapMarker.isSelected());
            }
        });
        panelBottom.add(showMapMarker);
        ///
        final JCheckBox showTreeLayers = new JCheckBox("Tree Layers visible");
        showTreeLayers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                treeMap.setTreeVisible(showTreeLayers.isSelected());
            }
        });
        panelBottom.add(showTreeLayers);
        ///
        final JCheckBox showToolTip = new JCheckBox("ToolTip visible");
        showToolTip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map().setToolTipText(null);
            }
        });
        panelBottom.add(showToolTip);
        ///
        final JCheckBox showTileGrid = new JCheckBox("Tile grid visible");
        showTileGrid.setSelected(map().isTileGridVisible());
        showTileGrid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map().setTileGridVisible(showTileGrid.isSelected());
            }
        });
        panelBottom.add(showTileGrid);
        final JCheckBox showZoomControls = new JCheckBox("Show zoom controls");
        showZoomControls.setSelected(map().getZoomControlsVisible());
        showZoomControls.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //map().setZoomContolsVisible(showZoomControls.isSelected());
            }
        });
        panelBottom.add(showZoomControls);
        final JCheckBox scrollWrapEnabled = new JCheckBox("Scrollwrap enabled");
        scrollWrapEnabled.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map().setScrollWrapEnabled(scrollWrapEnabled.isSelected());
            }
        });
        panelBottom.add(scrollWrapEnabled);
        panelBottom.add(button);

        panelTop.add(zoomLabel);
        panelTop.add(zoomValue);
        panelTop.add(mperpLabelName);
        panelTop.add(mperpLabelValue);
        add(treeMap, BorderLayout.CENTER);

		//LayerGroup koreaGroup = new LayerGroup("Korea");
		//MapMarkerDot seoul = new MapMarkerDot(37.56, 126.98);
       
        //05.16 CSV파일 불러와서 마커로 찍기..
        
		Layer korea = treeMap.addLayer("Korea");
        map().addMapMarker(new MapMarkerCircle(korea, "Seoul", new Coordinate(37.56, 126.98), 0.5));
        map().addMapMarker(new MapMarkerDot(korea, 37.56, 126.98));
        treeMap.getViewer().setDisplayPosition(new Coordinate(37.56, 126.98), 7);
        
        /*
        map().addMapMarker(new MapMarkerCircle(korea, "Busan", new Coordinate(35.18, 129.07), 0.05));
        map().addMapMarker(new MapMarkerDot(korea, 35.18 , 129.07));
        treeMap.getViewer().setDisplayPosition(new Coordinate(35.18 , 129.07),7);
      
        map().addMapMarker(new MapMarkerCircle(korea, "Daejeon", new Coordinate(36.35, 127.38), 0.05));
        map().addMapMarker(new MapMarkerDot(korea, 36.35, 127.38));
        treeMap.getViewer().setDisplayPosition(new Coordinate(36.35, 127.38),7);
        
        map().addMapMarker(new MapMarkerCircle(korea, "Daegu", new Coordinate( 35.87 , 128.55), 0.05));
        map().addMapMarker(new MapMarkerDot(korea, 35.87 , 128.55));
        treeMap.getViewer().setDisplayPosition(new Coordinate(35.87 , 128.55),7);
       */
        
        BufferedReader br = null;
		String line;
		
		String path = "C:\\Users\\dkxmp\\Documents\\Java project\\2016037069 한재윤 JAVA 중간고사 대체과제\\EQdata.csv";
		//절대경로이므로 다른 컴퓨터에서 실행시 파일 경로로 다시 수정하셔야 합니다. 
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			br.readLine();
			//br = new BufferedReader(new FileReader(path));
			
			while((line = br.readLine()) != null) {
				int i=0;
				String[] temp = line.split(","); // 쉼표로 구분
				
				String storeT = temp[0];
				String storeM = temp[1];
				String storeN = temp[2];
				String storeE = temp[3];
				String storeC = temp[4];

				String [] storeNorth = storeN.split("N");
				String [] storeEast = storeE.split("E");
				String [] storeMagnitude = storeM.split("");
				//String [] storeCity = storeC.split("");
				//String [] storeCitydiv = storeC.split(" ");
				
				double x1 = Double.valueOf(storeNorth[i]);
				double y1 = Double.valueOf(storeEast[i]);
				double m1 = Double.valueOf(storeMagnitude[i]);
				
				//pubtime[i] = temp[0];
			    //pubmag[i] = m1;
			    //pubnorth[i] = x1;
			    //pubeast[i] = y1;
			    //publoca[i] = temp[4];
				
				System.out.print(x1);
				System.out.print(y1);
				System.out.print("T: " + temp[0]);
				System.out.print("\n");
				System.out.print("C: " + temp[4]);
				System.out.print("\n");
				System.out.print("STORE: " + storeT);
				System.out.print("\n");
				
				
				map().addMapMarker(new MapMarkerCircle(korea, "" , new Coordinate(x1 , y1), 0 ));
		        map().addMapMarker(new MapMarkerDot(korea, x1 , y1));
		        //map().setToolTipText(storeT + ", " + storeMagnitude[i] + ", " + x1 + ", " + y1 + ", " +storeC);  
		        //map().setToolTipText(pubtime[i] + ", " + pubmag[i] + ", " + pubnorth[i] + ", " + pubeast[i] + ", " +publoca[i]);
		        treeMap.getViewer().setDisplayPosition(new Coordinate(x1 , y1),7);
				i = i+1;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	
		map().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    map().getAttribution().handleAttribution(e.getPoint(), true);
                }

            }
        });
		
        map().addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                boolean cursorHand = map().getAttribution().handleAttributionCursor(p);
                if (cursorHand) {
                    map().setCursor(new Cursor(Cursor.HAND_CURSOR));
                    
                } else {
                    map().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
                
                if (showToolTip.isSelected()) map().setToolTipText(map().getPosition(p).toString());
              
            }
        });
    }
    
    
    private boolean Coordinate(double x1, double y1) {
		// TODO Auto-generated method stub
		return false;
	}

	private JMapViewer map() {
        return treeMap.getViewer();
    }


	
    private static Coordinate c(double lat, double lon) {
        return new Coordinate(lat, lon);
    }
    						
    private void updateZoomParameters() {
        if (mperpLabelValue != null)
            mperpLabelValue.setText(String.format("%s", map().getMeterPerPixel()));
        if (zoomValue != null)
            zoomValue.setText(String.format("%s", map().getZoom()));
    }

    @Override
    public void processCommand(JMVCommandEvent command) {
        if (command.getCommand().equals(JMVCommandEvent.COMMAND.ZOOM) ||
                command.getCommand().equals(JMVCommandEvent.COMMAND.MOVE)) {
            updateZoomParameters();
        }
    }
}