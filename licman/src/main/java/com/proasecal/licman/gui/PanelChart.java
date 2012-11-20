package com.proasecal.licman.gui;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import com.proasecal.licman.dao.LicenciaDAO;
import com.proasecal.licman.svcs.ConfigManager;
import com.proasecal.licman.svcs.LicmanUtils;

public class PanelChart extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 426613665699410384L;
	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(PanelChart.class);
	private JFreeChart _chart;
	private ChartPanel _chartPanel;
	private DefaultPieDataset _dataset;

	public PanelChart() {
		_dataset = new DefaultPieDataset();
		_chart = createChart(_dataset,
				ConfigManager.getString("PanelChart.title"));
		setLayout(new GridLayout(0, 1, 0, 0));
		_chartPanel = new ChartPanel(_chart);
		add(_chartPanel);
	}

	private JFreeChart createChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true,
				true, false);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;
	}

	@Override
	public void update(Observable o, Object arg) {
		LicenciaDAO dao = new LicenciaDAO();
		try {
			Long lPorExpirar = dao.findNroLicXRangoVencimiento(30);
			Long lRecientes = dao.findNroLicXRangoCreacion(-30);
			Long lVencidas = dao.findNroLicenciasVencidas();
			_dataset.setValue("Recientes", lRecientes);
			_dataset.setValue("Por expirar", lPorExpirar);
			_dataset.setValue("Expiradas", lVencidas);
		} catch (Exception ex) {
			LOGGER.error(ex);
			LicmanUtils.MsgBox("error.database.consulta");
		}
	}
}
