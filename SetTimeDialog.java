package clockProject;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import java.awt.Font;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.Calendar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpinnerListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SetTimeDialog extends JDialog {
	private CalendarPanel pnCalendar;
	private JCheckBox chckbxTimeFormat;
	private JPanel panel_1;
	private JSpinner spnHour;
	private JSpinner spnMin;
	private JSpinner spnSec;
	private JSpinner spnAmpm;
	private JButton btnSave;
	private JButton btnCancel;
	/*public static Calendar newTime = Calendar.getInstance();*/
	private Calendar newTime;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	//getYear(), getMonth(), getDayOfTheWeek(), getDay(), getHour(), getMinute(), getSecond() ->clock c°´Ã¼ ÀÌ¿ë
	public SetTimeDialog(JFrame parent, Clock c) {
		
		super(parent,true);
		
		newTime = c.getToday();
		
		setBounds(100, 100, 575, 276); 
		setTitle("Set Time");
		getContentPane().setLayout(null);
		
		pnCalendar = new CalendarPanel(c);   //initialize calendar depends on passed Clock object
		pnCalendar.setBounds(12, 13, 203, 203);
		getContentPane().add(pnCalendar);
		
		chckbxTimeFormat = new JCheckBox("Use 24 hour format");
		chckbxTimeFormat.setFont(new Font("Tahoma", Font.PLAIN, 15));
		chckbxTimeFormat.setBounds(223, 35, 163, 33); 
		if(c.hourFormat24) {
			chckbxTimeFormat.setSelected(true);
		}
		else {
			chckbxTimeFormat.setSelected(false);
		}
		getContentPane().add(chckbxTimeFormat);
		
		panel_1 = new JPanel();
		panel_1.setBounds(227, 82, 322, 58); 
		getContentPane().add(panel_1);
		
		/*if(c.hourFormat24) {
			spnHour = new JSpinner();
			spnHour.setModel(new SpinnerNumberModel(0, 0, 23, 1));
			spnHour.setValue(c.getToday().get(Calendar.HOUR_OF_DAY));
			spnHour.setFont(new Font("Tahoma", Font.PLAIN, 30));
			panel_1.add(spnHour);
		
		}
		else {*/
			spnHour = new JSpinner();
			spnHour.setModel(new SpinnerNumberModel(1, 1, 12, 1));
			int h = c.getToday().get(Calendar.HOUR);
			if(h == 0) {    //show midnight and noon as 12, not 0.
				spnHour.setValue(12);
			}
			else {spnHour.setValue(h);}
			spnHour.setFont(new Font("Tahoma", Font.PLAIN, 30));
			panel_1.add(spnHour);
			
		/*}*/
		
		spnMin = new JSpinner();
		spnMin.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		spnMin.setValue(c.getToday().get(Calendar.MINUTE));
		spnMin.setFont(new Font("Tahoma", Font.PLAIN, 30));
		panel_1.add(spnMin);
		
		spnSec = new JSpinner();
		spnSec.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		spnSec.setValue(c.getToday().get(Calendar.SECOND));
		spnSec.setFont(new Font("Tahoma", Font.PLAIN, 30));
		panel_1.add(spnSec);
		
		/*if(!c.hourFormat24) {*/
			spnAmpm = new JSpinner();
			spnAmpm.setModel(new SpinnerListModel(new String[] {"AM", "PM"}));
			if(c.getToday().get(Calendar.AM_PM) == Calendar.AM) {
				spnAmpm.setValue("AM");
			}
			else {
				spnAmpm.setValue("PM");
			}
			spnAmpm.setFont(new Font("Tahoma", Font.PLAIN, 30));
			panel_1.add(spnAmpm);
		/*}*/
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.hourFormat24 = chckbxTimeFormat.isSelected();
				
				newTime = pnCalendar.getCal();
				if((String)spnAmpm.getValue() == "AM") {
					if((int)spnHour.getValue() == 12) {
						newTime.set(Calendar.HOUR_OF_DAY, 0);
					}
					else {
						newTime.set(Calendar.HOUR_OF_DAY, (int)spnHour.getValue());
					}
				}
				else {  //if time is after noon(pm)
					if((int)spnHour.getValue() == 12) {
						newTime.set(Calendar.HOUR_OF_DAY, 12);
					}
					else {
						newTime.set(Calendar.HOUR_OF_DAY, (int)spnHour.getValue() + 12);
					}
				}
				newTime.set(Calendar.MINUTE, (int)spnMin.getValue());
				newTime.set(Calendar.SECOND, (int)spnSec.getValue());
				if((String)spnAmpm.getValue() == "AM") {
					newTime.set(Calendar.AM_PM, Calendar.AM);
				}
				else {
					newTime.set(Calendar.AM_PM, Calendar.PM);
				}
				
				c.setToday(newTime);
				/*Date date = new Date(pnCalendar.getCalYear(), pnCalendar.getCalMonth(), pnCalendar.getCalDay(), 
						spnHour.getValue().hashCode(), spnMin.getValue().hashCode(), spnSec.getValue().hashCode());
				newTime.setTime(date);*/
				
				setVisible(false);
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSave.setBounds(258, 163, 97, 25);
		getContentPane().add(btnSave);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnCancel.setBounds(410, 163, 97, 25);  
		getContentPane().add(btnCancel);

	}

	/*public Calendar getNewTime() {
		return newTime;
	}*/
	
}
