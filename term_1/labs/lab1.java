public class lab1{
    static double[][] l = new double[10][10];


    public static double elem (long wi, double xj){
        if (wi == 4){
            return Math.cbrt(Math.log(Math.cos(xj) * Math.cos(xj))); 
            }
        else if (wi == 6 || wi == 10 || wi == 12 || wi == 18 || wi == 22){
            return Math.exp(Math.sin(Math.pow((0.25 + xj), xj)));
        }
        else{
            return Math.pow(Math.exp(Math.pow(2.0/(1.0-xj), 2)), 3);
        }
        }

   
    public static void main (String[] args){
        long[] w;
        int a = 22;
        w = new long[(22 - 4)/2 + 1];
        for (int i = 0; i < w.length; i++){
            w[i] = a;
            a -= 2;
        }
        // for (int i = 0; i < w.length; i++){
        //     System.out.print (w[i] + " " + "/n");
        // }
        double[] x = new double[10];

        for (int i = 0; i < x.length; i++){
            x[i] = Math.random() * 12 - 8;
        }
        
        for (int i = 0; i < l.length; i++){
            for (int j = 0; j < l.length; j++){
                    l[i][j] = elem (w[i], x[j]);
                }
            }
        output();
        } 
    public static void output (){
        for (int i = 0; i < l.length; i++){
            for (int j = 0; j < l.length; j++){
                    System.out.printf ("%10.5f ", l[i][j]);
                }
            System.out.println();
            }
    }
    }
