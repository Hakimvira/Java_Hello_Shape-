import java.util.Random;

class HelloShape{

    public static void main(String[] args) {
        
        System.out.println("Hello Shape!");
        DisplayShape displayShape = new DisplayShape();
       
        /* Circle circle = new Circle(new Vector2(10,8), 8);
        displayShape.storeShape(circle, ConsoleColors.RED);

        Box box = new Box(new Vector2(35, 15), new Vector2(20, 0));
        displayShape.storeShape(box, ConsoleColors.BLUE);

        Triangle triangle = new Triangle(new Vector2(37, 15), new Vector2(57, 15), new Vector2(47, 0));
        displayShape.storeShape(triangle, ConsoleColors.PURPLE); */

        RandomShape randomShape = new RandomShape(displayShape.resolution.clone(), new Vector2(1, 8));
        int maxNumber = 50;
        displayShape.setMaxShape(maxNumber);
        for (int i = 0; i < maxNumber; i++) {
            displayShape.storeShape(randomShape.randomCircle(), randomShape.randomColor());
        }
        
        displayShape.display();
    }

}

class Vector2{
    double x, y;

    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector2 clone(){
        return new Vector2(x, y);
    }

    public double distanceTo(Vector2 v){
        return Math.sqrt(Math.pow(this.x - v.x, 2) + Math.pow(this.y - v.y, 2));
    }
}

class Shape{
    public boolean intersect(Vector2 v){
        return true;
    }
}

class Circle extends Shape{
    Vector2 center;
    double radius;

    public Circle(Vector2 center, double radius){
        this.center = center;
        this.radius = radius;
    }

    @Override
    public boolean intersect(Vector2 v){
        double distToCenter = center.distanceTo(v);
        double intersect = radius - distToCenter;
        if(distToCenter <= radius){
            return true;
        }

        return false;
    }
}

class Box extends Shape{
    Vector2 maxPos;
    Vector2 minPos;

    public Box(Vector2 maxPos, Vector2 minPos){
        this.maxPos = maxPos;
        this.minPos = minPos;
    }

    @Override
    public boolean intersect(Vector2 v){
        if(v.x <= maxPos.x && v.y <= maxPos.y && v.x >= minPos.x && v.y >= minPos.y){
            return true;
        }

        return false;
    }
}

class Triangle extends Shape{
    Vector2 point1, point2, point3;

    public Triangle(Vector2 point1, Vector2 point2, Vector2 point3){
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
    }
    
    //Source - https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle
    private double sign (Vector2 p1, Vector2 p2, Vector2 p3)
    {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    @Override
    public boolean intersect(Vector2 v){
        double d1, d2, d3;
        boolean has_neg, has_pos;

        d1 = sign(v, point1, point2);
        d2 = sign(v, point2, point3);
        d3 = sign(v, point3, point1);

        has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(has_neg && has_pos);
    }
}

class DisplayShape{
    int maxShape = 20;
    int arrCounter = 0;
    Shape[] shapes = new Shape[maxShape];
    String[] colors = new String[maxShape];
    Vector2 resolution = new Vector2(200, 30);

    public void storeShape(Shape s, String c){
        if(arrCounter >= maxShape){
            System.out.println("Maximum number of shapes has been reached");
            return; 
        }

        shapes[arrCounter] = s;
        colors[arrCounter] = c;
        arrCounter++;
    }

    public void display(){
        for(int i = 0 ; i < resolution.y; i++){
            for(int j = 0 ; j < resolution.x; j++){
                Boolean shouldDraw = false;
                Vector2 currentPos = new Vector2(j * 0.5 , i);
                int shapeIndex = -1;
                for(int k = 0 ; k < shapes.length; k++){
                    shouldDraw = shapes[k].intersect(currentPos);
                    if(shouldDraw){
                        shapeIndex = k;
                    } 
                }

                if(shapeIndex != -1){
                    System.out.print(colors[shapeIndex] + "#" + ConsoleColors.RESET);
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println(" ");
        }
    }

    public void setMaxShape(int maxShape){
        this.maxShape = maxShape;
        this.shapes = new Shape[maxShape];
        this.colors = new String[maxShape];
    }

    public void TestResolution(){
        for(int i = 0 ; i < resolution.y; i++){
            for(int j = 0 ; j < resolution.x; j++){
                System.out.print(ConsoleColors.RED + "#" + ConsoleColors.RESET);
            }
            System.out.println("");
        }
    }
}

//source - https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
class ConsoleColors {
    // Reset
    public static final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE
}

class RandomShape{
    Vector2 resolution;
    Random rand;
    Vector2 radiusRange;
    
    public RandomShape(Vector2 res, Vector2 radiusRange){
        this.resolution = res;
        this.radiusRange = radiusRange;
        rand = new Random();
    }
    public Circle randomCircle(){
        Vector2 center = randomCenter();
        Double radius = randomRangeDouble(radiusRange.x, radiusRange.y);
        return new Circle(center, radius);
    }

    public String randomColor(){
        int colorCode = randomRangeInt(1, 7);
        return "\033[1;3" + colorCode + "m";
    }

    private Vector2 randomCenter(){
        int x = rand.nextInt((int)resolution.x);
        int y = rand.nextInt((int)resolution.y);
        return new Vector2(x, y);
    }

    private double randomRangeDouble(double min, double max){
        return rand.nextDouble() * (max - min) + min;
    }

    private int randomRangeInt(int min, int max){
        return (int) (rand.nextDouble() * (max - min) + min);
    }
}