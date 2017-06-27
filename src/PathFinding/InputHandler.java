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
import java.lang.reflect.Array;
import ws3dproxy.model.Creature;
import ws3dproxy.model.World;

public class InputHandler {

    @SuppressWarnings("empty-statement")
    public SquareGraph readMap(Creature creature) throws IOException, InvalidLetterException {

        try {

            int mapDimensionX = 800;
            int mapDimensionY = 600;

            //World.createBrick(1,198,190,224,459);
            //array brick = new int[4][6];
            //maria = {174,126,210,276};
//            World.createBrick(1,174,126,210,276);
//              World.createBrick(1,252,483,284,588);
//              World.createBrick(1,375,282,412,385);
//              World.createBrick(1,547,61,583,161);
//              World.createBrick(1,532,413,569,513);
//              World.createBrick(1,686,266,721,349);
            int tamVector = 4;
            int[][] matriz = {
                {174, 126, 210, 276},
                {252, 483, 284, 588},
                {375, 282, 412, 385},                
                {532, 413, 569, 513},
                {686, 266, 721, 349}
            };
            
         //   World.createBrick(1,547,61,583,161);

            char[][] wolrdGrid = new char[mapDimensionX][mapDimensionY];

            for (int i = 0; i < matriz.length; i++) {
                int x1 = matriz[i][0] -100;
                int y1 = matriz[i][1] -100;
                int x2 = matriz[i][2] +100;
                int y2 = matriz[i][3] +100;

                for (int x = 0; x < mapDimensionX; x++) {
                    // System.out.print(" |");
                    for (int y = 0; y < mapDimensionY; y++) {
                        if (((x >= x1) && (x <= x2)) && ((y >= y1) && (y <= y2))) {
                            wolrdGrid[x][y] = 'X';
                        } else {
                            wolrdGrid[x][y] = ' ';
                        }

                        // System.out.print(wolrdGrid[x][y] + "|");
                    }
                    // System.out.println("");
                }
            }

//            int xCreature = (int) creature.getPosition().getX();
//            int yCreature = (int) creature.getPosition().getY();
            //start Creature ;
            char valor = wolrdGrid[100][500];
            wolrdGrid[100][500] = 'G';

            //goal creature
            wolrdGrid[700][50] = 'T';

            SquareGraph graph = new SquareGraph(mapDimensionX, mapDimensionY);

            for (int x = 0; x < mapDimensionX; x++) {
                for (int y = 0; y < mapDimensionY; y++) {
                    char typeSymbol = wolrdGrid[x][y];
                    if (typeSymbol == ' ') {
                        Node n = new Node(x, y, "NORMAL");
                        graph.setMapCell(new Point(x, y), n);
                    } else if (typeSymbol == 'X') {
                        Node n = new Node(x, y, "OBSTACLE");
                        graph.setMapCell(new Point(x, y), n);
                    } else if (typeSymbol == 'T') {
                        Node n = new Node(x, y, "NORMAL");
                        graph.setMapCell(new Point(x, y), n);
                        graph.setStartPosition(new Point(x, y));
                    } else if (typeSymbol == 'G') {
                        Node n = new Node(x, y, "NORMAL");
                        graph.setMapCell(new Point(x, y), n);
                        graph.setTargetPosition(new Point(x, y));
                    } else {
                        throw new InvalidLetterException("There was a wrong character in the text file.The character must be X, ,T or G.");
                    }
                }
            }
            return graph;
        } catch (Exception e) {
            throw e;
        } finally {
            System.out.println("PathFinding done ...");
        }
    }

}
