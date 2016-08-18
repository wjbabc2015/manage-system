/*
 * before excute the application, please setup MySQL;
 * create database membership_management;
 * create table membership;
 * create table administrator;
 */
import java.awt.*; 
import javax.swing.*; 
import java.awt.event.*; 
import java.sql.*;

import javax.swing.table.*;
import java.awt.print.PrinterException;
import java.text.MessageFormat;

public class membershipManagement {
	
	public membershipManagement(){
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		loginDiagram lf = new loginDiagram();
        lf.setVisible(true);
        lf.addWindowListener(new WindowAdapter(){
        	public void windowClosing(WindowEvent e){
        		System.exit(0);
        	}
        });
	}
}


//main display
class mainDisplay extends JFrame implements ActionListener{ 
	MenuBar myMenuBar=new MenuBar();
	Menu myMenuFile,myMenuEdit,myMenuUser,myMenuAbout;
	MenuItem miNew,miOpen,miSave,miSaveAs,miExit,miReportM, miReportL;
	MenuItem miAdd,miEdit,miDel,miFind,miShow,miUser,miAbout;
	
	public JTable table = new JTable(); 
	public DefaultTableModel mm; 
		
	public mainDisplay(){
		myMenuFile=new Menu("File");
		miNew=new MenuItem("New");
		miOpen=new MenuItem("Open");
		miSave=new MenuItem("Save");
		miExit=new MenuItem("exit");
		miReportM=new MenuItem("Membership Report");
		miReportL=new MenuItem("Email Report");
		//miNew.enable(false);
		//miOpen.enable(false);
		//miSave.enable(false);
		//myMenuFile.add(miNew);
		//myMenuFile.add(miOpen);
		//myMenuFile.add(miSave);
		myMenuFile.add(miReportM);
		myMenuFile.add(miReportL);
		myMenuFile.add(miExit);
		
		myMenuEdit=new Menu("Data Edit");
		miFind=new MenuItem("Data Search");
		miAdd=new MenuItem("Data Add");
		miEdit=new MenuItem("Data Modify");
		miDel=new MenuItem("Data Delete");
		miShow=new MenuItem("Display All");
		myMenuEdit.add(miFind);
		myMenuEdit.add(miAdd);
		myMenuEdit.add(miEdit);
		myMenuEdit.add(miDel);
		myMenuEdit.add(miShow);
		
		myMenuUser=new Menu("User");
		miUser=new MenuItem("Edit User");
		myMenuUser.add(miUser);
		
		myMenuAbout=new Menu("My Assignment");
		miAbout=new MenuItem("Assignment explanation");
		myMenuAbout.add(miAbout);
		
		myMenuBar.add(myMenuFile);
		myMenuBar.add(myMenuEdit);
		myMenuBar.add(myMenuUser);
		myMenuBar.add(myMenuAbout);
		
		//set table title
		String[] col = {"Memebership ID","First Name","Last Name","School","State","Email","Year Joined","Code of Active/Non-active","Amount Owed"};
		//create character list
		mm= new DefaultTableModel(col,0); 
		table.setModel(mm); 
		JScrollPane tableScrollPane = new JScrollPane(table); 
			
		this.setMenuBar(myMenuBar);
		this.add(tableScrollPane); 
		
		//set button action
		miExit.addActionListener(this);
		miReportM.addActionListener(this);
		miReportL.addActionListener(this);
		miFind.addActionListener(this);
		miAdd.addActionListener(this);
		miEdit.addActionListener(this);
		miDel.addActionListener(this);
		miShow.addActionListener(this);
		miUser.addActionListener(this);
		miAbout.addActionListener(this); 
			  
	}
	
