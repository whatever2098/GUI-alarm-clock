package clockProject;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.UIManager;

public class CalendarPanel extends JPanel {
	private int calYear;
	private int calMonth;     //0-11
	private int calDay;       //1-
	private int calDayOfTheWeek;    //0-6 : Sunday-Saturday
	private JPanel panel;
	private JButton btnBack;
	private JLabel lblDate;
	private JButton btnForth;
	
	private JPanel panel_1;
	private int firstDay; //store the first day of the month(SUN=0,MON=1,TUE=2,WED=3,,,,SAT=6)
	private int lastDay;  //store the last day of the month
	private int lastDate; //store the last date of the month
	int[] lastDateList = {31,28,31,30,31,30,31,31,30,31,30,31};
	String[] months = { "January", "February", "March", "April", "May", "June", 
			"July", "August", "September", "October", "November", "December"};
	String[] days = {"S", "M","T", "W", "T", "F", "S"};
	

	/**
	 * Create the panel.
	 */
	public CalendarPanel(Clock clock) {
		
		calYear = clock.getToday().get(Calendar.YEAR);
		calMonth = clock.getToday().get(Calendar.MONTH);
		calDay = clock.getToday().get(Calendar.DAY_OF_MONTH);
		calDayOfTheWeek = clock.getToday().get(Calendar.DAY_OF_WEEK) - 1;     //get default date from clock
		
		firstDay = ((this.calDayOfTheWeek + 7) - (this.calDay - 1) % 7) % 7;     //calculate the default first day of month
		
		if(this.calMonth == 1) {                                        //set the default last date of month
			lastDate = lastDateList[this.calMonth] + leapCheck(this.calYear);
		}
		else {lastDate = lastDateList[this.calMonth];}
		
		lastDay = ((firstDay - 1) + lastDate) % 7;     //calculate the default last day of month
		
		setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnBack = new JButton("<");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {               //change to previous month
				
				if(calMonth != 0) {                                    //change the month
					calMonth -= 1;
				}
				else { 
					calMonth = 11; 
					calYear -= 1;
				}
				lblDate.setText(months[calMonth] + " " + calDay);      //change the label
				
				if(calMonth == 1) {                                    //change the last date of month
					lastDate = lastDateList[calMonth] + leapCheck(calYear);
				}
				else {lastDate = lastDateList[calMonth];}
				
				firstDay = ((firstDay + 35) - lastDate) % 7;   //change the first day of month
				
				lastDay = ((firstDay - 1) + lastDate) % 7;     //change the last day of month
				
				panel_1.removeAll();
				setCalDate();
				panel_1.revalidate();
				panel_1.repaint();
			}
		});
		btnBack.setBackground(new Color(255, 255, 240));
		panel.add(btnBack);
		
		lblDate = new JLabel(months[calMonth] + " " + calDay);
		panel.add(lblDate);
		
		btnForth = new JButton(">");
		btnForth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {    //change to next month
				
				if(calMonth != 11) {                                  //change the month
					calMonth += 1;
				}
				else { 
					calMonth = 0;
					calYear += 1;
				}
				lblDate.setText(months[calMonth] + " " + calDay);     //change the label
				
				firstDay = (firstDay + lastDate) % 7;   //change the first day of month
				
				if(calMonth == 1) {                                   //change the last date of month
					lastDate = lastDateList[calMonth] + leapCheck(calYear);
				}
				else {lastDate = lastDateList[calMonth];}
				
				lastDay = ((firstDay - 1) + lastDate) % 7;     //change the last day of month
		
				panel_1.removeAll();
				setCalDate();
				panel_1.revalidate();
				panel_1.repaint();
			}
		});
		btnForth.setBackground(new Color(255, 255, 240));
		panel.add(btnForth);
		
		panel_1 = new JPanel();
		panel_1.setSize(new Dimension(1000,1000));
		add(panel_1, BorderLayout.CENTER);
		
		setCalDate();
		
	}
	
	public void setCalDate() {                    //add date buttons depends on month and year
		
		int row = (firstDay + lastDate + (6 - lastDay)) / 7 + 1;    //set the grid row to add buttons properly 
		panel_1.setLayout(new GridLayout(row, 7, 0, 0));
		
		for(int i = 0;i < 7;i++) {                                  //first row is day of the week
			JButton btnDay = new JButton(days[i]);
			btnDay.setBackground(new Color(255, 255, 240));
			btnDay.setPreferredSize(new Dimension(27, 22));
			btnDay.setMargin(new Insets(1, 1, 1, 1) );
			panel_1.add(btnDay);
		}
		           
		if(0 == firstDay) {                    //if firstDay is Sunday, should not add empty buttons first
			createDateButtons();               //fill the grid with date buttons from the position of firstDay
			
			for(int i = 0;i < 6 - lastDay;i++) {
				createEmptyButton();
			}
		}
		else {
			for(int i = 0;i < firstDay;i++) {     //fill the grid with empty buttons before the position of firstDay
				createEmptyButton();
			}
			
			createDateButtons();                 //fill the grid with date buttons from the position of firstDay
			
			for(int i = 0;i < 6 - lastDay;i++) {
				createEmptyButton();
			}
		}
	}
	
	private int leapCheck(int year){       // test if the year is leap year
		if((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) return 1;
		else return 0;
	}
	
	public void createDateButtons() {     //fill the grid with date buttons
		for(int i = 0;i < lastDate;i++) {
			String btnName = i + 1 + "";
			JButton btnDate = new JButton(btnName);
			btnDate.setBackground(new Color(245, 245, 245));
			btnDate.setPreferredSize(new Dimension(27, 22));
			btnDate.setMargin(new Insets(1, 1, 1, 1) );
			btnDate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					calDay = Integer.parseInt(btnDate.getText());
					calDayOfTheWeek = ((calDay - 1) + firstDay) % 7;   //calculate the selected day of the week
					lblDate.setText(months[calMonth] + " " + calDay);  //change the label
				}
			});
			panel_1.add(btnDate);
		}
	}
	
	public void createEmptyButton() {     //fill the grid with 1 empty button
		JButton btnEmpty = new JButton();
	    	btnEmpty.setBackground(new Color(245, 245, 245));
	    	btnEmpty.setPreferredSize(new Dimension(27, 22));
		panel_1.add(btnEmpty);
	}

	public int getCalYear() {
		return calYear;
	}

	public int getCalMonth() {
		return calMonth;
	}

	public int getCalDay() {
		return calDay;
	}

	public int getCalDayOfTheWeek() {
		return calDayOfTheWeek + 1;
	}
	
	public Calendar getCal() {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(getCalYear(), getCalMonth(), getCalDay());
		cal.set(Calendar.DAY_OF_WEEK, calDayOfTheWeek + 1);
		return cal;
	}
}
