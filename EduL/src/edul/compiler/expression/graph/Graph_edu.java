package edul.compiler.expression.graph;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Graph_edu extends Frame {
	String in;
	int a, b, c; // Y = aX^2 + bX + c

	public Graph_edu(int a, int b, int c, int check) {

		this.a = a;
		this.b = b;
		this.c = c;

		if (check == 1) {
			add(new XCanvas());
			setSize(500, 500);
			setVisible(true);
			this.addWindowListener(new WindowAdapter() { // 닫기창 활성화
				public void windowClosing(WindowEvent e) { // 닫기창을 클릭하면
					System.exit(0); // 종료
				}
			});
		}
	}

	class XCanvas extends Canvas {
		int y;

		public void paint(Graphics g) {

			g.setColor(Color.red);

			int pastx, pasty;
			pastx = 250;
			pasty = 250;
			System.out.println("계수의 값들 : " + a + "," + b + "," + c);
			int flag = 0;
			for (int i = 0; i <= 250; i++) { // 1,4 사분면 그래프 그리기
				int x;
				x = i;
				y = a * x * x + b * x + c;
				// System.out.println(y)25;
				g.drawLine(pastx, pasty, 15 * x + 250, 250 - 15 * y);
				pastx = 15 * x + 250;
				pasty = 250 - 15 * y;
			}

			pastx = 250;
			pasty = 250;
			for (int i = 0; i <= 250; i++) { // 2,3 사분면 그래프 그리기
				int x;
				x = -i;
				y = a * x * x + b * x + c;
				g.drawLine(pastx, pasty, 15 * x + 250, 250 - 15 * y);
				pastx = 15 * x + 250;
				pasty = 250 - 15 * y;
			}
			g.setColor(Color.black);
			g.drawLine(0, 250, 500, 250);
			g.drawLine(250, 0, 250, 500);
		}
	}
}