public class _18{
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        int length = s.length();
        boolean result; 

        while(state!=-1 && i<length){
            final char c = Character.toLowerCase(s.charAt(i++)); //doesn't affect Character if it's a number
            
            switch(state){
                case 0:
                    if(c>='0'&&c<='9'){ state=2; }
                    else if(c=='e'){ state=10; }
                    else if(c=='.'){ state=3; }
                    else if(c=='+'||c=='-'){ state=1; }
                    else{ state=-1; }
                    break;
                case 1:
                    if(c>='0'&&c<='9'){ state=2; }
                    else if(c=='e'){state=10;}
                    else if(c=='.'){ state=3; }
                    else if(c=='+'||c=='-'){ state=10; }
                    else{ state=-1; }
                    break;
                case 2:
                    if(c>='0'&&c<='9'){}
                    else if(c=='e'){ state=5; }
                    else if(c=='.'){ state=3; }
                    else if(c=='+'||c=='-'){ state=10; }
                    else{ state=-1; }
                    break;
                case 3:
                    if(c>='0'&&c<='9'){ state=4; }
                    else if(c=='e'){ state=10; }
                    else if(c=='.'){ state=10; }
                    else if(c=='+'||c=='-'){ state=10; }
                    else{ state=-1; }
                    break;
                case 4:
                    if(c>='0'&&c<='9'){}
                    else if(c=='e'){ state=5; }
                    else if(c=='.'){ state=10; }
                    else if(c=='+'||c=='-'){ state=10; }
                    else{ state=-1; }               
                    break;
                case 5:
                    if(c>='0'&&c<='9'){ state=7; }
                    else if(c=='e'){ state=10; }
                    else if(c=='.'){ state=8; }
                    else if(c=='+'||c=='-'){ state=6;}
                    else{ state=-1; }
                    break;
                case 6:
                    if(c>='0'&&c<='9'){ state=7; }
                    else if(c=='e'){ state=10; }
                    else if(c=='.'){ state=8; }
                    else if(c=='+'||c=='-'){ state=10; }
                    else{ state=-1; }
                    break;
                case 7:
                    if(c>='0'&&c<='9'){}
                    else if(c=='e'){ state=10; }
                    else if(c=='.'){ state=8; }
                    else if(c=='+'||c=='-'){ state=10; }
                    else{ state=-1; }
                    break;
                case 8:
                    if(c>='0'&&c<='9'){ state=9; }
                    else if(c=='e'){ state=10; }
                    else if(c=='.'){ state=10; }
                    else if(c=='+'||c=='-'){ state=10; }
                    else{ state=-1; }
                    break;
                case 9:
                    if(c>='0'&&c<='9'){}
                    else if(c=='e'){ state=10; }
                    else if(c=='.'){ state=10; }
                    else if(c=='+'||c=='-'){ state=10; }
                    else{ state=-1; }
                    break;
                case 10: //stato pozzo
                    if(c>='0'&&c<='9'){}
                    else if(c=='e'){}
                    else if(c=='.'){}
                    else if(c=='+'||c=='-'){}
                    else{ state=-1; }
                    break;            
                }
        }
        result = (state == 2)||(state == 4)||(state == 7)||(state == 9);
        return result;
    }

    public static void main(String[] args){
        String[] correctStrings={"123", "123.5", ".567", "+7.5", "-.7", "67e10", "1e-2","-.7e2", "1e2.3"};
        String[] wrongStrings={".", "e3", "123.", "+e6", "1.2.3", "4e5e6", "++3"};
        for(String s:correctStrings)
            if(!scan(s))
            {
                System.out.println("Stringa corretta non riconosciuta: "+s);   
            }else
            	System.out.println("YES");
        for(String s:wrongStrings)
            if(scan(s))
            {
                System.out.println("Stringa erroneamente riconosciuta: "+s);   
            }else
            	System.out.println("YES");
    }

}