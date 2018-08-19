/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package golo.data;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Richie
 */

public class Anchor extends Circle implements Drag{

        double startX;
        double startY;
        AnchorPosition pos;
        DragRectangle node;        

        public Anchor(AnchorPosition initpos) {
            setRadius(15.0);
            setOpacity(1.0);
            startX = 0.0;
            startY = 0.0;
            pos = initpos;
            setFill(Color.YELLOW);
            setStroke(Color.BLACK);
        }


        @Override
        public void start(int x, int y) {
            startX = x;
            startY = y;
        }

        //https://stackoverflow.com/questions/26298873/resizable-and-movable-rectangle?answertab=active#tab-top
        //Modified this code snippet according to the requirements in the SRS
        @Override
        public void drag(int x, int y) {
            double diffX = x - startX;
            double diffY = y - startY;
            double newX = getCenterX() + diffX;
            double newY = getCenterY() + diffY;
            switch (pos) {
                case TL:
                    if (newX >= 15 && newX <= node.getX() + node.getWidth() - 15) {
                        node.setWidth(node.getWidth() - diffX);
                        node.setX(newX);
                    }  
                    if (newY >= 15 && newY <= node.getY() + node.getHeight() - 15) {
                        node.setHeight(node.getHeight() - diffY);
                        node.setY(newY);
                    }break;
                case BL:
                    if(node!=null){
                        if (newX >= 15  && newX <= node.getX() + node.getWidth() - 15) {
                            node.setX(newX);
                            node.setWidth(node.getWidth() - diffX);
                        }
                        double initNewY = node.getY() + node.getHeight() + diffY ;
                        if (initNewY >= node.getY() && initNewY <= node.getParent().getBoundsInLocal().getHeight() - 15) {
                            node.setHeight(node.getHeight() + diffY);
                        }
                    }   break;
                case BR:
                    if(node!=null){
                        double initNewX = node.getX() + node.getWidth() + diffX ;
                        if (initNewX  >= node.getX() && initNewX <= node.getParent().getBoundsInLocal().getWidth() - 15) {
                            node.setWidth(node.getWidth() + diffX);
                        }
                        double initNewY = node.getY() + node.getHeight() + diffY ;
                        if (initNewY >= node.getY() && initNewY <= node.getParent().getBoundsInLocal().getHeight() - 15) {
                            node.setHeight(node.getHeight() + diffY);
                        }
                    }   break;
                case TR:
                    if(node!=null){
                        double initNewX = node.getX() + node.getWidth() + diffX ;
                        if (initNewX  >= node.getX() && initNewX <= node.getParent().getBoundsInLocal().getWidth() - 15) {
                            node.setWidth(node.getWidth() + diffX);
                        }
                        if (newY >= 15 && newY <= node.getY() + node.getHeight() - 15) {
                            node.setY(newY);
                            node.setHeight(node.getHeight() - diffY);
                        }
                    }   break;
                default:
                    break;
            }
            startX = x;
            startY = y;
        }

        @Override
        public void size(int width, int height) {
            setRadius(width);
        }

        @Override
        public double getX() {
            return getCenterX();
        }

        @Override
        public double getY() {
            return getCenterY();
        }

        @Override
        public double getWidth() {
            return getRadius();
        }

        @Override
        public double getHeight() {
            return getRadius();
        }

        @Override
        public void setPosandSize(double initX, double initY, double initWidth, double initHeight) {
            setRadius(initWidth);
        }

        @Override
        public String getShapeType() {
            return ELLIPSE;
        }

        @Override
        public Node clone() {
            Anchor cloney = new Anchor(this.pos);
            cloney.setRadius(getRadius());
            cloney.setFill(getFill());
            return cloney;
        }

        @Override
        public goloItemPrototype getPrototype() {
            return null;
        }

        @Override
        public void setPrototype(goloItemPrototype initproto){
              
        }
        
        public DragRectangle getAttachedNode() {
            return node;
        }
        
        public void setAttachedNode(DragRectangle rect) {
            node = rect;
        }
}
