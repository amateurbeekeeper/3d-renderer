package renderer;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import renderer.Scene.Polygon;

public class Reader {
  ArrayList<Polygon> polygons;
  Vector3D light;
  int totalPolygons;
  Vector3D lightSource;

  public Reader(File scenesFile) {
    this.polygons = new ArrayList<>();
    this.totalPolygons = 0;

    read(scenesFile);
  }

  public void read(File file) {
    String lineJustFetched = null;
    String[] line;
    Boolean firstLine = true;

    try {
      BufferedReader buf = new BufferedReader(new FileReader(file));

      while (true) {
        lineJustFetched = buf.readLine();

        if (lineJustFetched == null) {
          break;
        } else {

          line = lineJustFetched.split(",");

          // FIRST LINE
          if (firstLine) {            
            totalPolygons = Integer.parseInt(line[0]);
            firstLine = false; 

            // LAST LINE
          } else if (polygons.size() == totalPolygons - 1) {       
            this.lightSource = new Vector3D(Float.parseFloat(line[0]), Float.parseFloat(line[1]), Float.parseFloat(line[2]));

            // ELSE POLYGON
          } else {   
            Color color = new Color(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]));
            
            Vector3D v1 = new Vector3D(Float.parseFloat(line[3]), Float.parseFloat(line[4]), Float.parseFloat(line[5]));
            Vector3D v2 = new Vector3D(Float.parseFloat(line[6]), Float.parseFloat(line[7]), Float.parseFloat(line[8]));
            Vector3D v3 = new Vector3D(Float.parseFloat(line[9]), Float.parseFloat(line[10]), Float.parseFloat(line[11]));

            Polygon p = new Polygon(v1, v2, v3, color);

            this.polygons.add(p);
          }

        }
      }
      buf.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<Polygon> getPolygons() {
    return this.polygons;
  }

  public Vector3D getLightPos() {
    return this.lightSource;
  }

}
