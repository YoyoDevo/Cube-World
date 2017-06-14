
/** *****************************************************************************
 * File Block.java
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
public class Block {

    private boolean IsActive;
    private BlockType Type;
    private float x, y, z;
    
    public Block(BlockType type) {
        Type = type;
    }

    public enum BlockType {
        BlockType_Grass(0),
        BlockType_Sand(1),
        BlockType_Water(2),
        BlockType_Dirt(3),
        BlockType_Stone(4),
        BlockType_Bedrock(5),
        BlockType_Default(6);

        private int BlockID;

        BlockType(int i) {
            BlockID = i;
        }

        public int GetID() {
            return BlockID;
        }

        public void SetID(int i) {
            BlockID = i;
        }
    }
    
    //method: setCoords
    //purpose: Sets the coordinates
    public void setCoords(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //method: isActive
    //purpose: Checks whether a block is active
    public boolean isActive() {
        return IsActive;
    }

    //method: setActive
    //purpose: Sets the activity for the blocks
    public void setActive(boolean active) {
        IsActive = active;
    }

    //method: setCoords
    //purpose: Gets the id
    public int getId() {
        return Type.GetID();
    }
}
