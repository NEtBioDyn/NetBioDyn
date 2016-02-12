/* This file is part of NetMDyn.util
 *
 *   NetMDyn is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 3 of the License, or
 *   any later version.
 *
 *   NetMDyn is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with NetBioDyn; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
/*
 * SaverLoader_NetMDyn.java
 *
 * Created on February 12 2016, 16:30
 */

/**
 * Save and load in NetMDyn
 * 
 * @author Master 2 Bioinformatique
 */


package NetMDyn.util;

public abstract class SaverLoader_NetMDyn {
	    
	    public abstract void save(Serialized_NetMDyn env);
	    public abstract Serialized_NetMDyn load();

}
