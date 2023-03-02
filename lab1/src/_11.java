public class _11{

    public static boolean scan(String s){
        int state=0;
        int i=0;
        int length=s.length();
        while(state!=-1 && i<length){
            final int c = s.charAt(i++)-'0';
            
            switch(state){
                case 0:
                    if(c==0)
                        state=1;
                    else if (c!=1)
                        state=-1;
                break;
                case 1:
                    if(c==0)
                        state=2;
                    else if(c==1)
                        state=0;
                    else state=-1;
                break;
                case 2:
                    if(c==0)
                        state=3;
                    else if(c==1)
                        state=0;
                    else state=-1;
                break;
                case 3:
                    if(c!=0&&c!=1)
                        state=-1;
                break;
            }
        }

        return (state == 0) || (state == 1) || (state == 2);
    }

    public static void main(String[] args){
        System.out.println(args.length>0?(scan(args[0])? "OK":"NOPE"):"NOPE");      
    }

}