	public void freshTable(String sql){		
		myConnection conn=new myConnection();
		ResultSet rs;
		rs=conn.getResult(sql);
		if (rs!=null){
			try{
				mm.setRowCount(0);
				table.setModel(mm);
				while(rs.next()){ 
				     String ID = rs.getString("ID"); 
				     String fName = rs.getString("firstname"); 
				     String lName = rs.getString("lastname"); 
				     String school = rs.getString("school"); 
				     String state = rs.getString("state"); 
				     String email = rs.getString("email"); 
				     String year = rs.getString("yearjoined"); 
				     String code = rs.getString("code"); 
				     String owed = rs.getString("amountowed");
				     String[] cloumns ={ID,fName,lName,school,state,email,year,code,owed}; 
				     
				     mm.addRow(cloumns);     
			   } 
			   //table.clearSelection();
			   table.setModel(mm);
			}catch(Exception e){
				System.out.println(e.toString());
			}
		}		
	}
	
	
	public void actionPerformed(ActionEvent e){
		//exit
		if (e.getSource()==miExit){
			System.exit(0);
		//Membership Report
		}else if (e.getSource()==miReportM){
			reportMember rg = new reportMember();
			rg.setVisible(true);
		}else if (e.getSource()==miReportL){
			reportEmail re = new reportEmail();
			re.setVisible(true);
		//search
		}else if(e.getSource()==miFind){
			findFunc ff=new findFunc(this);
			ff.setVisible(true);	
		//add		
		}else if(e.getSource()==miAdd){
			addFunc af=new addFunc(this);
			af.setVisible(true);
		//modify
		}else if(e.getSource()==miEdit){
			if (table.getSelectedRow()==-1){
				JOptionPane.showMessageDialog(null, "Please select what you want", "suggestion!", JOptionPane.INFORMATION_MESSAGE);
			}else{			
				editFunc ef=new editFunc(this);
				
				ef.ID.setText((String)table.getValueAt(table.getSelectedRow(),0));
				ef.fName.setText((String)table.getValueAt(table.getSelectedRow(),1));
				ef.lName.setText((String)table.getValueAt(table.getSelectedRow(),2));
				ef.school.setText((String)table.getValueAt(table.getSelectedRow(),3));
				ef.state.setText((String)table.getValueAt(table.getSelectedRow(),4));
				ef.email.setText((String)table.getValueAt(table.getSelectedRow(),5));
				ef.year.setText((String)table.getValueAt(table.getSelectedRow(),6));
				ef.code.setText((String)table.getValueAt(table.getSelectedRow(),7));
				ef.owed.setText((String)table.getValueAt(table.getSelectedRow(),8));
				ef.setVisible(true);
				this.freshTable("select * from membership where ID > 0");
			}
		//delete
		}else if(e.getSource()==miDel){
			if (table.getSelectedRow()==-1){
				JOptionPane.showMessageDialog(null, "Please slect the row you want to delete", "Warning", JOptionPane.INFORMATION_MESSAGE);
			
			}else {
				
				String sql="delete from membership where ID = '"+table.getValueAt(table.getSelectedRow(),0)+"'"; 
				//JOptionPane.showMessageDialog(null, sql, "Warning", JOptionPane.INFORMATION_MESSAGE);
				myConnection conn=new myConnection();
				if (conn.executeSql(sql)){					
					JOptionPane.showMessageDialog(null, "Delete Successfully", "Warning", JOptionPane.INFORMATION_MESSAGE);
					this.freshTable("select * from membership where ID > 0");
				}else {
					JOptionPane.showMessageDialog(null, "Unknown error", "Fail to delete", JOptionPane.INFORMATION_MESSAGE);
				}				
			}
		//display	
		}else if(e.getSource()==miShow){
			//JOptionPane.showMessageDialog(null, "Unknown error", "Fail to delete", JOptionPane.INFORMATION_MESSAGE);
			this.freshTable("select * from membership where ID > 0");
		//User management
		}else if(e.getSource()==miUser){
			userFunc uf=new userFunc();
			uf.setVisible(true);
		//reference	
		}else if(e.getSource()==miAbout){
			aboutFunc af=new aboutFunc();
			af.setVisible(true);
		}
	}
}

//service for connection between MySql and application
class myConnection{
	ResultSet re;

	String strurl = "jdbc:mysql://localhost/membership_management";
	static final String USER = "root";
	static final String PASS = "root";
	
	public myConnection(){}
	//three different functions for SQL statement
	public ResultSet getResult(String sql){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn=DriverManager.getConnection(strurl, USER, PASS);
			Statement stmt=conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet re=stmt.executeQuery(sql);
			return re;
		}
		catch(Exception e){
			System.out.println("getResult------"+e.toString());
			return null;
		}
	}
	
	public boolean executeSql(String sql){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn=DriverManager.getConnection(strurl, USER, PASS);
			Statement stmt=conn.createStatement();
			stmt.executeUpdate(sql);
			//conn.commit();
			return true;
		}
		catch(Exception e){
			System.out.println("executeSql----"+e.toString());
			return false;
		}
	}
	
	public boolean outfile (String sql) {
		try{
			Class.forName("com.mysql.jdbc.Driver");
		
		Connection conn=DriverManager.getConnection(strurl, USER, PASS);
		Statement stmt=conn.createStatement();
		stmt.execute(sql);
		return true;
		}
		catch(Exception e){
			System.out.println("outfile----"+e.toString());
			return false;
		}
	}
}

