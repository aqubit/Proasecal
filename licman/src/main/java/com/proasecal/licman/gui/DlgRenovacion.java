package com.proasecal.licman.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.openswing.swing.client.DateControl;
import org.openswing.swing.util.java.Consts;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.proasecal.licman.svcs.LicmanUtils;

public class DlgRenovacion extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6250231007405918804L;

	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle("com.proasecal.licman.messages"); //$NON-NLS-1$
	private final JPanel contentPanel = new JPanel();
	private JLabel lblInstrucciones;
	private final DateControl datectrlFechaVencimiento;
	private Boolean bFueCancelado = false;

	/**
	 * Create the dialog.
	 */
	public DlgRenovacion() {
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setResizable(false);
		setTitle(BUNDLE.getString("DlgRenovacion.this.title")); //$NON-NLS-1$
		setBounds(100, 100, 358, 180);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		// Put dummy data in design mode
		contentPanel
				.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"), }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("max(30dlu;default)"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("60px:grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));
		{
			lblInstrucciones = new JLabel(
					BUNDLE.getString("DlgRenovacion.lblInstrucciones.text")); //$NON-NLS-1$
			contentPanel.add(lblInstrucciones, "2, 2");
		}
		{
			datectrlFechaVencimiento = new DateControl();
			contentPanel.add(datectrlFechaVencimiento, "2, 4, center, fill");
			datectrlFechaVencimiento
					.setTimeFormat(BUNDLE
							.getString("DlgLicencia.datctrlFechaActivacion.timeFormat")); //$NON-NLS-1$
			datectrlFechaVencimiento.setDateType(Consts.TYPE_DATE);
			Date hoy = LicmanUtils.fechaHoy();
			datectrlFechaVencimiento.setDate(hoy);
			datectrlFechaVencimiento.setLowerLimit(hoy);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnCancelar = new JButton(
						BUNDLE.getString("btnCancel.text")); //$NON-NLS-1$
				btnCancelar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						bFueCancelado = true;
						DlgRenovacion.this.setVisible(false);
					}
				});
				{
					JButton btnGenerar = new JButton(
							BUNDLE.getString("DlgRenovacion.btnGenerar.text")); //$NON-NLS-1$
					btnGenerar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Date dateHoy = LicmanUtils.fechaHoy();
							Date dateSelected = LicmanUtils
									.removeTime(datectrlFechaVencimiento
											.getDate());
							if (dateSelected.compareTo(dateHoy) < 0) {
								LicmanUtils.MsgBox("error.text.fecha");
							} else {
								DlgRenovacion.this.setVisible(false);
							}
						}
					});
					buttonPane.add(btnGenerar);
				}
				buttonPane.add(btnCancelar);
			}
		}
		LicmanUtils.locateOnScreenCenter(this);
	}

	public Date getSelectedDate() {
		return LicmanUtils.removeTime(datectrlFechaVencimiento.getDate());
	}

	public Boolean getFueCancelado() {
		return bFueCancelado;
	}
}
