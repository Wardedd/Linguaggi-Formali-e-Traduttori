public class _12{
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        int length = s.length();

        while(state!=-1 && i<length){
            final char c = s.charAt(i++);
            
            switch(state){
                case 0:
                    if(c=='_'){
                        state = 2;
                    }
                    else if(Character.isDigit(c)){
                        state = 1;
                    }
                    else if(Character.isLetter(c)){
                        state = 3;
                    }
                    else{ state = -1;}
                    break;
                case 1:
                    //stato pozzo 
                    if(c=='_'){}
                    else if(Character.isDigit(c)){}
                    else if(Character.isLetter(c)){}
                    else{ state = -1;}                 
                    break;
                case 2:
                    if(c=='_'){
                        //stay in state
                    }
                    else if(Character.isDigit(c)){
                        state = 3;
                    }
                    else if(Character.isLetter(c)){
                        state = 3;
                    }
                    else{ state = -1;}
                    break;
                case 3: 
                //tutte le condizioni per accettatare str soddisfate, non accettatare str solo se viene inserito lettera non in alfabeto 
                    if(c=='_'){}
                    else if(Character.isDigit(c)){}
                    else if(Character.isLetter(c)){}
                    else{ state = -1;}

                    break;
            }
        }
        return state == 3;
    }

    public static void main(String[] args){
        String[] correctStrings={"x", "flag1", "x2y2", "x_1", "lft_lab",  "temp_", "x_1_y_2_", "x__",  "___Pippo"};
        String[] wrongStrings={"5",  "221B", "123", "9_to_5",  "___" , "_", "__"};
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