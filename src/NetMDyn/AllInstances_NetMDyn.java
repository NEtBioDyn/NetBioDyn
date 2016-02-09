package NetMDyn;

import java.util.ArrayList; //Possible creation of tables
import java.util.HashMap; //Possible creation of hashmaps

import netbiodyn.util.Lang;

public class AllInstances_NetMDyn extends ArrayList<InstanceReaxel_NetMDyn> implements Cloneable{


	    private final InstanceReaxel_NetMDyn[][][] matrix; //Initialization of a matrix of coordinates
	    private HashMap<String, Integer> entitiesBook; //Initialization of the table of all entities ?
	    //Initialization of coordinates
	    private int X;
	    private int Y;
	    private int Z;

	    //Creation of an AllInstances object, with coordinates as parameters
	    public AllInstances_NetMDyn(int x, int y, int z) {
	        super(); //Creation of an InstanceReaxel which will have more parameters after
	        X = x;
	        Y = y;
	        Z = z;
	        matrix = new InstanceReaxel_NetMDyn[x][y][z]; //Creation of an InstanceReaxel with these coordinates
	        entitiesBook = new HashMap<>(); //Creation of an empty Hashmap
	    }

	    //Creation of an AllInstances object, with all possible parameters 
	    public AllInstances_NetMDyn(ArrayList<InstanceReaxel_NetMDyn> l, InstanceReaxel_NetMDyn[][][] m, int x, int y, int z) {
	        super(l); //Creation from an ArrayList<InstanceReaxel>
	        //Put all parameters to values given into parameters
	        this.matrix = m;
	        X = x;
	        Y = y;
	        Z = z;
	        this.updateBook();
	    }

	    //Creation of an AllInstances object, with an other one in parameter
	    public AllInstances_NetMDyn(AllInstances_NetMDyn other) {
	        super(other.getList()); //Creation of the list of all InstanceReaxel
	        //Put all parameters to good values
	        X = other.getX(); 
	        Y = other.getY();
	        Z = other.getZ();
	        matrix = other.getMatrix();
	        updateBook();
	    }

	    //Update of the hashmap
	    private void updateBook() {
	        entitiesBook = new HashMap<>(); //Creation of a new hashmap
	        for (InstanceReaxel_NetMDyn c : this) { //Path of all InstanceReaxel into the AllInstance object 
	        	//Verification if the hashmap contains something
	            if (entitiesBook.containsKey(c.getNom()) == false) {
	                entitiesBook.put(c.getNom(), 1); //If the hashmap doesn't contain this InstanceReaxel, addition of this InstanceReaxel with the number 1
	            } else {
	                int nbr = entitiesBook.get(c.getNom()); //If it contains this InstanceReaxel, get the number of this InstanceReaxel into the old hashmap 
	                nbr += 1; //Incrementation of his number to create a shift
	                entitiesBook.put(c.getNom(), nbr); //Addition of this InstanceReaxel into the hashmap at the incremented position 
	            }
	        }
	    }

	    //Add an InstanceReaxel into the hashmap
	    private void addInBook(String type) {
	        if (!entitiesBook.containsKey(type)) {
	            entitiesBook.put(type, 1); //if this InstanceReaxel isn't contained into the hashmap, add it with the number 1
	        } else {
	            int nbr = entitiesBook.get(type); //If it contains this InstanceReaxel, get the number of this InstanceReaxel into the old hashmap 
	            nbr += 1; //Incrementation of his number to create a shift
	            entitiesBook.put(type, nbr);//Addition of this InstanceReaxel into the hashmap at the incremented position 
	        }
	    }
	    
	    //Remove an InstanceReaxel into the hashmap ?
	    private void removeInBook(InstanceReaxel_NetMDyn reaxel) {
	        String type = reaxel.getNom(); //Get the name of the InstanceReaxel
	        if (entitiesBook.containsKey(type)) { //Know if this is already into the hashmap
	            int nbr = entitiesBook.get(type); //If it's the case, get the number of it 
	            if (nbr > 0) {
	                nbr -= 1; //Diminution of this number
	            }
	            entitiesBook.put(type, nbr); //Put into the hashmap the InstanceReaxel at the new position ?
	        }
	    }

	    //Get the hashmap
	    public HashMap<String, Integer> getBook() {
	        return (HashMap<String, Integer>) entitiesBook.clone(); //return a clone of the hashmap
	    }

	    //return the "position" of the InstanceReaxel contained into the hashmap
	    public int getInstancesNbr(String name) {
	        if (entitiesBook.get(name) == null) {
	            return 0; //if the name of this InstanceReaxel is not into the hashmap, it returns 0
	        } else {
	            return entitiesBook.get(name); // if the name of this InstanceReaxel is into the hashmap, it return the position
	        }
	    }
	    
