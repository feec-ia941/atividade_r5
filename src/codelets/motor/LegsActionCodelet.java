/** ***************************************************************************
 * Copyright 2007-2015 DCA-FEEC-UNICAMP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *    Klaus Raizer, Andre Paraense, Ricardo Ribeiro Gudwin
 **************************************************************************** */
package codelets.motor;

import PathFinding.*;
import org.json.JSONObject;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Logger;
import org.json.JSONException;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Thing;

/**
 * Legs Action Codelet monitors working storage for instructions and acts on the
 * World accordingly.
 *
 * @author klaus
 *
 */
public class LegsActionCodelet extends Codelet {

    private MemoryObject legsActionMO;
    private double previousTargetx = 0;
    private double previousTargety = 0;
    private String previousLegsAction = "";
    private Creature c;
    double old_angle = 0;
    int k = 0;
    static Logger log = Logger.getLogger(LegsActionCodelet.class.getCanonicalName());

    private ArrayList<Node> path;
    private ArrayList<Thing> brickKnows;
    private boolean way = true;

    public LegsActionCodelet(Creature nc) {
        c = nc;
        this.path = new A_StarAlgorithm().main(c);

        System.out.println("path.size(): " + path.size());
    }

    @Override
    public void accessMemoryObjects() {
        legsActionMO = (MemoryObject) this.getInput("LEGS");
    }

    @Override
    public void proc() {

        String comm = (String) legsActionMO.getI();
        if (comm == null) {
            comm = "";
        }
        Random r = new Random();

        if (!comm.equals("")) {

            try {
                JSONObject command = new JSONObject(comm);
                if (command.has("ACTION")) {
                    int x = 0, y = 0;
                    String action = command.getString("ACTION");
                    if (action.equals("FORAGE")) {
                        //if (!comm.equals(previousLegsAction)) { 
                        if (!comm.equals(previousLegsAction)) {
                            log.info("Sending Forage command to agent");
                        }
                        try {
                            //c.rotate(2);                            
                            Node node = null;

                            int xCreature = (int) c.getPosition().getX();
                            int yCreature = (int) c.getPosition().getY();

                                                    
                            double menor = Integer.MAX_VALUE;
                            
                            

                            outerloop:
                            for (int i = 0; i < path.size(); i++) {

                                

                                Point2D pointGo = new Point();
                                pointGo.setLocation(path.get(i).getX(), path.get(i).getY());

                                Point2D pSelf = new Point();
                                pSelf.setLocation(xCreature, yCreature);

                                double distance = pSelf.distance(pointGo);
                                
                                if(distance < menor)
                                    menor = distance;

                                if (distance < 100 && distance > 10) {
                                    node = path.get(i);
                                    if (path.get(0).equals(path.get(i))) {
                                        Collections.reverse(path);
                                    }
                                    c.moveto(1, node.getX(), node.getY());
                                    break outerloop;
                                }

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (action.equals("GOTO")) {
                        if (!comm.equals(previousLegsAction)) {
                            double speed = command.getDouble("SPEED");
                            double targetx = command.getDouble("X");
                            double targety = command.getDouble("Y");
                            if (!comm.equals(previousLegsAction)) {
                                log.info("Sending move command to agent: [" + targetx + "," + targety + "]");
                            }
                            try {
                                c.moveto(speed, targetx, targety);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            previousTargetx = targetx;
                            previousTargety = targety;
                        }

                    } else {
                        log.info("Sending stop command to agent");
                        try {
                            c.moveto(0, 0, 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                previousLegsAction = comm;
                k++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }//end proc

    @Override
    public void calculateActivation() {

    }

}