//add new member in database 
class addFunc extends JDialog implements ActionListener{ 
	public static final int WIDTH = 400; 
	public static final int HEIGHT = 400; 
	
	JLabel ID1,fName1,lName1,school1,Age1,state1,email1,year1,code1,owed1;
	JTextField ID,fName,lName,school,state,email,year,code,owed;
	JButton b;
	JPanel p;
	
	mainDisplay mf;
	
	public addFunc(mainDisplay mmf){ 
	   setTitle("Add Membership Information"); 
	   setSize(WIDTH,HEIGHT); 
	   setLocation(120,180); 
	   
	   Container contentPane = getContentPane(); 
	   contentPane.setLayout(new FlowLayout()); 
	   
	   ID1=new JLabel("ID");
	   fName1=new JLabel("First Name");
	   lName1=new JLabel("Last Name");
	   school1=new JLabel("School");
	   state1=new JLabel("State");
	   email1=new JLabel("E-mail");
	   year1=new JLabel("Year Joined");	   
	   code1=new JLabel("Code of Active/Non-active");
	   owed1=new JLabel("Amount Owed");
	   
	   ID=new JTextField(5);
	   fName=new JTextField(5);
	   lName=new JTextField(5);
	   school=new JTextField(5);
	   state=new JTextField(5);
	   email=new JTextField(5);
	   year=new JTextField(10);
	   code=new JTextField(10);
	   owed=new JTextField(10);

	   b=new JButton("Submit");
	   p=new JPanel();
	   p.setLayout(new GridLayout(12,2,5,5));
	   p.add(ID1);
	   p.add(ID);
	   p.add(fName1);
	   p.add(fName);
	   p.add(lName1);
	   p.add(lName);
	   p.add(school1);
	   p.add(school);
	   p.add(state1);
	   p.add(state);
	   p.add(email1);
	   p.add(email);
	   p.add(year1);
	   p.add(year);
	   p.add(code1);
	   p.add(code);
	   p.add(owed1);
	   p.add(owed);
	   p.add(new Label(""));
	   p.add(new Label(""));
	   p.add(b);
	   contentPane.add(p);
	   //add button listener
	   b.addActionListener(this);
	   mf=mmf;
	}
	 	
	public void actionPerformed(ActionEvent e){
		if (ID.getText().toString().equals("")){
			JOptionPane.showMessageDialog(null, "Please enter ID", "Warning", JOptionPane.INFORMATION_MESSAGE);
		}else if (fName.getText().toString().equals("")){
			JOptionPane.showMessageDialog(null, "Please enter First Name", "Warning", JOptionPane.INFORMATION_MESSAGE);
		}else if (lName.getText().toString().equals("")){
			JOptionPane.showMessageDialog(null, "Please enter Last Name", "Warning", JOptionPane.INFORMATION_MESSAGE);
		}else{
			String sql="select * from membership where ID='" + ID.getText() + "'";//get the ID from the textField
			myConnection conn=new myConnection();
			ResultSet rs;
			rs=conn.getResult(sql);
			try{
				//System.out.println(rs.getRow());
				//check if the ID existed
				if (rs.next()){
					JOptionPane.showMessageDialog(null, "The ID has already existed", "Warning", JOptionPane.INFORMATION_MESSAGE);
				}else{
					sql="insert into membership values('" +ID.getText()+"','"+ fName.getText() +"','"+ lName.getText() +"','"+ school.getText() +"','"+ state.getText() +"','"+ email.getText() +"','"+ year.getText() +"','"+ code.getText() +"','"+ owed.getText() +"')";
					if (conn.executeSql(sql)){						
						JOptionPane.showMessageDialog(null, "Add successfully", "Warning", JOptionPane.INFORMATION_MESSAGE);
						mf.freshTable("select * from membership where ID > 0");
						ID.setText("");
						fName.setText("");
						lName.setText("");
						school.setText("");
						state.setText("");
						email.setText("");
						year.setText("");
						code.setText("");
						owed.setText("");
						
					}else{
						JOptionPane.showMessageDialog(null, "Failed to add", "Warning", JOptionPane.INFORMATION_MESSAGE);
					}				
				}
			}catch(Exception er){
				System.out.println(er.toString());
			}
		}		
	}
		
}

//the function for modifying the member information
class editFunc extends JDialog implements ActionListener{ 
	public static final int WIDTH = 400; 
	public static final int HEIGHT = 400; 
	
	JLabel ID1,fName1,lName1,school1,Age1,state1,email1,year1,code1,owed1;
	JTextField ID,fName,lName,school,state,email,year,code,owed;
	JButton b;
	JPanel p;
	
	mainDisplay mf;
	
