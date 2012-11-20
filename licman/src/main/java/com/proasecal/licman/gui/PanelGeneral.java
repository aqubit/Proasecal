package com.proasecal.licman.gui;

import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.proasecal.licman.gui.PanelReporteLicencia.RangoLicencias;

public class PanelGeneral extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2873058231475676465L;
	private static final Observable _observable = new Observable();

	public PanelGeneral() {
		setLayout(new GridLayout(0, 2, 0, 0));

		JSplitPane splitPnlLeft = new JSplitPane();
		add(splitPnlLeft);
		splitPnlLeft.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPnlLeft.setResizeWeight(0.5);
		PanelReporteLicencia pnlRecientes = new PanelReporteLicencia(
				"PanelReporteLicencia.text.recientes", RangoLicencias.RECIENTES);
		_observable.addObserver(pnlRecientes);
		splitPnlLeft.setTopComponent(pnlRecientes);
		PanelReporteLicencia pnlExpiradas = new PanelReporteLicencia(
				"PanelReporteLicencia.text.expiradas", RangoLicencias.EXPIRADAS);
		splitPnlLeft.setBottomComponent(pnlExpiradas);
		_observable.addObserver(pnlExpiradas);
		JSplitPane splitPnlRight = new JSplitPane();
		add(splitPnlRight);
		splitPnlRight.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPnlRight.setResizeWeight(0.428);
		PanelReporteLicencia pnlPorExpirar = new PanelReporteLicencia(
				"PanelReporteLicencia.text.por.expirar",
				RangoLicencias.POR_EXPIRAR);
		_observable.addObserver(pnlPorExpirar);
		splitPnlRight.setTopComponent(pnlPorExpirar);
		PanelChart pnlChart = new PanelChart();
		splitPnlRight.setBottomComponent(pnlChart);
		_observable.addObserver(pnlChart);
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				_observable.flagChanged();
				_observable.notifyObservers();
			}
		});
	}
}

class Observable extends java.util.Observable {
	public Observable() {
		super();
	}

	public void flagChanged() {
		setChanged();
	}
}
