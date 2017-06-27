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
package codelets.behaviors;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.json.JSONException;
import org.json.JSONObject;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import memory.CreatureInnerSense;
import ws3dproxy.model.Thing;

public class KnowsWallCloesestBrick extends Codelet {

    private MemoryObject closestBrickMO;
    private MemoryObject selfInfoMO;
    private MemoryObject legsMO;
    private int creatureBasicSpeed;
    private double reachDistance;

    public KnowsWallCloesestBrick(int creatureBasicSpeed, int reachDistance) {
        this.creatureBasicSpeed = creatureBasicSpeed;
        this.reachDistance = reachDistance;
    }

    @Override
    public void accessMemoryObjects() {
        closestBrickMO = (MemoryObject) this.getInput("CLOSEST_BRICK");
        selfInfoMO = (MemoryObject) this.getInput("INNER");
        legsMO = (MemoryObject) this.getOutput("LEGS");
    }

    @Override
    public void proc() {
        // Find distance between creature and closest apple
        //If far, go towards it
        //If close, stops

        Thing closestBrick = (Thing) closestBrickMO.getI();
        CreatureInnerSense cis = (CreatureInnerSense) selfInfoMO.getI();

        if (closestBrick != null) {
            double brickX1 = 0;
            double brickY1 = 0;
            double brickX2 = 0;
            double brickY2 = 0;
            try {
                brickX1 = closestBrick.getX1();
                brickY1 = closestBrick.getY1();

                brickX2 = closestBrick.getX2();
                brickY2 = closestBrick.getY2();

            } catch (Exception e) {
                e.printStackTrace();
            }

            double selfX = cis.position.getX();
            double selfY = cis.position.getY();

            Point2D pBrick1 = new Point();
            pBrick1.setLocation(brickX1, brickY1);

            Point2D pBrick2 = new Point();
            pBrick2.setLocation(brickX2, brickY2);

            Point2D pSelf = new Point();
            pSelf.setLocation(selfX, selfY);

            double distance1 = pSelf.distance(pBrick1);
            double distance2 = pSelf.distance(pBrick2);
            JSONObject message = new JSONObject();
            try {
                if ((distance1 / 2) < reachDistance * 3 || (distance2 / 2) < reachDistance * 3) {
                   // message.put("ACTION", "KNOWSWALL");
                   // message.put("BRICK", closestBrick);

                } else {

                   // message.put("ACTION", "KNOWSWALL");
                   // message.put("BRICK", closestBrick);

                    
                    //message.put("ACTION", "KNOWSWALL");
                    legsMO.updateI(message.toString());
                }
                
                

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//end proc

    @Override
    public void calculateActivation() {

    }

}
