package br.com.svbgabriel.joglcubos;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.FPSAnimator;

@SuppressWarnings("serial")
public class CuboAnimado extends GLCanvas implements GLEventListener, KeyListener, MouseListener {

	private GL2 gl;
	private GLU glu;
	private GLUT glut;

	// Para definir as Coordenadas do sistema
	float xMin, xMax, yMin, yMax, zMin, zMax;
	float mouseX = 0.0f;
	float mouseY = 0.0f;
	float tx = 0.0f;
	float ty = 0.0f;

	double x = 40.0;
	float escala = 1.0f;
	boolean xx = true;

	// Define constants for the top-level container
	private static String TITULO = "Cubo Animado";
	private static final int CANVAS_LARGURA = 400; // largura do drawable
	private static final int CANVAS_ALTURA = 400; // altura do drawable
	private static final int FPS = 60; // define frames per second para a
										// animacao

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Cria a janela de renderizacao OpenGL
				GLCanvas canvas = new CuboAnimado();
				canvas.setPreferredSize(new Dimension(CANVAS_LARGURA, CANVAS_ALTURA));
				final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
				final JFrame frame = new JFrame();

				frame.getContentPane().add(canvas);
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						new Thread() {
							@Override
							public void run() {
								if (animator.isStarted())
									animator.stop();
								System.exit(0);
							}
						}.start();
					}
				});
				frame.setTitle(TITULO);
				frame.pack();
				frame.setLocationRelativeTo(null); // Center frame
				frame.setVisible(true);
				animator.start(); // inicia o loop de animacao
			}
		});
	}

	/** Construtor da classe */
	public CuboAnimado() {
		this.addGLEventListener(this);

		this.addKeyListener(this);
		this.addMouseListener(this);
		this.setFocusable(true);
		this.requestFocus();
	}

	/**
	 * Chamado uma vez quando o contexto OpenGL eh criado
	 */
	@Override
	public void init(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2(); // obtem o contexto grafico OpenGL
		glu = new GLU();

		// Estabelece as coordenadas do SRU (Sistema de Referencia do Universo)
		xMin = -100;
		xMax = 100;
		yMin = -100;
		yMax = 100;
		zMin = -100;
		zMax = 100;

		// Habilita o buffer de profundidade
		gl.glEnable(GL2.GL_DEPTH_TEST);
	}

	/**
	 * Chamado quando a janela eh redimensionada
	 */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		gl = drawable.getGL().getGL2(); // obtem o contexto grafico OpenGL

		// Ativa a matriz de projecao
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		// Projecao ortogonal 3D
		gl.glOrtho(xMin, xMax, yMin, yMax, zMin, zMax);

		// Ativa a matriz de modelagem
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		System.out.println("Reshape: " + width + " " + height);

	}

	/**
	 * Chamado para renderizar a imagem do GLCanvas pelo animator
	 */
	@Override
	public void display(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2(); // obtem o contexto grafico OpenGL
		glut = new GLUT();

		// Especifica que a cor para limpar a janela de visualizacao eh preta
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Limpa a janela de visualizacao com a cor de fundo especificada
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		// Redefine a matriz atual com a matriz "identidade"
		gl.glLoadIdentity();

		// criar a cena aqui....
		gl.glColor3f(0.0f, 0.0f, 0.5f);

		gl.glPushMatrix();
		gl.glScalef(escala, escala, escala);
		desenhaEsfera();
		gl.glPopMatrix();

		escalaEsfera();

		// Executa os comandos OpenGL
		gl.glFlush();
	}

	public void desenhaEsfera() {
		glut.glutSolidSphere(40, 7, 4);
	}

	public void escalaEsfera() {

		if (xx) {

			if (escala > 0.1f) {
				escala -= 0.01f;
			} else {
				xx = false;
				escala = 0;
			}

		} else {
			if (escala <= 1.0f) {
				escala += 0.01f;
			} else {
				xx = true;
				escala = 1;
			}
		}
	}

	/**
	 * Chamado quando o contexto OpenGL eh destruido
	 */
	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		}

		char keyChar = e.getKeyChar();

		switch (keyChar) {
		case '1':
			// inicia animacao
			break;
		case '2':
			// para a animacao
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseClicked(MouseEvent e) {

		int botao = e.getButton();

		if (botao == MouseEvent.BUTTON1) {
			System.out.println("btn 1");
		}

		if (botao == MouseEvent.BUTTON2) {
			System.out.println("btn 2");
		}

		if (botao == MouseEvent.BUTTON3) {
			System.out.println("btn 3");
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
