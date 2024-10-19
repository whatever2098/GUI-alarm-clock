package clockProject;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.UIManager;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;

public class Clock implements ActionListener{

	private JFrame frame;
	private JTabbedPane tabbedPane;
	private JPanel pnClock;
	private JLabel lblTime;
	private JLabel lblDate;
	private JButton btnChange;
	private JPanel pnAlarms;
	private JButton btnAdd;
	private JScrollPane scrollPane;
	private JPanel pnList;
	Thread clockThread;
    Thread alarmThread;
    Date d = new Date();
	private ArrayList<AlarmPanel> lstAlarm = new ArrayList<AlarmPanel>();
	private Calendar today;
	SimpleDateFormat hFormat24 = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat hFormat12 = new SimpleDateFormat("hh:mm:ss");
	public boolean hourFormat24;

	String weekArray[] = { "*", "Sunday", "Monday", "TuesDay", "Wendesday", "Thursday", "Friday", "Saturday" };
	String monthArray[] = {"January", "Feburary", "March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December" };
	
	
	public void actionPerformed(ActionEvent e) {
		JButton bt = (JButton)e.getSource();
		if (bt == btnChange) {
			SetTimeDialog dlg = new SetTimeDialog(null, this); 
                                                                
			dlg.setVisible(true);
		}
		
		else if (bt == btnAdd) {
			AlarmPanel newAlarmPanel = new AlarmPanel(this);
			SetAlarmDialog dlg = new SetAlarmDialog(null, this, newAlarmPanel, true);
			dlg.setVisible(true);
			
			/*if(!dlg.isVisible()){
				updateAlarmListPanel();
				updateAlarmListFile();      //everytime lstAlarm changes, these 2 methods should execute 
			}*/
		}

	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Clock window = new Clock();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Clock() {
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		today = Calendar.getInstance();
		hourFormat24 = true;
		
		frame = new JFrame();
		frame.setTitle("Clock");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		pnClock = new JPanel();
		pnClock.setToolTipText("");
		tabbedPane.addTab("Clock", null, pnClock, null);
		pnClock.setLayout(null);

		Date d = new Date(getToday().getTimeInMillis()); //to use format(), change Calendar to Date        
		lblTime = new JLabel(hFormat24.format(d));  
		lblTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblTime.setFont(new Font("Tahoma", Font.PLAIN, 75));
		lblTime.setBounds(12, 27, 403, 92);
		pnClock.add(lblTime);

		lblDate = new JLabel(weekArray[getToday().get(Calendar.DAY_OF_WEEK)] + ", " + monthArray[getToday().get(Calendar.MONTH)] + " " 
					+ getToday().get(Calendar.DAY_OF_MONTH) + ", " + getToday().get(Calendar.YEAR));
		lblDate.setHorizontalAlignment(SwingConstants.CENTER);
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblDate.setBounds(12, 132, 403, 27);
		pnClock.add(lblDate);

		btnChange = new JButton("Change date and time");
		btnChange.addActionListener(this);
		btnChange.setBackground(UIManager.getColor("Button.background"));
		btnChange.setForeground(Color.BLACK);
		btnChange.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnChange.setBounds(108, 172, 211, 25);
		pnClock.add(btnChange);

		pnAlarms = new JPanel();
		tabbedPane.addTab("Alarms", null, pnAlarms, null);
		pnAlarms.setLayout(null);

		btnAdd = new JButton("Add");
		btnAdd.addActionListener(this);
		btnAdd.setBounds(12, 185, 275, 25);
		pnAlarms.add(btnAdd);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 13, 403, 159);
		pnAlarms.add(scrollPane);

		pnList = new JPanel();
		pnList.setLayout(new GridLayout(0, 1, 0, 0));
		scrollPane.setViewportView(pnList);
		
		readFromAlarmListFile();                //reads from file to initialize lstAlarm
		updateAlarmListPanel();                 //shows AlarmPanels in lstAlarm(initialize pnList)
		
		if(Thread.activeCount() == 2){
			new JFXPanel();
			clockThread = new Thread(new updateClock());
			clockThread.start();
			alarmThread = new Thread(new checkAlarm());
			alarmThread.start();
		}
	}

	class updateClock implements Runnable {

		public updateClock() {
		}

