package dk.itu.mario.level;

import java.util.Random;

import dk.itu.mario.MarioInterface.Constraints;
import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.engine.sprites.SpriteTemplate;
import dk.itu.mario.engine.sprites.Enemy;
import dk.itu.mario.engine.LevelFactory;

public class MyLevel extends Level implements LevelInterface {

	int[] heightMap;
    public MyLevel(int width, int height) {
        super(width, height);  
        heightMap = new int[width];
        creat();
    }

    private void creat() {
    	generateHeightMap();
    	generatePlatform();
        xExit=width-10;
        yExit=heightMap[xExit];
        
    }
    
    private void generateHeightMap(){
    	double rand;
    	int change,lastChange;
    	heightMap[0]=13;
    	lastChange=0;
    	for (int x=1;x<width;x++){
    		if (lastChange>5 & x<width-10){
    			rand=Math.random();
    			if (rand<.1)
    			{change=2; lastChange=0;}
    			else if (rand<.2)
    			{change=1; lastChange=0;}
    			else 
    			{change=0; lastChange=lastChange+1;}
    			if (heightMap[x-1]<7)
    				heightMap[x]=heightMap[x-1]+change;
    			else if (heightMap[x-1]>12)
    				heightMap[x]=heightMap[x-1]-change;
    			else
    			{rand=Math.random();
    			if (rand<0.5)
    				heightMap[x]=heightMap[x-1]-change;
    			else
    				heightMap[x]=heightMap[x-1]+change;}
    		}
    		else{
    			lastChange=lastChange+1;
    			heightMap[x]=heightMap[x-1];
    		}
    	}
    }
    private void generatePlatform(){
    	
    	// set beginning and end blocks
		setBlock(0,heightMap[0],Level.HILL_TOP);
		for (int y=heightMap[0]+1;y<height;y++)
			setBlock(0,y,Level.GROUND);
		setBlock(width,heightMap[width-1],Level.HILL_TOP);
		for (int y=heightMap[width-1]+1;y<height;y++)
			setBlock(width,y,Level.GROUND);
		// set rest of blocks
        for (int x=1;x<width-1;x++){
        	if (heightMap[x-1]>heightMap[x])
        	{
        		setBlock(x,heightMap[x],Level.LEFT_UP_GRASS_EDGE);
        		for (int y=heightMap[x]+1;y<heightMap[x-1];y++)
        			setBlock(x,y,Level.LEFT_GRASS_EDGE);
        		setBlock(x,heightMap[x-1],Level.RIGHT_POCKET_GRASS);
        		for (int y=heightMap[x-1]+1;y<height;y++)
        			setBlock(x,y,Level.GROUND);
        	}
        	else if (heightMap[x]<heightMap[x+1]){
        		setBlock(x,heightMap[x],Level.RIGHT_UP_GRASS_EDGE);
        		for (int y=heightMap[x]+1;y<heightMap[x+1];y++)
        			setBlock(x,y,Level.RIGHT_GRASS_EDGE);
        		setBlock(x,heightMap[x+1],Level.LEFT_POCKET_GRASS);
        		for (int y=heightMap[x+1]+1;y<height;y++)
        			setBlock(x,y,Level.GROUND);
        	}
        	else{
        		setBlock(x,heightMap[x],Level.HILL_TOP);
        		for (int y=heightMap[x]+1;y<height;y++)
        			setBlock(x,y,Level.GROUND);
        	}
        }
    }
}

  