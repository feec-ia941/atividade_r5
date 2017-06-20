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
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.List;
import java.util.Set;
import memory.CreatureInnerSense;
import ws3dproxy.model.Leaflet;
import ws3dproxy.model.Thing;

public class GoToClosestJewel extends Codelet {

    private MemoryObject closestJewelMO;
    private MemoryObject selfInfoMO;
    private MemoryObject legsMO;
    private int creatureBasicSpeed;
    private double reachDistance;

    

    public GoToClosestJewel(int creatureBasicSpeed, int reachDistance) {
        this.creatureBasicSpeed = creatureBasicSpeed;
        this.reachDistance = reachDistance;
    }

    @Override
    public void accessMemoryObjects() {
        closestJewelMO = (MemoryObject) this.getInput("CLOSEST_JEWEL");
        selfInfoMO = (MemoryObject) this.getInput("INNER");
        legsMO = (MemoryObject) this.getOutput("LEGS");
    }

    @Override
    public void proc() {
        // Find distance between creature and closest jewel
        //If far, go towards it
        //If close, stops

        Thing closestJewel = (Thing) closestJewelMO.getI();
        CreatureInnerSense cis = (CreatureInnerSense) selfInfoMO.getI();

        if (closestJewel != null) {
            double jewelX = 0;
            double jewelY = 0;
            try {
                jewelX = closestJewel.getX1();
                jewelY = closestJewel.getY1();

            } catch (Exception e) {
                e.printStackTrace();
            }

            double selfX = cis.position.getX();
            double selfY = cis.position.getY();

            Point2D pJewel = new Point();
            pJewel.setLocation(jewelX, jewelY);

            Point2D pSelf = new Point();
            pSelf.setLocation(selfX, selfY);

            double distance = pSelf.distance(pJewel);
            JSONObject message = new JSONObject();

            //Variaveis para verificar preferencia.
            Boolean preference = false;
            int count = 0;
            
            for (Leaflet itemLeaflet : cis.listaLeaflet) {
                for (String key : itemLeaflet.getItems().keySet()) {
                    Integer[] valor;
                    valor = itemLeaflet.getItems().get(key);

                    //Verifica se joia ainda necessita ser coletada 
                    if (valor[0] > valor[1]) {
                        System.out.println(count + " - Key: " + key + " => " + valor[0] + " || " + valor[1]);

                        if (key.equals(closestJewel.getAttributes().getColor())) {
                            System.out.println(count + " - Key: " + key + " (///.°)");
                            preference = true;
                        }
                    } else {
                        System.out.println(count + " - Key: " + key + " #####");
                    }

                }
                count++;

            }

            try {
                if (distance <= (reachDistance +50) || preference) { //Go to it
                    message.put("ACTION", "GOTO");
                    message.put("X", (int) jewelX);
                    message.put("Y", (int) jewelY);
                    message.put("SPEED", creatureBasicSpeed);

                } else {//Stop
                    message.put("ACTION", "GOTO");
                    message.put("X", (int) jewelX);
                    message.put("Y", (int) jewelY);
                    message.put("SPEED", 0);

                }
                legsMO.updateI(message.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }//end proc

    @Override
    public void calculateActivation() {

    }

}