	public editFunc(mainDisplay mmf){ 
	   setTitle("Modify Membership Information"); 
	   setSize(WIDTH,HEIGHT); 
	   setLocation(120,180); 
	   
	   Container contentPane = getContentPane(); 
	   contentPane.setLayout(new FlowLayout()); 
	   
	   ID1=new JLabel("ID");
	   fName1=new JLabel("First Name");
	   lName1=new JLabel("Last Name");
	   school1=new JLabel("School");
	   state1=new JLabel("State");
	   email1=new JLabel("E-mail");
	   year1=new JLabel("Year Joined");	   
	   code1=new JLabel("Code of Active/Non-active");
	   owed1=new JLabel("Amount Owed");
	   
	   ID=new JTextField(5);
	   fName=new JTextField(5);
	   lName=new JTextField(5);
	   school=new JTextField(5);
	   state=new JTextField(5);
	   email=new JTextField(5);
	   year=new JTextField(10);
	   code=new JTextField(10);
	   owed=new JTextField(10);
	   ID.setEnabled(false);

	   b=new JButton("Submit");
	   p=new JPanel();
	   p.setLayout(new GridLayout(12,2,5,5));
	   p.add(ID1);
	   p.add(ID);
	   p.add(fName1);
	   p.add(fName);
	   p.add(lName1);
	   p.add(lName);
	   p.add(school1);
	   p.add(school);
	   p.add(state1);
	   p.add(state);
	   p.add(email1);
	   p.add(email);
	   p.add(year1);
	   p.add(year);
	   p.add(code1);
	   p.add(code);
	   p.add(owed1);
	   p.add(owed);
	   p.add(new Label(""));
	   p.add(new Label(""));
	   p.add(b);
	   contentPane.add(p);
	   //add action listener
	   b.addActionListener(this);
	   mf=mmf;
	} 
		
	public void actionPerformed(ActionEvent e){
		
		if (ID.getText().toString().equals("")){
			JOptionPane.showMessageDialog(null, "Please enter ID", "Warning", JOptionPane.INFORMATION_MESSAGE);
		}else if (fName.getText().toString().equals("")){
			JOptionPane.showMessageDialog(null, "Please enter First Name", "Warning", JOptionPane.INFORMATION_MESSAGE);
		}else if (lName.getText().toString().equals("")){
			JOptionPane.showMessageDialog(null, "Please enter Last Name", "Warning", JOptionPane.INFORMATION_MESSAGE);
		}else{
			//SQL statement
			String sql="update member set firstname='"+fName.getText()+"',lastname='"+lName.getText()+"',school="+school.getText()+",state='"+state.getText()+"',email='"+email.getText()+"',yearjoined='"+year.getText()+"',code='"+code.getText()+"',amountowed='"+owed.getText()+"' where ID='" + ID.getText() + "'";
			myConnection conn=new myConnection();
			try{
				//JOptionPane.showMessageDialog(null, sql, "Warning", JOptionPane.INFORMATION_MESSAGE);
				if (conn.executeSql(sql)){
					JOptionPane.showMessageDialog(null, "Modified successfully", "Warning", JOptionPane.INFORMATION_MESSAGE);
					mf.freshTable("select * from membership where ID > 0");
					this.dispose();
				}else{
					JOptionPane.showMessageDialog(null, "Failed to modify", "Warning", JOptionPane.INFORMATION_MESSAGE);
				}
			}catch(Exception er){
				System.out.println(er.toString());
			}
		}		
	}
		
}

//function for searching
class findFunc extends JDialog implements ActionListener{ 

	mainDisplay mf;
	JPanel p;
	JComboBox c;
	JTextField t;
	JButton b;
	JButton fAll;
	String sql="select * from membership where ID > 0";
	String[] colStr={"Memebership Number","First Name","Last Name","School","State","Email","Year Joined","Code of Active/Non-active","Amount Owed"};

	public findFunc(mainDisplay mmf){
		mf=mmf;
		p=new JPanel();
		c=new JComboBox(colStr);
		t=new JTextField(10);
		b=new JButton("Search");
		fAll=new JButton("Display All");
		
		b.addActionListener(this);
		fAll.addActionListener(this);
		
		p.add(new JLabel("Select"));
		p.add(c);
		p.add(new JLabel("Searching Content"));
		p.add(t);
		p.add(b);
		p.add(fAll);
		this.add(p);
		this.setTitle("Search");
		this.setSize(450,120);
	}
	
	
	public void actionPerformed(ActionEvent e){
		//Search
		if (e.getSource()==b){
			String selectStr=c.getSelectedItem().toString();//get data from the drop list
			if (selectStr=="ID"){
				sql="select * from membership where "+selectStr+" = "+t.getText().toString();
			}
			else {
				sql="select * from membership where "+selectStr+" = '"+t.getText().toString()+"'";
			}

			
			mf.freshTable(sql);
		//display all
		}if (e.getSource()==fAll){
			sql="select * from membership where ID > 0";
			mf.freshTable(sql);
		}
	}
}

