package dk.itu.mario.level;

import java.util.Random;

import dk.itu.mario.MarioInterface.Constraints;
import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.engine.sprites.SpriteTemplate;
import dk.itu.mario.engine.sprites.Enemy;
import dk.itu.mario.engine.LevelFactory;

public class MyLevel extends Level implements LevelInterface {

	int[] heightMap,difficulty;
	double[] smoothDifficulty,prob;
	boolean[] edge,pit,block,hill;
    public MyLevel(int width, int height) {
        super(width, height);  
        heightMap = new int[width];
        difficulty= new int[width];
        smoothDifficulty = new double[width];
        prob = new double[width];
        edge =  new boolean[width];
        pit = new boolean[width];
        block = new boolean[width];
        hill = new boolean[width];
        for (int i=0;i<width;i++){
        	edge[i]=pit[i]=block[i]=false;
        	difficulty[i]=0;
        }
        creat();
    }

    private void creat() {
    	generateHeightMap();
    	generatePits();
    	generatePlatform();
    	putTubes();
    	computeSmoothDifficulty();
    	putEnemies();
    	putBlocks();
    	putCoins();
    	generateHills();
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
    			{change=2; lastChange=0; edge[x]=true;}
    			else if (rand<.2)
    			{change=1; lastChange=0; edge[x]=true;}
    			else 
    			{change=0; lastChange=lastChange+1;}
    			if (heightMap[x-1]<7)
    				heightMap[x]=heightMap[x-1]+change;
    			else if (heightMap[x-1]>12)
    				heightMap[x]=heightMap[x-1]-change;
    			else
    			{rand=Math.random();
    			if (rand<0.2)
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
    
    
    private void generatePits(){
    	double rand;
    	for (int x=10;x<width-20;x++)
    		if (checkPitLocation(x))
    		{
    			rand=Math.random();
    			if (rand<0.2)
    				{
    				  pit[x]=pit[x+1]=pit[x+2]=true;    				  
    				  heightMap[x]=heightMap[x+1]=heightMap[x+2]=height;
    				  difficulty[x]=difficulty[x]+2;
    				  putRocks(x,x+2);
    				}
    			else if (rand<.4)
				{
  				  pit[x]=pit[x+1]=true;
  				  heightMap[x]=heightMap[x+1]=height;
  				  difficulty[x]=difficulty[x]+1;
				  putRocks(x,x+1);
  				}	
    		}
    }
    
    private boolean checkPitLocation(int x){
    	boolean check=true;
    	for (int i=x-4;i<x+6;i++ )
    		check=check&!edge[i]&!pit[i];
    	return check;
    }
    
    private boolean checkTubeLocation(int x){
    	boolean check=true;
    	for (int i=x-3;i<x+4;i++ )
    		check=check&!edge[i]&!pit[i];
    	return check;
    }
    
    private void putRocks(int xstart,int xend){
    	double rand=Math.random();
    	if (rand<0.1)
    	{
    		setBlock(xstart-2,heightMap[xstart-2]-1,Level.ROCK);
    		setBlock(xstart-1,heightMap[xstart-1]-1,Level.ROCK);
    		setBlock(xstart-1,heightMap[xstart-1]-2,Level.ROCK);
    		setBlock(xend+2,heightMap[xend+2]-1,Level.ROCK);
    		setBlock(xend+1,heightMap[xend+1]-1,Level.ROCK);
    		setBlock(xend+1,heightMap[xend+1]-2,Level.ROCK);
    		difficulty[xstart]=difficulty[xstart]+2;
    		edge[xstart-2]=edge[xstart-1]=edge[xend+1]=edge[xend+2];
    	}
    	else if (rand<0.3)
    	{
    		setBlock(xstart-1,heightMap[xstart-1]-1,Level.ROCK);
    		setBlock(xend+1,heightMap[xend+1]-1,Level.ROCK);
    		difficulty[xstart]=difficulty[xend]+1;
    		edge[xstart-1]=edge[xend+1];

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
    
    private void putTubes(){
    	double rand;
    	for (int x=10;x<width-30;x++){
    		if (checkTubeLocation(x))
    		{
    			rand=Math.random();
    			if (rand<0.3 & heightMap[x]>8)
    			{
    				setBlock(x,heightMap[x]-1,Level.TUBE_SIDE_LEFT);
    				setBlock(x,heightMap[x]-2,Level.TUBE_TOP_LEFT);
    				setBlock(x+1,heightMap[x+1]-1,Level.TUBE_SIDE_RIGHT);
    				setBlock(x+1,heightMap[x+1]-2,Level.TUBE_TOP_RIGHT);
    				heightMap[x]=heightMap[x]-2;
    				heightMap[x+1]=heightMap[x+1]-2;
    				edge[x]=edge[x+1]=true;
        			rand=Math.random()*(double)x/width;
        			if (rand>0.3)
        				setSpriteTemplate(x,heightMap[x]+1,new SpriteTemplate(Enemy.ENEMY_FLOWER,true));
    				difficulty[x]=difficulty[x]+2;
    			}

    			
    		}
    	}
    }
    
    
    private void computeSmoothDifficulty(){
    	for (int x=1;x<width;x++){
    		smoothDifficulty[0]=0;
    		smoothDifficulty[x]=difficulty[x]+0.8*smoothDifficulty[x-1];
    	}
    }
    
    private void putEnemies(){
    	double rand,rand2;
//    	setBlock(10,heightMap[10]-4,Level.BLOCK_POWERUP);
//    	setBlock(11,heightMap[11]-4,Level.BLOCK_POWERUP);
//    	setBlock((int)Math.floor(width/4*3),heightMap[(int)Math.floor(width/4*3)]-4,Level.BLOCK_POWERUP);

    	for (int x=10;x<width-30;x++){
    		if (!pit[x]&!edge[x])
    		{
    			prob[x]=computeCurve((double)x/width)/(1+smoothDifficulty[x]);
    			rand=Math.random()*prob[x];
    			if (rand>0.6)
    			{
    				setSpriteTemplate(x,heightMap[x]-1,new SpriteTemplate(Enemy.ENEMY_SPIKY,true));
    				difficulty[x]=difficulty[x]+4;
    			}
    			else if (rand>0.5)
    			{
    				rand2=Math.random();
    				if (rand2<0.3)
    					setSpriteTemplate(x,heightMap[x]-1,new SpriteTemplate(Enemy.ENEMY_GOOMBA,true));
    				else if (rand2<0.6)
    					setSpriteTemplate(x,heightMap[x]-1,new SpriteTemplate(Enemy.ENEMY_GREEN_KOOPA,true));
    				else
    					setSpriteTemplate(x,heightMap[x]-1,new SpriteTemplate(Enemy.ENEMY_RED_KOOPA,true));
    				difficulty[x]=difficulty[x]+3;
    			}
    			else if (rand>0.4)
    			{
    				setSpriteTemplate(x,heightMap[x]-1,new SpriteTemplate(Enemy.ENEMY_SPIKY,false));
    				difficulty[x]=difficulty[x]+2;
    			}
    			else if (rand>0.3)
    			{
    				rand2=Math.random();
    				if (rand2<0.3)
    					setSpriteTemplate(x,heightMap[x]-1,new SpriteTemplate(Enemy.ENEMY_GOOMBA,false));
    				else if (rand2<0.6)
    					setSpriteTemplate(x,heightMap[x]-1,new SpriteTemplate(Enemy.ENEMY_GREEN_KOOPA,false));
    				else
    					setSpriteTemplate(x,heightMap[x]-1,new SpriteTemplate(Enemy.ENEMY_RED_KOOPA,false));
    				difficulty[x]=difficulty[x]+1;
    			}	
    		}
        	computeSmoothDifficulty();
    	}
    }
    private double computeCurve(double x){
    	double y;
    	if (x<0.3)
    		y=-20*Math.pow(x-0.2,2)+0.5;
    	else if (x<0.6)
    		y=-50*Math.pow(x-0.5,2)+0.7;
    	else
    		y=-80*Math.pow(x-0.8,2)+1;
    	if (y<0) y=0;
    	return y;
    }
    
    
    private double blockCurve(double x){
    	double y;
    	if (x<0.5)
    		y=-20*Math.pow(x-0.25,2)+1;
    	else
    		y=-80*Math.pow(x-0.75,2)+1;
    	if (y<0) y=0;
    	return y;
    }
    
    private void putBlocks()
    {
    	double rand;
    	int length;
    	for (int x=10;x<width-20;x++){
    		if (!pit[x]&!edge[x]&!block[x]&!edge[x-1]&!pit[x-1]){
    			rand = Math.random()*blockCurve((double)x/width);
    			if (rand>.7){
    				length=generateBlockLength(x);    				
    				for (int i=x;i<x+length;i++){
    					rand=Math.random();
    					block[i]=true;
    					if (rand>0.5)
    						setBlock(i,heightMap[i]-4,Level.BLOCK_EMPTY);
    					else if (rand>0.2)
    						setBlock(i,heightMap[i]-4,Level.BLOCK_COIN);
    					else
    						setBlock(i,heightMap[i]-4,Level.BLOCK_POWERUP);
    				}
    			}
    		}
    	}
    }
    
    private void putCoins()
    {
    	double rand;
    	int length;
    	for (int x=10;x<width-20;x++){
    		if (!pit[x]&!edge[x]&!block[x]){
    			rand = Math.random();
    			if (rand>.8){
    				length=generateBlockLength(x);    				
    				for (int i=x;i<x+length;i++){
    					setBlock(i,heightMap[i]-2,Level.COIN);
    				}
    			}
    		}
    	}
    }
    
    
    int generateBlockLength(int x){
    	int length=0;
    	int i=x;
    	while (i<width-10 & !edge[i] & !pit[i]){
    		i=i+1;
    	    length=length+1;
    	}
    	double rand=Math.random();
    	return (int)Math.floor((length-1)*rand);
    }

    private void generateHills(){
    	double rand;
    	for (int x=20;x<width-20;x++){
    		if (heightMap[x]>8&!pit[x]&!hill[x]&!hill[x-1]&!edge[x]){
    			rand=Math.random();
    			if (rand<0.1){
    				int length=checkLength(x);
    				if (length>3){
    					int height=(int)(heightMap[x]-Math.random()*2-2);
    					setBlock(x,height,Level.HILL_TOP_LEFT);
    					for (int y=height+1;y<heightMap[x];y++)
    						setBlock(x,y,Level.HILL_LEFT);
    					for (int i=x+1;i<x+length-1;i++){
    						rand=Math.random();
    						if (rand<0.5) setBlock(i,height-2,Level.COIN);
    						setBlock(i,height,Level.HILL_TOP);
    						for (int y=height+1;y<heightMap[i];y++)
    							setBlock(i,y,Level.HILL_FILL);
    						setBlock(x+length-1,height,Level.HILL_TOP_RIGHT);
    						for (int y=height+1;y<heightMap[x+length-1];y++)
    							setBlock(x+length-1,y,Level.HILL_RIGHT);
    					}
    					for (int i=x;i<x+length;i++) hill[i]=true;
    				}
    			}
    		}
    	}
    }

    private int checkLength(int x){
    	int i=0;
    	int length=5+(int)Math.random()*4;
    	while (i<length&i<width-20){
    		i=i+1;
    		if (pit[x+i]|hill[x+i]|edge[x+i]|block[x+i])
    			return 0;
    	}
    	return length;
    }
    
}



  