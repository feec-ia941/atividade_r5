package PathFinding;

/**
 * ************************************************
 * Copyright (C) 2014 Raptis Dimos <raptis.dimos@yahoo.gr>
 *
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * ************************************************
 */
import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import ws3dproxy.model.Creature;

public class A_StarAlgorithm {

    public ArrayList<Node> main(Creature c) {

        ArrayList<Node> path = null;
        
        try {
            
            InputHandler handler = new InputHandler();
            SquareGraph graph = handler.readMap(c);

            path = graph.executeAStar();
            
            graph.printPath(path);

        } catch (Exception e) {
            System.err.println("PathFinding.A_StarAlgorithm.main()" + e.getMessage());
        }

        ArrayList<Node> pathAtualizado = new ArrayList<Node>();
        
        for (int i = 0; i < path.size(); i++) {
            if(i % 3 == 0){
                System.out.println(i+" -Node  X: "+path.get(i).getX()+" Y:"+path.get(i).getY());
                Node nd = path.get(i);
                pathAtualizado.add(nd);
            }
        }
        
        
        
        
        return pathAtualizado;
    }

    

    

}