//please change the information below based on your situation
class aboutFunc extends JDialog{
	aboutFunc(){
		JPanel p1=new JPanel();
		p1.add(new JLabel("Membership Management System"));
		p1.add(new JLabel("Produced by Jeremy"));
		p1.add(new JLabel("Email: wangj15@students.ecu.edu"));
		p1.setLayout(new GridLayout(4,1,5,5));
		this.add(p1);
		this.setTitle("About Me");
		this.setSize(300,200);	
	}
}


//function for managing the administrator
class userFunc extends JFrame implements   ActionListener{ 
	
	JTextField user,pass;
	JButton add,del;
	JTable t;
	JPanel p1,p2,p3,p4,p5;
	DefaultTableModel m;
	
	public  userFunc(){
		p1=new JPanel();
		p2=new JPanel();
		p3=new JPanel();
		p4=new JPanel();
		p5=new JPanel();
		
		user=new JTextField(8);
		pass=new JTextField(8);
		add=new JButton("Add");
		del=new JButton("Delete");
		String[] col = {"Username","Password"};
		
		m= new DefaultTableModel(col,0); 
		t=new JTable();
		t.setModel(m); 		
		JScrollPane sp = new JScrollPane(t);
		
		p1.add(new JLabel("Username"));
		p1.add(user);
		p1.add(new JLabel("Password"));
		p1.add(pass);
		p1.add(add);
		p2.add(sp);
		p3.add(del);		
		
		add.addActionListener(this);
		del.addActionListener(this);
		
		myConnection conn=new myConnection();
		ResultSet rs;
		rs=conn.getResult("select * from administrator");
		if (rs!=null){
			try{
				//m.setRowCount(0);
				//table.setModel(mm);
				while(rs.next()){ 
				     String useName = rs.getString("username"); 
				     String passWord = rs.getString("password"); 
				     String[] cloumns ={useName,passWord}; 				     
				     m.addRow(cloumns);     
			   } 
			   t.setModel(m);
			}catch(Exception e){
				System.out.println(e.toString());
			}
		}
		
		
		
		this.add(p1,BorderLayout.NORTH);
		this.add(p2,BorderLayout.CENTER);
		this.add(p3,BorderLayout.SOUTH);
		this.add(p4,BorderLayout.WEST);
		this.add(p5,BorderLayout.EAST);
		this.setTitle("User Management");
		this.setSize(600,400);
	}

