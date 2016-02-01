/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netbiodyn;// Creation of a package, this line indicating that the actual file will be in this package

import java.util.ArrayList; //Possible creation of tables
import java.util.HashMap; //Possible creation of hashmaps

/**
 *
 * @author riviere
 */

public class AllInstances extends ArrayList<InstanceReaxel> implements Cloneable{

    private final InstanceReaxel[][][] matrix; //Initialization of a matrix of coordinates
    private HashMap<String, Integer> entitiesBook; //Initialization of the table of all entities ?
    //Initialization of coordinates
    private int X;
    private int Y;
    private int Z;

    //Creation of an AllInstances object, with coordinates as parameters
    public AllInstances(int x, int y, int z) {
        super(); //Creation of an InstanceReaxel which will have more parameters after
        X = x;
        Y = y;
        Z = z;
        matrix = new InstanceReaxel[x][y][z]; //Creation of an InstanceReaxel with these coordinates
        entitiesBook = new HashMap<>(); //Creation of an empty Hashmap
    }

    //Creation of an AllInstances object, with all possible parameters 
    public AllInstances(ArrayList<InstanceReaxel> l, InstanceReaxel[][][] m, int x, int y, int z) {
        super(l); //Creation from an ArrayList<InstanceReaxel>
        //Put all parameters to values given into parameters
        this.matrix = m;
        X = x;
        Y = y;
        Z = z;
        this.updateBook();
    }

    //Creation of an AllInstances object, with an other one in parameter
    public AllInstances(AllInstances other) {
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
        for (InstanceReaxel c : this) { //Path of all InstanceReaxel into the AllInstance object 
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
    private void removeInBook(InstanceReaxel reaxel) {
        String type = reaxel.getNom(); //Get the name of the InstanceReaxel
        if (entitiesBook.containsKey(type)) { //Know if this is already into the hashmap
            int nbr = entitiesBook.get(type); //If it's the case, get the number of it 
            if (nbr > 0) {
                nbr -= 1; //Diminution of this number
            }
            entitiesBook.put(type, nbr);
        }
    }

    public HashMap<String, Integer> getBook() {
        return (HashMap<String, Integer>) entitiesBook.clone();
    }

    public int getInstancesNbr(String name) {
        if (entitiesBook.get(name) == null) {
            return 0;
        } else {
            return entitiesBook.get(name);
        }
    }
    
    public ArrayList<InstanceReaxel> getByName(String name){
         ArrayList<InstanceReaxel> entities = new ArrayList<>();
        for (InstanceReaxel r : this) {
            if (r.getNom().equals(name)) {
                entities.add(r);
            }
        }
            return entities;
    }

    public int getSize() {
        return this.size();
    }

    public InstanceReaxel getInList(int pos) {
        if (pos < getSize()) {
            return this.get(pos);
        } else {
            return null;
        }
    }

    public InstanceReaxel getFast(int x, int y, int z) {
        return matrix[x][y][z];
    }

    public boolean addReaxel(InstanceReaxel reaxel) {
        int x = reaxel.getX();
        int y = reaxel.getY();
        int z = reaxel.getZ();
        if (matrix[x][y][z] == null) {
            this.add(reaxel);
            matrix[x][y][z] = reaxel;
            addInBook(reaxel.getNom());
            return true;
        }
        return false;
    }

    public void editReaxels(Entity entity, String old_name) {
        ArrayList<InstanceReaxel> copy = getList();
        for (InstanceReaxel reaxel : copy) {
            if (reaxel.getNom().equals(old_name)) {
                removeReaxel(reaxel);
                InstanceReaxel newReaxel = InstanceReaxel.CreerReaxel(entity);
                newReaxel.setX(reaxel.getX());
                newReaxel.setY(reaxel.getY());
                newReaxel.setZ(reaxel.getZ());
                addReaxel(newReaxel);
            }
        }
    }

    public void select(int x, int y, int z) {
        matrix[x][y][z].setSelectionne(true);
    }

    public void unselect(int x, int y, int z) {
        matrix[x][y][z].setSelectionne(false);
    }

    public boolean removeReaxel(InstanceReaxel reaxel) {
        int x = reaxel.getX();
        int y = reaxel.getY();
        int z = reaxel.getZ();
        if (matrix[x][y][z] != null) {
            this.remove(reaxel);
            matrix[x][y][z] = null;
            removeInBook(reaxel);
            return true;
        }
        return false;
    }

    public boolean removeReaxel(int x, int y, int z) {
        InstanceReaxel reaxel = matrix[x][y][z];
        if (reaxel != null) {
            this.remove(reaxel);
            matrix[x][y][z] = null;
            removeInBook(reaxel);
            return true;
        }
        return false;
    }
    
    public void removeEntityType(String etiquettes) {
        removeByName(etiquettes);
        if(entitiesBook.containsKey(etiquettes)){
            entitiesBook.remove(etiquettes);
        }
    }

    public void removeByName(String nom) {
        ArrayList<InstanceReaxel> copy = getList();
        for (InstanceReaxel reaxel : copy) {
            if (reaxel.getNom().equals(nom)) {
                removeReaxel(reaxel);
            }
        }
    }

    public void removeOnlyCleanable(int x, int y, int z) {
        InstanceReaxel c = matrix[x][y][z];
        if (c != null) {
            if ((this.contains(c)) && (c.isVidable())) {
                removeReaxel(c);
            }
        }
    }

    public void setMatrixAndList(ArrayList<InstanceReaxel> l) {
        for (InstanceReaxel rea : l) {
            addReaxel(rea.clone());
        }
        this.updateBook();
    }

    public InstanceReaxel[][][] getMatrix() {
        InstanceReaxel[][][] copy = new InstanceReaxel[getX()][getY()][getZ()];
        for (InstanceReaxel r : this) {
            copy[r.getX()][r.getY()][r.getZ()] = r.clone();
        }
        return copy;
    }

    public ArrayList<InstanceReaxel> getList() {
        ArrayList<InstanceReaxel> copy = new ArrayList<>();
        for (InstanceReaxel r : this) {
            copy.add(r.clone());
        }
        return copy;
    }

    @Override
    public AllInstances clone() {
        return new AllInstances(getList(), getMatrix(), getX(), getY(), getZ());
    }

    public void test(String name) {
        for (InstanceReaxel r : this) {
            if (matrix[r.getX()][r.getY()][r.getZ()] == null) {
                System.err.println(name + " TEST FAILED !!!! " + r.getX() + "*" + r.getY() + "*" + r.getZ());
            }
        }
    }

    public int getX() {
        return X;
    }

    public void setX(int X) {
        this.X = X;
    }

    public int getY() {
        return Y;
    }

    public void setY(int Y) {
        this.Y = Y;
    }

    public int getZ() {
        return Z;
    }

    public void setZ(int Z) {
        this.Z = Z;
    }


}
