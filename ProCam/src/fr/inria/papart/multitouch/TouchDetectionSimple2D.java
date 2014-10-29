/*
 * Copyright (C) 2014 Jeremy Laviole <jeremy.laviole@inria.fr>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package fr.inria.papart.multitouch;

import fr.inria.papart.depthcam.DepthData;
import fr.inria.papart.depthcam.Kinect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import processing.core.PApplet;
import processing.core.PVector;
import toxi.geom.Vec3D;

/**
 *
 * @author Jeremy Laviole <jeremy.laviole@inria.fr>
 */
public class TouchDetectionSimple2D extends TouchDetection{


    public TouchDetectionSimple2D(int size) {
        super(size);
    }

    @Override
    public ArrayList<TouchPoint> compute(DepthData dData, int skip) {
        this.depthData = dData;
        this.precision = skip;
        
        if(!hasCCToFind()){
            return new ArrayList<TouchPoint>();
        }

        ArrayList<ConnectedComponent> connectedComponents = findConnectedComponents();
        ArrayList<TouchPoint> touchPoints = this.createTouchPointsFrom(connectedComponents);

        return touchPoints;
    }
    

    @Override
    protected void setSearchParameters() {
        this.toVisit = new HashSet<Integer>();
        this.toVisit.addAll(depthData.validPointsList);

        currentPointValidityCondition = new CheckTouchPoint();
        int firstPoint = toVisit.iterator().next();
        setPrecisionFrom(firstPoint);
        searchDepth = precision * 7;// TODO: FIX this value !
        MAX_REC = 100; // TODO: fix this value.
    }


    public class CheckTouchPoint implements PointValidityCondition {

        @Override
        public boolean checkPoint(int offset, int currentPoint) {
            float distanceToCurrent = depthData.kinectPoints[offset].distanceTo(depthData.kinectPoints[currentPoint]);

            return !assignedPoints[offset] // not assigned  
                    && depthData.validPointsMask[offset] // is valid
                    && (depthData.kinectPoints[offset] != Kinect.INVALID_POINT) // not invalid point (invalid depth)
                    && distanceToCurrent < maxDistance;
        }
    }
}
