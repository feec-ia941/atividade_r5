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
package codelets.perception;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import memory.CreatureInnerSense;
import ws3dproxy.model.Leaflet;
import ws3dproxy.model.Thing;

/**
 * Detect apples in the vision field. This class detects a number of things
 * related to apples, such as if there are any within reach, any on sight, if
 * they are rotten, and so on.
 *
 * @author klaus
 *
 */
public class AppleDetector extends Codelet {

    private MemoryObject visionMO;
    private MemoryObject knownApplesMO;

    private MemoryObject selfInfoMO;
    private int reachDistance;

    public AppleDetector(int reachDistance) {
        this.reachDistance = reachDistance;
    }

    @Override
    public void accessMemoryObjects() {
        synchronized (this) {
            this.visionMO = (MemoryObject) this.getInput("VISION");
            this.selfInfoMO = (MemoryObject) this.getInput("INNER");
        }
        this.knownApplesMO = (MemoryObject) this.getOutput("KNOWN_APPLES");
    }

    @Override
    public void proc() {
        CopyOnWriteArrayList<Thing> vision;
        List<Thing> known;
        synchronized (visionMO) {
            //vision = Collections.synchronizedList((List<Thing>) visionMO.getI());
            vision = new CopyOnWriteArrayList((List<Thing>) visionMO.getI());
            known = Collections.synchronizedList((List<Thing>) knownApplesMO.getI());
            //known = new CopyOnWriteArrayList((List<Thing>) knownApplesMO.getI());    
            synchronized (vision) {
                for (Thing t : vision) {
                    boolean found = false;
                    synchronized (known) {
                        CopyOnWriteArrayList<Thing> myknown = new CopyOnWriteArrayList<>(known);
                        for (Thing e : myknown) {
                            if (t.getName().equals(e.getName())) {
                                found = true;
                                break;
                            }
                        }
                        if (found == false && t.getName().contains("PFood") && !t.getName().contains("NPFood")) {
                            CreatureInnerSense cis = (CreatureInnerSense) selfInfoMO.getI();

                            double selfX = cis.position.getX();
                            double selfY = cis.position.getY();

                            double appleX = t.getX1();
                            double appleY = t.getY1();

                            Point2D pApple = new Point();
                            pApple.setLocation(appleX, appleY);

                            Point2D pSelf = new Point();
                            pSelf.setLocation(selfX, selfY);

                            double distance = pSelf.distance(pApple);

                            if (cis.fuel <= 4000 || distance <= reachDistance) {
                                known.add(t);
                            }

                        }

                    }
                }
            }
        }
    }// end proc

    @Override
    public void calculateActivation() {

    }

}//end class

