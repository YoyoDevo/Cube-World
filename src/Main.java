/** *****************************************************************************
 * File: Main.java
 * Authors: Arsham Ravanipour
 *          John Quiros
 *          Cesar Pedroza
 *          William Wells
 *
 * Class CS 445 - Computer Graphics
 *
 * Assignment: Quarter Project - Final Check Point
 * Date last modified: 5/29/2017
 *
 * Purpose: Draws a cube and allows the user to control the camera and move
 * around using w,a,s,d,e and space.
 ***************************************************************************** */

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import static org.lwjgl.util.glu.GLU.gluPerspective;


public class Main {
    public static final int DISPLAY_HEIGHT = 480;
    public static final int DISPLAY_WIDTH = 640;
    private DisplayMode displayMode;
    private FPCameraController fp;
    
    private FloatBuffer lightPosition;
    private FloatBuffer whiteLight;

    public static void main(String[] args) {
        Main main = null;
        main = new Main();
        main.start();
    }

    /*
    * method: start
    * Program initializes here. The window and keyboard are created. Gl is 
    * initialized. The camera location is initialized and the game loop is 
    * started.
    */
    public void start() {
        try {
            createWindow();
            Keyboard.create();

            initGL();
            fp  = new FPCameraController(0F, 0F, 0F);
            fp.gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        Keyboard.destroy();
        Display.destroy();
    }

    //method: createWindow
    //purpose: Creates the window at the specified dimensions
    private void createWindow() throws Exception {
        displayMode = new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        Display.setDisplayMode(displayMode);
        Display.setFullscreen(false);
        Display.setTitle("Quarter Project");
        Display.create();
    }

    
    //method: initGL
    //purpose: Initializes all the gl stuff
    public void initGL() {
glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(100f, (float) Display.getWidth() / (float) Display.getHeight(), 0.1f, 300f);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		glEnable(GL_DEPTH_TEST);
		
		glEnable(GL_TEXTURE_2D);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		
		initLightArrays();
		glLight(GL_LIGHT0, GL_POSITION, lightPosition);
		glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);
		glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);
		glLight(GL_LIGHT0, GL_AMBIENT, whiteLight);
		
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
    }
    
    
    //method: initLightArrays
    //purpose: Initializes the light and its initial location
    private void initLightArrays() {
	lightPosition = BufferUtils.createFloatBuffer(4);
	lightPosition.put(0f).put(0f).put(0f).put(1f).flip();
		
	whiteLight = BufferUtils.createFloatBuffer(4);
	whiteLight.put(1f).put(1f).put(1f).put(0f).flip();

    }

}
