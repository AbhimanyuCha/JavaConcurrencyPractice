public class Runner {


    public static long fun(long t1, long t2 , long p){
        long minAns = (long)(1e9) + 7;
        for(int i = 0 ; i <= p ; i++){
            long x = t1 * i;
            long y = p - x;
            if(y % t2 == 0) {
                long d = y / t2;
                if(i + d < minAns)
                    minAns = i + d;
            }
        }
        return minAns;
    }
    public static void main(String[] args) {
        System.out.println(fun(3 , 4 , 7));
        System.out.println(fun(3 , 4 , 6));
    }
}
