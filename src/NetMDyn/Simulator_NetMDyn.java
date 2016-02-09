package NetMDyn;

import java.awt.event.ActionEvent; //A semantic event which indicates that a component-defined action occurred.
import java.awt.event.ActionListener; //The listener interface for receiving action events. 
import java.util.ArrayList; //Possible creation of tables
import java.util.HashMap; //Possible creation of hashmaps
import java.util.List; //Possible creation of lists
import javax.swing.event.EventListenerList; //Possible creation of EventListeners lists

import NetMDyn.ihm.IhmListener_NetMDyn;
import netbiodyn.ihm.IhmListener;
import netbiodyn.AllInstances;
import netbiodyn.InstanceReaction;
import netbiodyn.InstanceReaxel;
import netbiodyn.ihm.Env_Parameters;
import netbiodyn.util.RandomGen;
import netbiodyn.util.UtilPoint3D;

public class Simulator_NetMDyn {

	    private final EventListenerList listeners;

	    private int speed = 1;
	    private int time = 0;
	    private boolean pause = false;
	    private boolean stopped = true;
	    private int maxStep = -1;
	    private final Model_NetMDyn model;
	    private javax.swing.Timer timer_play;

	    private AllInstances_NetMDyn instances;
	    private AllInstances_NetMDyn instancesFutur;

	    private int nb_processus_a_traiter = 0;

	    //Initialization of the Simulator from the Model
	    public Simulator_NetMDyn(Model_NetMDyn model) {
	        listeners = new EventListenerList();

	        this.model = model;
	        init();
	        initTimer();
	    }
	    
	    //Creation of the instances from the Model
	    private void init() {
	        instances = new AllInstances_NetMDyn(model.getInstances());
	    }

	    //Creation of the Listeners from the Model
	    public void addListener(IhmListener_NetMDyn listen) {
	        listeners.add(IhmListener_NetMDyn.class, listen);
	    }
	    
