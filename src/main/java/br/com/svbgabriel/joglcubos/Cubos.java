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
public class Cubos extends GLCanvas implements GLEventListener, KeyListener {

	private GL2 gl;
	private GLU glu;
	private GLUT glut;

	// Para definir as Coordenadas do sistema
	float xMin, xMax, yMin, yMax, zMin, zMax;
	float tx = 0.0f;
	float ty = 0.0f;
	float x = 0.0f;
	float i = 0.0f;
	boolean b = false;

	private float angulo;
	private float incAngulo;

	int cuboSize = 20;

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
				GLCanvas canvas = new Cubos();
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
	public Cubos() {
		this.addGLEventListener(this);

		this.addKeyListener(this);
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

		angulo = 0;
		incAngulo = 0;

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
		gl.glRotatef(angulo, 0, 1, 1);
		gl.glTranslatef(tx, 0.0f, 0.0f);
		desenhaCubo(cuboSize);
		gl.glPopMatrix();

		gl.glColor3f(0.5f, 0.0f, 0.5f);

		gl.glPushMatrix();
		gl.glRotatef(angulo, 1, 0, 1);
		gl.glTranslatef(ty, 0.0f, 0.0f);
		desenhaCubo(cuboSize);
		gl.glPopMatrix();

		// Rotacao do Cubo
		rotacionarCubo();

		AnimacaoTranladaCubo();

		// Executa os comandos OpenGL
		gl.glFlush();
	}

	public void desenhaCubo(int size) {
		glut.glutSolidCube(size);
	}

	// Animacao de tranla��o do Cubo
	private void AnimacaoTranladaCubo() {
		if (tx < (xMax - 10)) {
			tx += i;
		} else if (tx > (xMin + 10)) {
			tx -= i;
		}

	}

	// Animacao de rotacao do Cubo
	private void rotacionarCubo() {
		angulo = angulo + incAngulo;
		if (angulo > 360f) {
			angulo = angulo - 360;
		}
		// System.out.println("ANGULO: " + (int)angulo);
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
			incAngulo = 1.0f;
			break;
		case '2':
			// para a animacao
			incAngulo = 0.0f;
			break;
		case '4':
			i += 1.0f;
			break;
		case '5':
			i -= 1.0f;
			break;
		case '7':
			cuboSize = cuboSize * 3;
			break;
		case '8':
			cuboSize = cuboSize / 3;
			break;
		case '6':
			tx++;
			ty--;
			break;
		case '9':
			tx--;
			ty++;
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

}
