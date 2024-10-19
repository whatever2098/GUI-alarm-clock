package clockProject;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JCheckBox;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.SpinnerListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.awt.event.ActionEvent;

public class SetAlarmDialog extends JDialog {
	private JCheckBox chckbxOnOff;
	private JTextField txtField;
	private JPanel panel;
	private JSpinner spnHour;
	private JSpinner spnMin;
	private JSpinner spnAmpm;
	private JButton btnChooseSong;
	private JCheckBox chckbxRepeat;
	private JTextField txtMp3;
	private JTextField txtStart;
	private JButton btnStart;
	private JTextField txtEnd;
	private JButton btnEnd;
	private JLabel lblTo;
	private JCheckBox chckbxMon;
	private JCheckBox chckbxTue;
	private JCheckBox chckbxWed;
	private JCheckBox chckbxThu;
	private JCheckBox chckbxFri;
	private JCheckBox chckbxSat;
	private JCheckBox chckbxSun;
	private JButton btnSave;
	private JButton btnCancel;
	private Popup popup;
	
	private Calendar alarmStartDate;
	private Calendar alarmEndDate;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 

	/**
	 * Launch the application.
	 */
	/**
	 * Create the dialog.
	 */
	public SetAlarmDialog(JFrame parent, Clock c, AlarmPanel pnAlarm, boolean isbtnAdd) {
		
		/*JFXPanel fxPanel = new JFXPanel();*/
		
		alarmStartDate = pnAlarm.getAlarmStartDate();
		alarmEndDate = pnAlarm.getAlarmEndDate();
		
		setBounds(100, 100, 422, 396);
		setTitle("Set Alarm");
		getContentPane().setLayout(null);
		
		chckbxOnOff = new JCheckBox("On/Off");
		chckbxOnOff.setFont(new Font("Tahoma", Font.PLAIN, 15));
		chckbxOnOff.setBounds(19, 21, 113, 25);
		chckbxOnOff.setSelected(pnAlarm.isAlarmState());
		getContentPane().add(chckbxOnOff);
		
		txtField = new JTextField();
		txtField.setFont(new Font("Tahoma", Font.PLAIN, 17));
		txtField.setBounds(12, 55, 198, 37);
		txtField.setColumns(10);
		txtField.setText(pnAlarm.getAlarmMessage());
		getContentPane().add(txtField);
		
		
		panel = new JPanel();
		panel.setBounds(217, 55, 175, 37);
		getContentPane().add(panel);
		
		if(c.hourFormat24) {
			spnHour = new JSpinner();
			spnHour.setModel(new SpinnerNumberModel(0, 0, 23, 1));
			spnHour.setValue(c.getToday().get(Calendar.HOUR_OF_DAY));
			spnHour.setFont(new Font("Tahoma", Font.PLAIN, 17));
			panel.add(spnHour);
		
		}
		else {
			spnHour = new JSpinner();
			spnHour.setModel(new SpinnerNumberModel(1, 1, 12, 1));
			int h = c.getToday().get(Calendar.HOUR);
			if(h == 0) {    //show midnight and noon as 12, not 0.
				spnHour.setValue(12);
			}
			else {spnHour.setValue(h);}
			spnHour.setFont(new Font("Tahoma", Font.PLAIN, 17));
			panel.add(spnHour);
			
		}
		
		spnMin = new JSpinner();
		spnMin.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		spnMin.setValue(c.getToday().get(Calendar.MINUTE));
		spnMin.setFont(new Font("Tahoma", Font.PLAIN, 17));
		panel.add(spnMin);
		
		if(!c.hourFormat24) {
			spnAmpm = new JSpinner();
			spnAmpm.setModel(new SpinnerListModel(new String[] {"AM", "PM"}));
			if(c.getToday().get(Calendar.AM_PM) == Calendar.AM) {
				spnAmpm.setValue("AM");
			}
			else {
				spnAmpm.setValue("PM");
			}
			spnAmpm.setFont(new Font("Tahoma", Font.PLAIN, 17));
			panel.add(spnAmpm);
		}
		
		txtMp3 = new JTextField();
		txtMp3.setFont(new Font("Tahoma", Font.PLAIN, 17));
		txtMp3.setBounds(12, 98, 198, 37);
		txtMp3.setColumns(10);
		txtMp3.setText(pnAlarm.getAlarmSong());
		getContentPane().add(txtMp3);
		
		
		btnChooseSong = new JButton("Choose Song");
		btnChooseSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(SetAlarmDialog.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {

					File file = fc.getSelectedFile();
					String mediaPath = file.toURI().toString();
					
					txtMp3.setText(mediaPath);

				}
			}
		});
		btnChooseSong.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnChooseSong.setBounds(227, 98, 154, 37);
		getContentPane().add(btnChooseSong);
		
		chckbxRepeat = new JCheckBox("Repeat");
		chckbxRepeat.setFont(new Font("Tahoma", Font.PLAIN, 15));
		chckbxRepeat.setBounds(19, 171, 113, 25);
		chckbxRepeat.setSelected(pnAlarm.isAlarmRepeat());
		getContentPane().add(chckbxRepeat);
		
		txtStart = new JTextField();
		txtStart.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtStart.setBounds(29, 205, 116, 22);
		txtStart.setColumns(10);
		Date d = new Date(pnAlarm.getAlarmStartDate().getTimeInMillis()); //to use format(), change Calendar to Date 
		txtStart.setText(sdf.format(d));  
		getContentPane().add(txtStart);
		
		
		btnStart = new JButton(new ImageIcon(((new ImageIcon("C:\\Users\\SAMSUNG\\Desktop\\iconCal.png")).getImage())
				.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				chooseDate(c, parent, btnStart, txtStart, alarmStartDate);

			}
		});
		btnStart.setBounds(147, 205, 37, 25);
		getContentPane().add(btnStart);
		
		txtEnd = new JTextField();
		txtEnd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtEnd.setColumns(10);
		txtEnd.setBounds(227, 205, 116, 22);
		Date d_1 = new Date(pnAlarm.getAlarmEndDate().getTimeInMillis()); //to use format(), change Calendar to Date 
		txtEnd.setText(sdf.format(d_1));
		getContentPane().add(txtEnd);
		
		btnEnd = new JButton(new ImageIcon(((new ImageIcon("C:\\Users\\SAMSUNG\\Desktop\\iconCal.png")).getImage())
				.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
		btnEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				chooseDate(c, parent, btnEnd, txtEnd, alarmEndDate);
				
			}
		});
		btnEnd.setBounds(344, 205, 37, 25);
		getContentPane().add(btnEnd);
		
		lblTo = new JLabel("to");
		lblTo.setBounds(196, 214, 24, 16);
		getContentPane().add(lblTo);
		
		chckbxMon = new JCheckBox("M");
		chckbxMon.setFont(new Font("Tahoma", Font.PLAIN, 15));
		chckbxMon.setBounds(29, 249, 47, 25);
		if(pnAlarm.getAlarmDays().contains(2)) {
			chckbxMon.setSelected(true);
		}
		getContentPane().add(chckbxMon);
		
		chckbxTue = new JCheckBox("T");
		chckbxTue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		chckbxTue.setBounds(80, 250, 47, 25);
		if(pnAlarm.getAlarmDays().contains(3)) {
			chckbxTue.setSelected(true);
		}
		getContentPane().add(chckbxTue);
		
		chckbxWed = new JCheckBox("W");
		chckbxWed.setFont(new Font("Tahoma", Font.PLAIN, 15));
		chckbxWed.setBounds(131, 250, 47, 25);
		if(pnAlarm.getAlarmDays().contains(4)) {
			chckbxWed.setSelected(true);
		}
		getContentPane().add(chckbxWed);
		
		chckbxThu = new JCheckBox("T");
		chckbxThu.setFont(new Font("Tahoma", Font.PLAIN, 15));
		chckbxThu.setBounds(182, 250, 47, 25);
		if(pnAlarm.getAlarmDays().contains(5)) {
			chckbxThu.setSelected(true);
		}
		getContentPane().add(chckbxThu);
		
		chckbxFri = new JCheckBox("F");
		chckbxFri.setFont(new Font("Tahoma", Font.PLAIN, 15));
		chckbxFri.setBounds(227, 250, 47, 25);
		if(pnAlarm.getAlarmDays().contains(6)) {
			chckbxFri.setSelected(true);
		}
		getContentPane().add(chckbxFri);
		
		chckbxSat = new JCheckBox("S");
		chckbxSat.setFont(new Font("Tahoma", Font.PLAIN, 15));
		chckbxSat.setBounds(278, 250, 47, 25);
		if(pnAlarm.getAlarmDays().contains(7)) {
			chckbxSat.setSelected(true);
		}
		getContentPane().add(chckbxSat);
		
		chckbxSun = new JCheckBox("S");
		chckbxSun.setFont(new Font("Tahoma", Font.PLAIN, 15));
		chckbxSun.setBounds(329, 250, 47, 25);
		if(pnAlarm.getAlarmDays().contains(1)) {
			chckbxSun.setSelected(true);
		}
		getContentPane().add(chckbxSun);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				pnAlarm.setAlarmState(chckbxOnOff.isSelected());
				pnAlarm.setAlarmMessage(txtField.getText());
				
				//set alarm hour and minute
				if(c.hourFormat24) {
					pnAlarm.setAlarmHour((int)spnHour.getValue());
					pnAlarm.setAlarmMinute((int)spnMin.getValue());
				}
				else {   //if hourFormat is 12hourformat
					if((String)spnAmpm.getValue() == "AM") {
						if((int)spnHour.getValue() == 12) {
							pnAlarm.setAlarmHour(0);
						}
						else {
							pnAlarm.setAlarmHour((int)spnHour.getValue());
						}
					}
					else {  //if hourFormat is 12hourformat and time is after noon(pm)
						if((int)spnHour.getValue() == 12) {
							pnAlarm.setAlarmHour(12);
						}
						else {
							pnAlarm.setAlarmHour((int)spnHour.getValue() + 12);
						}
					}
					pnAlarm.setAlarmMinute((int)spnMin.getValue());
				}
				
				pnAlarm.setAlarmSong(txtMp3.getText());
				pnAlarm.setAlarmRepeat(chckbxRepeat.isSelected());
				pnAlarm.setAlarmStartDate(alarmStartDate);
				pnAlarm.setAlarmEndDate(alarmEndDate);
				
				ArrayList<Integer> tempAlarmDays = new ArrayList<Integer>();
				if(chckbxSun.isSelected()) {
					tempAlarmDays.add(1);
				}
				if(chckbxMon.isSelected()) {
					tempAlarmDays.add(2);
				}
				if(chckbxTue.isSelected()) {
					tempAlarmDays.add(3);
				}
				if(chckbxWed.isSelected()) {
					tempAlarmDays.add(4);
				}
				if(chckbxThu.isSelected()) {
					tempAlarmDays.add(5);
				}
				if(chckbxFri.isSelected()) {
					tempAlarmDays.add(6);
				}
				if(chckbxSat.isSelected()) {
					tempAlarmDays.add(7);
				}
				pnAlarm.setAlarmDays(tempAlarmDays);
				
				pnAlarm.updateAlarmPanelLabel(c);
				pnAlarm.updateAlarmPanelColor();
				
				if(isbtnAdd) {     //if clicked button is btnAdd, add this panel to lstAlarm
					c.addLstAlarm(pnAlarm);
				}
				
				c.updateAlarmListPanel();
				c.updateAlarmListFile();
				
				setVisible(false);
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSave.setBounds(19, 288, 113, 37);
		getContentPane().add(btnSave);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnCancel.setBounds(267, 288, 113, 37);
		getContentPane().add(btnCancel);

	}
	
	public void chooseDate(Clock c, JFrame parent, JButton btnCal, JTextField txtCal, Calendar changingDate) {     //choose and store a date from pop-up calendar
		PopupFactory pf = PopupFactory.getSharedInstance();
		JPanel pn = new JPanel();                        //this panel contains a CalendarPanel and a ok button
		pn.setLayout(new BorderLayout(0, 0));
		
		JPanel pn_1 = new JPanel();                      
		pn_1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pn.add(pn_1, BorderLayout.SOUTH);                //add a panel that contains a ok button to the south of pn
		
		CalendarPanel pnCalendar = new CalendarPanel(c); 
		pnCalendar.setSize(203, 203);
		pnCalendar.setPreferredSize(new Dimension(203, 203));
		pn.add(pnCalendar, BorderLayout.CENTER);        //add a CalendarPanel to the center of pn
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Date d = new Date(pnCalendar.getCal().getTimeInMillis()); //to use format(), change Calendar to Date 
				txtCal.setText(sdf.format(d));
		
				changingDate.setTime(d);  //change the date to selected date
				/*System.out.print(changingDate);*/
				popup.hide();                           //close the pop-up calendar
				
			}
		});
		pn_1.add(btnOk);
		
		Point l = btnCal.getLocationOnScreen();
        popup = pf.getPopup(parent, pn, l.x, l.y + btnCal.getHeight());
        popup.show();                                   //make calendar pop-up at specific location
        
	}
}