	public void actionPerformed(ActionEvent e){
		//add
		if (e.getSource()==add){
			if (user.getText().toString().equals("")){
				JOptionPane.showMessageDialog(null, "Please enter username!", "Warning", JOptionPane.INFORMATION_MESSAGE);
			}else if (pass.getText().toString().equals("")){
				JOptionPane.showMessageDialog(null, "Please enter password!", "Warning", JOptionPane.INFORMATION_MESSAGE);
			}else{
				myConnection conn=new myConnection();
				ResultSet rs;
				try{
					rs=conn.getResult("select * from administrator where username='"+user.getText().toString()+"'");				
					if (rs.next()){
						JOptionPane.showMessageDialog(null, "The username has already existed!", "Warning", JOptionPane.INFORMATION_MESSAGE);
					}else{
						if (conn.executeSql("insert into administrator values('"+user.getText().toString()+"','"+pass.getText().toString()+"')")){
							String[] newUser={user.getText(),pass.getText()};							
							m.addRow(newUser);
							t.setModel(m);
							JOptionPane.showMessageDialog(null, "Add Successfully", "Warning", JOptionPane.INFORMATION_MESSAGE);
						}else{
							JOptionPane.showMessageDialog(null, "Failed to add", "Warning", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}catch(Exception er){
					System.out.println(er.toString());
				}
			}
			
			//delete
		}else if (e.getSource()==del){
			if (t.getSelectedRow()==-1){
				JOptionPane.showMessageDialog(null, "Please select which row you want to delete", "Warning", JOptionPane.INFORMATION_MESSAGE);
			
			}else {
				
				String sql="delete from administrator where username = '"+t.getValueAt(t.getSelectedRow(),0)+"'"; 
				//JOptionPane.showMessageDialog(null, sql, "Warning", JOptionPane.INFORMATION_MESSAGE);
				myConnection conn=new myConnection();
				if (conn.executeSql(sql)){					
					m.removeRow(t.getSelectedRow());
					t.setModel(m);
					//t.removeRowSelectionInterval();
					JOptionPane.showMessageDialog(null, "Delete Successfully", "Warning", JOptionPane.INFORMATION_MESSAGE);
				}else {
					JOptionPane.showMessageDialog(null, "Unknown Error", "Failed to delete", JOptionPane.INFORMATION_MESSAGE);
				}				
			}
		}
	}
}

//function for loging into the system
class loginDiagram extends JDialog implements ActionListener{ 

	JPanel p;
	JTextField user,pass;
	JButton login,cancel;

	public loginDiagram(){
		p=new JPanel();
		user=new JTextField(10);
		pass=new JTextField(10);
		login=new JButton("Login");
		cancel=new JButton("Quit");
		
		user.setText("admin");
		pass.setText("admin");
		
		login.addActionListener(this);
		cancel.addActionListener(this);
		
		p.add(new JLabel("Account"));
		p.add(user);
		p.add(new JLabel("Password"));
		p.add(pass);
		p.add(login);
		p.add(cancel);
		this.add(p);
		this.setTitle("System Login");
		this.setSize(210,180);
	}
	
	//comparing the username and password with the data stored in the table administrator
	public void actionPerformed(ActionEvent e){
		//search 
		if (e.getSource()==login){
			
			if (user.getText().toString().equals("")){
				JOptionPane.showMessageDialog(null, "Please enter username!", "Warning", JOptionPane.INFORMATION_MESSAGE);
			}else if (pass.getText().toString().equals("")){
				JOptionPane.showMessageDialog(null, "Please enter password!", "Warning", JOptionPane.INFORMATION_MESSAGE);
			}else{
				myConnection conn=new myConnection();
				ResultSet rs;
				String 	sql="select * from administrator where username = '"+user.getText().toString()+"' and password = '"+pass.getText().toString()+"'";
				try{
					rs=conn.getResult(sql);				
					if (rs.next()){
						this.dispose();
						//JOptionPane.showMessageDialog(null, "The username has already existed!", "Warning", JOptionPane.INFORMATION_MESSAGE);
				        sql="select * from membership where ID > 0";
				        mainDisplay mf=new mainDisplay();
				        mf.setTitle("Membership Management System");
				        mf.setSize(600,486);
				        mf.freshTable(sql);
				        mf.setVisible(true);
				        mf.addWindowListener(new WindowAdapter(){
				        	public void windowClosing(WindowEvent e){
				        		System.exit(0);
				        	}
				        });
					}else{						
						JOptionPane.showMessageDialog(null, "Incorrect username or password!", "Error!", JOptionPane.INFORMATION_MESSAGE);						
					}
				}catch(Exception er){
					System.out.println(er.toString());
				}
			}
		//cancellation
		}if (e.getSource()==cancel){
			System.exit(0);
		}
	}
}

//generating the report for members` owing balance
class reportMember extends JDialog implements ActionListener{
	
	JLabel active1, nonActive1, owingMember1, amountOwed1;
	JTextArea active ,nonActive, owingMember, amountOwed;
	JButton print;
	JTable reportTable;
	JPanel p1,p2,p3,p4,p5,p6,p;
	DefaultTableModel m;
	
	public  reportMember(){
		
		p1=new JPanel();
		p2=new JPanel();
		p3=new JPanel();
		p4=new JPanel();
		p5=new JPanel();
		p6=new JPanel();
		p =new JPanel();
		
		active=new JTextArea(1,1);
		active.setEditable(false);
		nonActive=new JTextArea(1,1);
		nonActive.setEditable(false);
		owingMember=new JTextArea(1,1);
		owingMember.setEditable(false);
		amountOwed=new JTextArea(1,1);
		amountOwed.setEditable(false);
		active1=new JLabel("Total Number of Active Members:");
		nonActive1=new JLabel("Total Number of Non-Active Members:");
		owingMember1=new JLabel("Total Number of Members owing:");
		amountOwed1=new JLabel("The Amount owed:");
		print=new JButton("Print");
		String[] col = {"Members Name","State"};
		
		m= new DefaultTableModel(col,0); 
		reportTable=new JTable();
		reportTable.setModel(m); 		
		JScrollPane sp = new JScrollPane(reportTable);
		
		p1.add(new JLabel("The Report of Members Owing Balance"));
		p2.add(sp);
		p3.add(p6);
		p6.add(p,BorderLayout.CENTER);
		p.setLayout(new GridLayout(4,2,1,1));
		p.add(nonActive1);
		p.add(nonActive);
		p.add(active1);
		p.add(active);
		p.add(owingMember1);
		p.add(owingMember);
		p.add(amountOwed1);
		p.add(amountOwed);
		p5.add(print);
		
		
		
		
		this.add(p1,BorderLayout.NORTH);
		this.add(p2,BorderLayout.CENTER);
		this.add(p3,BorderLayout.SOUTH);
		this.add(p4,BorderLayout.WEST);
		this.add(p5,BorderLayout.EAST);
		this.setTitle("Report Generation");
		this.setSize(600,400);
		
		print.addActionListener(this);
		
		//get data from database
		myConnection conn=new myConnection();
		ResultSet rs;
		rs=conn.getResult("select firstname, lastname, state from membership where amountowed>0 order by state");
		if (rs!=null){
			try{
				//m.setRowCount(0);
				//table.setModel(mm);
				while(rs.next()){ 
				     String firstName = rs.getString("firstname"); 
				     String lastName = rs.getString("lastname");
				     String state = rs.getString("state");
				     String memberName = firstName + " " + lastName;
				     String[] cloumns ={memberName,state}; 				     
				     m.addRow(cloumns);     
			   } 
				reportTable.setModel(m);
			}catch(Exception e){
				System.out.println(e.toString());
			}
		}
		
		//calculate the active member
		ResultSet count1;
		count1 = conn.getResult("select count(*) as counta from membership where code ='1'");
		if (count1 != null){
			try{
				while(count1.next()){
				String count = count1.getString("counta");
				active.setText(count);
				}
				}catch(Exception e){
				System.out.println(e.toString());
				
			}
		}
		
		//calculate the non-active member
		ResultSet count2;
		count2 = conn.getResult("select count(*) as countb from membership where code = '0'");
		if (count2 != null){
			try{
				while(count2.next()){
				String count = count2.getString("countb");
				nonActive.setText(count);
				}
				}catch(Exception e){
				System.out.println(e.toString());
				
			}
		}
		
		
		//calculate the amount numbers of members owed balance
		ResultSet number;
		number = conn.getResult("select count(*) as number from membership where amountowed > 0");
		if (number != null){
			try{
				while(number.next()){
				String count = number.getString("number");
				int temp = Integer.parseInt(count);
				String numberf = Integer.toString(temp);
				owingMember.setText(numberf);
				}
				}catch(Exception e){
				System.out.println(e.toString());
				
			}
		}

		
		//calculate the amount balance
		Double amountTotal = 0.00;
		ResultSet amountl;
		amountl = conn.getResult("select amountowed from membership where ID > 0");
		if (amountl!=null){
			try{
				while (amountl.next()){
					String amounts = amountl.getString("amountowed");
					double temp = Double.parseDouble(amounts);
					amountTotal = amountTotal + temp;
					String amount = Double.toString(amountTotal);
					amountOwed.setText(amount);
				}
			}catch(Exception e){
				System.out.println(e.toString());
			}
		}
	}

	//print the report
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		print.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	printreportTable();
            }
		}
);

}
			//function for printing the report
			private void printreportTable() {
				// TODO Auto-generated method stub
				MessageFormat header = new MessageFormat("The Report of Members Owing Balance");
				MessageFormat footer = new MessageFormat("- {0} -");
				
				boolean fitWidth = true;
				JTable.PrintMode mode = fitWidth ? JTable.PrintMode.FIT_WIDTH
                        : JTable.PrintMode.NORMAL;
				
				
				try{
					boolean complete1 = reportTable.print(mode, header, footer, true , null, false, null);
					
					if (complete1){
						JOptionPane.showMessageDialog(this,
                                "Printing Complete",
                                "Printing Result",
                                JOptionPane.INFORMATION_MESSAGE);
						} else {
							/* show a message indicating that printing was cancelled */
							JOptionPane.showMessageDialog(this,
                                "Printing Cancelled",
                                "Printing Result",
                                JOptionPane.INFORMATION_MESSAGE);
						}
				} catch (PrinterException pe) {
					/* Printing failed, report to the user */
					JOptionPane.showMessageDialog(this,
                            "Printing Failed: " + pe.getMessage(),
                            "Printing Result",
                            JOptionPane.ERROR_MESSAGE);
					}
		}	
}	


