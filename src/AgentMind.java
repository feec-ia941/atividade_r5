
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

import codelets.perception.JewelDetector;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.entities.Mind;
import codelets.behaviors.EatClosestApple;
import codelets.behaviors.Forage;
import codelets.behaviors.GoToClosestApple;
import codelets.behaviors.GoToClosestJewel;
import codelets.behaviors.PutSackClosestJewel;
import codelets.motor.HandsActionCodelet;
import codelets.motor.LegsActionCodelet;
import codelets.perception.AppleDetector;
import codelets.perception.ClosestAppleDetector;
import codelets.perception.ClosestJewelDetector;
import codelets.sensors.InnerSense;
import codelets.sensors.Vision;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import memory.CreatureInnerSense;
import support.MindView;
import ws3dproxy.model.Thing;

/**
 *
 * @author rgudwin
 */
public class AgentMind extends Mind {

    private static int creatureBasicSpeed = 2;
    private static int reachDistance = 100;

    public AgentMind(Environment env) {
        super();

        // Declare Memory Objects
        MemoryObject legsMO;
        MemoryObject handsMO;
        MemoryObject visionMO;
        MemoryObject innerSenseMO;
        MemoryObject closestAppleMO;
        MemoryObject closestJewelMO;
        MemoryObject knownApplesMO;
        MemoryObject knownJewelsMO;

        //Initialize Memory Objects
        legsMO = createMemoryObject("LEGS", "");
        handsMO = createMemoryObject("HANDS", "");
        List<Thing> vision_list = Collections.synchronizedList(new ArrayList<Thing>());
        visionMO = createMemoryObject("VISION", vision_list);
        CreatureInnerSense cis = new CreatureInnerSense();
        innerSenseMO = createMemoryObject("INNER", cis);
        Thing closestApple = null;
        Thing closestJewel = null;
        closestAppleMO = createMemoryObject("CLOSEST_APPLE", closestApple);
        closestJewelMO = createMemoryObject("CLOSEST_JEWEL", closestJewel);

        List<Thing> knownApples = Collections.synchronizedList(new ArrayList<Thing>());
        knownApplesMO = createMemoryObject("KNOWN_APPLES", knownApples);

        List<Thing> knownJewels = Collections.synchronizedList(new ArrayList<Thing>());
        knownJewelsMO = createMemoryObject("KNOWN_JEWELS", knownJewels);

        // Create and Populate MindViewer
        MindView mv = new MindView("MindView");
        mv.addMO(knownApplesMO);
        mv.addMO(knownJewelsMO);

        mv.addMO(visionMO);

        mv.addMO(closestAppleMO);
        mv.addMO(closestJewelMO);

        mv.addMO(innerSenseMO);
        mv.addMO(handsMO);
        mv.addMO(legsMO);
        mv.StartTimer();
        mv.setVisible(true);

        // Create Sensor Codelets	
        Codelet vision = new Vision(env.c);
        vision.addOutput(visionMO);
        insertCodelet(vision); //Creates a vision sensor

        Codelet innerSense = new InnerSense(env.c);
        innerSense.addOutput(innerSenseMO);
        insertCodelet(innerSense); //A sensor for the inner state of the creature

        // Create Actuator Codelets
        Codelet legs = new LegsActionCodelet(env.c);
        legs.addInput(legsMO);
        insertCodelet(legs);

        Codelet hands = new HandsActionCodelet(env.c);
        hands.addInput(handsMO);
        insertCodelet(hands);

        // Create Perception Codelets
        Codelet adApple = new AppleDetector(reachDistance);
        adApple.addInput(visionMO);
        adApple.addInput(innerSenseMO);
        adApple.addOutput(knownApplesMO);
        insertCodelet(adApple);

        Codelet adJewel = new JewelDetector(reachDistance);
        adJewel.addInput(visionMO);
        adJewel.addInput(innerSenseMO);
        adJewel.addOutput(knownJewelsMO);
        insertCodelet(adJewel);

        Codelet closestAppleDetector = new ClosestAppleDetector(reachDistance);
        closestAppleDetector.addInput(knownApplesMO);
        closestAppleDetector.addInput(innerSenseMO);
        closestAppleDetector.addOutput(closestAppleMO);
        insertCodelet(closestAppleDetector);

        Codelet closestJewelDetector = new ClosestJewelDetector();
        closestJewelDetector.addInput(knownJewelsMO);
        closestJewelDetector.addInput(innerSenseMO);
        closestJewelDetector.addOutput(closestJewelMO);
        insertCodelet(closestJewelDetector);

        // Create Behavior Codelets
        Codelet goToClosestApple = new GoToClosestApple(creatureBasicSpeed, reachDistance);
        goToClosestApple.addInput(closestAppleMO);
        goToClosestApple.addInput(innerSenseMO);
        goToClosestApple.addOutput(legsMO);
        insertCodelet(goToClosestApple);

        // Create Behavior Codelets
        Codelet goToClosestJewel = new GoToClosestJewel(creatureBasicSpeed, reachDistance);
        goToClosestJewel.addInput(closestJewelMO);
        goToClosestJewel.addInput(innerSenseMO);
        goToClosestJewel.addOutput(legsMO);
        insertCodelet(goToClosestJewel);

        Codelet eatApple = new EatClosestApple(reachDistance);
        eatApple.addInput(closestAppleMO);
        eatApple.addInput(innerSenseMO);
        eatApple.addOutput(handsMO);
        eatApple.addOutput(knownApplesMO);
        insertCodelet(eatApple);

        Codelet putSack = new PutSackClosestJewel(reachDistance);
        putSack.addInput(closestJewelMO);
        putSack.addInput(innerSenseMO);
        putSack.addOutput(handsMO);
        putSack.addOutput(knownJewelsMO);
        insertCodelet(putSack);

        Codelet forageApple = new Forage();
        forageApple.addInput(knownApplesMO);
        forageApple.addOutput(legsMO);
        insertCodelet(forageApple);

        Codelet forageJewel = new Forage();
        forageJewel.addInput(knownJewelsMO);
        forageJewel.addOutput(legsMO);
        insertCodelet(forageJewel);

        // sets a time step for running the codelets to avoid heating too much your machine
        for (Codelet c : this.getCodeRack().getAllCodelets()) {
            c.setTimeStep(200);
        }

        // Start Cognitive Cycle
        start();
    }

}