		public void run() {
			while (true) {

				try {
					/*today = SetTimeDialog.newtime;*/
					today.add(Calendar.SECOND, 1);
					lblDate.setText(weekArray[getToday().get(Calendar.DAY_OF_WEEK)] + ", " + monthArray[getToday().get(Calendar.MONTH)] + " " 
							+ getToday().get(Calendar.DAY_OF_MONTH) + ", " + getToday().get(Calendar.YEAR));
					Date d = new Date(getToday().getTimeInMillis()); //to use format(), change Calendar to Date 
					String time;
					if(hourFormat24) {
						time = hFormat24.format(d);
					}
					else { 
						time = hFormat12.format(d) +" "+ ((getToday().get(Calendar.AM_PM) == Calendar.AM)? "AM":"PM");
						lblTime.setFont(new Font("Tahoma", Font.PLAIN, 55));
					}        
					lblTime.setText(time);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}
	
	class checkAlarm implements Runnable {

		public checkAlarm() {
		}

		public void run() {

			while(true){
				if(lstAlarm.size() != 0){
					
					for(int i=0; i<lstAlarm.size(); i++){
						AlarmPanel testAlarm = lstAlarm.get(i);
						if(testAlarm.isAlarmState()                     //if the alarm is on and time is matched to clock time
								&& (testAlarm.getAlarmHour() == getToday().get(Calendar.HOUR_OF_DAY))
								&& (testAlarm.getAlarmMinute() == getToday().get(Calendar.MINUTE))) {
							
							if((testAlarm.getAlarmStartDate().get(Calendar.YEAR) <= getToday().get(Calendar.YEAR))     //if the date of clock is later than or equel to alarm 
									&& (testAlarm.getAlarmStartDate().get(Calendar.MONTH) <= getToday().get(Calendar.MONTH))
									&& (testAlarm.getAlarmStartDate().get(Calendar.DAY_OF_MONTH) <= getToday().get(Calendar.DAY_OF_MONTH))){
								
								if((testAlarm.getAlarmEndDate().get(Calendar.YEAR) >= getToday().get(Calendar.YEAR))  //if the date of clock is earlier than or equel to alarm
										&& (testAlarm.getAlarmEndDate().get(Calendar.MONTH) >= getToday().get(Calendar.MONTH))
										&& (testAlarm.getAlarmEndDate().get(Calendar.DAY_OF_MONTH) >= getToday().get(Calendar.DAY_OF_MONTH))) {
									
									if(testAlarm.getAlarmDays().contains(getToday().get(Calendar.DAY_OF_WEEK))) {    //if the day of clock is set to ring alarm
										
										Media hit = new Media(testAlarm.getAlarmSong());
										MediaPlayer mediaPlayer = new MediaPlayer(hit);
										mediaPlayer.play();
										JOptionPane.showMessageDialog(frame, testAlarm.getAlarmMessage(), 
												"Alarm!", JOptionPane.INFORMATION_MESSAGE);
										mediaPlayer.stop();
										
										if(testAlarm.isAlarmRepeat()) {     //if the alarm has to be repeated
											try {
												
												Thread.sleep(3000);     //Repeat alarm 3 seconds later
												mediaPlayer.play();
												JOptionPane.showMessageDialog(frame, testAlarm.getAlarmMessage(), 
														"Alarm!", JOptionPane.INFORMATION_MESSAGE);
												mediaPlayer.stop();
												
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}    
											
											
										}
										
									}
								}
							}
				
						}
						/*ArrayList<Integer> days = lstAlarm.get(i).getAlarmDays();
						System.out.println(d.getDay());		//요일을 나타내는 변수. 0이 일요일 1이 월요일
						
						System.out.println(days.size());
						System.out.println(lstAlarm.get(i).getAlarmStartDate());
						System.out.println(lstAlarm.get(i).getAlarmEndDate());
						System.out.println(lstAlarm.get(i).getAlarmMessage());
						

						//날짜 범위 맞을 경우 조건 추가 d를이용해서 lstAlarm Date와 비교
						//요일 맞을 경우 조건 추가 d.getDay 이용
						if((lstAlarm.get(i).getAlarmHour() == d.getHours()) && (lstAlarm.get(i).getAlarmMinute() == d.getMinutes())){//시간도 맞을 경우
							//알람 다이얼로그 실행
						}*/
					
					}
					try {		
						Thread.sleep(3000);      //check the alarm every 3 seconds
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}

	}
	
	public void readFromAlarmListFile() {      //reads AlarmPanel objects from alarmList file
		String filePath = "C:\\Users\\SAMSUNG\\eclipse-workspace\\Clock Project\\src\\clockProject\\alarmList.txt";
		File file = new File(filePath);
		boolean isExists = file.exists();
		
		if(!isExists) {                         //if the file does not exist, create the file 
			try {
				file.createNewFile();
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
				oos.writeInt(0);               //this int indicated the number of Objects(AlarmPanel), it is used later not to reach eofexception
				oos.flush();
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else {                                 //if the file exists, open a binary input stream and read each object to store in lstAlarm
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
				int sizeOflstAlarm = ois.readInt();
				if(sizeOflstAlarm != 0) {
					for(int i = 0;i < sizeOflstAlarm;i++) {
					
						AlarmPanel ap= (AlarmPanel)ois.readObject();
						lstAlarm.add(ap);                            
					
					}
				}
				ois.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void updateAlarmListFile() {            //open a binary output stream to update(write) alarmList file
		String filePath = "C:\\Users\\SAMSUNG\\eclipse-workspace\\Clock Project\\src\\clockProject\\alarmList.txt";
		try {
			File file = new File(filePath);
			if(file.exists()) {
				file.delete();         //delete the existing file
				file.createNewFile();  //create new file to write data newly
				
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
				
				oos.writeInt(lstAlarm.size());
				ListIterator<AlarmPanel> litr = lstAlarm.listIterator();
				while (litr.hasNext()) {
					AlarmPanel object = litr.next();
					oos.writeObject(object);
				}
				
				oos.close();
			}
			                      
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateAlarmListPanel() {           //update pnList depends on lstAlarm

		pnList.removeAll();

		ListIterator<AlarmPanel> litr = lstAlarm.listIterator();
		while (litr.hasNext()) {
		
			AlarmPanel element = litr.next();
			pnList.add(element);
			
		}
		
		pnList.revalidate();
		pnList.repaint();

	}
	
	public void removeAlarmPanel(AlarmPanel ap) {    //remove a AlarmPanel from the lstAlarm
		lstAlarm.remove(ap);
	}

	public Calendar getToday() {
		return today;
	}

	public void setToday(Calendar today) {
		this.today = today;
	}

	public boolean isHourFormat24() {
		return hourFormat24;
	}

	public void setHourFormat24(boolean hourFormat24) {
		this.hourFormat24 = hourFormat24;
	}

	public ArrayList<AlarmPanel> getLstAlarm() {
		return lstAlarm;
	}

	public void setLstAlarm(int index, AlarmPanel newpn) {
		this.lstAlarm.set(index, newpn);
	}
	
	public void addLstAlarm(AlarmPanel newpn) {
		this.lstAlarm.add(newpn);
	}
	

}
