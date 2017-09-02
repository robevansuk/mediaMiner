package uk.robevans.twitter.charts;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class DataBarChart extends JPanel {

	private BigDecimal[] values;
	private String[] names;
	private String title;
	
	public DataBarChart(){
		title = "Sentiment Analysis for Bitcoin";
		values = new BigDecimal[25];
		names = new String[25];

		names[0] = ":)";
		names[1] = ":(";
		names[2] = ":|";

		for (int i = 0; i<names.length; i++){
			if (i % 2 ==0){
				values[i] = new BigDecimal(Math.random() * 100);
			} else {
				values[i] = new BigDecimal(-1 *  Math.random() * 100);
			}
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (values==null || values.length == 0)
			return;
		
		BigDecimal minVal = new BigDecimal(0);
		BigDecimal maxVal = new BigDecimal(0);
		
		int halfHeight = (int) getSize().getHeight();
		
		g.drawLine(10, halfHeight, (int) getSize().getWidth()-50, halfHeight);
		
		// Work out the minimum and maximum scale that needs to be represented  
		for (int i = 0; i < values.length; i++) {
			System.out.println(i+"");
			if (minVal.compareTo(values[i]) >0)
				minVal = values[i];
			if (maxVal.compareTo(values[i]) < 1)
				maxVal = values[i];    
		}

		Dimension d = getSize();
		
		int clientWidth = d.width;    
		int clientHeight = d.height;    
		int barWidth = clientWidth / values.length;     
		
		Font titleFont = new Font("SansSerif", Font.BOLD, 16);    
		FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);    
		
		Font labelFont = new Font("SansSerif", Font.PLAIN, 12);    
		FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);
		
		Font valFont = new Font("SansSerif", Font.PLAIN, 9);    
		FontMetrics valFontMetrics = g.getFontMetrics(valFont);

		int titleWidth = titleFontMetrics.stringWidth(title);    
		int y = titleFontMetrics.getAscent();    
		int x = (clientWidth - titleWidth) / 2;
		
		g.setFont(titleFont);    
		g.drawString(title, x, y);
		
		int top = titleFontMetrics.getHeight();    
		int bottom = labelFontMetrics.getHeight();
		
		if (maxVal.equals(minVal))
			return;
		
		BigDecimal scale = new BigDecimal(clientHeight - top - bottom);
		scale = scale.divide(maxVal.subtract(minVal));
		
		y = clientHeight - labelFontMetrics.getDescent();
		g.setFont(labelFont);
		
		for (int i = 0; i < values.length; i++) {
			g.setFont(labelFont);
			int valueX = i * barWidth + 1; 
			int valueY = top;      
			int height = values[i].multiply(scale).intValue();
			if (values[i].compareTo(new BigDecimal(0)) >= 0)
				valueY = valueY + (maxVal.subtract(values[i])).multiply(scale).intValue();
			else {        
				valueY = valueY + (maxVal.multiply(scale).intValue());
				height = -height;
			} 
			if (i % 2 == 1){
				g.setColor(Color.red);
				g.fillRect(valueX, valueY, barWidth - 2, height);
				g.setColor(Color.black);
				g.draw3DRect(valueX, valueY, barWidth - 2, height, true);
				int labelWidth = labelFontMetrics.stringWidth(names[i]);
				x = i * barWidth + (barWidth - labelWidth) / 2;
				g.drawString(names[i], x, y);
			} else {
				g.setColor(Color.green);
				g.fillRect(valueX, valueY, barWidth - 2, height);
				g.setColor(Color.black);
				g.draw3DRect(valueX, valueY, barWidth - 2, height, true);
				int labelWidth = labelFontMetrics.stringWidth(names[i]);
				x = i * barWidth + (barWidth - labelWidth) / 2;
				g.drawString(names[i], x, y);
			}
			
			// Post Values onto the bar chart as well
			g.setFont(valFont);
			String val = values[i].toString();
			
			g.drawString("" + val + "", x-5, valueY);
		}
	}
}
