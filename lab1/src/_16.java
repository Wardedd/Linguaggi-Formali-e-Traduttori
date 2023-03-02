public class _16{
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        int length = s.length();
        boolean result; 

        while(state!=-1 && i<length){
            final char c = Character.toLowerCase(s.charAt(i++)); //doesn't affect Character if it's a number
            
            switch(state){
                case 0:
                    if(Character.isLetter(c))
                    	state=8;
                    else if(Character.isDigit(c)){
                        if((c-'0')%2==0)
                            state=1;
                        else 
                        	state=2;
                    }
                    else{ state = -1; }
                    break;
                case 1:
                    if(Character.isLetter(c))
                    	state=8;
                    else if(Character.isDigit(c)){
                        if((c-'0')%2==0)
                            state=3;
                        else 
                        	state=4;
                    }
                    else{ state = -1; }
                    break;
                case 2:
                    if(Character.isLetter(c))
                    	state=8;
                    else if(Character.isDigit(c)){
                        if((c-'0')%2==0)
                            state=5;
                        else 
                        	state=6;
                    }
                    else{ state = -1; }
                	break;
                case 3:
                    if(Character.isLetter(c)){
                        if(c>='a'&&c<='k')
                            state=7;
                        else if(c>='l'&&c<='z')
                            state=8;
                    }
                    else if(Character.isDigit(c)){
                        if((c-'0')%2==1)
                        	state=4;
                    }
                    else{ state = -1; }
                	break;
                case 4:
                    if(Character.isLetter(c)){
                        if(c>='a'&&c<='k')
                            state=7;
                        else if(c>='l'&&c<='z')
                            state=8;
                    }
                    else if(Character.isDigit(c)){
                        if((c-'0')%2==0)
                            state=5;
                        else 
                        	state=6;
                    }
                    else{ state = -1; }

                	break;
                case 5:
                    if(Character.isLetter(c)){
                        if(c>='a'&&c<='k')
                            state=8;
                        else if(c>='l'&&c<='z')
                            state=7;
                    }
                    else if(Character.isDigit(c)){
                        if((c-'0')%2==0)
                            state=3;
                        else 
                        	state=4;
                    }
                    else{ state = -1; }
                	break;
                case 6:
                	if(Character.isLetter(c)){
                        if(c>='a'&&c<='k')
                            state=8;
                        else if(c>='l'&&c<='z')
                            state=7;
                    }
                    else if(Character.isDigit(c)){
                        if((c-'0')%2==0)
                        	state=5;
                    }
                    else{ state = -1; }
                	break;
                case 7:
                    if(Character.isLetter(c)){}
                    else if(Character.isDigit(c)){ state = 8; }
                    else{ state = -1; }
                	break;
                case 8:
                    if(Character.isLetter(c)){}
                    else if(Character.isDigit(c)){}
                    else{ state = -1; }
                    break;
            }
        }

        result=(state == 7);
        return result;
    }

    public static void main(String[] args){
        String[] correctStrings={"654321Bianchi","123456Rossi","221B"};
        String[] wrongStrings={"123456Bianchi", "654321Rossi", "5", "654322", "Rossi", "2Bianchi"};
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