package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Shapes {
	
	private static void scaleArray(Double[] arr, Double x) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] *= x;
		}
	}
	
	public static Polygon getCheck(Double size, Color c) {
		Polygon p = new Polygon();
		Double[] pts = new Double[]{
				0.0, 50.0,
	            10.0, 50.0,
	            33.0, 90.0,
	            90.0, 0.0,
	            100.0, 0.0,
	            43.0, 100.0,
	            23.0, 100.0,
			};
		scaleArray(pts, 0.2*size);
		p.getPoints().addAll(pts);
		p.setFill(c);
		p.setStroke(c);
		p.setStrokeWidth(3);
		return p;
	}
	
	public static Polygon getUpArrow(double width, double height, Color c) {
        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(
                width/2, height - height/3,
                width/2 + width/6, height,
                width/2 - width/6, height
        );
        arrow.setFill(c);
        arrow.setStroke(c);
        arrow.setStrokeWidth(2);
        return arrow;
    }
	
	public static Polygon getOctogon(Double size, Color c) {
		Polygon p = new Polygon();
		Double[] pts = new Double[]{
		        50.0, 0.0,
		        100.0, 0.0,
		        150.0, 50.0,
		        150.0, 100.0,
		        100.0, 150.0,
		        50.0, 150.0,
		        0.0, 100.0,
		        0.0, 50.0
	        };
		scaleArray(pts, 0.2*size);
		p.getPoints().addAll(pts);
		p.setFill(Color.RED);
		p.setStroke(Color.BLACK);
		p.setStrokeWidth(3);
		return p;
	}
}
