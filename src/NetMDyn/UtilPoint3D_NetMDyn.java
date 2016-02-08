package NetMDyn;

import java.util.ArrayList;

import netbiodyn.util.UtilPoint3D;

public class UtilPoint3D_NetMDyn extends UtilPoint3D{
	
	 public UtilPoint3D_NetMDyn() {
	        x = 0;
	        y = 0;
	        z = 0;
	    }

	    public UtilPoint3D_NetMDyn(int i, int j, int k) {
	        x = i;
	        y = j;
	        z = k;
	    }


	  
	    public static ArrayList<UtilPoint3D> BresenhamRond3D(int x1, int y1, int z1, int r, int zMax) {
	        ArrayList<UtilPoint3D> lst_pts = new ArrayList<>();
	        int xmin = x1 -r -1;
	        int xmax = x1 + r + 1;
	        int ymin = y1 -r -1;
	        int ymax = y1 + r +1;
	        
	        int i,j;
	        for(i = xmin; i<xmax; i++){
	        	for(j = ymin; j<ymax; j++){
	        		int dx = x1-i;
	        		int dy = y1-j;
	        		int rTmp = (int) Math.sqrt(Math.pow(dx, 2)+Math.pow(dy,2));
	        		if(rTmp == r){
	        			for(int z = 0; z < zMax; z++){
	        				lst_pts.add(new UtilPoint3D(i, j, z));
	        			}
	           		}
	        	}
	        }
	        return lst_pts;   
	    }

	    public static UtilPoint3D_NetMDyn centreDeGravite(ArrayList<InstanceReaxel_NetMDyn> lst, boolean action) {
	        UtilPoint3D_NetMDyn pt = new UtilPoint3D_NetMDyn(0, 0, 0);
	        int nb = lst.size();
	        for (int i = 0; i < nb; i++) {
	            pt.x += lst.get(i).getX();
	            pt.y += lst.get(i).getY();
	            pt.z += lst.get(i).getZ();
	        }
	        pt.x = pt.x / nb;
	        pt.y = pt.y / nb;
	        pt.z = pt.z / nb;
	        return pt;
	    }

	    @Override
	    public String toString() {
	        return "X=" + this.x + " Y=" + this.y + " Z=" + this.z;
	    }
	}
