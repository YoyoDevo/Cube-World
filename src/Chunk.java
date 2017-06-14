/*******************************************************************************
 * File Chunk.java
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
 ******************************************************************************/
import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Chunk{
    
    static final int CHUNK_SIZE =30;
    static final int CUBE_LENGTH = 2;
    private Block[][][] Blocks;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int StartX;
    private int StartY;
    private int StartZ;
    private Random r;
    private int VBOTextureHandle;
    private Texture texture;
    
    
    //method: render
    //purpose: this method renders the chunks
    public void render(){
        glPushMatrix();
            glBindBuffer(GL_ARRAY_BUFFER,VBOVertexHandle);
            glVertexPointer(3,GL_FLOAT,0,0L);
            glBindBuffer(GL_ARRAY_BUFFER,VBOColorHandle);
            glColorPointer(3,GL_FLOAT,0,0L);
            glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
            glBindTexture(GL_TEXTURE_2D, 1);
            glTexCoordPointer(2,GL_FLOAT,0,0L);
            glDrawArrays(GL_QUADS, 0,CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);
        glPopMatrix();
    }                                   

    //method: rebuildMesh
    //purpose: this method rebuilds the mesh from the vertex data
    private void rebuildMesh(float startX, float startY, float startZ) {
        int seed = r.nextInt();
        SimplexNoise noise = new SimplexNoise(30, .3, seed);
        VBOVertexHandle  = glGenBuffers();
        VBOColorHandle   = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        FloatBuffer VertexPositionData = 
                BufferUtils.createFloatBuffer((CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE)*72);
        FloatBuffer VertexColorData = 
                BufferUtils.createFloatBuffer((CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE)*72);
        FloatBuffer VertexTextureData = 
                BufferUtils.createFloatBuffer((CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE)*72);     // texture
        
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                float height = (startY + (int)(15 * noise.getNoise(x,(int) startY, z)) * CUBE_LENGTH) + 10;
                        if( height > 30){
                            height = 30;
                        }
                for (int y = 0; y <= height; y++) {
                    VertexPositionData.put(createCube((startX + x*CUBE_LENGTH),
                            ((float)(CHUNK_SIZE*-2) + y*CUBE_LENGTH),
                            (startZ - z*CUBE_LENGTH)));
                    
                    VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[x][y][z])));
                    VertexTextureData.put(createTexCube(Blocks[x][y][z])); 
                }
            }
        }
        
        VertexPositionData.flip();
        VertexColorData.flip();
        VertexTextureData.flip();      //texture
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);                    //texture
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData, GL_STATIC_DRAW);   //texture
        glBindBuffer(GL_ARRAY_BUFFER, 0);                                   //texture
    }

    //method: createCubeVertexCol
    //purpose: this method grabs the cube colors and returns them
    private float[] createCubeVertexCol(float[] CubeColorArray) {
        float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
        
        for (int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = CubeColorArray[i %
            CubeColorArray.length];
        }
        return cubeColors;
    }
    
    //method: create
    //purpose: this method creates the blocks according to block type
    private void create() {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    if (y > 7)
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Grass);
                    else if (y > 5)
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
                    else if (y > 3)
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
                    else if (y > 1)
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                    else if (y > 0)
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);
                    else
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Bedrock);
                }
            }
        }
    }
    
    //method: createTexCube
    //purpose: this method grabs the textures from the textures class
    public static float[] createTexCube( Block block) {
        float offset = (1024f/16)/1024f;
        Textures texture = new Textures(offset);
        switch (block.getId()) {
            case 0 : return texture.grass();
            case 1 : return texture.sand();
            case 2 : return texture.water();
            case 3 : return texture.dirt();
            case 4 : return texture.stone();
            case 5 : return texture.bedrock();
            default: return texture.grass();
       }
    }
    
    //method: createcube
    //purpose: this method creates the cube 
    public static float[] createCube(float x, float y, float z) {
        int offset = CUBE_LENGTH / 2;
        return new float[] {
        // TOP QUAD
        x + offset, y + offset, z,
        x - offset, y + offset, z,
        x - offset, y + offset, z - CUBE_LENGTH,
        x + offset, y + offset, z - CUBE_LENGTH,
        // BOTTOM QUAD
        x + offset, y - offset, z - CUBE_LENGTH,
        x - offset, y - offset, z - CUBE_LENGTH,
        x - offset, y - offset, z,
        x + offset, y - offset, z,
        // FRONT QUAD
        x + offset, y + offset, z - CUBE_LENGTH,
        x - offset, y + offset, z - CUBE_LENGTH,
        x - offset, y - offset, z - CUBE_LENGTH,
        x + offset, y - offset, z - CUBE_LENGTH,
        // BACK QUAD
        x + offset, y - offset, z,
        x - offset, y - offset, z,
        x - offset, y + offset, z,
        x + offset, y + offset, z,
        // LEFT QUAD
        x - offset, y + offset, z - CUBE_LENGTH,
        x - offset, y + offset, z,
        x - offset, y - offset, z,
        x - offset, y - offset, z - CUBE_LENGTH,
        // RIGHT QUAD
        x + offset, y + offset, z,
        x + offset, y + offset, z - CUBE_LENGTH,
        x + offset, y - offset, z - CUBE_LENGTH,
        x + offset, y - offset, z };
    }
    
    //method: getCubeColor
    //purpose: this method grabs the cube color
    private float[] getCubeColor(Block block) {
        return new float[] { 1, 1, 1 };
}
    
    //method: Chunk
    //purpose: class constructor
    public Chunk(int startX, int startY, int startZ) {
        try{
            texture = TextureLoader.getTexture("PNG",
            ResourceLoader.getResourceAsStream("terrain.png"));
        }
        catch(Exception e)
        {
            System.out.print("ER-ROAR!");
        }
        r= new Random();
        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        
        create();
        VBOTextureHandle = glGenBuffers(); 
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        StartX = startX;
        StartY = startY;
        StartZ = startZ;
        rebuildMesh(startX, startY, startZ);
    }
}
