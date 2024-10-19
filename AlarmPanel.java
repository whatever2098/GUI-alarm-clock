package clockProject;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ListIterator;
import java.awt.event.ActionEvent;

public class AlarmPanel extends JPanel implements Serializable{
	private JPanel panel;
	private JLabel lblRingTime;
	private JLabel lblRingDay;
	private JButton btnEdit;
	private JButton btnDelete;
	
	private boolean alarmState;  //true = on, false = off
	private int alarmHour;
	private int alarmMinute;
	private String alarmMessage;
	private String alarmSong;   //path of alarm mp3
	private boolean alarmRepeat;
	private Calendar alarmStartDate;
	private Calendar alarmEndDate;
	private ArrayList<Integer> alarmDays = new ArrayList<Integer>(); //days which are selected to ring alarm in type integer(1=Sunday,2=Monday...7=Saturday)
	private String[] days = {"*", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

	/**
	 * Create the panel.
	 */
	public AlarmPanel(Clock c) {
		setAlarmState(true);
		setAlarmHour(c.getToday().get(Calendar.HOUR_OF_DAY));
		setAlarmMinute(c.getToday().get(Calendar.MINUTE));
		setAlarmMessage("");
		setAlarmSong("C:\\Users\\SAMSUNG\\eclipse-workspace\\Clock Project\\src\\clockProject\\Happy_Home");
		setAlarmRepeat(true);
		setAlarmStartDate(c.getToday());
		setAlarmEndDate(c.getToday());
		addAlarmDay(c.getToday().get(Calendar.DAY_OF_WEEK));              //initialize the default alarm data. These data are shown in setAlarmDialog when Add button is clicked.
		
		setLayout(null);
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 13, 300, 54);
		add(panel);
		panel.setLayout(new GridLayout(2, 1, 0, 0));
		
		lblRingTime = new JLabel(alarmHour + ":" + ((alarmMinute < 10)? "0" + alarmMinute : alarmMinute)  //set the label depends on this class' alarm data
				+ " - " + alarmMessage);
		lblRingTime.setBackground(Color.WHITE);
		lblRingTime.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel.add(lblRingTime);
		
		String lblRingDayText = "";
		ListIterator<Integer> litr = alarmDays.listIterator();
		while(litr.hasNext()) {
			int daysIndex = litr.next();
			lblRingDayText += (days[daysIndex] + 
					((daysIndex != alarmDays.get(alarmDays.size() - 1))? ", " : ""));       //shows only the selected days
		}
		lblRingDay = new JLabel(lblRingDayText);                              //set the label depends on this class' alarm data
		lblRingDay.setBackground(Color.WHITE);
		panel.add(lblRingDay);
		
		btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = c.getLstAlarm().indexOf(AlarmPanel.this);                 //get index of this AlarmPanel from the Clock lstAlarm
				SetAlarmDialog dlg = new SetAlarmDialog(null, c, AlarmPanel.this, false);
				dlg.setVisible(true);
				
				if(!dlg.isVisible()) {
					c.setLstAlarm(index, AlarmPanel.this);
					c.updateAlarmListPanel();
					c.updateAlarmListFile();
				}
				
			}
		});
		btnEdit.setText("Great");
		btnEdit.setBounds(306, 13, 97, 25);
		add(btnEdit);
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c.removeAlarmPanel(AlarmPanel.this);
				
				c.updateAlarmListPanel();
				c.updateAlarmListFile();
			}
		});
		btnDelete.setBounds(306, 42, 97, 25);
		add(btnDelete);

	}

	public boolean isAlarmState() {
		return alarmState;
	}

	public void setAlarmState(boolean alarmState) {
		this.alarmState = alarmState;
	}

	public int getAlarmHour() {
		return alarmHour;
	}

	public void setAlarmHour(int alarmHour) {
		this.alarmHour = alarmHour;
	}

	public int getAlarmMinute() {
		return alarmMinute;
	}

	public void setAlarmMinute(int alarmMinute) {
		this.alarmMinute = alarmMinute;
	}

	public String getAlarmMessage() {
		return alarmMessage;
	}

	public void setAlarmMessage(String alarmMessage) {
		this.alarmMessage = alarmMessage;
	}

	public String getAlarmSong() {
		return alarmSong;
	}

	public void setAlarmSong(String alarmSong) {
		this.alarmSong = alarmSong;
	}

	public boolean isAlarmRepeat() {
		return alarmRepeat;
	}

	public void setAlarmRepeat(boolean alarmRepeat) {
		this.alarmRepeat = alarmRepeat;
	}

	public Calendar getAlarmStartDate() {
		return alarmStartDate;
	}

	public void setAlarmStartDate(Calendar alarmStartDate) {
		this.alarmStartDate = alarmStartDate;
	}

	public Calendar getAlarmEndDate() {
		return alarmEndDate;
	}

	public void setAlarmEndDate(Calendar alarmEndDate) {
		this.alarmEndDate = alarmEndDate;
	}

	public ArrayList<Integer> getAlarmDays() {
		return alarmDays;
	}

	public void setAlarmDays(ArrayList<Integer> alarmDays) {
		this.alarmDays = alarmDays;
	}
	
	public void addAlarmDay(int alarmDay) {
		this.alarmDays.add(alarmDay);
	}
	
	public void updateAlarmPanelLabel(Clock c) {   //change the labels depends on updated alarm data
		if(c.hourFormat24) {
			lblRingTime.setText(alarmHour + ":" + ((alarmMinute < 10)? "0" + alarmMinute : alarmMinute)  
					+ " - " + alarmMessage);
		}
		else {   //if hourFormat is 12
			int formatHour;
			if(alarmHour == 0) {
				formatHour = 12;
			}
			else if(alarmHour <= 12) {
				formatHour = alarmHour;
			}
			else {
				formatHour = alarmHour - 12;
			}
			
			String formatAmpm;
			if(alarmHour < 12) { formatAmpm = "AM"; }
			else { formatAmpm = "PM"; }
			
			lblRingTime.setText(formatHour + ":" + ((alarmMinute < 10)? "0" + alarmMinute : alarmMinute)
					+ formatAmpm + " - " + alarmMessage);
		}
		
		String lblRingDayText = "";
		ListIterator<Integer> litr = alarmDays.listIterator();
		while(litr.hasNext()) {
			int daysIndex = litr.next();
			lblRingDayText += (days[daysIndex] + 
					((daysIndex != alarmDays.get(alarmDays.size() - 1))? ", " : ""));       //shows only the selected days
		}
		lblRingDay.setText(lblRingDayText); 
	}
	
	public void updateAlarmPanelColor() {   //if alarm is on, update panel color to white. if alarm is off, to gray
		if(alarmState == true) {
			panel.setBackground(Color.WHITE);
			lblRingTime.setBackground(Color.WHITE);
			lblRingDay.setBackground(Color.WHITE);
		}
		else {
			panel.setBackground(Color.GRAY);
			lblRingTime.setBackground(Color.GRAY);
			lblRingDay.setBackground(Color.GRAY);
		}
	}

}
