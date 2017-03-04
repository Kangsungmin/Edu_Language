package edul.view;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edul.NodeTreeView;
import edul.ReadFile;
import edul.compiler.common.Tuple;
import edul.compiler.common.UnitNode;
import edul.compiler.generator.Generator;
import edul.compiler.lexer.Lexer;
import edul.compiler.parser.Parser;
import edul.compiler.typechecker.TypeChecker;
import javafx.scene.paint.Color;


public class LabelFrame extends JFrame{
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JTextArea textArea;
	private JButton copyJButton;

	/*
	 * 
	 */



	public LabelFrame()
	{
		super("Edu-L for GUI");


		setLayout(new FlowLayout());

		label1 = new JLabel("New project\n");
		label1.setToolTipText("This is test compiler");
		add(label1);



		//메뉴관리 시작
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('x');
		fileMenu.add(exitItem);
		exitItem.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						System.exit(0);
					}
				});

		JMenu run = new JMenu("Run");
		run.setMnemonic('R');
		/**
		 * 而댄뙆�씪 踰꾪듉 
		 */
		JMenuItem run_run = new JMenuItem("Compile");
		JMenuItem run_detail = new JMenuItem("Detailed Compile(can see parse tree)");
		run.add(run_run);
		run.add(run_detail);
		run_run.addActionListener(
				new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						List<String> code = new ArrayList<>();
						String result_text = "";
						String result_code = "";
						String result_check = "";

						String text = textArea.getText();
						if(text.length()==0){
							return;
						}
						String [] text_lines = text.split("\n");
						for (int i = 0; i < text_lines.length; i++) {
							code.add(text_lines[i]);
						}

						for(String line : code){
							System.out.println(line);
							result_text+=line+"\n";
						}

						Lexer lexer = new Lexer();

						ArrayList<Tuple<String, Object>> scanned;
						try {
							scanned = lexer.scan_code(code);
							System.out.println(scanned);
							if(scanned.size()==0){
								return;
							}
							Parser parser = new Parser(scanned);

							UnitNode parsedTree = parser.startParsing();
							TypeChecker tc = new TypeChecker(parsedTree);
							tc.check();


							result_text+="\n===================scanned===================\n";
							result_text+=scanned;
							result_text+="\n====================parsed====================\n";
							result_text+=parsedTree.toString();
							result_text+="\n=================type checked=================\n";
							result_text+=tc.getInfo_table_map().get("decl_table");
							result_text+="\n===================generator==================\n";
							Generator generator = new Generator(parsedTree);
							result_text+=generator.gen();

							result_code += "=================================UCODE"
									+ "=================================\n";
							result_code += generator.gen();
							result_check += generator.getCheckResult();
							ResultFrame rf = new ResultFrame(result_text);
							ResultFrame rf2 = new ResultFrame(result_check+"\n\n"+result_code);
							rf2.setSize(600,600);
							rf2.setVisible(true);
							//						rf.setSize(600, 600);
							//						rf.setVisible(true);

						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}


					}
				}
				);
		run_detail.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				List<String> code = new ArrayList<>();
				String result_text = "";
				String result_code = "";
				String result_check = "";

				String text = textArea.getText();
				if(text.length()==0){
					return;
				}
				String [] text_lines = text.split("\n");
				for (int i = 0; i < text_lines.length; i++) {
					code.add(text_lines[i]);
				}

				for(String line : code){
					System.out.println(line);
					result_text+=line+"\n";
				}

				Lexer lexer = new Lexer();

				ArrayList<Tuple<String, Object>> scanned;
				try {
					scanned = lexer.scan_code(code);
					System.out.println(scanned);
					if(scanned.size()==0){
						return;
					}
					Parser parser = new Parser(scanned);

					UnitNode parsedTree = parser.startParsing();
					TypeChecker tc = new TypeChecker(parsedTree);
					tc.check();


					result_text+="\n===================scanned===================\n";
					result_text+=scanned;
					result_text+="\n====================parsed====================\n";
					result_text+=parsedTree.toString();
					result_text+="\n=================type checked=================\n";
					result_text+=tc.getInfo_table_map().get("decl_table");
					result_text+="\n===================generator==================\n";
					Generator generator = new Generator(parsedTree);
					result_text+=generator.gen();

					result_code += "=================================UCODE"
							+ "=================================\n";
					result_code += generator.gen();
					result_check += generator.getCheckResult();
					ResultFrame rf = new ResultFrame(result_text);
					ResultFrame rf2 = new ResultFrame(result_check+"\n\n"+result_code);
					rf2.setSize(600,600);
					rf2.setVisible(true);
					rf.setSize(600, 600);
					rf.setVisible(true);

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		JMenu help = new JMenu("도움말");
		JMenuBar bar = new JMenuBar();
		setJMenuBar(bar);
		bar.add(fileMenu);
		bar.add(run);
		bar.add(help);

		Box box = Box.createHorizontalBox();

		String s = System.getProperty("user.dir");
		textArea = new JTextArea("",25, 40);
		box.add(new JScrollPane(textArea));

		add(box);
		System.out.println(s+"\\textbg.png");

	}

	 
}