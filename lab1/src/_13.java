public class _13{
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        int length = s.length();
        boolean result; 

        while(state!=-1 && i<length){
            final char c = Character.toLowerCase(s.charAt(i++)); //doesn't affect Character if it's a number
            
            switch(state){
                case 0:
                    if(Character.isLetter(c)){
                        state = 4;
                    }
                    else if(Character.isDigit(c)){
                        if((c-'0')%2==0)
                            state = 1;
                        else state = 2;
                     }
                    else{ state = -1; }
                    break;
                case 1:
                    if(Character.isLetter(c)){
                        if(c>='a'&&c<='k')
                            state=3;
                        else if(c>='l'&&c<='z')
                            state=4;
                    }
                    else if(Character.isDigit(c)){ 
                        if((c-'0')%2==1)
                            state = 2;
                    }
                    else{ state = -1; }
                    break;
                case 2:
                    if(Character.isLetter(c)){
                        if(c>='a'&&c<='k')
                            state=4;
                        else if(c>='l'&&c<='z')
                            state=3;
                    }
                    else if(Character.isDigit(c)){ 
                        if((c-'0')%2==0)
                            state = 1;
                    }
                    else{ state = -1; }
                    break;
                case 3: 
                    if(Character.isLetter(c)){}
                    else if(Character.isDigit(c)){ state=4; }
                    else{ state = -1; }
                    break;
                case 4: //stato pozzo
                    if(Character.isLetter(c)){}
                    else if(Character.isDigit(c)){ }
                    else{ state = -1; }
                    break;
            }
        }
        result=state == 3;
        return result;
    }

    public static void main(String[] args){
        String[] correctStrings={"123456Bianchi","654321Rossi","2Bianchi","122B"};
        String[] wrongStrings={"654321Bianchi","123456Rossi","654322","Rossi"};
        
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