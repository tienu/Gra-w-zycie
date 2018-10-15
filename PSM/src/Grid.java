import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Grid extends JPanel {

	private List<Point> fillCells;
	int[][] cellsarray;
	int[][] summatrix;
	int[][] nextarr;
	int width;
	int heigh;
	int CELL_PER_ROW = 80;
	int CELLS_PER_COLUMN = 50;
	int cellWidth = 10;
	int cellHeigh = 10;

	public Grid(int w, int h) {

		cellsarray = new int[CELL_PER_ROW][CELLS_PER_COLUMN];

		this.width = w;
		this.heigh = h;
		// this.cellWidth=this.width/this.CELL_PER_ROW;
		// this.cellHeigh=this.heigh/this.CELLS_PER_COLUMN;

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		calccell();
		for (Point fillCell : fillCells) {
			int cellX = 10 + (fillCell.x * cellWidth);
			int cellY = 10 + (fillCell.y * cellHeigh);
			g.setColor(Color.BLACK);
			g.fillRect(cellX, cellY, cellWidth, cellHeigh);
		}

		g.setColor(Color.BLACK);

		drawGreed(g);

	}

	public void drawGreed(Graphics g) {

		g.drawRect(10, 10, this.width, this.heigh);

		for (int i = 10; i <= this.width; i += this.cellWidth) {
			g.drawLine(i, 10, i, this.heigh + 10);
		}

		for (int i = 10; i <= this.heigh; i += this.cellHeigh) {
			g.drawLine(10, i, this.width + 10, i);
		}

	}

	public void fillCell(int x, int y) {
		if (x >= 0 && x < CELL_PER_ROW && y >= 0 && y < CELLS_PER_COLUMN)
			if (cellsarray[x][y] == 0) {
				cellsarray[x][y] = 1;
			} else if (cellsarray[x][y] == 1) {
				cellsarray[x][y] = 0;
			}

		repaint();
	}

	public void calccell() {
		fillCells = new ArrayList<>();
		for (int i = 0; i < this.CELL_PER_ROW; i++) {
			for (int j = 0; j < this.CELLS_PER_COLUMN; j++) {
				if (this.cellsarray[i][j] == 1) {
					fillCells.add(new Point(i, j));
				}
			}

		}
	}

	public void calcsummatrix() {

		summatrix = new int[CELL_PER_ROW][CELLS_PER_COLUMN];
		//System.out.print(summatrix.length+","+summatrix[0].length);
		for (int j = 0; j < summatrix[0].length; j++) {
		for (int i = 0; i < summatrix.length ; i++) {
		
				if (i == 0 && j == 0) {
					summatrix[i][j] = cellsarray[i][j];
				} else if (i == 0) {
					summatrix[i][j] = cellsarray[i][j] + summatrix[i][j - 1];
				} else if (j == 0) {
					summatrix[i][j] = cellsarray[i][j] + summatrix[i - 1][j];
				} else {
					summatrix[i][j] = cellsarray[i][j] + summatrix[i - 1][j] + summatrix[i][j - 1]
							- summatrix[i - 1][j - 1];
				}
				//System.out.print("["+i +","+ j+"] ");
			}
			//System.out.println("");
		}

		nextarr = new int[80][50];
		int count = 0;
		for (int j = 0; j < nextarr[0].length -1; j++) {
			for (int i = 0; i < nextarr.length-1; i++) {
				//System.out.print("["+this.cellsarray[i][j]+",");
				if (i <= 1 && j <= 1) {
					// lewy gorny
					count = summatrix[i + 1][j + 1];
					nextarr[i][j] = calcnextstate(i, j, count);
				} else if (i == CELL_PER_ROW - 1 && j == CELLS_PER_COLUMN - 1) {
					// prawy dolny
					count = summatrix[i][j] - summatrix[i][j - 2] - summatrix[i - 2][j] + summatrix[i - 2][j - 2];
					nextarr[i][j] = calcnextstate(i, j, count);
				} else if (i <= 1 && j == CELLS_PER_COLUMN - 1) {
					// lewy dolny
					count = summatrix[i + 1][j] - summatrix[i + 1][j - 2];
					nextarr[i][j] = calcnextstate(i, j, count);
				} else if (j <= 1 && i == CELL_PER_ROW - 1) {
					// prawy gorny
					count = summatrix[i][j] - summatrix[i - 2][j + 1];
					nextarr[i][j] = calcnextstate(i, j, count);
				} else if (j == CELLS_PER_COLUMN - 1) {
					// prawy srodek
					count = summatrix[i][j] - summatrix[i - 2][j - 1] - summatrix[i][j - 2] + summatrix[i - 2][j - 2];
					nextarr[i][j] = calcnextstate(i, j, count);
				} else if (i == CELL_PER_ROW - 1) {
					// dolny srodek
					count = summatrix[i][j] - summatrix[i - 2][j] - summatrix[i + 1][j - 2] + summatrix[i - 2][j - 2];
					nextarr[i][j] = calcnextstate(i, j, count);
				} else if (i <= 1) {
					// lewy srodek
					count = summatrix[i + 1][j + 1] - summatrix[i + 1][j - 2];
					nextarr[i][j] = calcnextstate(i, j, count);
				} else if (j <= 1) {
					// gorny srodek
					count = summatrix[i + 1][j + 1] - summatrix[i - 2][j + 1];
					nextarr[i][j] = calcnextstate(i, j, count);
				} else {
					// srodek
					count = summatrix[i + 1][j + 1] - summatrix[i - 2][j + 1] - summatrix[i + 1][j - 2]
							+ summatrix[i - 2][j - 2];
					nextarr[i][j] = calcnextstate(i, j, count);
				}
			//System.out.print(nextarr[i][j] + ","+count+","+this.cellsarray[i][j]+"] ");
			//	System.out.print("["+i +","+ j+"] ");
			}
			
			//System.out.println( " ");
		}
		this.cellsarray = nextarr;

		repaint();
	}

	

	public int calcnextstate(int x, int y, int c) {
		int value = 0;
		if (this.cellsarray[x][y] == 0) {
			if (c == 3) {
				value = 1;
			}
		} else if (this.cellsarray[x][y] == 1) {
			if (c == 3 || c == 4) {
				value = 1;
			}
		}
		return value;
	}

	public void show() {

		for (int i = 0; i < this.cellsarray.length; i++) {
			for (int j = 0; j < this.cellsarray[0].length; j++) {
				System.out.print(cellsarray[i][j] + " ");
				// System.out.print(summatrix[i][j] + " ");
			}
			System.out.println("");
		}

	}

	public static void main(String[] a) {
		

		EventQueue.invokeLater(new Runnable() {
				
			
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
				}

				JFrame window = new JFrame();
				JPanel panel = new JPanel();
				JButton button = new JButton("start");
				panel.add(button);
				window.setSize(840, 570);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setVisible(true);
				Grid grid = new Grid(window.getWidth() - 40, window.getHeight() - 70);

				window.add(grid, BorderLayout.CENTER);
				window.add(panel, BorderLayout.NORTH);
				
				
				
				window.addMouseListener(new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {

						// int preparedx=(int)((e.getPoint().getX()-10)/10);
						// int preparedy=(int)((e.getPoint().getY()-10)/10);
						int preparedx = (int) (e.getPoint().getX() - 10) / 10;
						int preparedy = (int) ((e.getPoint().getY() - 70) / 10);
						// preparedy-=6;
						System.out.println("[ " + preparedx + "," + preparedy + " ]");
						grid.fillCell(preparedx, preparedy);

					}

					@Override
					public void mouseReleased(MouseEvent e) {

					}
				});

				Runnable ref = new Runnable() {

					@Override
					public void run() {
						while(true){
							grid.calcsummatrix();
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				};
				Thread action = new Thread(ref);
				
				
				button.addMouseListener(new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {
						action.start();

					}

					@Override
					public void mouseReleased(MouseEvent e) {

					}
				});
/*
				grid.fillCell(0, 0);
				grid.fillCell(79, 0);
				grid.fillCell(0, 49);
				grid.fillCell(79, 49);
				grid.fillCell(39, 24);
*/
			}
		});
	}

}