	    //Get all the InstanceReaxel which have a same name as in parameter
	    public ArrayList<InstanceReaxel_NetMDyn> getByName(String name){
	         ArrayList<InstanceReaxel_NetMDyn> entities = new ArrayList<>(); //Creation of a table which will contain InstanceReaxel with the same name as in parameter
	        for (InstanceReaxel_NetMDyn r : this) { //Path of all InstanceReaxel into the AllInstance object 
	            if (r.getNom().equals(name)) { //Comparison between the name of this InstanceReaxel and the name given in parameter
	                entities.add(r); //If there is a equality, addition of this InstanceReaxel into the new hashmap
	            }
	        }
	            return entities; //Return all the InstanceReaxel which have a same name as in parameter
	    }

	    //Get the size of the AllInstance object ("this" as parameter)
	    public int getSize() {
	        return this.size();
	    }

	    //Get the InstanceReaxel in a certain position given in parameter
	    public InstanceReaxel_NetMDyn getInList(int pos) {
	        if (pos < getSize()) { 
	            return this.get(pos); //If the position exists, it returns the InstanceReaxel at this position
	        } else {
	            return null; // If the position doesn't exist, it returns null
	        }
	    }

	    //Return the coordinates into the matrix ?
	    public InstanceReaxel_NetMDyn getFast(int x, int y, int z) {
	        return matrix[x][y][z];
	    }

	    //Add an InstanceReaxel into the hashmap
	    public boolean addReaxel(InstanceReaxel_NetMDyn reaxel) {
	    	//get the coordinates of the InstanceReaxel
	        int x = reaxel.getX();
	        int y = reaxel.getY();
	        int z = reaxel.getZ();
	        if (matrix[x][y][z] == null) {
	            this.add(reaxel); //if the matrix is empty, it adds the InstanceReaxel into the AllInstances
	            matrix[x][y][z] = reaxel; //the InstanceReaxel is added into the matrix by its coordinates
	            addInBook(reaxel.getNom()); //Add the InstanceReaxel into the hashmap 
	            return true; //Return true if the InstanceReaxel has been added
	        }
	        return false; //Return false if the InstanceReaxel can't be added because of the presence of these coordinates into the matrix
	    }

	    //Creation of a new InstanceReaxel instead of an old one
	    public void editReaxels(Entity_NetMDyn entity, String old_name) {
	        ArrayList<InstanceReaxel_NetMDyn> copy = getList(); //Clone of the AllInstances object
	        for (InstanceReaxel_NetMDyn reaxel : copy) { //Path of all InstanceReaxel into the copy of AllInstances object 
	            if (reaxel.getNom().equals(old_name)) { //Comparison between the name of the InstanceReaxel and the name in parameter
	                removeReaxel(reaxel); //Suppression of this InstanceReaxel
	                InstanceReaxel_NetMDyn newReaxel = InstanceReaxel_NetMDyn.CreerReaxel(entity);//Creation of a new InstanceReaxel based on the Entity given
	                //Put the coordinates into the new InstanceReaxel
	                newReaxel.setX(reaxel.getX());
	                newReaxel.setY(reaxel.getY());
	                newReaxel.setZ(reaxel.getZ());
	                addReaxel(newReaxel); //Add the new InstanceReaxel
	            }
	        }
	    }

	    //Select the InstanceReaxel with coordinates in parameters
	    public void select(int x, int y, int z) {
	        matrix[x][y][z].setSelectionne(true);
	    }

	    //Unselect the InstanceReaxel with coordinates in parameters
	    public void unselect(int x, int y, int z) {
	        matrix[x][y][z].setSelectionne(false);
	    }

	    //Remove an InstanceReaxel into the hashmap by its name
	    public boolean removeReaxel(InstanceReaxel_NetMDyn reaxel) {
	    	//Get the coordinates of this InstanceReaxel
	        int x = reaxel.getX();
	        int y = reaxel.getY();
	        int z = reaxel.getZ();
	        if (matrix[x][y][z] != null) {
	            this.remove(reaxel); //Remove the InstanceReaxel if the matrix is not empty
	            matrix[x][y][z] = null; //Remove the correspondence between coordinates and the InstanceReaxel
	            removeInBook(reaxel);
	            return true; //Return that the InstanceReaxel has been removed
	        }
	        return false; //Return that the InstanceReaxel hasn't been removed
	    }

	    //Remove an InstanceReaxel into the hashmap by its coordinates
	    public boolean removeReaxel(int x, int y, int z) {
	        InstanceReaxel_NetMDyn reaxel = matrix[x][y][z]; //Get the InstanceReaxel by its coordinates into the matrix
	        if (reaxel != null) {
	            this.remove(reaxel); //Remove the InstanceReaxel if the matrix is not empty
	            matrix[x][y][z] = null; //Remove the correspondence between coordinates and the InstanceReaxel
	            removeInBook(reaxel);
	            return true; //Return that the InstanceReaxel has been removed
	        }
	        return false; //Return that the InstanceReaxel hasn't been removed
	    }
	    