	    //Initialization of the Simulator to time 0
	    private void initTimer() {
	        timer_play = new javax.swing.Timer(this.getSpeed(), new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                play();
	            }
	        });
	    }

	    //Begin the simulation
	    public void start() {
	        if (getTime() == 0) {
	            init();
	        }
	        setStopped(false);
	        setPause(false);
	    }

	    //Stop the simulation
	    public void stop() {
	        setStopped(true);
	        timer_play.stop();
	        init();
	        pause = false;
	        setTime(0);

	        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
	            listen.matrixUpdate(getInstances(), this.updateList(), getTime());
	        }
	    }

	    
	    private void play() {        
	        // Half-time
	        // --------
	        for (int pos_in_list = instances.getSize() - 1; pos_in_list >= 0; pos_in_list--) {
	            InstanceReaxel_NetMDyn c = instances.getInList(pos_in_list);//_matrice_cubes[i, j];
	            // Management of the half-time
	            if (c.getDemie_vie() > 0 && !c.isSelectionne()) {
	                double proba_mort = 1 - Math.pow(0.5, 1.0 / c.getDemie_vie());
	                if (RandomGen.getInstance().nextDouble() < proba_mort) {
	                    instances.removeReaxel(c);
	                }
	            }
	        }

	        // ***************************************
	        // Inclusion of various reactions
	        // ***************************************
	        // List of all reactions motors
	        List<Behavior_NetMDyn> lst_react = model.getListManipulesReactions();
	        // Set to zero of the future matrix of reaxels
	        Env_Parameters param = model.getParameters();
	        instancesFutur = new AllInstances_NetMDyn(param.getX(), param.getY(), param.getZ());

	        // Parallel launch of all reactions
	        setNb_processus_a_traiter(lst_react.size());
	        for (Behavior_NetMDyn lst_react1 : lst_react) {
	            lst_react1.computeReactions(this, param, instances.clone(), getTime());
	        }

	        // Concatenation of all possible reactions
	        ArrayList<InstanceReaction> lst_rp = new ArrayList<>();
	        for (Behavior_NetMDyn lst_react1 : lst_react) {
	            lst_rp.addAll(lst_react1._reactionsPossibles);
	        }

	        // Index of blended reactions
	        ArrayList<Integer> lst_int = RandomGen.getInstance().liste_entiers_melanges(lst_rp.size());
	        // Execution of effective choice of reactions
	        for (int r = 0; r < lst_rp.size(); r++) {
	            InstanceReaction rp = lst_rp.get(lst_int.get(r));
	            // Tentative of application of the transformation 
	            boolean possible = true;

	            if (rp._type != 2) { // Located and "semi-situées" reactions
	                // Reagents always present ?
	                for (int i = 0; i < rp._reactifs_noms.size(); i++) {
	                    int x = rp._reactifs_pos.get(i).x;
	                    int y = rp._reactifs_pos.get(i).y;
	                    int z = rp._reactifs_pos.get(i).z;
	                    // Case of vacuum
	                    if (instances.getFast(x, y, z) == null) {
	                        if (!rp._reactifs_noms.get(i).equals("0")) { //The reaction wants a vacuum
	                            possible = false;
	                            i = rp._reactifs_noms.size();
	                        }
	                    } else // Case of no-vacuum 
	                    {
	                        if (!instances.getFast(x, y, z).getNom().equals(rp._reactifs_noms.get(i))) { // The reaction can find the good Reaxel name into the actual matrix
	                            possible = false;
	                            i = rp._reactifs_noms.size(); 
	                        }
	                    }
	                }

	                // Verify if there is some place for products 
	                if (possible == true) {
	                    for (int i = 0; i < rp._produits_noms.size(); i++) {
	                        int x = rp._produits_pos.get(i).x;
	                        int y = rp._produits_pos.get(i).y;
	                        int z = rp._produits_pos.get(i).z;
	                        if (instancesFutur.getFast(x, y, z) != null) { // If the space is occupied
	                            if (!rp._produits_noms.get(i).equals("")) { // No possibility to put into it a product
	                                possible = false;
	                                i = rp._produits_noms.size();
	                            }
	                        }
	                    }
	                }

	                // Do the transformation if it's possible 
	                if (possible == true) {
	                    // Add the products into the future matrix
	                    for (int i = 0; i < rp._produits_noms.size(); i++) {
	                        int x = rp._produits_pos.get(i).x;
	                        int y = rp._produits_pos.get(i).y;
	                        int z = rp._produits_pos.get(i).z;
	                        this.AjouterFuturReaxel(x, y, z, rp._produits_noms.get(i));
	                    }

	                    // Remove the reagents of actual matrix for preventing new reactions 
	                    for (int i = 0; i < rp._reactifs_noms.size(); i++) {
	                        int x = rp._reactifs_pos.get(i).x;
	                        int y = rp._reactifs_pos.get(i).y;
	                        int z = rp._reactifs_pos.get(i).z;
	                        if (instances.getFast(x, y, z) != null) {
	                            instances.removeReaxel(x, y, z);
	                        }
	                    }
	                }
	            }
	            // Next possible reaction
	        }

	        // End of effective application of reactions
	        // Put no-reacting reaxels into the future list and into the future matrix
	        instancesFutur.setMatrixAndList(instances.getList());
	        // Passive waiting of all returns 
	        while (getNb_processus_a_traiter() > 0) {
	        }

	        instances = new AllInstances_NetMDyn(instancesFutur.getList(), instancesFutur.getMatrix(), instancesFutur.getX(), instancesFutur.getY(), instancesFutur.getZ());
	        this.setTime(getTime() + 1);
	        int t = getTime();

	        if (getMaxStep() != -1) {
	            if (getTime() == getMaxStep()) {                
	                setStopped(true);
	                timer_play.stop();
	                pause = false;
	                setMaxStep(-1);
//	                setTime(0); // WARNING !! 
	            }
	        }

	        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
	            listen.matrixUpdate(getInstances(), this.updateList(), t);
	        }

	    }

	    public void play_one() {
	        if (this.getTime() == 0) {
	            init();
	        }
	        this.play();
	    }

	    /**
	     * Used to automatically remove all instances of the entities in reaxels
	     *
	     * @param reaxels
	     */
	    public void ProtoReaxelDeleted(ArrayList<String> reaxels) {
	        for (String p : reaxels) {
	            this.removeEntityInstances(p);
	        }
	        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
	            listen.matrixUpdate(getInstances(), updateList(), getTime());
	        }
	    }

	    /**
	     *
	     * @param entities
	     * @return
	     */
	    public HashMap<String, Integer> updateList() {
	        HashMap<String, Integer> entities = instances.getBook();
	        ArrayList<Entity_NetMDyn> reaxels = model.getListManipulesNoeuds();
	        for (Entity_NetMDyn entity : reaxels) {
	            if (entities.containsKey(entity._etiquettes) == false) {
	                entities.put(entity._etiquettes, 0);
	            }
	        }

	        return entities;
	    }

	    private void removeEntityInstances(String nom) {
	        for (int c = instances.getSize() - 1; c >= 0; c--) {
	            InstanceReaxel_NetMDyn cube = instances.getInList(c);
	            if (cube.getNom().equals(nom)) {
	                instances.removeReaxel(cube);
	            }
	        }
	    }

	    public void addEntityInstances(ArrayList<UtilPoint3D> points, String etiquette) {
	        boolean toUpdate = false;
	        for (UtilPoint3D point : points) {
	            int x = point.x;
	            int y = point.y;
	            int z = point.z;
	            if (this.AjouterReaxel(x, y, z, etiquette)) {
	                toUpdate = true;
	            }
	        }
	        if (toUpdate) {
	            for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
	                listen.matrixUpdate(getInstances(), updateList(), getTime());
	            }
	        }
	    }

	    public void removeEntityInstance(int x, int y, int z) {
	        boolean changed = instances.removeReaxel(x, y, z);
	        if (changed) {
	            for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
	                listen.matrixUpdate(getInstances(), updateList(), getTime());
	            }
	        }
	    }

	    private boolean AjouterReaxel(int i, int j, int k, String etiquette) {
	        boolean changed = false;
	        ArrayList<Entity_NetMDyn> reaxels = model.getListManipulesNoeuds();
	        for (int n = 0; n < reaxels.size(); n++) {
	            if (reaxels.get(n).TrouveEtiquette(etiquette) >= 0) {
	                InstanceReaxel_NetMDyn r = InstanceReaxel_NetMDyn.CreerReaxel(reaxels.get(n));
	                r.setX(i);
	                r.setY(j);
	                r.setZ(k);
	                changed = instances.addReaxel(r);
	                n = reaxels.size();
	            }
	        }
	        return changed;
	    }

	    public String getType(int x, int y, int z) {
	        InstanceReaxel r = instances.getFast(x, y, z);
	        if (r != null) {
	            return r.getNom();
	        } else {
	            return "";
	        }
	    }

	    private boolean AjouterFuturReaxel(int i, int j, int k, String etiquette) {
	        boolean changed = false;
	        ArrayList<Entity_NetMDyn> reaxels = model.getListManipulesNoeuds();
	        for (int n = 0; n < reaxels.size(); n++) {
	            if (reaxels.get(n).TrouveEtiquette(etiquette) >= 0) {
	                InstanceReaxel_NetMDyn r = InstanceReaxel_NetMDyn.CreerReaxel(reaxels.get(n));
	                r.setX(i);
	                r.setY(j);
	                r.setZ(k);
	                changed = instancesFutur.addReaxel(r);
	                n = reaxels.size();
	            }
	        }
	        return changed;
	    }

	    public void ProtoReaxelEdited(Entity_NetMDyn entity, String old_name) {
	        instances.editReaxels(entity, old_name);
	        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
	            listen.matrixUpdate(getInstances(), updateList(), getTime());
	        }
	    }

	    public boolean isRunning() {
	        return timer_play.isRunning();
	    }

	    public boolean isPause() {
	        return pause;
	    }

	    public void setPause(boolean pause) {
	        this.pause = pause;
	        if (isPause()) {
	            timer_play.stop();
	        } else {
	            timer_play.start();
	        }
	    }

	    public int getSpeed() {
	        return speed;
	    }

	    public void setSpeed(int speed) {
	        this.speed = speed;
	        if (!isStopped()) {
	            timer_play.stop();
	            initTimer();
	        } else {
	            initTimer();
	        }
	    }

	    public int getTime() {
	        return time;
	    }

	    public void setTime(int time) {
	        this.time = time;
	    }

	    public int getNb_processus_a_traiter() {
	        return nb_processus_a_traiter;
	    }

	    public void setNb_processus_a_traiter(int nb_processus_a_traiter) {
	        this.nb_processus_a_traiter = nb_processus_a_traiter;
	    }

	    public synchronized void decrementer_nb_processus_a_traiter() {
	        setNb_processus_a_traiter(getNb_processus_a_traiter() - 1);
	    }

	    public boolean isStopped() {
	        return stopped;
	    }

	    public void setStopped(boolean stopped) {
	        this.stopped = stopped;
	    }

	    public int getMaxStep() {
	        return maxStep;
	    }

	    public void setMaxStep(int maxStep) {
	        this.maxStep = maxStep;
	    }

	    public AllInstances_NetMDyn getInstances() {
	        return instances;
	    }

	}

	