//generating the report of members` personal information
class reportEmail extends JDialog implements ActionListener{
	
	JButton print, export;
	JTable reportTable;
	JPanel p1,p2,p3,p4,p5;
	DefaultTableModel m;
	
	public  reportEmail(){
		
		final int WIDE = 260;
		p1=new JPanel();
		p2=new JPanel();
		p3=new JPanel();
		p4=new JPanel();
		p5=new JPanel();
		
		print=new JButton("Print");
		export=new JButton("Export Excel");
		String[] col = {"Members Name","Email", "State"};
		
		m= new DefaultTableModel(col,0); 
		reportTable=new JTable();
		reportTable.setModel(m);
		reportTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < reportTable.getColumnCount(); i++) {
            TableColumn column = reportTable.getColumnModel().getColumn(i);
            if (i == 0){
            	column.setMinWidth(WIDE/2);
            }
            
            if (i ==1){
            	column.setMinWidth(WIDE);
            }
        }
        //reportTable.setPreferredScrollableViewportSize(new Dimension(
        //		reportTable.getColumnCount() * 100, reportTable.getRowHeight() * 16));
		JScrollPane sp = new JScrollPane(reportTable);
		
		p1.add(new JLabel("The Report of Members Owing Balance"));
		p2.add(sp);
		p3.add(print);
		p3.add(export);
		
		
		
		
		this.add(p1,BorderLayout.NORTH);
		this.add(p2,BorderLayout.CENTER);
		this.add(p3,BorderLayout.SOUTH);
		this.add(p4,BorderLayout.WEST);
		this.add(p5,BorderLayout.EAST);
		this.setTitle("Report Generation");
		this.setSize(600,400);
		