	    //Remove all InstanceReaxel using this name/tags
	    public void removeEntityType(String etiquettes) {
	        removeByName(etiquettes); // Remove all InstanceReaxel using this tag into the AllInstances object
	        if(entitiesBook.containsKey(etiquettes)){ 
	            entitiesBook.remove(etiquettes); // Remove all Entities having this tag
	        }
	    }

	    //Remove all InstanceReaxel using this name into the AllInstances object
	    public void removeByName(String nom) {
	        ArrayList<InstanceReaxel_NetMDyn> copy = getList(); //Clone of the AllInstances object
	        for (InstanceReaxel_NetMDyn reaxel : copy) { //Path of all InstanceReaxel into the copy of AllInstances object 
	            if (reaxel.getNom().equals(nom)) { 
	                removeReaxel(reaxel); //If the required name is the same as the InstanceReaxel, remove this InstanceReaxel
	            }
	        }
	    }

	    //Remove the InstanceReaxel by its coordinates, but only if it's possible
	    public void removeOnlyCleanable(int x, int y, int z) {
	        InstanceReaxel_NetMDyn c = matrix[x][y][z]; //Get the InstanceReaxel by its coordinates into the matrix
	        if (c != null) { 
	            if ((this.contains(c)) && (c.isVidable())) {
	                removeReaxel(c); //if c has entries and can be vidable, and if the AllInstances object contains it, remove this InstanceReaxel
	            }
	        }
	    }

	    
	    public void setMatrixAndList(ArrayList<InstanceReaxel_NetMDyn> l) {
	        for (InstanceReaxel_NetMDyn rea : l) {
	            addReaxel(rea.clone()); //Add every InstanceReaxel putted into the AllInstance object put in parameter, into the hashmap
	        }
	        this.updateBook(); //Update the hashmap
	    }

	    //Get the matrix
	    public InstanceReaxel_NetMDyn[][][] getMatrix() {
	        InstanceReaxel_NetMDyn[][][] copy = new InstanceReaxel_NetMDyn[getX()][getY()][getZ()]; //Clone of the matrix
	        for (InstanceReaxel_NetMDyn r : this) { //Path of all InstanceReaxel into the AllInstances object 
	            copy[r.getX()][r.getY()][r.getZ()] = r.clone(); //Link the coordinates of the InstanceReaxel into the cloned matrix and a clone of the InstanceReaxel
	        }
	        return copy; //return the copy of the matrix
	    }

	    //Get the ArrayList
	    public ArrayList<InstanceReaxel_NetMDyn> getList() {
	        ArrayList<InstanceReaxel_NetMDyn> copy = new ArrayList<>(); //Clone of the ArrayList
	        for (InstanceReaxel_NetMDyn r : this) { //Path of all InstanceReaxel into the AllInstances object 
	            copy.add(r.clone()); //Add a copy of the InstanceReaxel into the new ArrayList
	        }
	        return copy; //return the copy of the ArrayList
	    }

	    //Get all the values into the AllInstances object (matrix, ArrayList and coordinates)
	    public AllInstances_NetMDyn clone() {
	        return new AllInstances_NetMDyn(getList(), getMatrix(), getX(), getY(), getZ());
	    }

	    
	    public void test(String name) {
	        for (InstanceReaxel_NetMDyn r : this) { //Path of all InstanceReaxel into the AllInstances object 
	            if (matrix[r.getX()][r.getY()][r.getZ()] == null) { 
	                if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
		                System.err.println(name + " ECHEC DU TEST !!!! " + r.getX() + "*" + r.getY() + "*" + r.getZ()); //if the InstanceReaxel hasn't its coordinates into the matrix, return an error
	                }
	                else{
	                	System.err.println(name + " TEST FAILED !!!! " + r.getX() + "*" + r.getY() + "*" + r.getZ()); //if the InstanceReaxel hasn't its coordinates into the matrix, return an error
	                }
	            }
	        }
	    }
	    
	    //Return the coordinate X of the AllInstances object
	    public int getX() {
	        return X;
	    }

	    //Put a new value to the coordinate X of the AllInstances object
	    public void setX(int X) {
	        this.X = X;
	    }

	    //Return the coordinate Y of the AllInstances object
	    public int getY() {
	        return Y;
	    }
	    
	    //Put a new value to the coordinate Y of the AllInstances object
	    public void setY(int Y) {
	        this.Y = Y;
	    }
	    //Return the coordinate Z of the AllInstances object
	    public int getZ() {
	        return Z;
	    }

	    //Put a new value to the coordinate Z of the AllInstances object
	    public void setZ(int Z) {
	        this.Z = Z;
	    }
	}