		print.addActionListener(this);
		export.addActionListener(this);
		
		myConnection conn=new myConnection();
		ResultSet rs;
		String createSql = "create table report ("
				+ " member varchar(255),"
				+ " email varchar(255),"
				+ " state varchar(255));";
		//each time when people view the table, drop the old table
		if (!conn.executeSql(createSql)){
			conn.executeSql("drop table report");
		}
		rs=conn.getResult("select firstname, lastname, state, email from membership where yearjoined < 2011 && yearjoined > 0 order by state");
		conn.executeSql(createSql);
		//intert the title of the email report
		conn.executeSql("insert into report values ('Member Name', 'Email', 'State');");
		if (rs!=null){
			try{
				//m.setRowCount(0);
				//table.setModel(mm);
				while(rs.next()){ 
				     String firstName = rs.getString("firstname"); 
				     String lastName = rs.getString("lastname");
				     String email = rs.getString("email");
				     String state = rs.getString("state");
				     String memberName = firstName + " " + lastName;
				     
				     //add the data retrieved from database into the new table report
				     conn.executeSql("insert into report values('" + memberName+ "', '" + email + "', '" + state + "')");
				     
				     String[] cloumns ={memberName,email,state}; 				     
				     m.addRow(cloumns);
			   } 
				reportTable.setModel(m);
			}catch(Exception e){
				System.out.println(e.toString());
			}
		}			
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		print.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                printreportTable();
            }
		}
);
		
		//export the table report to the excel in the folder which was defined by the MySQL
		export.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	myConnection conn=new myConnection();
				//add the path of the excel file
            	String sql = "select * from report into outfile 'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/memberlist.cvs' fields terminated by ',' enclosed by '''' lines terminated by '\n'";
            	if (conn.outfile(sql)){
            		JOptionPane.showMessageDialog(null, "Export successfully", "Warning", JOptionPane.INFORMATION_MESSAGE);
            	}
            	
            }
		}
);
}
			private void printreportTable() {
				// TODO Auto-generated method stub
				MessageFormat header = new MessageFormat("The Report of Members List");
				MessageFormat footer = new MessageFormat("- {0} -");
				
				boolean fitWidth = true;
				JTable.PrintMode mode = fitWidth ? JTable.PrintMode.FIT_WIDTH
                        : JTable.PrintMode.NORMAL;
				
				try{
					boolean complete = reportTable.print(mode, header, footer, true , null, false, null);
					
					if (complete){
						JOptionPane.showMessageDialog(this,
                                "Printing Complete",
                                "Printing Result",
                                JOptionPane.INFORMATION_MESSAGE);
						} else {
							/* show a message indicating that printing was cancelled */
							JOptionPane.showMessageDialog(this,
                                "Printing Cancelled",
                                "Printing Result",
                                JOptionPane.INFORMATION_MESSAGE);
						}
				} catch (PrinterException pe) {
					/* Printing failed, report to the user */
					JOptionPane.showMessageDialog(this,
                            "Printing Failed: " + pe.getMessage(),
                            "Printing Result",
                            JOptionPane.ERROR_MESSAGE);
					}
		}	
}


